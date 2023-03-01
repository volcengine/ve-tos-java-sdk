package com.volcengine.tos.internal.taskman;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.Utils;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.comm.event.UploadEventType;
import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.internal.TosObjectRequestHandler;
import com.volcengine.tos.internal.util.CRC64Utils;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.object.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class UploadFileTaskHandler {
    private UploadFileV2Input input;
    private TosObjectRequestHandler handler;
    private UploadFileV2Checkpoint checkpoint;
    private boolean enableCrcCheck;
    private TaskManager taskMan;
    private List<UploadedPartV2> uploadedParts;
    private AbortTaskHook abortTaskHook;
    private final AtomicLong consumedBytes;

    public UploadFileTaskHandler(UploadFileV2Input input, TosObjectRequestHandler handler, boolean enableCrcCheck) {
        ParamsChecker.ensureNotNull(input, "UploadFileV2Input");
        ParamsChecker.ensureNotNull(input.getFilePath(), "UploadFilePath");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        ParamsChecker.ensureNotNull(handler, "TosObjectRequestHandler");
        this.input = input;
        this.handler = handler;
        this.enableCrcCheck = enableCrcCheck;
        this.consumedBytes = new AtomicLong(0);
    }

    public boolean isNullFile() {
        return new File(input.getFilePath()).length() == 0;
    }

    public void initTask() {
        validateInput();
        if (this.input.isEnableCheckpoint()) {
            validateCheckpointPath();
        }
        UploadFileInfo fileInfo = getUploadFileInfo(input.getFilePath());
        setCheckpoint(fileInfo);
        int partsNum = this.checkpoint.getUploadPartInfos().size();
        this.abortTaskHook = new UploadFileTaskCanceler(this.handler, this.taskMan, this.checkpoint.getBucket(), this.checkpoint.getKey(),
                this.checkpoint.getUploadID(), this.input.getCheckpointFile(), this.input.isEnableCheckpoint());
        this.taskMan = new TaskManagerImpl(this.input.getTaskNum(), partsNum, null, this.abortTaskHook);
        if (this.input.getCancelHook() != null && this.input.getCancelHook() instanceof UploadFileTaskCanceler) {
            ((UploadFileTaskCanceler) this.input.getCancelHook()).setHandler(this.handler).setTaskMan(this.taskMan)
                    .setBucket(checkpoint.getBucket()).setKey(checkpoint.getKey()).setUploadID(checkpoint.getUploadID())
                    .setEnableCheckpoint(input.isEnableCheckpoint()).setCheckpointFilePath(input.getCheckpointFile());
        }
        this.uploadedParts = new ArrayList<>(partsNum);
    }

    public void dispatch() {
        for (int i = 0; i < this.checkpoint.getUploadPartInfos().size(); i++) {
            if (!this.checkpoint.getUploadPartInfos().get(i).isCompleted()) {
                taskMan.dispatch(new UploadFileTask(this.checkpoint, i, consumedBytes)
                        .setEnableCheckpoint(input.isEnableCheckpoint())
                        .setCheckpointFile(input.getCheckpointFile())
                        .setUploadEventListener(input.getUploadEventListener())
                        .setOptions(input.getOptions())
                        .setRateLimiter(input.getRateLimiter())
                        .setDataTransferListener(input.getDataTransferListener())
                        .setHandler(handler));
            } else {
                UploadPartInfo partInfo = checkpoint.getUploadPartInfos().get(i);
                uploadedParts.add(new UploadedPartV2().setEtag(partInfo.getEtag())
                        .setPartNumber(partInfo.getPartNumber()));
            }
        }
    }

    public UploadFileV2Output handle() {
        Util.postDataTransferStatus(this.input.getDataTransferListener(), new DataTransferStatus()
                .setType(DataTransferType.DATA_TRANSFER_STARTED).setTotalBytes(checkpoint.getFileSize())
                .setConsumedBytes(this.consumedBytes.get()));
        this.taskMan.handle();
        List<TaskOutput<?>> outputs = this.taskMan.get();
        for (TaskOutput<?> output : outputs) {
            UploadPartV2Output tmp = (UploadPartV2Output) output.getOutput();
            if (tmp == null) {
                continue;
            }
            uploadedParts.add(new UploadedPartV2().setPartNumber(tmp.getPartNumber()).setEtag(tmp.getEtag()));
        }
        long total = checkpoint.getFileSize();
        long consumed = this.consumedBytes.get();
        DataTransferStatus status = new DataTransferStatus().setTotalBytes(total).setConsumedBytes(consumed);
        if (!readyForComplete()) {
            String message = "tos: some upload tasks failed. bucket is " + this.input.getBucket() +
                    ", key is " + this.input.getKey();
            throw new TosClientException(message, null);
        }
        return completeUploadFileTask(status);
    }

    private UploadFileV2Output completeUploadFileTask(DataTransferStatus status) {
        Util.postDataTransferStatus(this.input.getDataTransferListener(), status.setType(DataTransferType.DATA_TRANSFER_SUCCEED));
        CompleteMultipartUploadV2Input input = new CompleteMultipartUploadV2Input()
                .setBucket(this.checkpoint.getBucket())
                .setKey(this.checkpoint.getKey())
                .setUploadID(this.checkpoint.getUploadID())
                .setUploadedParts(uploadedParts);
        CompleteMultipartUploadV2Output comp;
        UploadEvent event = new UploadEvent().setUploadID(this.checkpoint.getUploadID())
                .setBucket(this.checkpoint.getBucket())
                .setKey(this.checkpoint.getKey())
                .setCheckpointFile(this.input.getCheckpointFile())
                .setFilePath(this.input.getFilePath());
        try{
            comp = this.handler.completeMultipartUpload(input);
            if (enableCrcCheck) {
                combineCrcAndCheck(comp.getHashCrc64ecma());
            }
            Util.postUploadEvent(this.input.getUploadEventListener(),
                    event.setUploadEventType(UploadEventType.UploadEventCompleteMultipartUploadSucceed));
        } catch (TosException e) {
            Util.postUploadEvent(this.input.getUploadEventListener(),
                    event.setTosException(e).setUploadEventType(UploadEventType.UploadEventCompleteMultipartUploadFailed));
            throw e;
        }
        if (this.input.isEnableCheckpoint()) {
            Util.deleteCheckpointFile(this.input.getCheckpointFile());
        }
        return new UploadFileV2Output().setRequestInfo(comp.getRequestInfo())
                .setBucket(comp.getBucket())
                .setKey(comp.getKey())
                .setUploadID(checkpoint.getUploadID())
                .setEtag(comp.getEtag())
                .setLocation(comp.getLocation())
                .setHashCrc64ecma(comp.getHashCrc64ecma())
                .setVersionID(comp.getVersionID())
                .setSsecAlgorithm(checkpoint.getSseAlgorithm())
                .setSsecKeyMD5(checkpoint.getSseKeyMd5())
                .setEncodingType(checkpoint.getEncodingType());
    }

    private boolean readyForComplete() {
        if (checkpoint == null || checkpoint.getBucket() == null
        || checkpoint.getKey() == null || checkpoint.getUploadID() == null
        || checkpoint.getUploadPartInfos() == null || uploadedParts == null) {
            return false;
        }
        if (uploadedParts.size() != checkpoint.getUploadPartInfos().size()) {
            return false;
        }
        for (UploadPartInfo part : checkpoint.getUploadPartInfos()) {
            if (!part.isCompleted()) {
                return false;
            }
            if (enableCrcCheck && part.getPartSize() > 0 && part.getHashCrc64ecma() == 0) {
                return false;
            }
        }
        if (input.getDataTransferListener() != null &&
                this.consumedBytes.get() != checkpoint.getFileSize()) {
            throw new TosClientException("tos: some upload tasks failed, total: " + checkpoint.getFileSize()
                    + ", consumed: " + this.consumedBytes.get(), null);
        }
        return true;
    }

    public void validateInput() {
        if (input.getPartSize() == 0) {
            input.setPartSize(Consts.DEFAULT_PART_SIZE);
        }
        Util.validatePartSize(input.getPartSize());
        input.setTaskNum(Util.determineTaskNum(input.getTaskNum()));
        File file = new File(input.getFilePath());
        if (!file.exists()) {
            throw new TosClientException("invalid file path, the file does not exist: " + input.getFilePath(), null);
        }
        if (file.isDirectory()) {
            // 不支持文件夹上传
            throw new TosClientException("do not support directory, please specific your file path", null);
        }
    }

    private void validateCheckpointPath() {
        String checkpointFileSuffix = Util.checkpointPathMd5(input.getBucket(), input.getKey(), "")
                + Consts.UPLOAD_CHECKPOINT_FILE_SUFFIX;
        if (StringUtils.isEmpty(input.getCheckpointFile())) {
            input.setCheckpointFile(input.getFilePath() + "." + checkpointFileSuffix);
        } else {
            File ufcf = new File(input.getCheckpointFile());
            if (ufcf.isDirectory()) {
                input.setCheckpointFile(input.getCheckpointFile() + File.separator + checkpointFileSuffix);
            }
        }
        ParamsChecker.ensureNotNull(input.getCheckpointFile(), "checkpointFilePath");
    }

    private void setCheckpoint(UploadFileInfo fileInfo) {
        UploadFileV2Checkpoint checkpoint = null;
        if (this.input.isEnableCheckpoint()) {
            try{
                checkpoint = loadCheckpointFromFile(input.getCheckpointFile());
            } catch (IOException | ClassNotFoundException e){
                Util.deleteCheckpointFile(input.getCheckpointFile());
            }
        }
        boolean valid = false;
        if (checkpoint != null) {
            valid = checkpoint.isValid(fileInfo.getFileSize(), fileInfo.getLastModified(),
                    this.input.getBucket(), this.input.getKey(), input.getFilePath());
            if (!valid) {
                Util.deleteCheckpointFile(input.getCheckpointFile());
            } else {
                long uploadedBytes = 0;
                for (UploadPartInfo partInfo : checkpoint.getUploadPartInfos()) {
                    if (partInfo.isCompleted()) {
                        uploadedBytes += partInfo.getPartSize();
                    }
                }
                consumedBytes.compareAndSet(consumedBytes.get(), uploadedBytes);
            }
        }
        if (checkpoint == null || !valid) {
            checkpoint = initCheckpoint(fileInfo);
            if (input.isEnableCheckpoint()) {
                try{
                    checkpoint.writeToFile(input.getCheckpointFile());
                } catch (IOException e) {
                    throw new TosClientException("tos: record to checkpoint file failed", e);
                }
            }
        }
        this.checkpoint = checkpoint;
    }

    private UploadFileInfo getUploadFileInfo(String uploadFilePath){
        File file = new File(uploadFilePath);
        return new UploadFileInfo().setFilePath(uploadFilePath).setFileSize(file.length()).setLastModified(file.lastModified());
    }

    private UploadFileV2Checkpoint initCheckpoint(UploadFileInfo info) throws TosException {
        UploadFileV2Checkpoint checkpoint = new UploadFileV2Checkpoint()
                .setBucket(input.getBucket())
                .setKey(input.getKey())
                .setUploadPartInfos(getPartsFromFile(info.getFileSize(), input.getPartSize()))
                .setFilePath(info.getFilePath()).setFileSize(info.getFileSize()).setLastModified(info.getLastModified());
        CreateMultipartUploadOutput output;
        UploadEvent createMultipart = new UploadEvent()
                .setBucket(input.getBucket())
                .setKey(input.getKey())
                .setCheckpointFile(input.getCheckpointFile())
                .setFilePath(input.getFilePath());
        try {
            output = this.handler.createMultipartUpload(new CreateMultipartUploadInput().setBucket(input.getBucket())
                    .setKey(input.getKey()).setOptions(input.getOptions()).setEncodingType(input.getEncodingType()));
            Util.postUploadEvent(this.input.getUploadEventListener(), createMultipart.setUploadID(output.getUploadID())
                    .setUploadEventType(UploadEventType.UploadEventCreateMultipartUploadSucceed));
        }catch (TosException e) {
            Util.postUploadEvent(this.input.getUploadEventListener(), createMultipart.setTosException(e)
                    .setUploadEventType(UploadEventType.UploadEventCreateMultipartUploadFailed));
            throw e;
        }

        checkpoint.setUploadID(output.getUploadID()).setEncodingType(output.getEncodingType());
        return checkpoint;
    }

    private List<UploadPartInfo> getPartsFromFile(long uploadFileSize, long partSize) {
        long partNum = uploadFileSize / partSize;
        long lastPartSize = uploadFileSize % partSize;
        if (lastPartSize != 0) {
            partNum++;
        }
        if (partNum > Consts.MAX_PART_NUM) {
            throw new TosClientException("unsupported part number, the maximum is 10000", null);
        }
        List<UploadPartInfo> partInfoList = new ArrayList<>((int) partNum);
        for(int i = 0; i < partNum; i++) {
            if (i < partNum-1) {
                partInfoList.add(new UploadPartInfo().setPartSize(partSize).setPartNumber(i+1).setOffset(i * partSize));
            } else {
                partInfoList.add(new UploadPartInfo().setPartSize(lastPartSize).setPartNumber(i+1).setOffset(i * partSize));
            }
        }
        if (partNum == 0) {
            // 空文件场景
            partInfoList.add(new UploadPartInfo().setPartNumber(1).setPartSize(0).setOffset(0));
        }
        return partInfoList;
    }

    private UploadFileV2Checkpoint loadCheckpointFromFile(String checkpointFilePath) throws IOException, ClassNotFoundException{
        ParamsChecker.ensureNotNull(checkpointFilePath, "checkpointFilePath is null");
        File f = new File(checkpointFilePath);
        try(FileInputStream checkpointFile = new FileInputStream(f)) {
            byte[] data = new byte[(int)f.length()];
            checkpointFile.read(data);
            return TosUtils.getJsonMapper().readValue(data, new TypeReference<UploadFileV2Checkpoint>(){});
        }
    }

    private void combineCrcAndCheck(String serverCrc64Value) {
        if (checkpoint.getUploadPartInfos() == null
                || checkpoint.getUploadPartInfos().size() == 0
                || serverCrc64Value == null) {
            return;
        }
        long crc = getCrc();
        if (!Utils.isSameHashCrc64Ecma(crc, serverCrc64Value)) {
            if (input.isEnableCheckpoint()) {
                new File(this.input.getCheckpointFile()).delete();
            }
            throw new TosClientException("tos: expect crc64 " + serverCrc64Value +
                    ", actual crc64 " + crc, null);
        }
    }

    private long getCrc() {
        checkpoint.getUploadPartInfos().sort(new Comparator<UploadPartInfo>() {
            @Override
            public int compare(UploadPartInfo o1, UploadPartInfo o2) {
                return o1.getPartNumber() - o2.getPartNumber();
            }
        });
        long crc = checkpoint.getUploadPartInfos().get(0).getHashCrc64ecma();
        for (int i = 1; i < checkpoint.getUploadPartInfos().size(); i++) {
            long len = checkpoint.getUploadPartInfos().get(i).getPartSize();
            crc = CRC64Utils.combine(crc, checkpoint.getUploadPartInfos().get(i).getHashCrc64ecma(), len);
        }
        return crc;
    }
}

package com.volcengine.tos.internal.taskman;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.Utils;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.comm.event.DownloadEventType;
import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.internal.TosObjectRequestHandler;
import com.volcengine.tos.internal.util.*;
import com.volcengine.tos.model.object.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DownloadFileTaskHandler {
    private DownloadFileInput input;
    private TosObjectRequestHandler handler;
    private DownloadFileCheckpoint checkpoint;
    private HeadObjectV2Output headObjectV2Output;
    private boolean enableCrcCheck;
    private TaskManager taskMan;
    private List<DownloadPartInfo> downloadPartInfos;
    private AbortTaskHook abortTaskHook;
    private final AtomicLong consumedBytes;

    public DownloadFileTaskHandler(DownloadFileInput input, TosObjectRequestHandler handler, boolean enableCrcCheck) {
        ParamsChecker.ensureNotNull(input, "DownloadFileInput");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        ParamsChecker.ensureNotNull(handler, "TosObjectRequestHandler");
        this.input = input;
        this.handler = handler;
        this.enableCrcCheck = enableCrcCheck;
        this.consumedBytes = new AtomicLong(0);
    }

    public void initTask() {
        validateInput();
        headObjectV2Output = handler.headObject(new HeadObjectV2Input().setBucket(input.getBucket()).setKey(input.getKey())
                .setVersionID(input.getVersionID()).setOptions(input.getOptions()));
        String newPath = FileUtils.parseFilePath(input.getFilePath(), input.getKey());
        if (StringUtils.isEmpty(newPath)) {
            return;
        }
        input.setFilePath(newPath);
        input.setTempFilePath(newPath + Consts.TEMP_FILE_SUFFIX);
        if (this.input.isEnableCheckpoint()) {
            validateCheckpointPath();
        }
        setCheckpoint(headObjectV2Output);
        int partsNum = this.checkpoint.getDownloadPartInfos().size();
        this.abortTaskHook = new DownloadFileTaskCanceler(this.handler, this.taskMan, this.checkpoint.getBucket(),
                this.checkpoint.getKey(), this.input.getCheckpointFile(), this.input.isEnableCheckpoint(), this.input.getTempFilePath());
        this.taskMan = new TaskManagerImpl(this.input.getTaskNum(), partsNum, null, this.abortTaskHook);
        if (this.input.getCancelHook() != null && this.input.getCancelHook() instanceof DownloadFileTaskCanceler) {
            ((DownloadFileTaskCanceler) this.input.getCancelHook()).setHandler(this.handler).setTaskMan(this.taskMan)
                    .setBucket(checkpoint.getBucket()).setKey(checkpoint.getKey()).setTempFilePath(input.getTempFilePath())
                    .setEnableCheckpoint(input.isEnableCheckpoint()).setCheckpointFilePath(input.getCheckpointFile());
        }
        this.downloadPartInfos = new ArrayList<>(partsNum);
    }

    public void dispatch() {
        for (int i = 0; i < this.checkpoint.getDownloadPartInfos().size(); i++) {
            if (!this.checkpoint.getDownloadPartInfos().get(i).isCompleted()) {
                taskMan.dispatch(new DownloadFileTask(this.checkpoint, i, consumedBytes)
                        .setEnableCheckpoint(input.isEnableCheckpoint())
                        .setCheckpointFile(input.getCheckpointFile())
                        .setEnableCrcCheck(this.enableCrcCheck)
                        .setDownloadEventListener(input.getDownloadEventListener())
                        .setHandler(handler)
                        .setHeadObjectV2Input(new HeadObjectV2Input().setBucket(input.getBucket())
                                .setKey(input.getKey()).setOptions(input.getOptions()).setVersionID(input.getVersionID()))
                        .setRateLimiter(input.getRateLimiter())
                        .setDataTransferListener(input.getDataTransferListener()));
            } else {
                downloadPartInfos.add(checkpoint.getDownloadPartInfos().get(i));
            }
        }
    }

    public DownloadFileOutput handle() {
        Util.postDataTransferStatus(this.input.getDataTransferListener(), new DataTransferStatus()
                .setType(DataTransferType.DATA_TRANSFER_STARTED)
                .setTotalBytes(checkpoint.getDownloadObjectInfo().getObjectSize())
                .setConsumedBytes(this.consumedBytes.get()));
        this.taskMan.handle();
        List<TaskOutput<?>> outputs = this.taskMan.get();
        for (TaskOutput<?> output : outputs) {
            DownloadPartInfo tmp = (DownloadPartInfo) output.getOutput();
            if (tmp == null) {
                continue;
            }
            downloadPartInfos.add(tmp);
        }
        DataTransferStatus status = new DataTransferStatus().setConsumedBytes(this.consumedBytes.get())
                .setTotalBytes(checkpoint.getDownloadObjectInfo().getObjectSize());
        if (!readyForComplete()) {
            throw new TosClientException("tos: some download tasks failed.", null);
        }
        Util.postDataTransferStatus(this.input.getDataTransferListener(), status.setType(DataTransferType.DATA_TRANSFER_SUCCEED));

        return completeDownloadFileTask();
    }

    private DownloadFileOutput completeDownloadFileTask() {
        DownloadEvent event = new DownloadEvent()
                .setBucket(this.checkpoint.getBucket())
                .setKey(this.checkpoint.getKey())
                .setCheckpointFile(this.input.getCheckpointFile())
                .setFilePath(this.input.getFilePath());

        if (this.enableCrcCheck) {
            combineCrcAndCheck();
        }

        try{
            renameFile();
            Util.postDownloadEvent(this.input.getDownloadEventListener(),
                    event.setDownloadEventType(DownloadEventType.DownloadEventRenameTempFileSucceed));
        } catch (TosException e) {
            Util.postDownloadEvent(this.input.getDownloadEventListener(),
                    event.setTosException(e).setDownloadEventType(DownloadEventType.DownloadEventRenameTempFileFailed));
            throw e;
        }
        if (this.input.isEnableCheckpoint()) {
            Util.deleteCheckpointFile(this.input.getCheckpointFile());
        }
        return new DownloadFileOutput().setOutput(headObjectV2Output);
    }

    private void validateInput() {
        if (input.getPartSize() == 0) {
            input.setPartSize(Consts.DEFAULT_PART_SIZE);
        }
        Util.validatePartSize(input.getPartSize());
        input.setTaskNum(Util.determineTaskNum(input.getTaskNum()));
    }

    private void validateCheckpointPath() {
        String checkpointFileSuffix = Util.checkpointPathMd5(input.getBucket(), input.getKey(), input.getVersionID()) +
                Consts.DOWNLOAD_CHECKPOINT_FILE_SUFFIX;
        if (StringUtils.isEmpty(input.getCheckpointFile())) {
            input.setCheckpointFile(input.getFilePath() + checkpointFileSuffix);
        } else {
            File ufcf = new File(input.getCheckpointFile());
            if (ufcf.isDirectory()) {
                throw new TosClientException("The input checkpoint file is directory: " + input.getCheckpointFile(), null);
            }
        }
    }

    private void createTempFile() {
        DownloadEvent downloadEvent = new DownloadEvent().setBucket(input.getBucket()).setKey(input.getKey())
                .setVersionID(input.getVersionID()).setFilePath(input.getFilePath());
        File file = new File(this.input.getTempFilePath());
        try{
            if (file.exists()) {
                TosUtils.getLogger().debug("tos: temp file already exists.");
            } else {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            Util.postDownloadEvent(this.input.getDownloadEventListener(),
                    downloadEvent.setDownloadEventType(DownloadEventType.DownloadEventCreateTempFileSucceed));
        } catch (IOException e) {
            Util.postDownloadEvent(this.input.getDownloadEventListener(),
                    downloadEvent.setDownloadEventType(DownloadEventType.DownloadEventCreateTempFileFailed));
            throw new TosClientException("tos: create temp file failed.", e);
        }
    }

    private void setCheckpoint(HeadObjectV2Output head) {
        DownloadFileCheckpoint checkpoint = null;
        if (this.input.isEnableCheckpoint()) {
            try{
                checkpoint = loadCheckpointFromFile(input.getCheckpointFile());
            } catch (IOException | ClassNotFoundException e){
                TosUtils.getLogger().debug("loadCheckpointFromFile failed, {}", e.toString());
                Util.deleteCheckpointFile(input.getCheckpointFile());
            }
        }
        boolean valid = false;
        if (checkpoint != null) {
            String etag = null;
            if (head != null) {
                etag = head.getEtag();
            }
            valid = checkpoint.isValid(this.input.getBucket(), this.input.getKey(), input.getFilePath(), etag);
            if (!valid) {
                Util.deleteCheckpointFile(input.getCheckpointFile());
            } else {
                // checkpoint exists and is valid, set consumed bytes
                long downloadedBytes = 0;
                for (DownloadPartInfo partInfo : checkpoint.getDownloadPartInfos()) {
                    if (partInfo.isCompleted()) {
                        downloadedBytes += partInfo.getRangeEnd() - partInfo.getRangeStart() + 1;
                    }
                }
                consumedBytes.compareAndSet(consumedBytes.get(), downloadedBytes);
            }
        }
        if (checkpoint == null || !valid) {
            createTempFile();
            checkpoint = initCheckpoint(head);
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

    private DownloadFileCheckpoint loadCheckpointFromFile(String checkpointFilePath) throws IOException, ClassNotFoundException{
        ParamsChecker.ensureNotNull(checkpointFilePath, "checkpointFilePath is null");
        File f = new File(checkpointFilePath);
        try(FileInputStream checkpointFile = new FileInputStream(f)) {
            byte[] data = new byte[(int)f.length()];
            checkpointFile.read(data);
            return TosUtils.getJsonMapper().readValue(data, new TypeReference<DownloadFileCheckpoint>(){});
        }
    }

    private DownloadFileCheckpoint initCheckpoint(HeadObjectV2Output head) throws TosException {
        ParamsChecker.ensureNotNull(head, "HeadObjectV2Output");
        return new DownloadFileCheckpoint().setBucket(input.getBucket()).setKey(input.getKey()).setVersionID(input.getVersionID())
                .setIfMatch(input.getOptions() == null ? null : input.getOptions().getIfMatch())
                .setIfModifiedSince(input.getOptions() == null ? null : input.getOptions().getIfModifiedSince())
                .setIfNoneMatch(input.getOptions() == null ? null : input.getOptions().getIfNoneMatch())
                .setIfUnModifiedSince(input.getOptions() == null ? null : input.getOptions().getIfUnmodifiedSince())
                .setSsecAlgorithm(input.getOptions() == null ? null : input.getOptions().getSsecAlgorithm())
                .setSsecKeyMD5(input.getOptions() == null ? null : input.getOptions().getSsecKeyMD5())
                .setDownloadFileInfo(new DownloadFileInfo().setFilePath(input.getFilePath()).setTempFilePath(input.getTempFilePath()))
                .setDownloadObjectInfo(new DownloadObjectInfo().setObjectSize(head.getContentLength()).setEtag(head.getEtag())
                        .setHashCrc64ecma(head.getHashCrc64ecma()).setLastModified(head.getLastModifiedInDate()))
                .setPartSize(input.getPartSize()).setDownloadPartInfos(getPartsFromFile(input.getPartSize(), head.getContentLength()));
    }

    private List<DownloadPartInfo> getPartsFromFile(long partSize, long contentLength) {
        long partNum = contentLength / partSize;
        long lastPartSize = contentLength % partSize;
        if (lastPartSize != 0) {
            partNum++;
        }
        List<DownloadPartInfo> partInfoList = new ArrayList<>((int) partNum);
        for(int i = 0; i < partNum; i++) {
            partInfoList.add(new DownloadPartInfo().setPartNumber(i+1).setRangeStart(i * partSize).setRangeEnd((i+1) * partSize - 1));
        }
        partInfoList.get((int)partNum-1).setRangeEnd((partNum-1) * partSize + lastPartSize- 1);
        return partInfoList;
    }

    private boolean readyForComplete() {
        if (checkpoint == null || checkpoint.getBucket() == null
                || checkpoint.getKey() == null || checkpoint.getDownloadPartInfos() == null) {
            return false;
        }
        if (downloadPartInfos.size() != checkpoint.getDownloadPartInfos().size()) {
            return false;
        }
        for (DownloadPartInfo part : checkpoint.getDownloadPartInfos()) {
            if (!part.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    private void combineCrcAndCheck() {
        boolean headResNotNull = headObjectV2Output != null;
        if (!headResNotNull) {
            return;
        }
        if (headObjectV2Output.getHashCrc64ecma() == null) {
            return;
        }
        if (downloadPartInfos == null || downloadPartInfos.size() == 0) {
            return;
        }
        String serverCrc64 = headObjectV2Output.getHashCrc64ecma();
        long crc = computeClientCrc();
        if (!Utils.isSameHashCrc64Ecma(crc, serverCrc64)) {
            clearTmpFile();
            throw new TosClientException("tos: expect crc64 " + serverCrc64 + ", actual crc64 " + crc, null);
        }
    }

    private void clearTmpFile() {
        if (input.isEnableCheckpoint()) {
            new File(input.getCheckpointFile()).delete();
        }
        new File(input.getTempFilePath()).delete();
    }

    private long computeClientCrc() {
        downloadPartInfos.sort(new Comparator<DownloadPartInfo>() {
            @Override
            public int compare(DownloadPartInfo o1, DownloadPartInfo o2) {
                return o1.getPartNumber() - o2.getPartNumber();
            }
        });
        long crc = downloadPartInfos.get(0).getHashCrc64ecma();
        for (int i = 1; i < downloadPartInfos.size(); i++) {
            long len = downloadPartInfos.get(i).getRangeEnd() - downloadPartInfos.get(i).getRangeStart() + 1;
            crc = CRC64Utils.combine(crc, downloadPartInfos.get(i).getHashCrc64ecma(), len);
        }
        return crc;
    }

    private void renameFile() {
        File fileSrc = new File(this.input.getTempFilePath());
        File fileDst = new File(this.input.getFilePath());
        if (!fileSrc.exists()) {
            throw new TosClientException("tos: temp download file not found, " + this.input.getTempFilePath(), null);
        }
        if (!fileSrc.renameTo(fileDst)) {
            throw new TosClientException("tos: move temp file to dst file failed", null);
        }
    }
}

package com.volcengine.tos.internal.taskman;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.CopyEventType;
import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.internal.TosObjectRequestHandler;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.object.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResumableCopyObjectTaskHandler {
    private final ResumableCopyObjectInput input;
    private final TosObjectRequestHandler handler;
    private ResumableCopyObjectCheckpoint checkpoint;
    private boolean enableCrcCheck;
    private TaskManager taskMan;
    private List<UploadedPartV2> copiedPartInfos;
    private AbortTaskHook abortTaskHook;

    public ResumableCopyObjectTaskHandler(ResumableCopyObjectInput input, TosObjectRequestHandler handler, boolean enableCrcCheck) {
        ParamsChecker.ensureNotNull(input, "ResumableCopyObjectInput");
        ParamsChecker.isValidBucketNameAndKey(input.getSrcBucket(), input.getSrcKey());
        ParamsChecker.ensureNotNull(handler, "TosObjectRequestHandler");
        this.input = input;
        this.handler = handler;
        this.enableCrcCheck = enableCrcCheck;
    }

    public void initTask() {
        validateInput();
        if (this.input.isEnableCheckpoint()) {
            validateCheckpointPath();
        }
        CopySourceObjectInfo copySourceObjectInfo = getCopySourceObjectInfo(input.getSrcBucket(), input.getSrcKey());
        setCheckpoint(copySourceObjectInfo);
        int partsNum = this.checkpoint.getCopyPartInfoList().size();
        this.abortTaskHook = new ResumableCopyObjectTaskCanceler(this.handler, this.taskMan, this.checkpoint.getBucket(), this.checkpoint.getKey(),
                this.checkpoint.getUploadID(), this.input.getCheckpointFile(), this.input.isEnableCheckpoint());
        this.taskMan = new TaskManagerImpl(this.input.getTaskNum(), partsNum, null, this.abortTaskHook);
        if (this.input.getCancelHook() != null && this.input.getCancelHook() instanceof ResumableCopyObjectTaskCanceler) {
            ((ResumableCopyObjectTaskCanceler) this.input.getCancelHook()).setHandler(this.handler).setTaskMan(this.taskMan)
                    .setBucket(checkpoint.getBucket()).setKey(checkpoint.getKey()).setUploadID(checkpoint.getUploadID())
                    .setEnableCheckpoint(input.isEnableCheckpoint()).setCheckpointFilePath(input.getCheckpointFile());
        }
        this.copiedPartInfos = new ArrayList<>(partsNum);
    }

    public void dispatch() {
        for (int i = 0; i < this.checkpoint.getCopyPartInfoList().size(); i++) {
            if (!this.checkpoint.getCopyPartInfoList().get(i).isCompleted()) {
                taskMan.dispatch(new ResumableCopyObjectTask(this.checkpoint, i)
                        .setEnableCheckpoint(input.isEnableCheckpoint())
                        .setCheckpointFile(input.getCheckpointFile())
                        .setCopyEventListener(input.getCopyEventListener())
                        .setOptions(input.getOptions())
                        .setCopySourceSSECKey(input.getCopySourceSSECKey())
                        .setHandler(handler)
                        .setTrafficLimit(input.getTrafficLimit()));
            } else {
                CopyPartInfo partInfo = checkpoint.getCopyPartInfoList().get(i);
                copiedPartInfos.add(new UploadedPartV2().setPartNumber(partInfo.getPartNumber()).setEtag(partInfo.getEtag()));
            }
        }
    }

    public ResumableCopyObjectOutput handle() {
        this.taskMan.handle();
        List<TaskOutput<?>> outputs = this.taskMan.get();
        for (TaskOutput<?> output : outputs) {
            UploadPartCopyV2Output tmp = (UploadPartCopyV2Output) output.getOutput();
            if (tmp == null) {
                continue;
            }
            copiedPartInfos.add(new UploadedPartV2().setPartNumber(tmp.getPartNumber()).setEtag(tmp.getEtag()));
        }
        if (!readyForComplete()) {
            String message = "tos: some copy tasks failed. bucket is " + this.input.getBucket() +
                    ", dest key is " + this.input.getKey() + ", source key is " + this.input.getSrcKey();
            throw new TosClientException(message, null);
        }
        return completeResumableCopyObjectTask();
    }

    private ResumableCopyObjectOutput completeResumableCopyObjectTask() {
        CompleteMultipartUploadV2Input input = new CompleteMultipartUploadV2Input()
                .setBucket(this.checkpoint.getBucket())
                .setKey(this.checkpoint.getKey())
                .setUploadID(this.checkpoint.getUploadID())
                .setUploadedParts(copiedPartInfos);
        CompleteMultipartUploadV2Output comp;
        CopyEvent event = new CopyEvent().setUploadID(this.checkpoint.getUploadID())
                .setBucket(this.checkpoint.getBucket())
                .setKey(this.checkpoint.getKey())
                .setCheckpointFile(this.input.getCheckpointFile())
                .setSrcBucket(this.input.getSrcBucket())
                .setSrcKey(this.input.getSrcKey())
                .setSrcVersionID(this.input.getSrcVersionID());
        try {
            comp = this.handler.completeMultipartUpload(input);
            if (enableCrcCheck) {
                String srcCrc64 = checkpoint.getCopySourceObjectInfo().getHashCrc64ecma();
                String dstCrc64 = comp.getHashCrc64ecma();
                if (!StringUtils.equals(srcCrc64, dstCrc64)) {
                    throw new TosClientException("tos: expect crc64 " + srcCrc64 +
                            ", actual crc64 " + dstCrc64, null);
                }
            }
            Util.postCopyEvent(this.input.getCopyEventListener(),
                    event.setType(CopyEventType.CopyEventCompleteMultipartUploadSucceed));
        } catch (TosException e) {
            Util.postCopyEvent(this.input.getCopyEventListener(),
                    event.setException(e).setType(CopyEventType.CopyEventCompleteMultipartUploadFailed));
            throw e;
        }
        if (this.input.isEnableCheckpoint()) {
            Util.deleteCheckpointFile(this.input.getCheckpointFile());
        }
        return new ResumableCopyObjectOutput().setRequestInfo(comp.getRequestInfo())
                .setBucket(comp.getBucket())
                .setKey(comp.getKey())
                .setUploadID(checkpoint.getUploadID())
                .setEtag(comp.getEtag())
                .setLocation(comp.getLocation())
                .setHashCrc64ecma(comp.getHashCrc64ecma())
                .setVersionID(comp.getVersionID())
                .setSsecAlgorithm(checkpoint.getSsecAlgorithm())
                .setSsecKeyMD5(checkpoint.getSsecKeyMD5())
                .setEncodingType(checkpoint.getEncodingType());
    }

    private boolean readyForComplete() {
        if (checkpoint == null || checkpoint.getBucket() == null || checkpoint.getKey() == null
                || checkpoint.getUploadID() == null || checkpoint.getCopyPartInfoList() == null
                || copiedPartInfos == null || checkpoint.getCopySourceObjectInfo() == null) {
            return false;
        }
        if (copiedPartInfos.size() != checkpoint.getCopyPartInfoList().size()) {
            return false;
        }
        for (CopyPartInfo part : checkpoint.getCopyPartInfoList()) {
            if (!part.isCompleted()) {
                return false;
            }
        }
        return true;
    }

    private void validateInput() {
        if (input.getPartSize() == 0) {
            input.setPartSize(Consts.DEFAULT_PART_SIZE);
        }
        Util.validatePartSize(input.getPartSize());
        input.setTaskNum(Util.determineTaskNum(input.getTaskNum()));
    }

    private void validateCheckpointPath() {
        String checkpointFileSuffix = Util.checkpointPathMd5(input.getBucket(), input.getKey(), "")
                + Consts.COPY_CHECKPOINT_FILE_SUFFIX;
        if (StringUtils.isEmpty(input.getCheckpointFile())) {
            throw new TosClientException("tos: ResumableCopyObject enable checkpoint but checkpoint file path is not set.", null);
        } else {
            File ufcf = new File(input.getCheckpointFile());
            if (ufcf.isDirectory()) {
                input.setCheckpointFile(input.getCheckpointFile() + File.separator + checkpointFileSuffix);
            }
        }
        ParamsChecker.ensureNotNull(input.getCheckpointFile(), "checkpointFilePath");
    }

    private void setCheckpoint(CopySourceObjectInfo copySourceObjectInfo) {
        ResumableCopyObjectCheckpoint checkpoint = null;
        if (this.input.isEnableCheckpoint()) {
            try{
                checkpoint = loadCheckpointFromFile(input.getCheckpointFile());
            } catch (IOException | ClassNotFoundException e){
                Util.deleteCheckpointFile(input.getCheckpointFile());
            }
        }
        boolean valid = false;
        if (checkpoint != null) {
            valid = checkpoint.isValid(copySourceObjectInfo, this.input.getBucket(), this.input.getKey(),
                    this.input.getSrcBucket(), this.input.getSrcKey(), this.input.getSrcVersionID());
        }
        if (!valid) {
            Util.deleteCheckpointFile(input.getCheckpointFile());
        }
        if (checkpoint == null || !valid) {
            checkpoint = initCheckpoint(copySourceObjectInfo);
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

    private CopySourceObjectInfo getCopySourceObjectInfo(String bucket, String key){
        // head source object
        try{
            HeadObjectV2Output head = this.handler.headObject(new HeadObjectV2Input().setBucket(bucket).setKey(key));
            GetObjectBasicOutput basicOutput = head.getHeadObjectBasicOutput();
            return new CopySourceObjectInfo().setObjectSize(basicOutput.getContentLength())
                    .setHashCrc64ecma(basicOutput.getHashCrc64ecma()).setEtag(basicOutput.getEtag())
                    .setLastModified(basicOutput.getLastModifiedInDate());
        } catch (TosException e) {
            throw new TosClientException("tos: ResumableCopyObject get copySourceObject info failed", e);
        }
    }

    private ResumableCopyObjectCheckpoint initCheckpoint(CopySourceObjectInfo info) throws TosException {
        String ssecAlgorithm = null, ssecKeyMD5 = null;
        if (input.getOptions() != null) {
            ssecAlgorithm = input.getOptions().getSsecAlgorithm();
            ssecKeyMD5 = input.getOptions().getSsecKeyMD5();
        }
        ResumableCopyObjectCheckpoint checkpoint = new ResumableCopyObjectCheckpoint()
                .setBucket(input.getBucket())
                .setKey(input.getKey())
                .setSrcBucket(input.getSrcBucket())
                .setSrcKey(input.getSrcKey())
                .setCopyPartInfoList(getPartsFromSourceObject(info.getObjectSize(), input.getPartSize()))
                .setCopySourceObjectInfo(info).setPartSize(input.getPartSize())
                .setCopySourceIfMatch(input.getCopySourceIfMatch() == null ? info.getEtag() : input.getCopySourceIfMatch())
                .setCopySourceIfModifiedSince(input.getCopySourceIfModifiedSince())
                .setCopySourceIfNoneMatch(input.getCopySourceIfNoneMatch())
                .setCopySourceIfUnModifiedSince(input.getCopySourceIfUnModifiedSince())
                .setCopySourceSSECAlgorithm(input.getCopySourceSSECAlgorithm())
                .setCopySourceSSECKeyMD5(input.getCopySourceSSECKeyMD5())
                .setSsecAlgorithm(ssecAlgorithm).setSsecKeyMD5(ssecKeyMD5);
        CreateMultipartUploadOutput output;
        CopyEvent createMultipart = new CopyEvent().setBucket(input.getBucket()).setKey(input.getKey()).setCheckpointFile(input.getCheckpointFile())
                .setSrcBucket(input.getSrcBucket()).setSrcKey(input.getSrcKey()).setSrcVersionID(input.getSrcVersionID());
        try {
            output = this.handler.createMultipartUpload(new CreateMultipartUploadInput().setBucket(input.getBucket())
                    .setKey(input.getKey()).setOptions(input.getOptions()).setEncodingType(input.getEncodingType()));
            Util.postCopyEvent(this.input.getCopyEventListener(), createMultipart.setUploadID(output.getUploadID())
                    .setType(CopyEventType.CopyEventCreateMultipartUploadSucceed));
            checkpoint.setUploadID(output.getUploadID()).setEncodingType(output.getEncodingType());
        } catch (TosException e) {
            Util.postCopyEvent(this.input.getCopyEventListener(), createMultipart.setException(e)
                    .setType(CopyEventType.CopyEventCreateMultipartUploadFailed));
            throw e;
        }
        return checkpoint;
    }

    private List<CopyPartInfo> getPartsFromSourceObject(long copySourceObjectSize, long partSize) {
        long partNum = copySourceObjectSize / partSize;
        long lastPartSize = copySourceObjectSize % partSize;
        if (lastPartSize != 0) {
            partNum++;
        }
        if (partNum > Consts.MAX_PART_NUM) {
            throw new TosClientException("unsupported part number, the maximum is 10000", null);
        }
        List<CopyPartInfo> partInfoList = new ArrayList<>((int) partNum);
        for(int i = 0; i < partNum; i++) {
            if (i < partNum-1) {
                partInfoList.add(new CopyPartInfo().setPartNumber(i+1).setCopySourceRangeStart(i * partSize).setCopySourceRangeEnd((i+1) * partSize - 1));
            } else {
                partInfoList.add(new CopyPartInfo().setPartNumber(i+1).setCopySourceRangeStart(i * partSize).setCopySourceRangeEnd((partNum-1) * partSize + lastPartSize- 1));
            }
        }
        if (partNum == 0) {
            // 空对象场景
            partInfoList.add(new CopyPartInfo().setPartNumber(1).setCopySourceRangeStart(0).setCopySourceRangeEnd(0));
        }
        return partInfoList;
    }

    private ResumableCopyObjectCheckpoint loadCheckpointFromFile(String checkpointFilePath) throws IOException, ClassNotFoundException{
        ParamsChecker.ensureNotNull(checkpointFilePath, "checkpointFilePath is null");
        File f = new File(checkpointFilePath);
        try(FileInputStream checkpointFile = new FileInputStream(f)) {
            byte[] data = new byte[(int)f.length()];
            checkpointFile.read(data);
            return TosUtils.getJsonMapper().readValue(data, new TypeReference<ResumableCopyObjectCheckpoint>(){});
        }
    }
}

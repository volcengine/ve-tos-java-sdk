package com.volcengine.tos.internal.taskman;

import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.*;
import com.volcengine.tos.internal.TosObjectRequestHandler;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.model.object.*;

import java.util.concurrent.Callable;

public class ResumableCopyObjectTask extends TosTask implements TaskOutput<UploadPartCopyV2Output> {
    private ResumableCopyObjectCheckpoint checkpoint;
    private String copySourceSSECKey;
    private CopyPartInfo partInfo;
    private boolean enableCheckpoint;
    private String checkpointFile;
    private CopyEventListener copyEventListener;
    private TosObjectRequestHandler handler;
    private UploadPartCopyV2Output output;
    private ObjectMetaRequestOptions options;

    public ResumableCopyObjectTask(ResumableCopyObjectCheckpoint checkpoint, int taskIdx) {
        ParamsChecker.ensureNotNull(checkpoint, "ResumableCopyObjectCheckpoint");
        ParamsChecker.ensureNotNull(checkpoint.getCopySourceObjectInfo(), "CopySourceObjectInfo");
        ParamsChecker.ensureNotNull(checkpoint.getCopyPartInfoList(), "CopyPartInfo list");
        this.checkpoint = checkpoint;
        this.partInfo = checkpoint.getCopyPartInfoList().get(taskIdx);
    }

    @Override
    public Callable<TaskOutput<?>> getCallableTask() {
        return () -> {
            UploadPartCopyV2Input input = UploadPartCopyV2Input.builder()
                    .bucket(checkpoint.getBucket())
                    .key(checkpoint.getKey())
                    .sourceBucket(checkpoint.getSrcBucket())
                    .sourceKey(checkpoint.getSrcKey())
                    .sourceVersionID(checkpoint.getSrcVersionID())
                    .uploadID(checkpoint.getUploadID())
                    .partNumber(partInfo.getPartNumber())
                    .copySourceRange(partInfo.getCopySourceRangeStart(), partInfo.getCopySourceRangeEnd())
                    .copySourceIfMatch(checkpoint.getCopySourceIfMatch())
                    .copySourceIfModifiedSinceDate(checkpoint.getCopySourceIfModifiedSince())
                    .copySourceIfNoneMatch(checkpoint.getCopySourceIfNoneMatch())
                    .copySourceIfUnmodifiedSinceDate(checkpoint.getCopySourceIfUnModifiedSince())
                    .copySourceSSECAlgorithm(checkpoint.getCopySourceSSECAlgorithm())
                    .copySourceSSECKeyMD5(checkpoint.getCopySourceSSECKeyMD5())
                    .copySourceSSECKey(copySourceSSECKey)
                    .options(this.options).build();
            CopyEvent event = new CopyEvent().setBucket(checkpoint.getBucket()).setKey(checkpoint.getKey())
                    .setSrcBucket(checkpoint.getSrcBucket()).setSrcKey(checkpoint.getSrcKey())
                    .setSrcVersionID(checkpoint.getSrcVersionID()).setCheckpointFile(checkpointFile)
                    .setUploadID(checkpoint.getUploadID());
            try{
                this.output = this.handler.uploadPartCopy(input);
                this.partInfo.setCompleted(true);
                System.out.println("we set it!!");
                this.partInfo.setEtag(this.output.getEtag());
                if (this.enableCheckpoint) {
                    this.checkpoint.writeToFile(this.checkpointFile);
                }
                Util.postCopyEvent(this.copyEventListener, event.setCopyPartInfo(this.partInfo)
                        .setType(CopyEventType.CopyEventUploadPartCopySucceed));
            } catch (TosException e) {
                if (Util.needAbortTask(e.getStatusCode())) {
                    Util.postCopyEvent(this.copyEventListener, event.setException(e)
                            .setType(CopyEventType.CopyEventUploadPartCopyAborted));
                    throw e;
                } else {
                    Util.postCopyEvent(this.copyEventListener, event.setException(e)
                            .setType(CopyEventType.CopyEventUploadPartCopyFailed));
                }
            }
            return this;
        };
    }

    @Override
    public UploadPartCopyV2Output getOutput() {
        return this.output;
    }

    public ResumableCopyObjectCheckpoint getCheckpoint() {
        return checkpoint;
    }

    public ResumableCopyObjectTask setCheckpoint(ResumableCopyObjectCheckpoint checkpoint) {
        this.checkpoint = checkpoint;
        return this;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public ResumableCopyObjectTask setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public ResumableCopyObjectTask setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }

    public CopyEventListener getCopyEventListener() {
        return copyEventListener;
    }

    public ResumableCopyObjectTask setCopyEventListener(CopyEventListener copyEventListener) {
        this.copyEventListener = copyEventListener;
        return this;
    }

    public TosObjectRequestHandler getHandler() {
        return handler;
    }

    public ResumableCopyObjectTask setHandler(TosObjectRequestHandler handler) {
        this.handler = handler;
        return this;
    }

    public ResumableCopyObjectTask setOutput(UploadPartCopyV2Output output) {
        this.output = output;
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public ResumableCopyObjectTask setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public String getCopySourceSSECKey() {
        return copySourceSSECKey;
    }

    public ResumableCopyObjectTask setCopySourceSSECKey(String copySourceSSECKey) {
        this.copySourceSSECKey = copySourceSSECKey;
        return this;
    }
}

package com.volcengine.tos.internal.taskman;

import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.comm.event.UploadEventType;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.model.ConcurrentDataTransferListenInputStream;
import com.volcengine.tos.internal.util.CRC64Utils;
import com.volcengine.tos.model.object.UploadEvent;
import com.volcengine.tos.internal.TosObjectRequestHandler;
import com.volcengine.tos.internal.util.FileUtils;
import com.volcengine.tos.model.object.*;

import java.io.InputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;

class UploadFileTask extends TosTask implements TaskOutput<UploadPartV2Output> {
    private UploadFileV2Checkpoint checkpoint;
    private UploadPartInfo partInfo;
    private boolean enableCheckpoint;
    private String checkpointFile;
    private UploadEventListener uploadEventListener;
    private TosObjectRequestHandler handler;
    private UploadPartV2Output output;
    private ObjectMetaRequestOptions options;
    private RateLimiter rateLimiter;
    private DataTransferListener dataTransferListener;
    private final AtomicLong consumedBytes;

    public UploadFileTask(UploadFileV2Checkpoint checkpoint, int taskIdx, AtomicLong consumedBytes) {
        this.checkpoint = checkpoint;
        this.partInfo = checkpoint.getUploadPartInfos().get(taskIdx);
        this.consumedBytes = consumedBytes;
    }

    @Override
    public Callable<TaskOutput<?>> getCallableTask() {
        return () -> {
            InputStream in = FileUtils.getBoundedFileContent(null, null,
                    this.checkpoint.getFilePath(), this.partInfo.getOffset(), this.partInfo.getPartSize());
            if (dataTransferListener != null) {
                in = new ConcurrentDataTransferListenInputStream(in,
                        dataTransferListener, checkpoint.getFileSize(), consumedBytes);
            }
            UploadPartBasicInput basic = UploadPartBasicInput.builder()
                    .bucket(checkpoint.getBucket())
                    .key(checkpoint.getKey())
                    .uploadID(checkpoint.getUploadID())
                    .partNumber(partInfo.getPartNumber())
                    .rateLimiter(rateLimiter)
                    .options(this.options).build();
            UploadPartV2Input input = UploadPartV2Input.builder()
                    .uploadPartBasicInput(basic)
                    .content(in)
                    .contentLength(partInfo.getPartSize())
                    .build();
            UploadEvent event = new UploadEvent().setBucket(this.checkpoint.getBucket())
                    .setKey(this.checkpoint.getKey()).setCheckpointFile(this.checkpointFile)
                    .setUploadID(this.checkpoint.getUploadID()).setFilePath(this.checkpoint.getFilePath());
            DataTransferStatus status = new DataTransferStatus().setTotalBytes(checkpoint.getFileSize());
            try{
                this.output = this.handler.uploadPart(input);
                this.partInfo.setCompleted(true);
                this.partInfo.setEtag(this.output.getEtag());
                this.partInfo.setHashCrc64ecma(CRC64Utils.unsignedLongStringToLong(this.output.getHashCrc64ecma()));
                if (this.enableCheckpoint) {
                    this.checkpoint.writeToFile(this.checkpointFile);
                }
                Util.postUploadEvent(this.uploadEventListener, event.setUploadPartInfo(this.partInfo)
                        .setUploadEventType(UploadEventType.UploadEventUploadPartSucceed));
            } catch (TosException e) {
                if (Util.needAbortTask(e.getStatusCode())) {
                    Util.postUploadEvent(this.uploadEventListener, event.setTosException(e)
                            .setUploadEventType(UploadEventType.UploadEventUploadPartAborted));
                    Util.postDataTransferStatus(this.dataTransferListener, status.setType(DataTransferType.DATA_TRANSFER_FAILED)
                            .setConsumedBytes(consumedBytes.get()));
                    throw e;
                } else {
                    Util.postUploadEvent(this.uploadEventListener, event.setTosException(e)
                            .setUploadEventType(UploadEventType.UploadEventUploadPartFailed));
                }
            }
            return this;
        };
    }

    @Override
    public UploadPartV2Output getOutput() {
        return this.output;
    }

    public UploadFileV2Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public UploadFileTask setCheckpoint(UploadFileV2Checkpoint checkpoint) {
        this.checkpoint = checkpoint;
        return this;
    }

    public UploadPartInfo getPartInfo() {
        return partInfo;
    }

    public UploadFileTask setPartInfo(UploadPartInfo partInfo) {
        this.partInfo = partInfo;
        return this;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public UploadFileTask setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public UploadFileTask setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }

    public UploadEventListener getUploadEventListener() {
        return uploadEventListener;
    }

    public UploadFileTask setUploadEventListener(UploadEventListener uploadEventListener) {
        this.uploadEventListener = uploadEventListener;
        return this;
    }

    public TosObjectRequestHandler getHandler() {
        return handler;
    }

    public UploadFileTask setHandler(TosObjectRequestHandler handler) {
        this.handler = handler;
        return this;
    }

    public UploadFileTask setOutput(UploadPartV2Output output) {
        this.output = output;
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public UploadFileTask setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public UploadFileTask setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public UploadFileTask setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }
}

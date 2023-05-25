package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.taskman.UploadFileTaskCanceler;

public class UploadFileV2Input {
    private CreateMultipartUploadInput createMultipartUploadInput = new CreateMultipartUploadInput();
    private String filePath;
    private long partSize;
    private int taskNum;
    private boolean enableCheckpoint;
    private String checkpointFile;
    private DataTransferListener dataTransferListener;
    private UploadEventListener uploadEventListener;
    private CancelHook cancelHook;
    /** 客户端限速，单位 Byte/s **/
    private RateLimiter rateLimiter;
    private long trafficLimit;

    public String getBucket() {
        return createMultipartUploadInput.getBucket();
    }

    public UploadFileV2Input setBucket(String bucket) {
        this.createMultipartUploadInput.setBucket(bucket);
        return this;
    }

    public String getKey() {
        return createMultipartUploadInput.getKey();
    }

    public UploadFileV2Input setKey(String key) {
        this.createMultipartUploadInput.setKey(key);
        return this;
    }

    public String getEncodingType() {
        return createMultipartUploadInput.getEncodingType();
    }

    public UploadFileV2Input setEncodingType(String encodingType) {
        this.createMultipartUploadInput.setEncodingType(encodingType);
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return createMultipartUploadInput.getOptions();
    }

    public UploadFileV2Input setOptions(ObjectMetaRequestOptions options) {
        this.createMultipartUploadInput.setOptions(options);
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public UploadFileV2Input setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public UploadFileV2Input setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public UploadFileV2Input setTaskNum(int taskNum) {
        this.taskNum = taskNum;
        return this;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public UploadFileV2Input setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public UploadFileV2Input setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public UploadFileV2Input setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }

    public UploadEventListener getUploadEventListener() {
        return uploadEventListener;
    }

    public UploadFileV2Input setUploadEventListener(UploadEventListener uploadEventListener) {
        this.uploadEventListener = uploadEventListener;
        return this;
    }

    public CancelHook getCancelHook() {
        return cancelHook;
    }

    public UploadFileV2Input setCancelHook(boolean withCancelHook) {
        if (withCancelHook) {
            this.cancelHook = new UploadFileTaskCanceler();
        } else {
            this.cancelHook = null;
        }
        return this;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public UploadFileV2Input setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    public long getTrafficLimit() {
        return trafficLimit;
    }

    public UploadFileV2Input setTrafficLimit(long trafficLimit) {
        this.trafficLimit = trafficLimit;
        return this;
    }

    @Deprecated
    public CreateMultipartUploadInput getCreateMultipartUploadInput() {
        return createMultipartUploadInput;
    }

    @Deprecated
    public UploadFileV2Input setCreateMultipartUploadInput(CreateMultipartUploadInput createMultipartUploadInput) {
        this.createMultipartUploadInput = createMultipartUploadInput;
        return this;
    }

    @Override
    public String toString() {
        return "UploadFileV2Input{" +
                "bucket='" + getBucket() + '\'' +
                ", key='" + getKey() + '\'' +
                ", encodingType='" + getEncodingType() + '\'' +
                ", options=" + getOptions() +
                ", filePath='" + filePath + '\'' +
                ", partSize=" + partSize +
                ", taskNum=" + taskNum +
                ", enableCheckpoint=" + enableCheckpoint +
                ", checkpointFile='" + checkpointFile + '\'' +
                ", trafficLimit=" + trafficLimit +
                '}';
    }

    public static UploadFileV2InputBuilder builder() {
        return new UploadFileV2InputBuilder();
    }

    public static final class UploadFileV2InputBuilder {
        private CreateMultipartUploadInput createMultipartUploadInput = new CreateMultipartUploadInput();
        private String filePath;
        private long partSize;
        private int taskNum;
        private boolean enableCheckpoint;
        private String checkpointFile;
        private DataTransferListener dataTransferListener;
        private UploadEventListener uploadEventListener;
        private CancelHook cancelHook;
        private RateLimiter rateLimiter;
        private long trafficLimit;

        private UploadFileV2InputBuilder() {
        }

        public UploadFileV2InputBuilder bucket(String bucket) {
            this.createMultipartUploadInput.setBucket(bucket);
            return this;
        }

        public UploadFileV2InputBuilder key(String key) {
            this.createMultipartUploadInput.setKey(key);
            return this;
        }

        public UploadFileV2InputBuilder encodingType(String encodingType) {
            this.createMultipartUploadInput.setEncodingType(encodingType);
            return this;
        }

        public UploadFileV2InputBuilder options(ObjectMetaRequestOptions options) {
            this.createMultipartUploadInput.setOptions(options);
            return this;
        }

        @Deprecated
        public UploadFileV2InputBuilder createMultipartUploadInput(CreateMultipartUploadInput createMultipartUploadInput) {
            this.createMultipartUploadInput = createMultipartUploadInput;
            return this;
        }

        public UploadFileV2InputBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public UploadFileV2InputBuilder partSize(long partSize) {
            this.partSize = partSize;
            return this;
        }

        public UploadFileV2InputBuilder taskNum(int taskNum) {
            this.taskNum = taskNum;
            return this;
        }

        public UploadFileV2InputBuilder enableCheckpoint(boolean enableCheckpoint) {
            this.enableCheckpoint = enableCheckpoint;
            return this;
        }

        public UploadFileV2InputBuilder checkpointFile(String checkpointFile) {
            this.checkpointFile = checkpointFile;
            return this;
        }

        public UploadFileV2InputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.dataTransferListener = dataTransferListener;
            return this;
        }

        public UploadFileV2InputBuilder uploadEventListener(UploadEventListener uploadEventListener) {
            this.uploadEventListener = uploadEventListener;
            return this;
        }

        public UploadFileV2InputBuilder cancelHook(boolean withCancelHook) {
            if (withCancelHook) {
                this.cancelHook = new UploadFileTaskCanceler();
            } else {
                this.cancelHook = null;
            }
            return this;
        }

        public UploadFileV2InputBuilder rateLimiter(RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
            return this;
        }

        public UploadFileV2InputBuilder trafficLimit(long trafficLimit) {
            this.trafficLimit = trafficLimit;
            return this;
        }

        public UploadFileV2Input build() {
            UploadFileV2Input uploadFileV2Input = new UploadFileV2Input();
            uploadFileV2Input.setCreateMultipartUploadInput(createMultipartUploadInput);
            uploadFileV2Input.setFilePath(filePath);
            uploadFileV2Input.setPartSize(partSize);
            uploadFileV2Input.setTaskNum(taskNum);
            uploadFileV2Input.setEnableCheckpoint(enableCheckpoint);
            uploadFileV2Input.setCheckpointFile(checkpointFile);
            uploadFileV2Input.setDataTransferListener(dataTransferListener);
            uploadFileV2Input.setUploadEventListener(uploadEventListener);
            uploadFileV2Input.cancelHook = cancelHook;
            uploadFileV2Input.setRateLimiter(rateLimiter);
            uploadFileV2Input.setTrafficLimit(trafficLimit);
            return uploadFileV2Input;
        }
    }
}

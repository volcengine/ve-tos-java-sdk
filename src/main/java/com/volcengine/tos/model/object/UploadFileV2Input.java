package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.taskman.UploadFileTaskCanceler;

public class UploadFileV2Input {
    private CreateMultipartUploadInput createMultipartUploadInput;
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

    public CreateMultipartUploadInput getCreateMultipartUploadInput() {
        return createMultipartUploadInput;
    }

    public UploadFileV2Input setCreateMultipartUploadInput(CreateMultipartUploadInput createMultipartUploadInput) {
        this.createMultipartUploadInput = createMultipartUploadInput;
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

    @Override
    public String toString() {
        return "UploadFileV2Input{" +
                "createMultipartUploadInput=" + createMultipartUploadInput +
                ", filePath='" + filePath + '\'' +
                ", partSize=" + partSize +
                ", taskNum=" + taskNum +
                ", enableCheckpoint=" + enableCheckpoint +
                ", checkpointFile='" + checkpointFile + '\'' +
                '}';
    }

    public static UploadFileV2InputBuilder builder() {
        return new UploadFileV2InputBuilder();
    }

    public static final class UploadFileV2InputBuilder {
        private CreateMultipartUploadInput createMultipartUploadInput;
        private String filePath;
        private long partSize;
        private int taskNum;
        private boolean enableCheckpoint;
        private String checkpointFile;
        private DataTransferListener dataTransferListener;
        private UploadEventListener uploadEventListener;
        private CancelHook cancelHook;
        private RateLimiter rateLimiter;

        private UploadFileV2InputBuilder() {
        }

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
            return uploadFileV2Input;
        }
    }
}

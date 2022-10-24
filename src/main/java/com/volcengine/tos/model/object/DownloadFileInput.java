package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.taskman.DownloadFileTaskCanceler;

public class DownloadFileInput {
    private HeadObjectV2Input headObjectV2Input;
    private String filePath;
    private String tempFilePath;
    private long partSize;
    private int taskNum;
    private boolean enableCheckpoint;
    private String checkpointFile;
    private DataTransferListener dataTransferListener;
    private DownloadEventListener downloadEventListener;

    private CancelHook cancelHook;

    /** 客户端限速，单位 Byte/s **/
    private RateLimiter rateLimiter;

    public HeadObjectV2Input getHeadObjectV2Input() {
        return headObjectV2Input;
    }

    public DownloadFileInput setHeadObjectV2Input(HeadObjectV2Input headObjectV2Input) {
        this.headObjectV2Input = headObjectV2Input;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public DownloadFileInput setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public DownloadFileInput setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public DownloadFileInput setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public DownloadFileInput setTaskNum(int taskNum) {
        this.taskNum = taskNum;
        return this;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public DownloadFileInput setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public DownloadFileInput setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public DownloadFileInput setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }

    public DownloadEventListener getDownloadEventListener() {
        return downloadEventListener;
    }

    public DownloadFileInput setDownloadEventListener(DownloadEventListener downloadEventListener) {
        this.downloadEventListener = downloadEventListener;
        return this;
    }

    public CancelHook getCancelHook() {
        return cancelHook;
    }

    public DownloadFileInput setCancelHook(boolean withCancelHook) {
        if (withCancelHook) {
            this.cancelHook = new DownloadFileTaskCanceler();
        } else {
            this.cancelHook = null;
        }
        return this;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public DownloadFileInput setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    @Override
    public String toString() {
        return "DownloadFileInput{" +
                "headObjectV2Input=" + headObjectV2Input +
                ", filePath='" + filePath + '\'' +
                ", tempFilePath='" + tempFilePath + '\'' +
                ", partSize=" + partSize +
                ", taskNum=" + taskNum +
                ", enableCheckpoint=" + enableCheckpoint +
                ", checkpointFile='" + checkpointFile + '\'' +
                ", rateLimiter=" + rateLimiter +
                '}';
    }

    public static DownloadFileInputBuilder builder() {
        return new DownloadFileInputBuilder();
    }

    public static final class DownloadFileInputBuilder {
        private HeadObjectV2Input headObjectV2Input;
        private String filePath;
        private String tempFilePath;
        private long partSize;
        private int taskNum;
        private boolean enableCheckpoint;
        private String checkpointFile;
        private DataTransferListener dataTransferListener;
        private DownloadEventListener downloadEventListener;
        private CancelHook cancelHook;
        private RateLimiter rateLimiter;

        private DownloadFileInputBuilder() {
        }

        public DownloadFileInputBuilder headObjectV2Input(HeadObjectV2Input headObjectV2Input) {
            this.headObjectV2Input = headObjectV2Input;
            return this;
        }

        public DownloadFileInputBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public DownloadFileInputBuilder tempFilePath(String tempFilePath) {
            this.tempFilePath = tempFilePath;
            return this;
        }

        public DownloadFileInputBuilder partSize(long partSize) {
            this.partSize = partSize;
            return this;
        }

        public DownloadFileInputBuilder taskNum(int taskNum) {
            this.taskNum = taskNum;
            return this;
        }

        public DownloadFileInputBuilder enableCheckpoint(boolean enableCheckpoint) {
            this.enableCheckpoint = enableCheckpoint;
            return this;
        }

        public DownloadFileInputBuilder checkpointFile(String checkpointFile) {
            this.checkpointFile = checkpointFile;
            return this;
        }

        public DownloadFileInputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.dataTransferListener = dataTransferListener;
            return this;
        }

        public DownloadFileInputBuilder downloadEventListener(DownloadEventListener downloadEventListener) {
            this.downloadEventListener = downloadEventListener;
            return this;
        }

        public DownloadFileInputBuilder cancelHook(boolean withCancelHook) {
            if (withCancelHook) {
                this.cancelHook = new DownloadFileTaskCanceler();
            } else {
                this.cancelHook = null;
            }
            return this;
        }

        public DownloadFileInputBuilder rateLimiter(RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
            return this;
        }

        public DownloadFileInput build() {
            DownloadFileInput downloadFileInput = new DownloadFileInput();
            downloadFileInput.setHeadObjectV2Input(headObjectV2Input);
            downloadFileInput.setFilePath(filePath);
            downloadFileInput.setTempFilePath(tempFilePath);
            downloadFileInput.setPartSize(partSize);
            downloadFileInput.setTaskNum(taskNum);
            downloadFileInput.setEnableCheckpoint(enableCheckpoint);
            downloadFileInput.setCheckpointFile(checkpointFile);
            downloadFileInput.setDataTransferListener(dataTransferListener);
            downloadFileInput.setDownloadEventListener(downloadEventListener);
            downloadFileInput.cancelHook = cancelHook;
            downloadFileInput.setRateLimiter(rateLimiter);
            return downloadFileInput;
        }
    }
}
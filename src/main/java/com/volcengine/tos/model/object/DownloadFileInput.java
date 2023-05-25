package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.taskman.DownloadFileTaskCanceler;

public class DownloadFileInput {
    private HeadObjectV2Input headObjectV2Input = new HeadObjectV2Input();
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

    private long trafficLimit;

    public String getBucket() {
        return headObjectV2Input.getBucket();
    }

    public DownloadFileInput setBucket(String bucket) {
        this.headObjectV2Input.setBucket(bucket);
        return this;
    }

    public String getKey() {
        return headObjectV2Input.getKey();
    }

    public DownloadFileInput setKey(String key) {
        this.headObjectV2Input.setKey(key);
        return this;
    }

    public String getVersionID() {
        return headObjectV2Input.getVersionID();
    }

    public DownloadFileInput setVersionID(String versionID) {
        this.headObjectV2Input.setVersionID(versionID);
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return headObjectV2Input.getOptions();
    }

    public DownloadFileInput setOptions(ObjectMetaRequestOptions options) {
        this.headObjectV2Input.setOptions(options);
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

    public long getTrafficLimit() {
        return trafficLimit;
    }

    public DownloadFileInput setTrafficLimit(long trafficLimit) {
        this.trafficLimit = trafficLimit;
        return this;
    }

    @Deprecated
    public HeadObjectV2Input getHeadObjectV2Input() {
        return headObjectV2Input;
    }

    @Deprecated
    public DownloadFileInput setHeadObjectV2Input(HeadObjectV2Input headObjectV2Input) {
        this.headObjectV2Input = headObjectV2Input;
        return this;
    }

    @Override
    public String toString() {
        return "DownloadFileInput{" +
                "bucket='" + getBucket() + '\'' +
                ", key='" + getKey() + '\'' +
                ", versionID='" + getVersionID() + '\'' +
                ", options=" + getOptions() +
                ", filePath='" + filePath + '\'' +
                ", tempFilePath='" + tempFilePath + '\'' +
                ", partSize=" + partSize +
                ", taskNum=" + taskNum +
                ", enableCheckpoint=" + enableCheckpoint +
                ", checkpointFile='" + checkpointFile + '\'' +
                ", rateLimiter=" + rateLimiter +
                ", trafficLimit=" + trafficLimit +
                '}';
    }

    public static DownloadFileInputBuilder builder() {
        return new DownloadFileInputBuilder();
    }

    public static final class DownloadFileInputBuilder {
        private HeadObjectV2Input headObjectV2Input = new HeadObjectV2Input();
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
        private long trafficLimit;

        private DownloadFileInputBuilder() {
        }

        @Deprecated
        public DownloadFileInputBuilder headObjectV2Input(HeadObjectV2Input headObjectV2Input) {
            this.headObjectV2Input = headObjectV2Input;
            return this;
        }

        public DownloadFileInputBuilder bucket(String bucket) {
            this.headObjectV2Input.setBucket(bucket);
            return this;
        }

        public DownloadFileInputBuilder key(String key) {
            this.headObjectV2Input.setKey(key);
            return this;
        }

        public DownloadFileInputBuilder versionID(String versionID) {
            this.headObjectV2Input.setVersionID(versionID);
            return this;
        }

        public DownloadFileInputBuilder options(ObjectMetaRequestOptions options) {
            this.headObjectV2Input.setOptions(options);
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

        public DownloadFileInputBuilder trafficLimit(long trafficLimit) {
            this.trafficLimit = trafficLimit;
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
            downloadFileInput.setTrafficLimit(trafficLimit);
            return downloadFileInput;
        }
    }
}

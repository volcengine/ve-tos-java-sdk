package com.volcengine.tos.model.object;

import com.volcengine.tos.internal.taskman.ResumableCopyObjectTaskCanceler;

import java.util.Date;

public class ResumableCopyObjectInput {
    private CreateMultipartUploadInput createMultipartUploadInput = new CreateMultipartUploadInput();
    private String srcBucket;
    private String srcKey;
    private String srcVersionID;

    private String copySourceIfMatch;
    private Date copySourceIfModifiedSince;
    private String copySourceIfNoneMatch;
    private Date copySourceIfUnModifiedSince;

    private String copySourceSSECAlgorithm;
    private String copySourceSSECKey;
    private String copySourceSSECKeyMD5;

    private long partSize;
    private int taskNum;
    private boolean enableCheckpoint;
    private String checkpointFile;

    private CopyEventListener copyEventListener;
    private CancelHook cancelHook;

    @Deprecated
    public CreateMultipartUploadInput getCreateMultipartUploadInput() {
        return createMultipartUploadInput;
    }

    @Deprecated
    public ResumableCopyObjectInput setCreateMultipartUploadInput(CreateMultipartUploadInput createMultipartUploadInput) {
        this.createMultipartUploadInput = createMultipartUploadInput;
        return this;
    }

    public String getBucket() {
        return createMultipartUploadInput.getBucket();
    }

    public ResumableCopyObjectInput setBucket(String bucket) {
        this.createMultipartUploadInput.setBucket(bucket);
        return this;
    }

    public String getKey() {
        return createMultipartUploadInput.getKey();
    }

    public ResumableCopyObjectInput setKey(String key) {
        this.createMultipartUploadInput.setKey(key);
        return this;
    }

    public String getEncodingType() {
        return createMultipartUploadInput.getEncodingType();
    }

    public ResumableCopyObjectInput setEncodingType(String encodingType) {
        this.createMultipartUploadInput.setEncodingType(encodingType);
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return createMultipartUploadInput.getOptions();
    }

    public ResumableCopyObjectInput setOptions(ObjectMetaRequestOptions options) {
        this.createMultipartUploadInput.setOptions(options);
        return this;
    }

    public String getSrcBucket() {
        return srcBucket;
    }

    public ResumableCopyObjectInput setSrcBucket(String srcBucket) {
        this.srcBucket = srcBucket;
        return this;
    }

    public String getSrcKey() {
        return srcKey;
    }

    public ResumableCopyObjectInput setSrcKey(String srcKey) {
        this.srcKey = srcKey;
        return this;
    }

    public String getSrcVersionID() {
        return srcVersionID;
    }

    public ResumableCopyObjectInput setSrcVersionID(String srcVersionID) {
        this.srcVersionID = srcVersionID;
        return this;
    }

    public String getCopySourceIfMatch() {
        return copySourceIfMatch;
    }

    public ResumableCopyObjectInput setCopySourceIfMatch(String copySourceIfMatch) {
        this.copySourceIfMatch = copySourceIfMatch;
        return this;
    }

    public Date getCopySourceIfModifiedSince() {
        return copySourceIfModifiedSince;
    }

    public ResumableCopyObjectInput setCopySourceIfModifiedSince(Date copySourceIfModifiedSince) {
        this.copySourceIfModifiedSince = copySourceIfModifiedSince;
        return this;
    }

    public String getCopySourceIfNoneMatch() {
        return copySourceIfNoneMatch;
    }

    public ResumableCopyObjectInput setCopySourceIfNoneMatch(String copySourceIfNoneMatch) {
        this.copySourceIfNoneMatch = copySourceIfNoneMatch;
        return this;
    }

    public Date getCopySourceIfUnModifiedSince() {
        return copySourceIfUnModifiedSince;
    }

    public ResumableCopyObjectInput setCopySourceIfUnModifiedSince(Date copySourceIfUnModifiedSince) {
        this.copySourceIfUnModifiedSince = copySourceIfUnModifiedSince;
        return this;
    }

    public String getCopySourceSSECAlgorithm() {
        return copySourceSSECAlgorithm;
    }

    public ResumableCopyObjectInput setCopySourceSSECAlgorithm(String copySourceSSECAlgorithm) {
        this.copySourceSSECAlgorithm = copySourceSSECAlgorithm;
        return this;
    }

    public String getCopySourceSSECKey() {
        return copySourceSSECKey;
    }

    public ResumableCopyObjectInput setCopySourceSSECKey(String copySourceSSECKey) {
        this.copySourceSSECKey = copySourceSSECKey;
        return this;
    }

    public String getCopySourceSSECKeyMD5() {
        return copySourceSSECKeyMD5;
    }

    public ResumableCopyObjectInput setCopySourceSSECKeyMD5(String copySourceSSECKeyMD5) {
        this.copySourceSSECKeyMD5 = copySourceSSECKeyMD5;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public ResumableCopyObjectInput setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public ResumableCopyObjectInput setTaskNum(int taskNum) {
        this.taskNum = taskNum;
        return this;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public ResumableCopyObjectInput setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public ResumableCopyObjectInput setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }

    public CopyEventListener getCopyEventListener() {
        return copyEventListener;
    }

    public ResumableCopyObjectInput setCopyEventListener(CopyEventListener copyEventListener) {
        this.copyEventListener = copyEventListener;
        return this;
    }

    public CancelHook getCancelHook() {
        return cancelHook;
    }

    public ResumableCopyObjectInput setCancelHook(boolean withCancelHook) {
        if (withCancelHook) {
            this.cancelHook = new ResumableCopyObjectTaskCanceler();
        } else {
            this.cancelHook = null;
        }
        return this;
    }

    @Override
    public String toString() {
        return "ResumableCopyObjectInput{" +
                "bucket='" + getBucket() + '\'' +
                ", key='" + getKey() + '\'' +
                ", encodingType='" + getEncodingType() + '\'' +
                ", options=" + getOptions() +
                ", srcBucket='" + srcBucket + '\'' +
                ", srcKey='" + srcKey + '\'' +
                ", srcVersionID='" + srcVersionID + '\'' +
                ", copySourceIfMatch='" + copySourceIfMatch + '\'' +
                ", copySourceIfModifiedSince=" + copySourceIfModifiedSince +
                ", copySourceIfNoneMatch='" + copySourceIfNoneMatch + '\'' +
                ", copySourceIfUnModifiedSince=" + copySourceIfUnModifiedSince +
                ", copySourceSSECAlgorithm='" + copySourceSSECAlgorithm + '\'' +
                ", copySourceSSECKey='" + copySourceSSECKey + '\'' +
                ", copySourceSSECKeyMD5='" + copySourceSSECKeyMD5 + '\'' +
                ", partSize=" + partSize +
                ", taskNum=" + taskNum +
                ", enableCheckpoint=" + enableCheckpoint +
                ", checkpointFile='" + checkpointFile + '\'' +
                ", copyEventListener=" + copyEventListener +
                ", cancelHook=" + cancelHook +
                '}';
    }

    public static ResumableCopyObjectInputBuilder builder() {
        return new ResumableCopyObjectInputBuilder();
    }

    public static final class ResumableCopyObjectInputBuilder {
        private CreateMultipartUploadInput createMultipartUploadInput = new CreateMultipartUploadInput();
        private String srcBucket;
        private String srcKey;
        private String srcVersionID;
        private String copySourceIfMatch;
        private Date copySourceIfModifiedSince;
        private String copySourceIfNoneMatch;
        private Date copySourceIfUnModifiedSince;
        private String copySourceSSECAlgorithm;
        private String copySourceSSECKey;
        private String copySourceSSECKeyMD5;
        private long partSize;
        private int taskNum;
        private boolean enableCheckpoint;
        private String checkpointFile;
        private CopyEventListener copyEventListener;
        private CancelHook cancelHook;

        private ResumableCopyObjectInputBuilder() {
        }

        @Deprecated
        public ResumableCopyObjectInputBuilder createMultipartUploadInput(CreateMultipartUploadInput createMultipartUploadInput) {
            this.createMultipartUploadInput = createMultipartUploadInput;
            return this;
        }

        public ResumableCopyObjectInputBuilder bucket(String bucket) {
            this.createMultipartUploadInput.setBucket(bucket);
            return this;
        }

        public ResumableCopyObjectInputBuilder key(String key) {
            this.createMultipartUploadInput.setKey(key);
            return this;
        }

        public ResumableCopyObjectInputBuilder encodingType(String encodingType) {
            this.createMultipartUploadInput.setEncodingType(encodingType);
            return this;
        }

        public ResumableCopyObjectInputBuilder options(ObjectMetaRequestOptions options) {
            this.createMultipartUploadInput.setOptions(options);
            return this;
        }

        public ResumableCopyObjectInputBuilder srcBucket(String srcBucket) {
            this.srcBucket = srcBucket;
            return this;
        }

        public ResumableCopyObjectInputBuilder srcKey(String srcKey) {
            this.srcKey = srcKey;
            return this;
        }

        public ResumableCopyObjectInputBuilder srcVersionID(String srcVersionID) {
            this.srcVersionID = srcVersionID;
            return this;
        }

        public ResumableCopyObjectInputBuilder copySourceIfMatch(String copySourceIfMatch) {
            this.copySourceIfMatch = copySourceIfMatch;
            return this;
        }

        public ResumableCopyObjectInputBuilder copySourceIfModifiedSince(Date copySourceIfModifiedSince) {
            this.copySourceIfModifiedSince = copySourceIfModifiedSince;
            return this;
        }

        public ResumableCopyObjectInputBuilder copySourceIfNoneMatch(String copySourceIfNoneMatch) {
            this.copySourceIfNoneMatch = copySourceIfNoneMatch;
            return this;
        }

        public ResumableCopyObjectInputBuilder copySourceIfUnModifiedSince(Date copySourceIfUnModifiedSince) {
            this.copySourceIfUnModifiedSince = copySourceIfUnModifiedSince;
            return this;
        }

        public ResumableCopyObjectInputBuilder copySourceSSECAlgorithm(String copySourceSSECAlgorithm) {
            this.copySourceSSECAlgorithm = copySourceSSECAlgorithm;
            return this;
        }

        public ResumableCopyObjectInputBuilder copySourceSSECKey(String copySourceSSECKey) {
            this.copySourceSSECKey = copySourceSSECKey;
            return this;
        }

        public ResumableCopyObjectInputBuilder copySourceSSECKeyMD5(String copySourceSSECKeyMD5) {
            this.copySourceSSECKeyMD5 = copySourceSSECKeyMD5;
            return this;
        }

        public ResumableCopyObjectInputBuilder partSize(long partSize) {
            this.partSize = partSize;
            return this;
        }

        public ResumableCopyObjectInputBuilder taskNum(int taskNum) {
            this.taskNum = taskNum;
            return this;
        }

        public ResumableCopyObjectInputBuilder enableCheckpoint(boolean enableCheckpoint) {
            this.enableCheckpoint = enableCheckpoint;
            return this;
        }

        public ResumableCopyObjectInputBuilder checkpointFile(String checkpointFile) {
            this.checkpointFile = checkpointFile;
            return this;
        }

        public ResumableCopyObjectInputBuilder copyEventListener(CopyEventListener copyEventListener) {
            this.copyEventListener = copyEventListener;
            return this;
        }

        public ResumableCopyObjectInputBuilder cancelHook(boolean withCancelHook) {
            if (withCancelHook) {
                this.cancelHook = new ResumableCopyObjectTaskCanceler();
            } else {
                this.cancelHook = null;
            }
            return this;
        }

        public ResumableCopyObjectInput build() {
            ResumableCopyObjectInput resumableCopyObjectInput = new ResumableCopyObjectInput();
            resumableCopyObjectInput.setCreateMultipartUploadInput(createMultipartUploadInput);
            resumableCopyObjectInput.setSrcBucket(srcBucket);
            resumableCopyObjectInput.setSrcKey(srcKey);
            resumableCopyObjectInput.setSrcVersionID(srcVersionID);
            resumableCopyObjectInput.setCopySourceIfMatch(copySourceIfMatch);
            resumableCopyObjectInput.setCopySourceIfModifiedSince(copySourceIfModifiedSince);
            resumableCopyObjectInput.setCopySourceIfNoneMatch(copySourceIfNoneMatch);
            resumableCopyObjectInput.setCopySourceIfUnModifiedSince(copySourceIfUnModifiedSince);
            resumableCopyObjectInput.setCopySourceSSECAlgorithm(copySourceSSECAlgorithm);
            resumableCopyObjectInput.setCopySourceSSECKey(copySourceSSECKey);
            resumableCopyObjectInput.setCopySourceSSECKeyMD5(copySourceSSECKeyMD5);
            resumableCopyObjectInput.setPartSize(partSize);
            resumableCopyObjectInput.setTaskNum(taskNum);
            resumableCopyObjectInput.setEnableCheckpoint(enableCheckpoint);
            resumableCopyObjectInput.setCheckpointFile(checkpointFile);
            resumableCopyObjectInput.setCopyEventListener(copyEventListener);
            resumableCopyObjectInput.cancelHook = cancelHook;
            return resumableCopyObjectInput;
        }
    }
}

package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CompleteMultipartUploadV2Input {
    @JsonIgnore
    private String bucket;
    @JsonIgnore
    private String key;
    @JsonIgnore
    private String uploadID;
    @JsonIgnore
    private boolean completeAll;
    @JsonProperty("Parts")
    private List<UploadedPartV2> uploadedParts;
    private String callback;
    private String callbackVar;

    public CompleteMultipartUploadV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public CompleteMultipartUploadV2Input setKey(String key) {
        this.key = key;
        return this;
    }

    public CompleteMultipartUploadV2Input setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public CompleteMultipartUploadV2Input setUploadedParts(List<UploadedPartV2> uploadedParts) {
        this.uploadedParts = uploadedParts;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getUploadID() {
        return uploadID;
    }

    public List<UploadedPartV2> getUploadedParts() {
        return uploadedParts;
    }

    public boolean isCompleteAll() {
        return completeAll;
    }

    public CompleteMultipartUploadV2Input setCompleteAll(boolean completeAll) {
        this.completeAll = completeAll;
        return this;
    }

    public String getCallback() {
        return callback;
    }

    public CompleteMultipartUploadV2Input setCallback(String callback) {
        this.callback = callback;
        return this;
    }

    public String getCallbackVar() {
        return callbackVar;
    }

    public CompleteMultipartUploadV2Input setCallbackVar(String callbackVar) {
        this.callbackVar = callbackVar;
        return this;
    }

    public static CompleteMultipartUploadV2InputBuilder builder() {
        return new CompleteMultipartUploadV2InputBuilder();
    }

    @Override
    public String toString() {
        return "CompleteMultipartUploadV2Input{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", completeAll=" + completeAll +
                ", uploadedParts=" + uploadedParts +
                ", callback='" + callback + '\'' +
                ", callbackVar='" + callbackVar + '\'' +
                '}';
    }

    public static final class CompleteMultipartUploadV2InputBuilder {
        private String bucket;
        private String key;
        private String uploadID;
        private boolean completeAll;
        private List<UploadedPartV2> uploadedParts;
        private String callback;
        private String callbackVar;

        private CompleteMultipartUploadV2InputBuilder() {
        }

        public CompleteMultipartUploadV2InputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public CompleteMultipartUploadV2InputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public CompleteMultipartUploadV2InputBuilder uploadID(String uploadID) {
            this.uploadID = uploadID;
            return this;
        }

        public CompleteMultipartUploadV2InputBuilder uploadedParts(List<UploadedPartV2> uploadedParts) {
            this.uploadedParts = uploadedParts;
            return this;
        }

        public CompleteMultipartUploadV2InputBuilder completeAll(boolean completeAll) {
            this.completeAll = completeAll;
            return this;
        }

        public CompleteMultipartUploadV2InputBuilder callback(String callback) {
            this.callback = callback;
            return this;
        }

        public CompleteMultipartUploadV2InputBuilder callbackVar(String callbackVar) {
            this.callbackVar = callbackVar;
            return this;
        }

        public CompleteMultipartUploadV2Input build() {
            CompleteMultipartUploadV2Input completeMultipartUploadV2Input = new CompleteMultipartUploadV2Input();
            completeMultipartUploadV2Input.setBucket(bucket);
            completeMultipartUploadV2Input.setKey(key);
            completeMultipartUploadV2Input.setUploadID(uploadID);
            completeMultipartUploadV2Input.setUploadedParts(uploadedParts);
            completeMultipartUploadV2Input.setCompleteAll(completeAll);
            completeMultipartUploadV2Input.setCallback(callback);
            completeMultipartUploadV2Input.setCallbackVar(callbackVar);
            return completeMultipartUploadV2Input;
        }
    }
}

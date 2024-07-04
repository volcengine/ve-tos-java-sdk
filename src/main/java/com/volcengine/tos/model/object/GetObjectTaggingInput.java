package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class GetObjectTaggingInput extends GenericInput {
    private String bucket;
    private String key;
    private String versionID;

    public String getBucket() {
        return bucket;
    }

    public GetObjectTaggingInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public GetObjectTaggingInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public GetObjectTaggingInput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectTaggingInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                '}';
    }

    public static GetObjectTaggingInputBuilder builder() {
        return new GetObjectTaggingInputBuilder();
    }

    public static final class GetObjectTaggingInputBuilder {
        private String bucket;
        private String key;
        private String versionID;

        private GetObjectTaggingInputBuilder() {
        }

        public GetObjectTaggingInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectTaggingInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public GetObjectTaggingInputBuilder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public GetObjectTaggingInput build() {
            GetObjectTaggingInput getObjectTaggingInput = new GetObjectTaggingInput();
            getObjectTaggingInput.setBucket(bucket);
            getObjectTaggingInput.setKey(key);
            getObjectTaggingInput.setVersionID(versionID);
            return getObjectTaggingInput;
        }
    }
}

package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PutObjectTaggingInput {
    @JsonIgnore
    private String bucket;
    @JsonIgnore
    private String key;
    @JsonIgnore
    private String versionID;
    @JsonProperty("TagSet")
    private TagSet tagSet;

    public String getBucket() {
        return bucket;
    }

    public PutObjectTaggingInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public PutObjectTaggingInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public PutObjectTaggingInput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public TagSet getTagSet() {
        return tagSet;
    }

    public PutObjectTaggingInput setTagSet(TagSet tagSet) {
        this.tagSet = tagSet;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectTaggingInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", tagSet=" + tagSet +
                '}';
    }

    public static PutObjectTaggingInputBuilder builder() {
        return new PutObjectTaggingInputBuilder();
    }

    public static final class PutObjectTaggingInputBuilder {
        private String bucket;
        private String key;
        private String versionID;
        private TagSet tagSet;

        private PutObjectTaggingInputBuilder() {
        }

        public PutObjectTaggingInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutObjectTaggingInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public PutObjectTaggingInputBuilder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public PutObjectTaggingInputBuilder tagSet(TagSet tagSet) {
            this.tagSet = tagSet;
            return this;
        }

        public PutObjectTaggingInput build() {
            PutObjectTaggingInput putObjectTaggingInput = new PutObjectTaggingInput();
            putObjectTaggingInput.setBucket(bucket);
            putObjectTaggingInput.setKey(key);
            putObjectTaggingInput.setVersionID(versionID);
            putObjectTaggingInput.setTagSet(tagSet);
            return putObjectTaggingInput;
        }
    }
}

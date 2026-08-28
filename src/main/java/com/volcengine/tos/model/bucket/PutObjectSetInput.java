package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;
import com.volcengine.tos.model.object.TagSet;

public class PutObjectSetInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("ObjectSetName")
    private String objectSetName;

    @JsonProperty("TagSet")
    private TagSet tagSet;

    public String getBucket() {
        return bucket;
    }

    public PutObjectSetInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public PutObjectSetInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    public TagSet getTagSet() {
        return tagSet;
    }

    public PutObjectSetInput setTagSet(TagSet tagSet) {
        this.tagSet = tagSet;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                ", tagSet=" + tagSet +
                '}';
    }

    public static PutObjectSetInputBuilder builder() {
        return new PutObjectSetInputBuilder();
    }

    public static final class PutObjectSetInputBuilder {
        private String bucket;
        private String objectSetName;
        private TagSet tagSet;

        private PutObjectSetInputBuilder() {
        }

        public PutObjectSetInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutObjectSetInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public PutObjectSetInputBuilder tagSet(TagSet tagSet) {
            this.tagSet = tagSet;
            return this;
        }

        public PutObjectSetInput build() {
            PutObjectSetInput input = new PutObjectSetInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            input.setTagSet(tagSet);
            return input;
        }
    }
}

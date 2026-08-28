package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;
import com.volcengine.tos.model.object.TagSet;

public class PutObjectSetTaggingInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("ObjectSetName")
    private String objectSetName;

    @JsonProperty("TagSet")
    private TagSet tagSet;

    public String getBucket() {
        return bucket;
    }

    public PutObjectSetTaggingInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public PutObjectSetTaggingInput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    public TagSet getTagSet() {
        return tagSet;
    }

    public PutObjectSetTaggingInput setTagSet(TagSet tagSet) {
        this.tagSet = tagSet;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetTaggingInput{" +
                "bucket='" + bucket + '\'' +
                ", objectSetName='" + objectSetName + '\'' +
                ", tagSet=" + tagSet +
                '}';
    }

    public static PutObjectSetTaggingInputBuilder builder() {
        return new PutObjectSetTaggingInputBuilder();
    }

    public static final class PutObjectSetTaggingInputBuilder {
        private String bucket;
        private String objectSetName;
        private TagSet tagSet;

        private PutObjectSetTaggingInputBuilder() {
        }

        public PutObjectSetTaggingInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutObjectSetTaggingInputBuilder objectSetName(String objectSetName) {
            this.objectSetName = objectSetName;
            return this;
        }

        public PutObjectSetTaggingInputBuilder tagSet(TagSet tagSet) {
            this.tagSet = tagSet;
            return this;
        }

        public PutObjectSetTaggingInput build() {
            PutObjectSetTaggingInput input = new PutObjectSetTaggingInput();
            input.setBucket(bucket);
            input.setObjectSetName(objectSetName);
            input.setTagSet(tagSet);
            return input;
        }
    }
}

package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;
import com.volcengine.tos.model.object.TagSet;

public class PutBucketTaggingInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("TagSet")
    private TagSet tagSet;

    public String getBucket() {
        return bucket;
    }

    public PutBucketTaggingInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public TagSet getTagSet() {
        return tagSet;
    }

    public PutBucketTaggingInput setTagSet(TagSet tagSet) {
        this.tagSet = tagSet;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketTaggingInput{" +
                "bucket='" + bucket + '\'' +
                ", tagSet=" + tagSet +
                '}';
    }
}

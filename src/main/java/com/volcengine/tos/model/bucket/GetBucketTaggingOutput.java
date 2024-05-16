package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.object.TagSet;

public class GetBucketTaggingOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("TagSet")
    private TagSet tagSet;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketTaggingOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public TagSet getTagSet() {
        return tagSet;
    }

    public GetBucketTaggingOutput setTagSet(TagSet tagSet) {
        this.tagSet = tagSet;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketTaggingOutput{" +
                "requestInfo=" + requestInfo +
                ", tagSet=" + tagSet +
                '}';
    }
}

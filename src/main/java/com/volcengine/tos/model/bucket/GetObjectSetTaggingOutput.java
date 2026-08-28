package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.object.TagSet;

public class GetObjectSetTaggingOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("ObjectSetName")
    private String objectSetName;

    @JsonProperty("TagSet")
    private TagSet tagSet;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectSetTaggingOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public GetObjectSetTaggingOutput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    public TagSet getTagSet() {
        return tagSet;
    }

    public GetObjectSetTaggingOutput setTagSet(TagSet tagSet) {
        this.tagSet = tagSet;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetTaggingOutput{" +
                "requestInfo=" + requestInfo +
                ", objectSetName='" + objectSetName + '\'' +
                ", tagSet=" + tagSet +
                '}';
    }
}

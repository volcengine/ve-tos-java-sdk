package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.object.TagSet;

public class GetObjectSetOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("ObjectSetName")
    private String objectSetName;

    @JsonProperty("TagSet")
    private TagSet tagSet;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectSetOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getObjectSetName() {
        return objectSetName;
    }

    public GetObjectSetOutput setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    public TagSet getTagSet() {
        return tagSet;
    }

    public GetObjectSetOutput setTagSet(TagSet tagSet) {
        this.tagSet = tagSet;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetOutput{" +
                "requestInfo=" + requestInfo +
                ", objectSetName='" + objectSetName + '\'' +
                ", tagSet=" + tagSet +
                '}';
    }
}

package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetObjectTaggingOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonIgnore
    private String versionID;
    @JsonProperty("TagSet")
    private TagSet tagSet;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectTaggingOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public GetObjectTaggingOutput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public TagSet getTagSet() {
        return tagSet;
    }

    public GetObjectTaggingOutput setTagSet(TagSet tagSet) {
        this.tagSet = tagSet;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectTaggingOutput{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                ", tagSet=" + tagSet +
                '}';
    }
}

package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UploadPartCopyOutputJson {
    @JsonProperty("ETag")
    String etag;
    @JsonProperty("LastModified")
    Date lastModified;

    public String getEtag() {
        return etag;
    }

    public Date getLastModified() {
        return lastModified;
    }
}
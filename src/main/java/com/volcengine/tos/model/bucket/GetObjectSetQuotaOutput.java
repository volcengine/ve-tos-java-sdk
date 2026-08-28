package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetObjectSetQuotaOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("StorageQuota")
    private String storageQuota;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectSetQuotaOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getStorageQuota() {
        return storageQuota;
    }

    public GetObjectSetQuotaOutput setStorageQuota(String storageQuota) {
        this.storageQuota = storageQuota;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetQuotaOutput{" +
                "requestInfo=" + requestInfo +
                ", storageQuota='" + storageQuota + '\'' +
                '}';
    }
}

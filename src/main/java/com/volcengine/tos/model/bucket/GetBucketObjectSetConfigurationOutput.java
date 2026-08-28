package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetBucketObjectSetConfigurationOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("PathLevel")
    private Integer pathLevel;

    @JsonProperty("CustomDelimiter")
    private String customDelimiter;

    @JsonProperty("EnableDefaultObjectSet")
    private Boolean enableDefaultObjectSet;

    @JsonProperty("Qos")
    private ObjectSetQos qos;

    @JsonProperty("StorageQuota")
    private String storageQuota;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketObjectSetConfigurationOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public Integer getPathLevel() {
        return pathLevel;
    }

    public GetBucketObjectSetConfigurationOutput setPathLevel(Integer pathLevel) {
        this.pathLevel = pathLevel;
        return this;
    }

    public String getCustomDelimiter() {
        return customDelimiter;
    }

    public GetBucketObjectSetConfigurationOutput setCustomDelimiter(String customDelimiter) {
        this.customDelimiter = customDelimiter;
        return this;
    }

    public Boolean getEnableDefaultObjectSet() {
        return enableDefaultObjectSet;
    }

    public GetBucketObjectSetConfigurationOutput setEnableDefaultObjectSet(Boolean enableDefaultObjectSet) {
        this.enableDefaultObjectSet = enableDefaultObjectSet;
        return this;
    }

    public ObjectSetQos getQos() {
        return qos;
    }

    public GetBucketObjectSetConfigurationOutput setQos(ObjectSetQos qos) {
        this.qos = qos;
        return this;
    }

    public String getStorageQuota() {
        return storageQuota;
    }

    public GetBucketObjectSetConfigurationOutput setStorageQuota(String storageQuota) {
        this.storageQuota = storageQuota;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketObjectSetConfigurationOutput{" +
                "requestInfo=" + requestInfo +
                ", pathLevel=" + pathLevel +
                ", customDelimiter='" + customDelimiter + '\'' +
                ", enableDefaultObjectSet=" + enableDefaultObjectSet +
                ", qos=" + qos +
                ", storageQuota='" + storageQuota + '\'' +
                '}';
    }
}

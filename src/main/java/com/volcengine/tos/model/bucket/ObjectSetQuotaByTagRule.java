package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObjectSetQuotaByTagRule {
    @JsonProperty("Tag")
    private Tag tag;

    @JsonProperty("Qos")
    private ObjectSetQos qos;

    @JsonProperty("StorageQuota")
    private String storageQuota;

    public Tag getTag() {
        return tag;
    }

    public ObjectSetQuotaByTagRule setTag(Tag tag) {
        this.tag = tag;
        return this;
    }

    public ObjectSetQos getQos() {
        return qos;
    }

    public ObjectSetQuotaByTagRule setQos(ObjectSetQos qos) {
        this.qos = qos;
        return this;
    }

    public String getStorageQuota() {
        return storageQuota;
    }

    public ObjectSetQuotaByTagRule setStorageQuota(String storageQuota) {
        this.storageQuota = storageQuota;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectSetQuotaByTagRule{" +
                "tag=" + tag +
                ", qos=" + qos +
                ", storageQuota='" + storageQuota + '\'' +
                '}';
    }
}

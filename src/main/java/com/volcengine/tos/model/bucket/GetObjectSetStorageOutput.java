package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class GetObjectSetStorageOutput {
    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("TotalStorageStat")
    private ObjectSetStorageStat totalStorageStat;

    @JsonProperty("StandardStorageStat")
    private ObjectSetStorageStat standardStorageStat;

    @JsonProperty("IAStorageStat")
    private ObjectSetStorageStat iaStorageStat;

    @JsonProperty("ArchiveFrStorageStat")
    private ObjectSetStorageStat archiveFrStorageStat;

    @JsonProperty("ArchiveStorageStat")
    private ObjectSetStorageStat archiveStorageStat;

    @JsonProperty("ColdArchiveStat")
    private ObjectSetStorageStat coldArchiveStat;

    @JsonProperty("DeepColdArchiveStorageStat")
    private ObjectSetStorageStat deepColdArchiveStorageStat;

    @JsonProperty("IntelligentTieringStorageStats")
    private IntelligentTieringStorageStats intelligentTieringStorageStats;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectSetStorageOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public ObjectSetStorageStat getTotalStorageStat() {
        return totalStorageStat;
    }

    public GetObjectSetStorageOutput setTotalStorageStat(ObjectSetStorageStat totalStorageStat) {
        this.totalStorageStat = totalStorageStat;
        return this;
    }

    public ObjectSetStorageStat getStandardStorageStat() {
        return standardStorageStat;
    }

    public GetObjectSetStorageOutput setStandardStorageStat(ObjectSetStorageStat standardStorageStat) {
        this.standardStorageStat = standardStorageStat;
        return this;
    }

    public ObjectSetStorageStat getIaStorageStat() {
        return iaStorageStat;
    }

    public GetObjectSetStorageOutput setIaStorageStat(ObjectSetStorageStat iaStorageStat) {
        this.iaStorageStat = iaStorageStat;
        return this;
    }

    public ObjectSetStorageStat getArchiveFrStorageStat() {
        return archiveFrStorageStat;
    }

    public GetObjectSetStorageOutput setArchiveFrStorageStat(ObjectSetStorageStat archiveFrStorageStat) {
        this.archiveFrStorageStat = archiveFrStorageStat;
        return this;
    }

    public ObjectSetStorageStat getArchiveStorageStat() {
        return archiveStorageStat;
    }

    public GetObjectSetStorageOutput setArchiveStorageStat(ObjectSetStorageStat archiveStorageStat) {
        this.archiveStorageStat = archiveStorageStat;
        return this;
    }

    public ObjectSetStorageStat getColdArchiveStat() {
        return coldArchiveStat;
    }

    public GetObjectSetStorageOutput setColdArchiveStat(ObjectSetStorageStat coldArchiveStat) {
        this.coldArchiveStat = coldArchiveStat;
        return this;
    }

    public ObjectSetStorageStat getDeepColdArchiveStorageStat() {
        return deepColdArchiveStorageStat;
    }

    public GetObjectSetStorageOutput setDeepColdArchiveStorageStat(ObjectSetStorageStat deepColdArchiveStorageStat) {
        this.deepColdArchiveStorageStat = deepColdArchiveStorageStat;
        return this;
    }

    public IntelligentTieringStorageStats getIntelligentTieringStorageStats() {
        return intelligentTieringStorageStats;
    }

    public GetObjectSetStorageOutput setIntelligentTieringStorageStats(IntelligentTieringStorageStats intelligentTieringStorageStats) {
        this.intelligentTieringStorageStats = intelligentTieringStorageStats;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectSetStorageOutput{" +
                "requestInfo=" + requestInfo +
                ", totalStorageStat=" + totalStorageStat +
                ", standardStorageStat=" + standardStorageStat +
                ", iaStorageStat=" + iaStorageStat +
                ", archiveFrStorageStat=" + archiveFrStorageStat +
                ", archiveStorageStat=" + archiveStorageStat +
                ", coldArchiveStat=" + coldArchiveStat +
                ", deepColdArchiveStorageStat=" + deepColdArchiveStorageStat +
                ", intelligentTieringStorageStats=" + intelligentTieringStorageStats +
                '}';
    }
}

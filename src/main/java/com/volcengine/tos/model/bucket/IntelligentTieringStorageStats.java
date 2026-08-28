package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IntelligentTieringStorageStats {
    @JsonProperty("HighFreqStorageStat")
    private ObjectSetStorageStat highFreqStorageStat;

    @JsonProperty("LowFreqStorageStat")
    private ObjectSetStorageStat lowFreqStorageStat;

    @JsonProperty("ArchiveStorageStat")
    private ObjectSetStorageStat archiveStorageStat;

    public ObjectSetStorageStat getHighFreqStorageStat() {
        return highFreqStorageStat;
    }

    public IntelligentTieringStorageStats setHighFreqStorageStat(ObjectSetStorageStat highFreqStorageStat) {
        this.highFreqStorageStat = highFreqStorageStat;
        return this;
    }

    public ObjectSetStorageStat getLowFreqStorageStat() {
        return lowFreqStorageStat;
    }

    public IntelligentTieringStorageStats setLowFreqStorageStat(ObjectSetStorageStat lowFreqStorageStat) {
        this.lowFreqStorageStat = lowFreqStorageStat;
        return this;
    }

    public ObjectSetStorageStat getArchiveStorageStat() {
        return archiveStorageStat;
    }

    public IntelligentTieringStorageStats setArchiveStorageStat(ObjectSetStorageStat archiveStorageStat) {
        this.archiveStorageStat = archiveStorageStat;
        return this;
    }

    @Override
    public String toString() {
        return "IntelligentTieringStorageStats{" +
                "highFreqStorageStat=" + highFreqStorageStat +
                ", lowFreqStorageStat=" + lowFreqStorageStat +
                ", archiveStorageStat=" + archiveStorageStat +
                '}';
    }
}

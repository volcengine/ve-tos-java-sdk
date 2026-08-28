package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BucketTrashPrefixRule {
    @JsonProperty("PrefixList")
    private List<String> prefixList;
    @JsonProperty("TrashPath")
    private String trashPath;
    @JsonProperty("CleanInterval")
    private Integer cleanInterval;

    public List<String> getPrefixList() {
        return prefixList;
    }

    public BucketTrashPrefixRule setPrefixList(List<String> prefixList) {
        this.prefixList = prefixList;
        return this;
    }

    public String getTrashPath() {
        return trashPath;
    }

    public BucketTrashPrefixRule setTrashPath(String trashPath) {
        this.trashPath = trashPath;
        return this;
    }

    public Integer getCleanInterval() {
        return cleanInterval;
    }

    public BucketTrashPrefixRule setCleanInterval(Integer cleanInterval) {
        this.cleanInterval = cleanInterval;
        return this;
    }

    @Override
    public String toString() {
        return "BucketTrashPrefixRule{" +
                "prefixList=" + prefixList +
                ", trashPath='" + trashPath + '\'' +
                ", cleanInterval=" + cleanInterval +
                '}';
    }
}

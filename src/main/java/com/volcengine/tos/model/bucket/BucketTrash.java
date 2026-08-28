package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StatusType;

import java.util.List;

public class BucketTrash {
    @JsonProperty("TrashPath")
    private String trashPath;
    @JsonProperty("CleanInterval")
    private Integer cleanInterval;
    @JsonProperty("Status")
    private StatusType status;
    @JsonProperty("PrefixMatchRules")
    private List<BucketTrashPrefixRule> prefixMatchRules;

    public String getTrashPath() {
        return trashPath;
    }

    public BucketTrash setTrashPath(String trashPath) {
        this.trashPath = trashPath;
        return this;
    }

    public Integer getCleanInterval() {
        return cleanInterval;
    }

    public BucketTrash setCleanInterval(Integer cleanInterval) {
        this.cleanInterval = cleanInterval;
        return this;
    }

    public StatusType getStatus() {
        return status;
    }

    public BucketTrash setStatus(StatusType status) {
        this.status = status;
        return this;
    }

    public List<BucketTrashPrefixRule> getPrefixMatchRules() {
        return prefixMatchRules;
    }

    public BucketTrash setPrefixMatchRules(List<BucketTrashPrefixRule> prefixMatchRules) {
        this.prefixMatchRules = prefixMatchRules;
        return this;
    }

    @Override
    public String toString() {
        return "BucketTrash{" +
                "trashPath='" + trashPath + '\'' +
                ", cleanInterval=" + cleanInterval +
                ", status=" + status +
                ", prefixMatchRules=" + prefixMatchRules +
                '}';
    }
}

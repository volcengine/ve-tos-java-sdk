package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StatusType;

import java.util.List;

public class ReplicationRule {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Status")
    private StatusType status;
    @JsonProperty("PrefixSet")
    private List<String> prefixSet;
    @JsonProperty("Tags")
    private List<Tag> tags;
    @JsonProperty("Destination")
    private Destination destination;
    @JsonProperty("HistoricalObjectReplication")
    private StatusType historicalObjectReplication;
    @JsonProperty("AccessControlTranslation")
    private AccessControlTranslation accessControlTranslation;
    @JsonProperty("Progress")
    private Progress progress;

    public String getId() {
        return id;
    }

    public ReplicationRule setId(String id) {
        this.id = id;
        return this;
    }

    public StatusType getStatus() {
        return status;
    }

    public ReplicationRule setStatus(StatusType status) {
        this.status = status;
        return this;
    }

    public List<String> getPrefixSet() {
        return prefixSet;
    }

    public ReplicationRule setPrefixSet(List<String> prefixSet) {
        this.prefixSet = prefixSet;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public ReplicationRule setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public Destination getDestination() {
        return destination;
    }

    public ReplicationRule setDestination(Destination destination) {
        this.destination = destination;
        return this;
    }

    public StatusType getHistoricalObjectReplication() {
        return historicalObjectReplication;
    }

    public ReplicationRule setHistoricalObjectReplication(StatusType historicalObjectReplication) {
        this.historicalObjectReplication = historicalObjectReplication;
        return this;
    }

    public AccessControlTranslation getAccessControlTranslation() {
        return accessControlTranslation;
    }

    public ReplicationRule setAccessControlTranslation(AccessControlTranslation accessControlTranslation) {
        this.accessControlTranslation = accessControlTranslation;
        return this;
    }

    public Progress getProgress() {
        return progress;
    }

    public ReplicationRule setProgress(Progress progress) {
        this.progress = progress;
        return this;
    }

    @Override
    public String toString() {
        return "ReplicationRule{" +
                "id='" + id + '\'' +
                ", status=" + status +
                ", prefixSet=" + prefixSet +
                ", tags=" + tags +
                ", destination=" + destination +
                ", historicalObjectReplication=" + historicalObjectReplication +
                ", accessControlTranslation=" + accessControlTranslation +
                ", progress=" + progress +
                '}';
    }

    public static ReplicationRuleBuilder builder() {
        return new ReplicationRuleBuilder();
    }

    public static final class ReplicationRuleBuilder {
        private String id;
        private StatusType status;
        private List<String> prefixSet;
        private List<Tag> tags;
        private Destination destination;
        private StatusType historicalObjectReplication;
        private AccessControlTranslation accessControlTranslation;
        private Progress progress;

        private ReplicationRuleBuilder() {
        }

        public ReplicationRuleBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ReplicationRuleBuilder status(StatusType status) {
            this.status = status;
            return this;
        }

        public ReplicationRuleBuilder prefixSet(List<String> prefixSet) {
            this.prefixSet = prefixSet;
            return this;
        }

        public ReplicationRuleBuilder tags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public ReplicationRuleBuilder destination(Destination destination) {
            this.destination = destination;
            return this;
        }

        public ReplicationRuleBuilder historicalObjectReplication(StatusType historicalObjectReplication) {
            this.historicalObjectReplication = historicalObjectReplication;
            return this;
        }

        public ReplicationRuleBuilder accessControlTranslation(AccessControlTranslation accessControlTranslation) {
            this.accessControlTranslation = accessControlTranslation;
            return this;
        }

        public ReplicationRuleBuilder progress(Progress progress) {
            this.progress = progress;
            return this;
        }

        public ReplicationRule build() {
            ReplicationRule replicationRule = new ReplicationRule();
            replicationRule.setId(id);
            replicationRule.setStatus(status);
            replicationRule.setPrefixSet(prefixSet);
            replicationRule.setTags(tags);
            replicationRule.setDestination(destination);
            replicationRule.setHistoricalObjectReplication(historicalObjectReplication);
            replicationRule.setProgress(progress);
            replicationRule.setAccessControlTranslation(accessControlTranslation);
            return replicationRule;
        }
    }
}

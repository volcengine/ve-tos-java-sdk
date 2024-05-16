package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StatusType;

import java.util.List;

public class LifecycleRule {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("Status")
    private StatusType status;
    @JsonProperty("Transitions")
    private List<Transition> transitions;
    @JsonProperty("Expiration")
    private Expiration expiration;
    @JsonProperty("NoncurrentVersionTransitions")
    private List<NoncurrentVersionTransition> noncurrentVersionTransitions;
    @JsonProperty("NoncurrentVersionExpiration")
    private NoncurrentVersionExpiration noncurrentVersionExpiration;
    @JsonProperty("Tags")
    private List<Tag> tags;
    @JsonProperty("AbortIncompleteMultipartUpload")
    private AbortInCompleteMultipartUpload abortInCompleteMultipartUpload;
    @JsonProperty("Filter")
    private LifecycleRuleFilter filter;

    public String getId() {
        return id;
    }

    public LifecycleRule setId(String id) {
        this.id = id;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public LifecycleRule setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public StatusType getStatus() {
        return status;
    }

    public LifecycleRule setStatus(StatusType status) {
        this.status = status;
        return this;
    }

    public List<Transition> getTransitions() {
        return transitions;
    }

    public LifecycleRule setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
        return this;
    }

    public Expiration getExpiration() {
        return expiration;
    }

    public LifecycleRule setExpiration(Expiration expiration) {
        this.expiration = expiration;
        return this;
    }

    public List<NoncurrentVersionTransition> getNoncurrentVersionTransitions() {
        return noncurrentVersionTransitions;
    }

    public LifecycleRule setNoncurrentVersionTransitions(List<NoncurrentVersionTransition> noncurrentVersionTransitions) {
        this.noncurrentVersionTransitions = noncurrentVersionTransitions;
        return this;
    }

    public NoncurrentVersionExpiration getNoncurrentVersionExpiration() {
        return noncurrentVersionExpiration;
    }

    public LifecycleRule setNoncurrentVersionExpiration(NoncurrentVersionExpiration noncurrentVersionExpiration) {
        this.noncurrentVersionExpiration = noncurrentVersionExpiration;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public LifecycleRule setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public AbortInCompleteMultipartUpload getAbortInCompleteMultipartUpload() {
        return abortInCompleteMultipartUpload;
    }

    public LifecycleRule setAbortInCompleteMultipartUpload(AbortInCompleteMultipartUpload abortInCompleteMultipartUpload) {
        this.abortInCompleteMultipartUpload = abortInCompleteMultipartUpload;
        return this;
    }

    public LifecycleRuleFilter getFilter() {
        return filter;
    }

    public LifecycleRule setFilter(LifecycleRuleFilter filter) {
        this.filter = filter;
        return this;
    }

    @Override
    public String toString() {
        return "LifecycleRule{" +
                "id='" + id + '\'' +
                ", prefix='" + prefix + '\'' +
                ", status=" + status +
                ", transitions=" + transitions +
                ", expiration=" + expiration +
                ", noncurrentVersionTransitions=" + noncurrentVersionTransitions +
                ", noncurrentVersionExpiration=" + noncurrentVersionExpiration +
                ", tags=" + tags +
                ", abortInCompleteMultipartUpload=" + abortInCompleteMultipartUpload +
                ", filter=" + filter +
                '}';
    }

    public static LifecycleRuleBuilder builder() {
        return new LifecycleRuleBuilder();
    }

    public static final class LifecycleRuleBuilder {
        private String id;
        private String prefix;
        private StatusType status;
        private List<Transition> transitions;
        private Expiration expiration;
        private List<NoncurrentVersionTransition> noncurrentVersionTransitions;
        private NoncurrentVersionExpiration noncurrentVersionExpiration;
        private List<Tag> tags;
        private AbortInCompleteMultipartUpload abortInCompleteMultipartUpload;
        private LifecycleRuleFilter filter;

        private LifecycleRuleBuilder() {
        }

        public LifecycleRuleBuilder id(String id) {
            this.id = id;
            return this;
        }

        public LifecycleRuleBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public LifecycleRuleBuilder status(StatusType status) {
            this.status = status;
            return this;
        }

        public LifecycleRuleBuilder transitions(List<Transition> transitions) {
            this.transitions = transitions;
            return this;
        }

        public LifecycleRuleBuilder expiration(Expiration expiration) {
            this.expiration = expiration;
            return this;
        }

        public LifecycleRuleBuilder noncurrentVersionTransitions(List<NoncurrentVersionTransition> noncurrentVersionTransitions) {
            this.noncurrentVersionTransitions = noncurrentVersionTransitions;
            return this;
        }

        public LifecycleRuleBuilder noncurrentVersionExpiration(NoncurrentVersionExpiration noncurrentVersionExpiration) {
            this.noncurrentVersionExpiration = noncurrentVersionExpiration;
            return this;
        }

        public LifecycleRuleBuilder tags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public LifecycleRuleBuilder abortInCompleteMultipartUpload(AbortInCompleteMultipartUpload abortInCompleteMultipartUpload) {
            this.abortInCompleteMultipartUpload = abortInCompleteMultipartUpload;
            return this;
        }

        public LifecycleRuleBuilder filter(LifecycleRuleFilter filter) {
            this.filter = filter;
            return this;
        }

        public LifecycleRule build() {
            LifecycleRule lifecycleRule = new LifecycleRule();
            lifecycleRule.setId(id);
            lifecycleRule.setPrefix(prefix);
            lifecycleRule.setStatus(status);
            lifecycleRule.setTransitions(transitions);
            lifecycleRule.setExpiration(expiration);
            lifecycleRule.setNoncurrentVersionTransitions(noncurrentVersionTransitions);
            lifecycleRule.setNoncurrentVersionExpiration(noncurrentVersionExpiration);
            lifecycleRule.setTags(tags);
            lifecycleRule.setAbortInCompleteMultipartUpload(abortInCompleteMultipartUpload);
            lifecycleRule.setFilter(filter);
            return lifecycleRule;
        }
    }
}

package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.List;

public class PutObjectSetQuotaByTagInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("Rules")
    private List<ObjectSetQuotaByTagRule> rules;

    public String getBucket() {
        return bucket;
    }

    public PutObjectSetQuotaByTagInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public List<ObjectSetQuotaByTagRule> getRules() {
        return rules;
    }

    public PutObjectSetQuotaByTagInput setRules(List<ObjectSetQuotaByTagRule> rules) {
        this.rules = rules;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectSetQuotaByTagInput{" +
                "bucket='" + bucket + '\'' +
                ", rules=" + rules +
                '}';
    }

    public static PutObjectSetQuotaByTagInputBuilder builder() {
        return new PutObjectSetQuotaByTagInputBuilder();
    }

    public static final class PutObjectSetQuotaByTagInputBuilder {
        private String bucket;
        private List<ObjectSetQuotaByTagRule> rules;

        private PutObjectSetQuotaByTagInputBuilder() {
        }

        public PutObjectSetQuotaByTagInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutObjectSetQuotaByTagInputBuilder rules(List<ObjectSetQuotaByTagRule> rules) {
            this.rules = rules;
            return this;
        }

        public PutObjectSetQuotaByTagInput build() {
            PutObjectSetQuotaByTagInput input = new PutObjectSetQuotaByTagInput();
            input.setBucket(bucket);
            input.setRules(rules);
            return input;
        }
    }
}

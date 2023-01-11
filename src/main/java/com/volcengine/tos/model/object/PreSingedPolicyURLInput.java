package com.volcengine.tos.model.object;

import java.util.List;

public class PreSingedPolicyURLInput {
    private String bucket;
    private long expires;
    private List<PolicySignatureCondition> conditions;
    private String alternativeEndpoint;
    private boolean isCustomDomain;

    public long getExpires() {
        return expires;
    }

    public PreSingedPolicyURLInput setExpires(long expires) {
        this.expires = expires;
        return this;
    }

    public List<PolicySignatureCondition> getConditions() {
        return conditions;
    }

    public PreSingedPolicyURLInput setConditions(List<PolicySignatureCondition> conditions) {
        this.conditions = conditions;
        return this;
    }

    public String getAlternativeEndpoint() {
        return alternativeEndpoint;
    }

    public PreSingedPolicyURLInput setAlternativeEndpoint(String alternativeEndpoint) {
        this.alternativeEndpoint = alternativeEndpoint;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public PreSingedPolicyURLInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public boolean isCustomDomain() {
        return isCustomDomain;
    }

    public PreSingedPolicyURLInput setCustomDomain(boolean customDomain) {
        this.isCustomDomain = customDomain;
        return this;
    }

    @Override
    public String toString() {
        return "PreSingedPolicyURLInput{" +
                "bucket='" + bucket + '\'' +
                ", expires=" + expires +
                ", conditions=" + conditions +
                ", alternativeEndpoint='" + alternativeEndpoint + '\'' +
                ", isCustomDomain=" + isCustomDomain +
                '}';
    }

    public static PreSingedPolicyURLInputBuilder builder() {
        return new PreSingedPolicyURLInputBuilder();
    }

    public static final class PreSingedPolicyURLInputBuilder {
        private String bucket;
        private long expires;
        private List<PolicySignatureCondition> conditions;
        private String alternativeEndpoint;
        private boolean useEndpointWithoutBucket;

        private PreSingedPolicyURLInputBuilder() {
        }

        public PreSingedPolicyURLInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PreSingedPolicyURLInputBuilder expires(long expires) {
            this.expires = expires;
            return this;
        }

        public PreSingedPolicyURLInputBuilder conditions(List<PolicySignatureCondition> conditions) {
            this.conditions = conditions;
            return this;
        }

        public PreSingedPolicyURLInputBuilder alternativeEndpoint(String alternativeEndpoint) {
            this.alternativeEndpoint = alternativeEndpoint;
            return this;
        }

        public PreSingedPolicyURLInputBuilder useEndpointWithoutBucket(boolean useEndpointWithoutBucket) {
            this.useEndpointWithoutBucket = useEndpointWithoutBucket;
            return this;
        }

        public PreSingedPolicyURLInput build() {
            PreSingedPolicyURLInput preSingedPolicyURLInput = new PreSingedPolicyURLInput();
            preSingedPolicyURLInput.setBucket(bucket);
            preSingedPolicyURLInput.setExpires(expires);
            preSingedPolicyURLInput.setConditions(conditions);
            preSingedPolicyURLInput.setAlternativeEndpoint(alternativeEndpoint);
            preSingedPolicyURLInput.setCustomDomain(useEndpointWithoutBucket);
            return preSingedPolicyURLInput;
        }
    }
}

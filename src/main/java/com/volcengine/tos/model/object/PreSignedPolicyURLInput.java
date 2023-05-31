package com.volcengine.tos.model.object;

import java.util.List;

public class PreSignedPolicyURLInput {
    private String bucket;
    private long expires;
    private List<PolicySignatureCondition> conditions;
    private String alternativeEndpoint;
    private boolean isCustomDomain;

    public long getExpires() {
        return expires;
    }

    public PreSignedPolicyURLInput setExpires(long expires) {
        this.expires = expires;
        return this;
    }

    public List<PolicySignatureCondition> getConditions() {
        return conditions;
    }

    public PreSignedPolicyURLInput setConditions(List<PolicySignatureCondition> conditions) {
        this.conditions = conditions;
        return this;
    }

    public String getAlternativeEndpoint() {
        return alternativeEndpoint;
    }

    public PreSignedPolicyURLInput setAlternativeEndpoint(String alternativeEndpoint) {
        this.alternativeEndpoint = alternativeEndpoint;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public PreSignedPolicyURLInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public boolean isCustomDomain() {
        return isCustomDomain;
    }

    public PreSignedPolicyURLInput setCustomDomain(boolean customDomain) {
        this.isCustomDomain = customDomain;
        return this;
    }

    @Override
    public String toString() {
        return "PreSignedPolicyURLInput{" +
                "bucket='" + bucket + '\'' +
                ", expires=" + expires +
                ", conditions=" + conditions +
                ", alternativeEndpoint='" + alternativeEndpoint + '\'' +
                ", isCustomDomain=" + isCustomDomain +
                '}';
    }

    public static PreSignedPolicyURLInput.PreSignedPolicyURLInputBuilder builder() {
        return new PreSignedPolicyURLInput.PreSignedPolicyURLInputBuilder();
    }

    public static final class PreSignedPolicyURLInputBuilder {
        private String bucket;
        private long expires;
        private List<PolicySignatureCondition> conditions;
        private String alternativeEndpoint;
        private boolean useEndpointWithoutBucket;

        private PreSignedPolicyURLInputBuilder() {
        }

        public PreSignedPolicyURLInput.PreSignedPolicyURLInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PreSignedPolicyURLInput.PreSignedPolicyURLInputBuilder expires(long expires) {
            this.expires = expires;
            return this;
        }

        public PreSignedPolicyURLInput.PreSignedPolicyURLInputBuilder conditions(List<PolicySignatureCondition> conditions) {
            this.conditions = conditions;
            return this;
        }

        public PreSignedPolicyURLInput.PreSignedPolicyURLInputBuilder alternativeEndpoint(String alternativeEndpoint) {
            this.alternativeEndpoint = alternativeEndpoint;
            return this;
        }

        public PreSignedPolicyURLInput.PreSignedPolicyURLInputBuilder useEndpointWithoutBucket(boolean useEndpointWithoutBucket) {
            this.useEndpointWithoutBucket = useEndpointWithoutBucket;
            return this;
        }

        public PreSignedPolicyURLInput build() {
            PreSignedPolicyURLInput PreSignedPolicyURLInput = new PreSignedPolicyURLInput();
            PreSignedPolicyURLInput.setBucket(bucket);
            PreSignedPolicyURLInput.setExpires(expires);
            PreSignedPolicyURLInput.setConditions(conditions);
            PreSignedPolicyURLInput.setAlternativeEndpoint(alternativeEndpoint);
            PreSignedPolicyURLInput.setCustomDomain(useEndpointWithoutBucket);
            return PreSignedPolicyURLInput;
        }
    }
}

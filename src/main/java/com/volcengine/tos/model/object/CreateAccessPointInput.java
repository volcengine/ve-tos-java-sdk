package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class CreateAccessPointInput extends GenericInput {

    @JsonIgnore
    private String accountId;

    @JsonIgnore
    private String accessPointName;

    @JsonProperty("Bucket")
    private String bucket;

    @JsonProperty("BucketAccountId")
    private String bucketAccountId;

    @JsonProperty("NetworkOrigin")
    private String networkOrigin;

    @JsonProperty("VpcId")
    private String vpcId;

    public CreateAccessPointInput() {
    }

    public String getAccountId() {
        return accountId;
    }

    public CreateAccessPointInput setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getAccessPointName() {
        return accessPointName;
    }

    public CreateAccessPointInput setAccessPointName(String accessPointName) {
        this.accessPointName = accessPointName;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public CreateAccessPointInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getBucketAccountId() {
        return bucketAccountId;
    }

    public CreateAccessPointInput setBucketAccountId(String bucketAccountId) {
        this.bucketAccountId = bucketAccountId;
        return this;
    }

    public String getNetworkOrigin() {
        return networkOrigin;
    }

    public CreateAccessPointInput setNetworkOrigin(String networkOrigin) {
        this.networkOrigin = networkOrigin;
        return this;
    }

    public String getVpcId() {
        return vpcId;
    }

    public CreateAccessPointInput setVpcId(String vpcId) {
        this.vpcId = vpcId;
        return this;
    }

    @Override
    public String toString() {
        return "CreateAccessPointInput{" +
                "accountId='" + accountId + '\'' +
                ", accessPointName='" + accessPointName + '\'' +
                ", bucket='" + bucket + '\'' +
                ", bucketAccountId='" + bucketAccountId + '\'' +
                ", networkOrigin='" + networkOrigin + '\'' +
                ", vpcId='" + vpcId + '\'' +
                '}';
    }

    public static CreateAccessPointInputBuilder builder() {
        return new CreateAccessPointInputBuilder();
    }

    public static final class CreateAccessPointInputBuilder {
        private String accountId;
        private String accessPointName;
        private String bucket;
        private String bucketAccountId;
        private String networkOrigin;
        private String vpcId;

        private CreateAccessPointInputBuilder() {
        }

        public CreateAccessPointInputBuilder accountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public CreateAccessPointInputBuilder accessPointName(String accessPointName) {
            this.accessPointName = accessPointName;
            return this;
        }

        public CreateAccessPointInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public CreateAccessPointInputBuilder bucketAccountId(String bucketAccountId) {
            this.bucketAccountId = bucketAccountId;
            return this;
        }

        public CreateAccessPointInputBuilder networkOrigin(String networkOrigin) {
            this.networkOrigin = networkOrigin;
            return this;
        }

        public CreateAccessPointInputBuilder vpcId(String vpcId) {
            this.vpcId = vpcId;
            return this;
        }

        public CreateAccessPointInput build() {
            CreateAccessPointInput input = new CreateAccessPointInput();
            input.setAccountId(accountId);
            input.setAccessPointName(accessPointName);
            input.setBucket(bucket);
            input.setBucketAccountId(bucketAccountId);
            input.setNetworkOrigin(networkOrigin);
            input.setVpcId(vpcId);
            return input;
        }
    }
}

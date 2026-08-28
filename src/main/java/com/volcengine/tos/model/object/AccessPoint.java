package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessPoint {

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Alias")
    private String alias;

    @JsonProperty("Bucket")
    private String bucket;

    @JsonProperty("BucketAccountId")
    private String bucketAccountId;

    @JsonProperty("AccessPointType")
    private String accessPointType;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("NetworkOrigin")
    private String networkOrigin;

    @JsonProperty("VpcId")
    private String vpcId;

    @JsonProperty("AccessPointTrn")
    private String accessPointTrn;

    @JsonProperty("CreationDate")
    private String creationDate;

    @JsonProperty("Endpoints")
    private AccessPointEndpoints endpoints;

    public AccessPoint() {
    }

    public String getName() {
        return name;
    }

    public AccessPoint setName(String name) {
        this.name = name;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public AccessPoint setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public AccessPoint setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getBucketAccountId() {
        return bucketAccountId;
    }

    public AccessPoint setBucketAccountId(String bucketAccountId) {
        this.bucketAccountId = bucketAccountId;
        return this;
    }

    public String getAccessPointType() {
        return accessPointType;
    }

    public AccessPoint setAccessPointType(String accessPointType) {
        this.accessPointType = accessPointType;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public AccessPoint setStatus(String status) {
        this.status = status;
        return this;
    }

    public String getNetworkOrigin() {
        return networkOrigin;
    }

    public AccessPoint setNetworkOrigin(String networkOrigin) {
        this.networkOrigin = networkOrigin;
        return this;
    }

    public String getVpcId() {
        return vpcId;
    }

    public AccessPoint setVpcId(String vpcId) {
        this.vpcId = vpcId;
        return this;
    }

    public String getAccessPointTrn() {
        return accessPointTrn;
    }

    public AccessPoint setAccessPointTrn(String accessPointTrn) {
        this.accessPointTrn = accessPointTrn;
        return this;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public AccessPoint setCreationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public AccessPointEndpoints getEndpoints() {
        return endpoints;
    }

    public AccessPoint setEndpoints(AccessPointEndpoints endpoints) {
        this.endpoints = endpoints;
        return this;
    }

    @Override
    public String toString() {
        return "AccessPoint{" +
                "name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", bucket='" + bucket + '\'' +
                ", bucketAccountId='" + bucketAccountId + '\'' +
                ", accessPointType='" + accessPointType + '\'' +
                ", status='" + status + '\'' +
                ", networkOrigin='" + networkOrigin + '\'' +
                ", vpcId='" + vpcId + '\'' +
                ", accessPointTrn='" + accessPointTrn + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", endpoints=" + endpoints +
                '}';
    }
}

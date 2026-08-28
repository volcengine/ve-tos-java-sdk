package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.AzRedundancyType;
import com.volcengine.tos.comm.common.BucketType;
import com.volcengine.tos.comm.common.StatusType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.acl.Owner;

import java.time.LocalDateTime;

public class BucketInfo {
    @JsonProperty("Name")
    private String name;

    @JsonProperty("Owner")
    private Owner owner;

    @JsonProperty("CreationDate")
    private String creationDate;

    @JsonProperty("StorageClass")
    private StorageClassType storageClass;

    @JsonProperty("ProjectName")
    private String projectName;

    @JsonProperty("Type")
    private BucketType type;

    @JsonProperty("Location")
    private String location;

    @JsonProperty("AzRedundancy")
    private AzRedundancyType azRedundancy;

    @JsonProperty("ExtranetEndpoint")
    private String extranetEndpoint;

    @JsonProperty("IntranetEndpoint")
    private String intranetEndpoint;

    @JsonProperty("ExtranetS3Endpoint")
    private String extranetS3Endpoint;

    @JsonProperty("IntranetS3Endpoint")
    private String intranetS3Endpoint;

    @JsonProperty("Versioning")
    private String versioning;

    @JsonProperty("CrossRegionReplication")
    private StatusType crossRegionReplication;

    @JsonProperty("TransferAcceleration")
    private StatusType transferAcceleration;

    @JsonProperty("AccessMonitor")
    private StatusType accessMonitor;

    @JsonProperty("ServerSideEncryptionConfiguration")
    private ServerSideEncryptionConfiguration serverSideEncryptionConfiguration;

    public String getName() {
        return name;
    }

    public BucketInfo setName(String name) {
        this.name = name;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public BucketInfo setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public BucketInfo setCreationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public BucketInfo setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public BucketInfo setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public BucketType getType() {
        return type;
    }

    public BucketInfo setType(BucketType type) {
        this.type = type;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public BucketInfo setLocation(String location) {
        this.location = location;
        return this;
    }

    public AzRedundancyType getAzRedundancy() {
        return azRedundancy;
    }

    public BucketInfo setAzRedundancy(AzRedundancyType azRedundancy) {
        this.azRedundancy = azRedundancy;
        return this;
    }

    public String getExtranetEndpoint() {
        return extranetEndpoint;
    }

    public BucketInfo setExtranetEndpoint(String extranetEndpoint) {
        this.extranetEndpoint = extranetEndpoint;
        return this;
    }

    public String getIntranetEndpoint() {
        return intranetEndpoint;
    }

    public BucketInfo setIntranetEndpoint(String intranetEndpoint) {
        this.intranetEndpoint = intranetEndpoint;
        return this;
    }

    public String getExtranetS3Endpoint() {
        return extranetS3Endpoint;
    }

    public BucketInfo setExtranetS3Endpoint(String extranetS3Endpoint) {
        this.extranetS3Endpoint = extranetS3Endpoint;
        return this;
    }

    public String getIntranetS3Endpoint() {
        return intranetS3Endpoint;
    }

    public BucketInfo setIntranetS3Endpoint(String intranetS3Endpoint) {
        this.intranetS3Endpoint = intranetS3Endpoint;
        return this;
    }

    public String getVersioning() {
        return versioning;
    }

    public BucketInfo setVersioning(String versioning) {
        this.versioning = versioning;
        return this;
    }

    public StatusType getCrossRegionReplication() {
        return crossRegionReplication;
    }

    public BucketInfo setCrossRegionReplication(StatusType crossRegionReplication) {
        this.crossRegionReplication = crossRegionReplication;
        return this;
    }

    public StatusType getTransferAcceleration() {
        return transferAcceleration;
    }

    public BucketInfo setTransferAcceleration(StatusType transferAcceleration) {
        this.transferAcceleration = transferAcceleration;
        return this;
    }

    public StatusType getAccessMonitor() {
        return accessMonitor;
    }

    public BucketInfo setAccessMonitor(StatusType accessMonitor) {
        this.accessMonitor = accessMonitor;
        return this;
    }

    public ServerSideEncryptionConfiguration getServerSideEncryptionConfiguration() {
        return serverSideEncryptionConfiguration;
    }

    public BucketInfo setServerSideEncryptionConfiguration(ServerSideEncryptionConfiguration serverSideEncryptionConfiguration) {
        this.serverSideEncryptionConfiguration = serverSideEncryptionConfiguration;
        return this;
    }
}
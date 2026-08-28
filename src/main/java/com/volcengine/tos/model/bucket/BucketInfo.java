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
    private LocalDateTime creationDate;

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

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public void setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public BucketType getType() {
        return type;
    }

    public void setType(BucketType type) {
        this.type = type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public AzRedundancyType getAzRedundancy() {
        return azRedundancy;
    }

    public void setAzRedundancy(AzRedundancyType azRedundancy) {
        this.azRedundancy = azRedundancy;
    }

    public String getExtranetEndpoint() {
        return extranetEndpoint;
    }

    public void setExtranetEndpoint(String extranetEndpoint) {
        this.extranetEndpoint = extranetEndpoint;
    }

    public String getIntranetEndpoint() {
        return intranetEndpoint;
    }

    public void setIntranetEndpoint(String intranetEndpoint) {
        this.intranetEndpoint = intranetEndpoint;
    }

    public String getExtranetS3Endpoint() {
        return extranetS3Endpoint;
    }

    public void setExtranetS3Endpoint(String extranetS3Endpoint) {
        this.extranetS3Endpoint = extranetS3Endpoint;
    }

    public String getIntranetS3Endpoint() {
        return intranetS3Endpoint;
    }

    public void setIntranetS3Endpoint(String intranetS3Endpoint) {
        this.intranetS3Endpoint = intranetS3Endpoint;
    }

    public String getVersioning() {
        return versioning;
    }

    public void setVersioning(String versioning) {
        this.versioning = versioning;
    }

    public StatusType getCrossRegionReplication() {
        return crossRegionReplication;
    }

    public void setCrossRegionReplication(StatusType crossRegionReplication) {
        this.crossRegionReplication = crossRegionReplication;
    }

    public StatusType getTransferAcceleration() {
        return transferAcceleration;
    }

    public void setTransferAcceleration(StatusType transferAcceleration) {
        this.transferAcceleration = transferAcceleration;
    }

    public StatusType getAccessMonitor() {
        return accessMonitor;
    }

    public void setAccessMonitor(StatusType accessMonitor) {
        this.accessMonitor = accessMonitor;
    }

    public ServerSideEncryptionConfiguration getServerSideEncryptionConfiguration() {
        return serverSideEncryptionConfiguration;
    }

    public void setServerSideEncryptionConfiguration(ServerSideEncryptionConfiguration serverSideEncryptionConfiguration) {
        this.serverSideEncryptionConfiguration = serverSideEncryptionConfiguration;
    }
}
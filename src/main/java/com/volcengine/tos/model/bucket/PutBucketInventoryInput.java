package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.InventoryIncludedObjType;
import com.volcengine.tos.model.GenericInput;

public class PutBucketInventoryInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Id")
    private String id;
    @JsonProperty("IsEnabled")
    private boolean isEnabled;
    @JsonProperty("Filter")
    private BucketInventoryConfiguration.InventoryFilter filter;
    @JsonProperty("Destination")
    private BucketInventoryConfiguration.InventoryDestination destination;
    @JsonProperty("Schedule")
    private BucketInventoryConfiguration.InventorySchedule schedule;
    @JsonProperty("IncludedObjectVersions")
    private InventoryIncludedObjType includedObjectVersions;
    @JsonProperty("OptionalFields")
    private BucketInventoryConfiguration.InventoryOptionalFields optionalFields;

    public String getBucket() {
        return bucket;
    }

    public PutBucketInventoryInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }


    public String getId() {
        return id;
    }

    public PutBucketInventoryInput setId(String id) {
        this.id = id;
        return this;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public PutBucketInventoryInput setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public BucketInventoryConfiguration.InventoryFilter getFilter() {
        return filter;
    }

    public PutBucketInventoryInput setFilter(BucketInventoryConfiguration.InventoryFilter filter) {
        this.filter = filter;
        return this;
    }

    public BucketInventoryConfiguration.InventoryDestination getDestination() {
        return destination;
    }

    public PutBucketInventoryInput setDestination(BucketInventoryConfiguration.InventoryDestination destination) {
        this.destination = destination;
        return this;
    }

    public BucketInventoryConfiguration.InventorySchedule getSchedule() {
        return schedule;
    }

    public PutBucketInventoryInput setSchedule(BucketInventoryConfiguration.InventorySchedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public InventoryIncludedObjType getIncludedObjectVersions() {
        return includedObjectVersions;
    }

    public PutBucketInventoryInput setIncludedObjectVersions(InventoryIncludedObjType includedObjectVersions) {
        this.includedObjectVersions = includedObjectVersions;
        return this;
    }

    public BucketInventoryConfiguration.InventoryOptionalFields getOptionalFields() {
        return optionalFields;
    }

    public PutBucketInventoryInput setOptionalFields(BucketInventoryConfiguration.InventoryOptionalFields optionalFields) {
        this.optionalFields = optionalFields;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketInventoryInput{" +
                "bucket='" + bucket + '\'' +
                ", id='" + id + '\'' +
                ", isEnabled=" + isEnabled +
                ", filter=" + filter +
                ", destination=" + destination +
                ", schedule=" + schedule +
                ", includedObjectVersions=" + includedObjectVersions +
                ", optionalFields=" + optionalFields +
                '}';
    }
}

package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StorageClassInheritDirectiveType;
import com.volcengine.tos.comm.common.StorageClassType;

public class Destination {
    @JsonProperty("Bucket")
    private String bucket;
    @JsonProperty("Location")
    private String location;
    @JsonProperty("StorageClass")
    private StorageClassType storageClass;
    @JsonProperty("StorageClassInheritDirective")
    private StorageClassInheritDirectiveType storageClassInheritDirectiveType;

    public String getBucket() {
        return bucket;
    }

    public Destination setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public Destination setLocation(String location) {
        this.location = location;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public Destination setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public StorageClassInheritDirectiveType getStorageClassInheritDirectiveType() {
        return storageClassInheritDirectiveType;
    }

    public Destination setStorageClassInheritDirectiveType(StorageClassInheritDirectiveType storageClassInheritDirectiveType) {
        this.storageClassInheritDirectiveType = storageClassInheritDirectiveType;
        return this;
    }

    @Override
    public String toString() {
        return "Destination{" +
                "bucket='" + bucket + '\'' +
                ", location='" + location + '\'' +
                ", storageClass=" + storageClass +
                ", storageClassInheritDirectiveType=" + storageClassInheritDirectiveType +
                '}';
    }

    public static DestinationBuilder builder() {
        return new DestinationBuilder();
    }

    public static final class DestinationBuilder {
        private String bucket;
        private String location;
        private StorageClassType storageClass;
        private StorageClassInheritDirectiveType storageClassInheritDirectiveType;

        private DestinationBuilder() {
        }

        public DestinationBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DestinationBuilder location(String location) {
            this.location = location;
            return this;
        }

        public DestinationBuilder storageClass(StorageClassType storageClass) {
            this.storageClass = storageClass;
            return this;
        }

        public DestinationBuilder storageClassInheritDirectiveType(StorageClassInheritDirectiveType storageClassInheritDirectiveType) {
            this.storageClassInheritDirectiveType = storageClassInheritDirectiveType;
            return this;
        }

        public Destination build() {
            Destination destination = new Destination();
            destination.setBucket(bucket);
            destination.setLocation(location);
            destination.setStorageClass(storageClass);
            destination.setStorageClassInheritDirectiveType(storageClassInheritDirectiveType);
            return destination;
        }
    }
}

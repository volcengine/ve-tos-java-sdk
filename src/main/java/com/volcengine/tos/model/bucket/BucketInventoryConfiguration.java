package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.InventoryFormatType;
import com.volcengine.tos.comm.common.InventoryFrequencyType;
import com.volcengine.tos.comm.common.InventoryIncludedObjType;

import java.util.List;

public class BucketInventoryConfiguration {
    @JsonProperty("Id")
    protected String id;
    @JsonProperty("IsEnabled")
    protected boolean isEnabled;
    @JsonProperty("Filter")
    protected InventoryFilter filter;
    @JsonProperty("Destination")
    protected InventoryDestination destination;
    @JsonProperty("Schedule")
    protected InventorySchedule schedule;
    @JsonProperty("IncludedObjectVersions")
    protected InventoryIncludedObjType includedObjectVersions;
    @JsonProperty("OptionalFields")
    protected InventoryOptionalFields optionalFields;

    public String getId() {
        return id;
    }

    public BucketInventoryConfiguration setId(String id) {
        this.id = id;
        return this;
    }

    public boolean getIsEnabled() {
        return isEnabled;
    }

    public BucketInventoryConfiguration setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
        return this;
    }

    public InventoryFilter getFilter() {
        return filter;
    }

    public BucketInventoryConfiguration setFilter(InventoryFilter filter) {
        this.filter = filter;
        return this;
    }

    public InventoryDestination getDestination() {
        return destination;
    }

    public BucketInventoryConfiguration setDestination(InventoryDestination destination) {
        this.destination = destination;
        return this;
    }

    public InventorySchedule getSchedule() {
        return schedule;
    }

    public BucketInventoryConfiguration setSchedule(InventorySchedule schedule) {
        this.schedule = schedule;
        return this;
    }

    public InventoryIncludedObjType getIncludedObjectVersions() {
        return includedObjectVersions;
    }

    public BucketInventoryConfiguration setIncludedObjectVersions(InventoryIncludedObjType includedObjectVersions) {
        this.includedObjectVersions = includedObjectVersions;
        return this;
    }

    public InventoryOptionalFields getOptionalFields() {
        return optionalFields;
    }

    public BucketInventoryConfiguration setOptionalFields(InventoryOptionalFields optionalFields) {
        this.optionalFields = optionalFields;
        return this;
    }

    public static class InventoryFilter {
        @JsonProperty("Prefix")
        private String prefix;

        public String getPrefix() {
            return prefix;
        }

        public InventoryFilter setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        @Override
        public String toString() {
            return "InventoryFilter{" +
                    "prefix='" + prefix + '\'' +
                    '}';
        }
    }

    public static class InventoryDestination {
        @JsonProperty("TOSBucketDestination")
        private TOSBucketDestination tosBucketDestination;

        public TOSBucketDestination getTosBucketDestination() {
            return tosBucketDestination;
        }

        public InventoryDestination setTosBucketDestination(TOSBucketDestination tosBucketDestination) {
            this.tosBucketDestination = tosBucketDestination;
            return this;
        }

        @Override
        public String toString() {
            return "InventoryDestination{" +
                    "tosBucketDestination=" + tosBucketDestination +
                    '}';
        }
    }

    public static class TOSBucketDestination{
        @JsonProperty("Format")
        private InventoryFormatType format;
        @JsonProperty("AccountId")
        private String accountId;
        @JsonProperty("Role")
        private String role;
        @JsonProperty("Bucket")
        private String bucket;
        @JsonProperty("Prefix")
        private String prefix;

        public InventoryFormatType getFormat() {
            return format;
        }

        public TOSBucketDestination setFormat(InventoryFormatType format) {
            this.format = format;
            return this;
        }

        public String getAccountId() {
            return accountId;
        }

        public TOSBucketDestination setAccountId(String accountId) {
            this.accountId = accountId;
            return this;
        }

        public String getRole() {
            return role;
        }

        public TOSBucketDestination setRole(String role) {
            this.role = role;
            return this;
        }

        public String getBucket() {
            return bucket;
        }

        public TOSBucketDestination setBucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public String getPrefix() {
            return prefix;
        }

        public TOSBucketDestination setPrefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        @Override
        public String toString() {
            return "TOSBucketDestination{" +
                    "format=" + format +
                    ", accountId='" + accountId + '\'' +
                    ", role='" + role + '\'' +
                    ", bucket='" + bucket + '\'' +
                    ", prefix='" + prefix + '\'' +
                    '}';
        }
    }

    public static class InventorySchedule {
        @JsonProperty("Frequency")
        private InventoryFrequencyType frequency;

        public InventoryFrequencyType getFrequency() {
            return frequency;
        }

        public InventorySchedule setFrequency(InventoryFrequencyType frequency) {
            this.frequency = frequency;
            return this;
        }

        @Override
        public String toString() {
            return "InventorySchedule{" +
                    "frequency=" + frequency +
                    '}';
        }
    }

    public static class InventoryOptionalFields {
        @JsonProperty("Field")
        private List<String> field;

        public List<String> getField() {
            return field;
        }

        public InventoryOptionalFields setField(List<String> field) {
            this.field = field;
            return this;
        }

        @Override
        public String toString() {
            return "InventoryOptionalFields{" +
                    "field=" + field +
                    '}';
        }
    }
}

package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BucketLifecycleNotFilter {
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("Tags")
    private List<Tag> tags;

    public String getPrefix() {
        return prefix;
    }

    public BucketLifecycleNotFilter setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public BucketLifecycleNotFilter setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }

    public static BucketLifecycleNotFilterBuilder builder() {
        return new BucketLifecycleNotFilterBuilder();
    }

    public static final class BucketLifecycleNotFilterBuilder {
        private String prefix;
        private List<Tag> tags;

        private BucketLifecycleNotFilterBuilder() {
        }

        public BucketLifecycleNotFilterBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public BucketLifecycleNotFilterBuilder tags(List<Tag> tags) {
            this.tags = tags;
            return this;
        }

        public BucketLifecycleNotFilter build() {
            BucketLifecycleNotFilter bucketLifecycleNotFilter = new BucketLifecycleNotFilter();
            bucketLifecycleNotFilter.setPrefix(prefix);
            bucketLifecycleNotFilter.setTags(tags);
            return bucketLifecycleNotFilter;
        }
    }
}

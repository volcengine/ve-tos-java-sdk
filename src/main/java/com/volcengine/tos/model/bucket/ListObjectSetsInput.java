package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class ListObjectSetsInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String prefix;

    @JsonIgnore
    private String tags;

    @JsonIgnore
    private Integer maxKeys;

    @JsonIgnore
    private String marker;

    public String getBucket() {
        return bucket;
    }

    public ListObjectSetsInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListObjectSetsInput setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getTags() {
        return tags;
    }

    public ListObjectSetsInput setTags(String tags) {
        this.tags = tags;
        return this;
    }

    public Integer getMaxKeys() {
        return maxKeys;
    }

    public ListObjectSetsInput setMaxKeys(Integer maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public String getMarker() {
        return marker;
    }

    public ListObjectSetsInput setMarker(String marker) {
        this.marker = marker;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectSetsInput{" +
                "bucket='" + bucket + '\'' +
                ", prefix='" + prefix + '\'' +
                ", tags='" + tags + '\'' +
                ", maxKeys=" + maxKeys +
                ", marker='" + marker + '\'' +
                '}';
    }

    public static ListObjectSetsInputBuilder builder() {
        return new ListObjectSetsInputBuilder();
    }

    public static final class ListObjectSetsInputBuilder {
        private String bucket;
        private String prefix;
        private String tags;
        private Integer maxKeys;
        private String marker;

        private ListObjectSetsInputBuilder() {
        }

        public ListObjectSetsInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListObjectSetsInputBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ListObjectSetsInputBuilder tags(String tags) {
            this.tags = tags;
            return this;
        }

        public ListObjectSetsInputBuilder maxKeys(Integer maxKeys) {
            this.maxKeys = maxKeys;
            return this;
        }

        public ListObjectSetsInputBuilder marker(String marker) {
            this.marker = marker;
            return this;
        }

        public ListObjectSetsInput build() {
            ListObjectSetsInput input = new ListObjectSetsInput();
            input.setBucket(bucket);
            input.setPrefix(prefix);
            input.setTags(tags);
            input.setMaxKeys(maxKeys);
            input.setMarker(marker);
            return input;
        }
    }
}

package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListObjectsType2Output {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("ContinuationToken")
    private String continuationToken;
    @JsonProperty("MaxKeys")
    private int maxKeys;
    @JsonProperty("Delimiter")
    private String delimiter;
    @JsonProperty("EncodingType")
    private String encodingType;
    @JsonProperty("KeyCount")
    private int keyCount;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("NextContinuationToken")
    private String nextContinuationToken;
    @JsonProperty("CommonPrefixes")
    private List<ListedCommonPrefix> commonPrefixes;
    @JsonProperty("Contents")
    private List<ListedObjectV2> contents;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListObjectsType2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getName() {
        return name;
    }

    public ListObjectsType2Output setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListObjectsType2Output setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getContinuationToken() {
        return continuationToken;
    }

    public ListObjectsType2Output setContinuationToken(String continuationToken) {
        this.continuationToken = continuationToken;
        return this;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public ListObjectsType2Output setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListObjectsType2Output setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectsType2Output setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public int getKeyCount() {
        return keyCount;
    }

    public ListObjectsType2Output setKeyCount(int keyCount) {
        this.keyCount = keyCount;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListObjectsType2Output setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public String getNextContinuationToken() {
        return nextContinuationToken;
    }

    public ListObjectsType2Output setNextContinuationToken(String nextContinuationToken) {
        this.nextContinuationToken = nextContinuationToken;
        return this;
    }

    public List<ListedCommonPrefix> getCommonPrefixes() {
        return commonPrefixes;
    }

    public ListObjectsType2Output setCommonPrefixes(List<ListedCommonPrefix> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
        return this;
    }

    public List<ListedObjectV2> getContents() {
        return contents;
    }

    public ListObjectsType2Output setContents(List<ListedObjectV2> contents) {
        this.contents = contents;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectsType2Output{" +
                "requestInfo=" + requestInfo +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", continuationToken='" + continuationToken + '\'' +
                ", maxKeys=" + maxKeys +
                ", delimiter='" + delimiter + '\'' +
                ", encodingType='" + encodingType + '\'' +
                ", keyCount=" + keyCount +
                ", isTruncated=" + isTruncated +
                ", nextContinuationToken='" + nextContinuationToken + '\'' +
                ", commonPrefixes=" + commonPrefixes +
                ", contents=" + contents +
                '}';
    }

    public static ListObjectsType2OutputBuilder builder() {
        return new ListObjectsType2OutputBuilder();
    }

    public static final class ListObjectsType2OutputBuilder {
        private RequestInfo requestInfo;
        private String name;
        private String prefix;
        private String continuationToken;
        private int maxKeys;
        private String delimiter;
        private String encodingType;
        private int keyCount;
        private boolean isTruncated;
        private String nextContinuationToken;
        private List<ListedCommonPrefix> commonPrefixes;
        private List<ListedObjectV2> contents;

        private ListObjectsType2OutputBuilder() {
        }

        public ListObjectsType2OutputBuilder requestInfo(RequestInfo requestInfo) {
            this.requestInfo = requestInfo;
            return this;
        }

        public ListObjectsType2OutputBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ListObjectsType2OutputBuilder prefix(String prefix) {
            this.prefix = prefix;
            return this;
        }

        public ListObjectsType2OutputBuilder continuationToken(String continuationToken) {
            this.continuationToken = continuationToken;
            return this;
        }

        public ListObjectsType2OutputBuilder maxKeys(int maxKeys) {
            this.maxKeys = maxKeys;
            return this;
        }

        public ListObjectsType2OutputBuilder delimiter(String delimiter) {
            this.delimiter = delimiter;
            return this;
        }

        public ListObjectsType2OutputBuilder encodingType(String encodingType) {
            this.encodingType = encodingType;
            return this;
        }

        public ListObjectsType2OutputBuilder keyCount(int keyCount) {
            this.keyCount = keyCount;
            return this;
        }

        public ListObjectsType2OutputBuilder isTruncated(boolean isTruncated) {
            this.isTruncated = isTruncated;
            return this;
        }

        public ListObjectsType2OutputBuilder nextContinuationToken(String nextContinuationToken) {
            this.nextContinuationToken = nextContinuationToken;
            return this;
        }

        public ListObjectsType2OutputBuilder commonPrefixes(List<ListedCommonPrefix> commonPrefixes) {
            this.commonPrefixes = commonPrefixes;
            return this;
        }

        public ListObjectsType2OutputBuilder contents(List<ListedObjectV2> contents) {
            this.contents = contents;
            return this;
        }

        public ListObjectsType2Output build() {
            ListObjectsType2Output listObjectsType2Output = new ListObjectsType2Output();
            listObjectsType2Output.setRequestInfo(requestInfo);
            listObjectsType2Output.setName(name);
            listObjectsType2Output.setPrefix(prefix);
            listObjectsType2Output.setContinuationToken(continuationToken);
            listObjectsType2Output.setMaxKeys(maxKeys);
            listObjectsType2Output.setDelimiter(delimiter);
            listObjectsType2Output.setEncodingType(encodingType);
            listObjectsType2Output.setKeyCount(keyCount);
            listObjectsType2Output.setNextContinuationToken(nextContinuationToken);
            listObjectsType2Output.setCommonPrefixes(commonPrefixes);
            listObjectsType2Output.setContents(contents);
            listObjectsType2Output.isTruncated = this.isTruncated;
            return listObjectsType2Output;
        }
    }
}

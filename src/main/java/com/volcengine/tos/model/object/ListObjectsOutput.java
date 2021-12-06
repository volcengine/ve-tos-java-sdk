package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class ListObjectsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("Marker")
    private String marker;
    @JsonProperty("MaxKeys")
    private long maxKeys;
    @JsonProperty("NextMarker")
    private String nextMarker;
    @JsonProperty("Delimiter")
    private String delimiter;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("EncodingType")
    private String encodingType;
    @JsonProperty("CommonPrefixes")
    private ListedCommonPrefix[] commonPrefixes;
    @JsonProperty("Contents")
    private ListedObject[] contents;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListObjectsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getName() {
        return name;
    }

    public ListObjectsOutput setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListObjectsOutput setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getMarker() {
        return marker;
    }

    public ListObjectsOutput setMarker(String marker) {
        this.marker = marker;
        return this;
    }

    public long getMaxKeys() {
        return maxKeys;
    }

    public ListObjectsOutput setMaxKeys(long maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public ListObjectsOutput setNextMarker(String nextMarker) {
        this.nextMarker = nextMarker;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListObjectsOutput setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListObjectsOutput setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectsOutput setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public ListedCommonPrefix[] getCommonPrefixes() {
        return commonPrefixes;
    }

    public ListObjectsOutput setCommonPrefixes(ListedCommonPrefix[] commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
        return this;
    }

    public ListedObject[] getContents() {
        return contents;
    }

    public ListObjectsOutput setContents(ListedObject[] contents) {
        this.contents = contents;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectsOutput{" +
                "requestInfo=" + requestInfo +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", marker='" + marker + '\'' +
                ", maxKeys=" + maxKeys +
                ", nextMarker='" + nextMarker + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", isTruncated=" + isTruncated +
                ", encodingType='" + encodingType + '\'' +
                ", commonPrefixes=" + Arrays.toString(commonPrefixes) +
                ", contents=" + Arrays.toString(contents) +
                '}';
    }
}

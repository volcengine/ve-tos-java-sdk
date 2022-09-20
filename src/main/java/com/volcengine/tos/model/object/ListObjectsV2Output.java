package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListObjectsV2Output {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("Marker")
    private String marker;
    @JsonProperty("MaxKeys")
    private int maxKeys;
    @JsonProperty("NextMarker")
    private String nextMarker;
    @JsonProperty("Delimiter")
    private String delimiter;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("EncodingType")
    private String encodingType;
    @JsonProperty("CommonPrefixes")
    private List<ListedCommonPrefix> commonPrefixes;
    @JsonProperty("Contents")
    private List<ListedObjectV2> contents;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListObjectsV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getName() {
        return name;
    }

    public ListObjectsV2Output setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListObjectsV2Output setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getMarker() {
        return marker;
    }

    public ListObjectsV2Output setMarker(String marker) {
        this.marker = marker;
        return this;
    }

    public int getMaxKeys() {
        return maxKeys;
    }

    public ListObjectsV2Output setMaxKeys(int maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public String getNextMarker() {
        return nextMarker;
    }

    public ListObjectsV2Output setNextMarker(String nextMarker) {
        this.nextMarker = nextMarker;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListObjectsV2Output setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListObjectsV2Output setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectsV2Output setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public List<ListedCommonPrefix> getCommonPrefixes() {
        return commonPrefixes;
    }

    public ListObjectsV2Output setCommonPrefixes(List<ListedCommonPrefix> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
        return this;
    }

    public List<ListedObjectV2> getContents() {
        return contents;
    }

    public ListObjectsV2Output setContents(List<ListedObjectV2> contents) {
        this.contents = contents;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectsV2Output{" +
                "requestInfo=" + requestInfo +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", marker='" + marker + '\'' +
                ", maxKeys=" + maxKeys +
                ", nextMarker='" + nextMarker + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", isTruncated=" + isTruncated +
                ", encodingType='" + encodingType + '\'' +
                ", commonPrefixes=" + commonPrefixes +
                ", contents=" + contents +
                '}';
    }
}

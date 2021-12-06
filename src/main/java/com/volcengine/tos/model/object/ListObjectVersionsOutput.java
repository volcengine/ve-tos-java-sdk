package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class ListObjectVersionsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Prefix")
    private String prefix;
    @JsonProperty("KeyMarker")
    private String keyMarker;
    @JsonProperty("VersionIdMarker")
    private String versionIDMarker;
    @JsonProperty("Delimiter")
    private String delimiter;
    @JsonProperty("EncodingType")
    private String encodingType;
    @JsonProperty("MaxKeys")
    private long maxKeys;
    @JsonProperty("NextKeyMarker")
    private String nextKeyMarker;
    @JsonProperty("NextVersionIdMarker")
    private String nextVersionIDMarker;
    @JsonProperty("IsTruncated")
    private boolean isTruncated;
    @JsonProperty("CommonPrefixes")
    private ListedCommonPrefix[] commonPrefixes;
    @JsonProperty("Versions")
    private ListedObjectVersion[] versions;
    @JsonProperty("DeleteMarkers")
    private ListedDeleteMarkerEntry[] deleteMarkers;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListObjectVersionsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getName() {
        return name;
    }

    public ListObjectVersionsOutput setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListObjectVersionsOutput setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public ListObjectVersionsOutput setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
        return this;
    }

    public String getVersionIDMarker() {
        return versionIDMarker;
    }

    public ListObjectVersionsOutput setVersionIDMarker(String versionIDMarker) {
        this.versionIDMarker = versionIDMarker;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListObjectVersionsOutput setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectVersionsOutput setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public long getMaxKeys() {
        return maxKeys;
    }

    public ListObjectVersionsOutput setMaxKeys(long maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public String getNextKeyMarker() {
        return nextKeyMarker;
    }

    public ListObjectVersionsOutput setNextKeyMarker(String nextKeyMarker) {
        this.nextKeyMarker = nextKeyMarker;
        return this;
    }

    public String getNextVersionIDMarker() {
        return nextVersionIDMarker;
    }

    public ListObjectVersionsOutput setNextVersionIDMarker(String nextVersionIDMarker) {
        this.nextVersionIDMarker = nextVersionIDMarker;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListObjectVersionsOutput setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public ListedCommonPrefix[] getCommonPrefixes() {
        return commonPrefixes;
    }

    public ListObjectVersionsOutput setCommonPrefixes(ListedCommonPrefix[] commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
        return this;
    }

    public ListedObjectVersion[] getVersions() {
        return versions;
    }

    public ListObjectVersionsOutput setVersions(ListedObjectVersion[] versions) {
        this.versions = versions;
        return this;
    }

    public ListedDeleteMarkerEntry[] getDeleteMarkers() {
        return deleteMarkers;
    }

    public ListObjectVersionsOutput setDeleteMarkers(ListedDeleteMarkerEntry[] deleteMarkers) {
        this.deleteMarkers = deleteMarkers;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectVersionsOutput{" +
                "requestInfo=" + requestInfo +
                ", name='" + name + '\'' +
                ", prefix='" + prefix + '\'' +
                ", keyMarker='" + keyMarker + '\'' +
                ", versionIDMarker='" + versionIDMarker + '\'' +
                ", delimiter='" + delimiter + '\'' +
                ", encodingType='" + encodingType + '\'' +
                ", maxKeys=" + maxKeys +
                ", nextKeyMarker='" + nextKeyMarker + '\'' +
                ", nextVersionIDMarker='" + nextVersionIDMarker + '\'' +
                ", isTruncated=" + isTruncated +
                ", commonPrefixes=" + Arrays.toString(commonPrefixes) +
                ", versions=" + Arrays.toString(versions) +
                ", deleteMarkers=" + Arrays.toString(deleteMarkers) +
                '}';
    }
}

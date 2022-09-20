package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListObjectVersionsV2Output {
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
    private List<ListedCommonPrefix> commonPrefixes;
    @JsonProperty("Versions")
    private List<ListedObjectVersion> versions;
    @JsonProperty("DeleteMarkers")
    private List<ListedDeleteMarkerEntry> deleteMarkers;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListObjectVersionsV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getName() {
        return name;
    }

    public ListObjectVersionsV2Output setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrefix() {
        return prefix;
    }

    public ListObjectVersionsV2Output setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getKeyMarker() {
        return keyMarker;
    }

    public ListObjectVersionsV2Output setKeyMarker(String keyMarker) {
        this.keyMarker = keyMarker;
        return this;
    }

    public String getVersionIDMarker() {
        return versionIDMarker;
    }

    public ListObjectVersionsV2Output setVersionIDMarker(String versionIDMarker) {
        this.versionIDMarker = versionIDMarker;
        return this;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public ListObjectVersionsV2Output setDelimiter(String delimiter) {
        this.delimiter = delimiter;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ListObjectVersionsV2Output setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public long getMaxKeys() {
        return maxKeys;
    }

    public ListObjectVersionsV2Output setMaxKeys(long maxKeys) {
        this.maxKeys = maxKeys;
        return this;
    }

    public String getNextKeyMarker() {
        return nextKeyMarker;
    }

    public ListObjectVersionsV2Output setNextKeyMarker(String nextKeyMarker) {
        this.nextKeyMarker = nextKeyMarker;
        return this;
    }

    public String getNextVersionIDMarker() {
        return nextVersionIDMarker;
    }

    public ListObjectVersionsV2Output setNextVersionIDMarker(String nextVersionIDMarker) {
        this.nextVersionIDMarker = nextVersionIDMarker;
        return this;
    }

    public boolean isTruncated() {
        return isTruncated;
    }

    public ListObjectVersionsV2Output setTruncated(boolean truncated) {
        isTruncated = truncated;
        return this;
    }

    public List<ListedCommonPrefix> getCommonPrefixes() {
        return commonPrefixes;
    }

    public ListObjectVersionsV2Output setCommonPrefixes(List<ListedCommonPrefix> commonPrefixes) {
        this.commonPrefixes = commonPrefixes;
        return this;
    }

    public List<ListedObjectVersion> getVersions() {
        return versions;
    }

    public ListObjectVersionsV2Output setVersions(List<ListedObjectVersion> versions) {
        this.versions = versions;
        return this;
    }

    public List<ListedDeleteMarkerEntry> getDeleteMarkers() {
        return deleteMarkers;
    }

    public ListObjectVersionsV2Output setDeleteMarkers(List<ListedDeleteMarkerEntry> deleteMarkers) {
        this.deleteMarkers = deleteMarkers;
        return this;
    }

    @Override
    public String toString() {
        return "ListObjectVersionsV2Output{" +
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
                ", commonPrefixes=" + commonPrefixes +
                ", versions=" + versions +
                ", deleteMarkers=" + deleteMarkers +
                '}';
    }
}

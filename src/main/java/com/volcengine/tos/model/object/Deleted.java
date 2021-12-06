package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Deleted {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("VersionId")
    private String versionID;
    @JsonProperty("DeleteMarker")
    private boolean deleteMarker;
    @JsonProperty("DeleteMarkerVersionId")
    private String deleteMarkerVersionID;

    public String getKey() {
        return key;
    }

    public Deleted setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public Deleted setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public boolean isDeleteMarker() {
        return deleteMarker;
    }

    public Deleted setDeleteMarker(boolean deleteMarker) {
        this.deleteMarker = deleteMarker;
        return this;
    }

    public String getDeleteMarkerVersionID() {
        return deleteMarkerVersionID;
    }

    public Deleted setDeleteMarkerVersionID(String deleteMarkerVersionID) {
        this.deleteMarkerVersionID = deleteMarkerVersionID;
        return this;
    }

    @Override
    public String toString() {
        return "Deleted{" +
                "key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", deleteMarker=" + deleteMarker +
                ", deleteMarkerVersionID='" + deleteMarkerVersionID + '\'' +
                '}';
    }
}

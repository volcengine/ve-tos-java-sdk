package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.model.acl.Owner;

import java.util.Date;

public class ListedDeleteMarkerEntry {
    @JsonProperty("IsLatest")
    private boolean isLatest;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("LastModified")
    private String lastModified;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("VersionId")
    private String versionID;

    public boolean isLatest() {
        return isLatest;
    }

    public ListedDeleteMarkerEntry setLatest(boolean latest) {
        isLatest = latest;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ListedDeleteMarkerEntry setKey(String key) {
        this.key = key;
        return this;
    }

    public String getLastModified() {
        return lastModified;
    }

    public Date getLastModifiedInDate() {
        return DateConverter.rfc1123StringToDate(lastModified);
    }

    public ListedDeleteMarkerEntry setLastModified(String lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public ListedDeleteMarkerEntry setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public ListedDeleteMarkerEntry setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "ListedDeleteMarkerEntry{" +
                "isLatest=" + isLatest +
                ", key='" + key + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", owner=" + owner +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}

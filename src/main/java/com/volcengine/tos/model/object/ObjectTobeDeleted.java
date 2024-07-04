package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class ObjectTobeDeleted {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("VersionId")
    private String versionID;

    public String getKey() {
        return key;
    }

    public ObjectTobeDeleted setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public ObjectTobeDeleted setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectTobeDeleted{" +
                "key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectTobeDeleted that = (ObjectTobeDeleted) o;
        return Objects.equals(key, that.key) && Objects.equals(versionID, that.versionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, versionID);
    }

    public static ObjectTobeDeletedBuilder builder() {
        return new ObjectTobeDeletedBuilder();
    }

    public static final class ObjectTobeDeletedBuilder {
        private String key;
        private String versionID;

        private ObjectTobeDeletedBuilder() {
        }

        public ObjectTobeDeletedBuilder key(String key) {
            this.key = key;
            return this;
        }

        public ObjectTobeDeletedBuilder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public ObjectTobeDeleted build() {
            ObjectTobeDeleted objectTobeDeleted = new ObjectTobeDeleted();
            objectTobeDeleted.key = key;
            objectTobeDeleted.versionID = versionID;
            return objectTobeDeleted;
        }
    }
}

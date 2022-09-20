package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObjectTobeDeleted {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("VersionId")
    private String versionID;

    public String getKey() {
        return key;
    }

    @Deprecated
    public ObjectTobeDeleted setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    @Deprecated
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

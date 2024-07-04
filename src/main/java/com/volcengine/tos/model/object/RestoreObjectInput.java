package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class RestoreObjectInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonIgnore
    private String key;
    @JsonIgnore
    private String versionID;
    @JsonProperty("Days")
    private int days;
    @JsonProperty("RestoreJobParameters")
    private RestoreJobParameters restoreJobParameters;

    public String getBucket() {
        return bucket;
    }

    public RestoreObjectInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public RestoreObjectInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public RestoreObjectInput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public int getDays() {
        return days;
    }

    public RestoreObjectInput setDays(int days) {
        this.days = days;
        return this;
    }

    public RestoreJobParameters getRestoreJobParameters() {
        return restoreJobParameters;
    }

    public RestoreObjectInput setRestoreJobParameters(RestoreJobParameters restoreJobParameters) {
        this.restoreJobParameters = restoreJobParameters;
        return this;
    }

    @Override
    public String toString() {
        return "RestoreObjectInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", days=" + days +
                ", restoreJobParameters=" + restoreJobParameters +
                '}';
    }

    public static RestoreObjectInputBuilder builder() {
        return new RestoreObjectInputBuilder();
    }

    public static final class RestoreObjectInputBuilder {
        private String bucket;
        private String key;
        private String versionID;
        private int days;
        private RestoreJobParameters restoreJobParameters;

        private RestoreObjectInputBuilder() {
        }

        public RestoreObjectInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public RestoreObjectInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public RestoreObjectInputBuilder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public RestoreObjectInputBuilder days(int days) {
            this.days = days;
            return this;
        }

        public RestoreObjectInputBuilder restoreJobParameters(RestoreJobParameters restoreJobParameters) {
            this.restoreJobParameters = restoreJobParameters;
            return this;
        }

        public RestoreObjectInput build() {
            RestoreObjectInput restoreObjectInput = new RestoreObjectInput();
            restoreObjectInput.setBucket(bucket);
            restoreObjectInput.setKey(key);
            restoreObjectInput.setVersionID(versionID);
            restoreObjectInput.setDays(days);
            restoreObjectInput.setRestoreJobParameters(restoreJobParameters);
            return restoreObjectInput;
        }
    }
}

package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeleteError {
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("Key")
    private String key;
    @JsonProperty("VersionId")
    private String versionID;

    public String getCode() {
        return code;
    }

    public DeleteError setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public DeleteError setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getKey() {
        return key;
    }

    public DeleteError setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public DeleteError setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteError{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}

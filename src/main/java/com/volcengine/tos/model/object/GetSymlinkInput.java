package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class GetSymlinkInput extends GenericInput {
    private String bucket;
    private String key;
    private String versionID;

    public String getBucket() {
        return bucket;
    }

    public GetSymlinkInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public GetSymlinkInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public GetSymlinkInput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "GetSymlinkInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                '}';
    }
}

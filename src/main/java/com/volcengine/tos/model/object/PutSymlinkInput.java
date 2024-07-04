package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.GenericInput;

import java.util.Map;

public class PutSymlinkInput extends GenericInput {
    private String bucket;
    private String key;
    private String symlinkTargetKey;
    private String symlinkTargetBucket;
    private boolean forbidOverwrite;
    private ACLType acl;
    private Map<String, String> meta;
    private StorageClassType storageClass;

    public String getBucket() {
        return bucket;
    }

    public PutSymlinkInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public PutSymlinkInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getSymlinkTargetKey() {
        return symlinkTargetKey;
    }

    public PutSymlinkInput setSymlinkTargetKey(String symlinkTargetKey) {
        this.symlinkTargetKey = symlinkTargetKey;
        return this;
    }

    public String getSymlinkTargetBucket() {
        return symlinkTargetBucket;
    }

    public PutSymlinkInput setSymlinkTargetBucket(String symlinkTargetBucket) {
        this.symlinkTargetBucket = symlinkTargetBucket;
        return this;
    }

    public boolean isForbidOverwrite() {
        return forbidOverwrite;
    }

    public PutSymlinkInput setForbidOverwrite(boolean forbidOverwrite) {
        this.forbidOverwrite = forbidOverwrite;
        return this;
    }

    public ACLType getAcl() {
        return acl;
    }

    public PutSymlinkInput setAcl(ACLType acl) {
        this.acl = acl;
        return this;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public PutSymlinkInput setMeta(Map<String, String> meta) {
        this.meta = meta;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public PutSymlinkInput setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    @Override
    public String toString() {
        return "PutSymlinkInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", symlinkTargetKey='" + symlinkTargetKey + '\'' +
                ", symlinkTargetBucket='" + symlinkTargetBucket + '\'' +
                ", forbidOverwrite=" + forbidOverwrite +
                ", acl=" + acl +
                ", meta=" + meta +
                ", storageClass=" + storageClass +
                '}';
    }
}

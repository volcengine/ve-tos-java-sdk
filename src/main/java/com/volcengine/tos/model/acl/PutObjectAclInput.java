package com.volcengine.tos.model.acl;

import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated
public class PutObjectAclInput {
    @JsonProperty("Key")
    private String key;
    @JsonProperty("VersionId")
    private String versionId;
    @JsonProperty("AclGrant")
    private ObjectAclGrant aclGrant;
    @JsonProperty("AclRules")
    private ObjectAclRules aclRules;

    public String getKey() {
        return key;
    }

    public PutObjectAclInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionId() {
        return versionId;
    }

    public PutObjectAclInput setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public ObjectAclGrant getAclGrant() {
        return aclGrant;
    }

    public PutObjectAclInput setAclGrant(ObjectAclGrant aclGrant) {
        this.aclGrant = aclGrant;
        return this;
    }

    public ObjectAclRules getAclRules() {
        return aclRules;
    }

    public PutObjectAclInput setAclRules(ObjectAclRules aclRules) {
        this.aclRules = aclRules;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectAclInput{" +
                "key='" + key + '\'' +
                ", versionId='" + versionId + '\'' +
                ", aclGrant=" + aclGrant +
                ", aclRules=" + aclRules +
                '}';
    }
}

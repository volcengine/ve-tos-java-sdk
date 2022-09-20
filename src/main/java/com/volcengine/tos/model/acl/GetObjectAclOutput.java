package com.volcengine.tos.model.acl;

import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * deprecated, use GetObjectAclV2Output instead
 */
@Deprecated
public class GetObjectAclOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("VersionId")
    private String versionId;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("Grants")
    private Grant[] grants;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectAclOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionId() {
        return versionId;
    }

    public GetObjectAclOutput setVersionId(String versionId) {
        this.versionId = versionId;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public GetObjectAclOutput setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public Grant[] getGrants() {
        return grants;
    }

    public GetObjectAclOutput setGrants(Grant[] grants) {
        this.grants = grants;
        return this;
    }
}

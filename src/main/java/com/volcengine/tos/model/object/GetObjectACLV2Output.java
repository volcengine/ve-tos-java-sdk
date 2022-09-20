package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.Owner;

import java.util.List;

public class GetObjectACLV2Output {
    private RequestInfo requestInfo;
    private String versionID;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("Grants")
    private List<GrantV2> grants;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetObjectACLV2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public GetObjectACLV2Output setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public GetObjectACLV2Output setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public List<GrantV2> getGrants() {
        return grants;
    }

    public GetObjectACLV2Output setGrants(List<GrantV2> grants) {
        this.grants = grants;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectACLV2Output{" +
                "requestInfo=" + requestInfo +
                ", versionID='" + versionID + '\'' +
                ", owner=" + owner +
                ", grants=" + grants +
                '}';
    }
}

package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.Owner;

import java.util.List;

public class GetBucketACLOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("Grants")
    private List<GrantV2> grants;
    @JsonProperty("BucketAclDelivered")
    private boolean bucketAclDelivered;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketACLOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public GetBucketACLOutput setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public List<GrantV2> getGrants() {
        return grants;
    }

    public GetBucketACLOutput setGrants(List<GrantV2> grants) {
        this.grants = grants;
        return this;
    }

    public boolean isBucketAclDelivered() {
        return bucketAclDelivered;
    }

    public GetBucketACLOutput setBucketAclDelivered(boolean bucketAclDelivered) {
        this.bucketAclDelivered = bucketAclDelivered;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketACLOutput{" +
                "requestInfo=" + requestInfo +
                ", owner=" + owner +
                ", grants=" + grants +
                ", bucketAclDelivered=" + bucketAclDelivered +
                '}';
    }
}

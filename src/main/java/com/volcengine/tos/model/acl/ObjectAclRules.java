package com.volcengine.tos.model.acl;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@Deprecated
public class ObjectAclRules {
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("Grants")
    private Grant[] grants;

    public Owner getOwner() {
        return owner;
    }

    public ObjectAclRules setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public Grant[] getGrants() {
        return grants;
    }

    public ObjectAclRules setGrants(Grant[] grants) {
        this.grants = grants;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectAclRules{" +
                "owner=" + owner +
                ", grants=" + Arrays.toString(grants) +
                '}';
    }
}

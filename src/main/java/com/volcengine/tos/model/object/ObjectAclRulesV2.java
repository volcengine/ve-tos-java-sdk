package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.Owner;

import java.util.Arrays;
import java.util.List;

public class ObjectAclRulesV2 {
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("Grants")
    private List<GrantV2> grants;
    @JsonProperty("BucketOwnerEntrusted")
    private boolean bucketOwnerEntrusted;

    public Owner getOwner() {
        return owner;
    }

    public ObjectAclRulesV2 setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public List<GrantV2> getGrants() {
        return grants;
    }

    public ObjectAclRulesV2 setGrants(List<GrantV2> grants) {
        this.grants = grants;
        return this;
    }

    public boolean isBucketOwnerEntrusted() {
        return bucketOwnerEntrusted;
    }

    public ObjectAclRulesV2 setBucketOwnerEntrusted(boolean bucketOwnerEntrusted) {
        this.bucketOwnerEntrusted = bucketOwnerEntrusted;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectAclRulesV2{" +
                "owner=" + owner +
                ", grants=" + grants +
                ", bucketOwnerEntrusted=" + bucketOwnerEntrusted +
                '}';
    }

    public static ObjectAclRulesV2Builder builder() {
        return new ObjectAclRulesV2Builder();
    }

    public static final class ObjectAclRulesV2Builder {
        private Owner owner;
        private List<GrantV2> grants;
        private boolean bucketOwnerEntrusted;

        private ObjectAclRulesV2Builder() {
        }

        public ObjectAclRulesV2Builder owner(Owner owner) {
            this.owner = owner;
            return this;
        }

        public ObjectAclRulesV2Builder grants(List<GrantV2> grants) {
            this.grants = grants;
            return this;
        }

        public ObjectAclRulesV2Builder bucketOwnerEntrusted(boolean bucketOwnerEntrusted) {
            this.bucketOwnerEntrusted = bucketOwnerEntrusted;
            return this;
        }

        public ObjectAclRulesV2 build() {
            ObjectAclRulesV2 objectAclRulesV2 = new ObjectAclRulesV2();
            objectAclRulesV2.owner = this.owner;
            objectAclRulesV2.grants = this.grants;
            objectAclRulesV2.bucketOwnerEntrusted = this.bucketOwnerEntrusted;
            return objectAclRulesV2;
        }
    }
}

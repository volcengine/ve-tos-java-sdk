package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.model.GenericInput;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.Owner;

import java.util.List;

public class PutBucketACLInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonIgnore
    private ACLType acl;
    @JsonIgnore
    private String grantFullControl;
    @JsonIgnore
    private String grantRead;
    @JsonIgnore
    private String grantReadAcp;
    @JsonIgnore
    private String grantWrite;
    @JsonIgnore
    private String grantWriteAcp;
    @JsonProperty("Owner")
    private Owner owner;
    @JsonProperty("Grants")
    private List<GrantV2> grants;

    public String getBucket() {
        return bucket;
    }

    public PutBucketACLInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public ACLType getAcl() {
        return acl;
    }

    public PutBucketACLInput setAcl(ACLType acl) {
        this.acl = acl;
        return this;
    }

    public String getGrantFullControl() {
        return grantFullControl;
    }

    public PutBucketACLInput setGrantFullControl(String grantFullControl) {
        this.grantFullControl = grantFullControl;
        return this;
    }

    public String getGrantRead() {
        return grantRead;
    }

    public PutBucketACLInput setGrantRead(String grantRead) {
        this.grantRead = grantRead;
        return this;
    }

    public String getGrantReadAcp() {
        return grantReadAcp;
    }

    public PutBucketACLInput setGrantReadAcp(String grantReadAcp) {
        this.grantReadAcp = grantReadAcp;
        return this;
    }

    public String getGrantWrite() {
        return grantWrite;
    }

    public PutBucketACLInput setGrantWrite(String grantWrite) {
        this.grantWrite = grantWrite;
        return this;
    }

    public String getGrantWriteAcp() {
        return grantWriteAcp;
    }

    public PutBucketACLInput setGrantWriteAcp(String grantWriteAcp) {
        this.grantWriteAcp = grantWriteAcp;
        return this;
    }

    public Owner getOwner() {
        return owner;
    }

    public PutBucketACLInput setOwner(Owner owner) {
        this.owner = owner;
        return this;
    }

    public List<GrantV2> getGrants() {
        return grants;
    }

    public PutBucketACLInput setGrants(List<GrantV2> grants) {
        this.grants = grants;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketACLInput{" +
                "bucket='" + bucket + '\'' +
                ", acl=" + acl +
                ", grantFullControl='" + grantFullControl + '\'' +
                ", grantRead='" + grantRead + '\'' +
                ", grantReadAcp='" + grantReadAcp + '\'' +
                ", grantWrite='" + grantWrite + '\'' +
                ", grantWriteAcp='" + grantWriteAcp + '\'' +
                ", owner=" + owner +
                ", grants=" + grants +
                '}';
    }

    public static PutBucketACLInputBuilder builder() {
        return new PutBucketACLInputBuilder();
    }

    public static final class PutBucketACLInputBuilder {
        private String bucket;
        private ACLType acl;
        private String grantFullControl;
        private String grantRead;
        private String grantReadAcp;
        private String grantWrite;
        private String grantWriteAcp;
        private Owner owner;
        private List<GrantV2> grants;

        private PutBucketACLInputBuilder() {
        }

        public PutBucketACLInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketACLInputBuilder acl(ACLType acl) {
            this.acl = acl;
            return this;
        }

        public PutBucketACLInputBuilder grantFullControl(String grantFullControl) {
            this.grantFullControl = grantFullControl;
            return this;
        }

        public PutBucketACLInputBuilder grantRead(String grantRead) {
            this.grantRead = grantRead;
            return this;
        }

        public PutBucketACLInputBuilder grantReadAcp(String grantReadAcp) {
            this.grantReadAcp = grantReadAcp;
            return this;
        }

        public PutBucketACLInputBuilder grantWrite(String grantWrite) {
            this.grantWrite = grantWrite;
            return this;
        }

        public PutBucketACLInputBuilder grantWriteAcp(String grantWriteAcp) {
            this.grantWriteAcp = grantWriteAcp;
            return this;
        }

        public PutBucketACLInputBuilder owner(Owner owner) {
            this.owner = owner;
            return this;
        }

        public PutBucketACLInputBuilder grants(List<GrantV2> grants) {
            this.grants = grants;
            return this;
        }

        public PutBucketACLInput build() {
            PutBucketACLInput putBucketACLInput = new PutBucketACLInput();
            putBucketACLInput.setBucket(bucket);
            putBucketACLInput.setAcl(acl);
            putBucketACLInput.setGrantFullControl(grantFullControl);
            putBucketACLInput.setGrantRead(grantRead);
            putBucketACLInput.setGrantReadAcp(grantReadAcp);
            putBucketACLInput.setGrantWrite(grantWrite);
            putBucketACLInput.setGrantWriteAcp(grantWriteAcp);
            putBucketACLInput.setOwner(owner);
            putBucketACLInput.setGrants(grants);
            return putBucketACLInput;
        }
    }
}

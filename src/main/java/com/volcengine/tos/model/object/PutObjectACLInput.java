package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.Owner;

import java.util.List;

public class PutObjectACLInput {
    private String bucket;
    private String key;
    private String versionID;

    private ACLType acl;
    private String grantFullControl;
    private String grantRead;
    private String grantReadAcp;
    private String grantWriteAcp;

    private ObjectAclRulesV2 objectAclRules;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getVersionID() {
        return versionID;
    }

    public ACLType getAcl() {
        return acl;
    }

    public String getGrantFullControl() {
        return grantFullControl;
    }

    public String getGrantRead() {
        return grantRead;
    }

    public String getGrantReadAcp() {
        return grantReadAcp;
    }

    public String getGrantWriteAcp() {
        return grantWriteAcp;
    }

    public Owner getOwner() {
        return this.objectAclRules != null ? this.objectAclRules.getOwner() : null;
    }

    public List<GrantV2> getGrants() {
        return this.objectAclRules != null ? this.objectAclRules.getGrants() : null;
    }

    public ObjectAclRulesV2 getObjectAclRules() {
        return objectAclRules;
    }

    public PutObjectACLInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public PutObjectACLInput setKey(String key) {
        this.key = key;
        return this;
    }

    public PutObjectACLInput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public PutObjectACLInput setAcl(ACLType acl) {
        this.acl = acl;
        return this;
    }

    public PutObjectACLInput setGrantFullControl(String grantFullControl) {
        this.grantFullControl = grantFullControl;
        return this;
    }

    public PutObjectACLInput setGrantRead(String grantRead) {
        this.grantRead = grantRead;
        return this;
    }

    public PutObjectACLInput setGrantReadAcp(String grantReadAcp) {
        this.grantReadAcp = grantReadAcp;
        return this;
    }

    public PutObjectACLInput setGrantWriteAcp(String grantWriteAcp) {
        this.grantWriteAcp = grantWriteAcp;
        return this;
    }

    public PutObjectACLInput setObjectAclRules(ObjectAclRulesV2 objectAclRules) {
        this.objectAclRules = objectAclRules;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectACLInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", acl=" + acl +
                ", grantFullControl='" + grantFullControl + '\'' +
                ", grantRead='" + grantRead + '\'' +
                ", grantReadAcp='" + grantReadAcp + '\'' +
                ", grantWriteAcp='" + grantWriteAcp + '\'' +
                ", objectAclRules=" + objectAclRules +
                '}';
    }

    public static PutObjectACLInputBuilder builder() {
        return new PutObjectACLInputBuilder();
    }

    public static final class PutObjectACLInputBuilder {
        private String bucket;
        private String key;
        private String versionID;
        private ACLType acl;
        private String grantFullControl;
        private String grantRead;
        private String grantReadAcp;
        private String grantWriteAcp;
        private Owner owner;
        private List<GrantV2> grants;

        private PutObjectACLInputBuilder() {
        }

        public PutObjectACLInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutObjectACLInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public PutObjectACLInputBuilder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public PutObjectACLInputBuilder acl(ACLType acl) {
            this.acl = acl;
            return this;
        }

        public PutObjectACLInputBuilder grantFullControl(String grantFullControl) {
            this.grantFullControl = grantFullControl;
            return this;
        }

        public PutObjectACLInputBuilder grantRead(String grantRead) {
            this.grantRead = grantRead;
            return this;
        }

        public PutObjectACLInputBuilder grantReadAcp(String grantReadAcp) {
            this.grantReadAcp = grantReadAcp;
            return this;
        }

        public PutObjectACLInputBuilder grantWriteAcp(String grantWriteAcp) {
            this.grantWriteAcp = grantWriteAcp;
            return this;
        }

        public PutObjectACLInputBuilder owner(Owner owner) {
            this.owner = owner;
            return this;
        }

        public PutObjectACLInputBuilder grants(List<GrantV2> grants) {
            this.grants = grants;
            return this;
        }

        public PutObjectACLInput build() {
            PutObjectACLInput putObjectACLInput = new PutObjectACLInput();
            putObjectACLInput.key = this.key;
            putObjectACLInput.grantReadAcp = this.grantReadAcp;
            putObjectACLInput.grantWriteAcp = this.grantWriteAcp;
            putObjectACLInput.bucket = this.bucket;
            putObjectACLInput.acl = this.acl;
            putObjectACLInput.grantRead = this.grantRead;
            putObjectACLInput.grantFullControl = this.grantFullControl;
            putObjectACLInput.versionID = this.versionID;
            putObjectACLInput.objectAclRules = new ObjectAclRulesV2()
                    .setOwner(this.owner).setGrants(this.grants);
            return putObjectACLInput;
        }
    }
}

package com.volcengine.tos.model.acl;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObjectAclGrant {
    @JsonProperty("ACL")
    private String acl;
    @JsonProperty("GrantFullControl")
    private String grantFullControl;
    @JsonProperty("GrantRead")
    private String grantRead;
    @JsonProperty("GrantReadAcp")
    private String grantReadAcp;
    @JsonProperty("GrantWrite")
    private String grantWrite;
    @JsonProperty("GrantWriteAcp")
    private String grantWriteAcp;

    public String getAcl() {
        return acl;
    }

    public ObjectAclGrant setAcl(String acl) {
        this.acl = acl;
        return this;
    }

    public ObjectAclGrant setAclPrivate() {
        this.acl = ACLConst.ACL_PRIVATE;
        return this;
    }

    public ObjectAclGrant setAclPublicRead() {
        this.acl = ACLConst.ACL_PUBLIC_READ;
        return this;
    }

    public ObjectAclGrant setAclPublicReadWrite() {
        this.acl = ACLConst.ACL_PUBLIC_READ_WRITE;
        return this;
    }

    public ObjectAclGrant setAclAuthRead() {
        this.acl = ACLConst.ACL_AUTH_READ;
        return this;
    }

    public ObjectAclGrant setAclBucketOwnerRead() {
        this.acl = ACLConst.ACL_BUCKET_OWNER_READ;
        return this;
    }

    public ObjectAclGrant setAclBucketOwnerFullControl() {
        this.acl = ACLConst.ACL_BUCKET_OWNER_FULL_CONTROL;
        return this;
    }

    public ObjectAclGrant setAclLogDeliveryWrite() {
        this.acl = ACLConst.ACL_LOG_DELIVERY_WRITE;
        return this;
    }


    public String getGrantFullControl() {
        return grantFullControl;
    }

    @Deprecated
    public ObjectAclGrant setGrantFullControl(String grantFullControl) {
        this.grantFullControl = grantFullControl;
        return this;
    }

    public ObjectAclGrant setGrantFullControl() {
        this.grantFullControl = ACLConst.PERMISSION_TYPE_FULL_CONTROL;
        return this;
    }

    public String getGrantRead() {
        return grantRead;
    }

    @Deprecated
    public ObjectAclGrant setGrantRead(String grantRead) {
        this.grantRead = grantRead;
        return this;
    }

    public ObjectAclGrant setGrantRead() {
        this.grantRead = ACLConst.PERMISSION_TYPE_READ;
        return this;
    }

    public String getGrantReadAcp() {
        return grantReadAcp;
    }

    @Deprecated
    public ObjectAclGrant setGrantReadAcp(String grantReadAcp) {
        this.grantReadAcp = grantReadAcp;
        return this;
    }

    public ObjectAclGrant setGrantReadAcp() {
        this.grantReadAcp = ACLConst.PERMISSION_TYPE_READ_ACP;
        return this;
    }

    public String getGrantWrite() {
        return grantWrite;
    }

    @Deprecated
    public ObjectAclGrant setGrantWrite(String grantWrite) {
        this.grantWrite = grantWrite;
        return this;
    }

    public ObjectAclGrant setGrantWrite() {
        this.grantWrite = ACLConst.PERMISSION_TYPE_WRITE;
        return this;
    }

    public String getGrantWriteAcp() {
        return grantWriteAcp;
    }

    @Deprecated
    public ObjectAclGrant setGrantWriteAcp(String grantWriteAcp) {
        this.grantWriteAcp = grantWriteAcp;
        return this;
    }

    public ObjectAclGrant setGrantWriteAcp() {
        this.grantWriteAcp = ACLConst.PERMISSION_TYPE_WRITE_ACP;
        return this;
    }
}

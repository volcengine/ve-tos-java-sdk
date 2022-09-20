package com.volcengine.tos.model.acl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.PermissionType;

@Deprecated
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
        this.acl = ACLType.ACL_PRIVATE.toString();
        return this;
    }

    public ObjectAclGrant setAclPublicRead() {
        this.acl = ACLType.ACL_PUBLIC_READ.toString();
        return this;
    }

    public ObjectAclGrant setAclPublicReadWrite() {
        this.acl = ACLType.ACL_PUBLIC_READ_WRITE.toString();
        return this;
    }

    public ObjectAclGrant setAclAuthRead() {
        this.acl = ACLType.ACL_AUTHENTICATED_READ.toString();
        return this;
    }

    public ObjectAclGrant setAclBucketOwnerRead() {
        this.acl = ACLType.ACL_BUCKET_OWNER_READ.toString();
        return this;
    }

    public ObjectAclGrant setAclBucketOwnerFullControl() {
        this.acl = ACLType.ACL_BUCKET_OWNER_FULL_CONTROL.toString();
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
        this.grantFullControl = PermissionType.PERMISSION_FULL_CONTROL.toString();
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
        this.grantRead = PermissionType.PERMISSION_READ.toString();
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
        this.grantReadAcp = PermissionType.PERMISSION_READ_ACP.toString();
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
        this.grantWrite = PermissionType.PERMISSION_WRITE.toString();
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
        this.grantWriteAcp = PermissionType.PERMISSION_WRITE_ACP.toString();
        return this;
    }
}

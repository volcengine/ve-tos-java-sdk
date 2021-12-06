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

    public String getGrantFullControl() {
        return grantFullControl;
    }

    public ObjectAclGrant setGrantFullControl(String grantFullControl) {
        this.grantFullControl = grantFullControl;
        return this;
    }

    public String getGrantRead() {
        return grantRead;
    }

    public ObjectAclGrant setGrantRead(String grantRead) {
        this.grantRead = grantRead;
        return this;
    }

    public String getGrantReadAcp() {
        return grantReadAcp;
    }

    public ObjectAclGrant setGrantReadAcp(String grantReadAcp) {
        this.grantReadAcp = grantReadAcp;
        return this;
    }

    public String getGrantWrite() {
        return grantWrite;
    }

    public ObjectAclGrant setGrantWrite(String grantWrite) {
        this.grantWrite = grantWrite;
        return this;
    }

    public String getGrantWriteAcp() {
        return grantWriteAcp;
    }

    public ObjectAclGrant setGrantWriteAcp(String grantWriteAcp) {
        this.grantWriteAcp = grantWriteAcp;
        return this;
    }
}

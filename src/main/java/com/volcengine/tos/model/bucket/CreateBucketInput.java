package com.volcengine.tos.model.bucket;

@Deprecated
public class CreateBucketInput {
    private String bucket;
    private String acl;
    private String grantFullControl;
    private String grantRead;
    private String grantReadAcp;
    private String grantWrite;
    private String grantWriteAcp;
    private String storageClass;

    public CreateBucketInput(String bucket) {
        this.bucket = bucket;
    }

    public CreateBucketInput(String bucket, String acl, String grantFullControl,
                             String grantRead, String grantReadAcp,
                             String grantWrite, String grantWriteAcp, String storageClass) {
        this.bucket = bucket;
        this.acl = acl;
        this.grantFullControl = grantFullControl;
        this.grantRead = grantRead;
        this.grantReadAcp = grantReadAcp;
        this.grantWrite = grantWrite;
        this.grantWriteAcp = grantWriteAcp;
        this.storageClass = storageClass;
    }

    public String getBucket() {
        return bucket;
    }

    public CreateBucketInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getAcl() {
        return acl;
    }

    public CreateBucketInput setAcl(String acl) {
        this.acl = acl;
        return this;
    }

    public String getGrantFullControl() {
        return grantFullControl;
    }

    public CreateBucketInput setGrantFullControl(String grantFullControl) {
        this.grantFullControl = grantFullControl;
        return this;
    }

    public String getGrantRead() {
        return grantRead;
    }

    public CreateBucketInput setGrantRead(String grantRead) {
        this.grantRead = grantRead;
        return this;
    }

    public String getGrantReadAcp() {
        return grantReadAcp;
    }

    public CreateBucketInput setGrantReadAcp(String grantReadAcp) {
        this.grantReadAcp = grantReadAcp;
        return this;
    }

    public String getGrantWrite() {
        return grantWrite;
    }

    public CreateBucketInput setGrantWrite(String grantWrite) {
        this.grantWrite = grantWrite;
        return this;
    }

    public String getGrantWriteAcp() {
        return grantWriteAcp;
    }

    public CreateBucketInput setGrantWriteAcp(String grantWriteAcp) {
        this.grantWriteAcp = grantWriteAcp;
        return this;
    }

    public String getStorageClass() {
        return storageClass;
    }

    public CreateBucketInput setStorageClass(String storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    @Override
    public String toString() {
        return "CreateBucketInput{" +
                "bucket='" + bucket + '\'' +
                ", acl='" + acl + '\'' +
                ", grantFullControl='" + grantFullControl + '\'' +
                ", grantRead='" + grantRead + '\'' +
                ", grantReadAcp='" + grantReadAcp + '\'' +
                ", grantWrite='" + grantWrite + '\'' +
                ", grantWriteAcp='" + grantWriteAcp + '\'' +
                ", storageClass='" + storageClass + '\'' +
                '}';
    }
}

package com.volcengine.tos.model.bucket;

import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.AzRedundancyType;
import com.volcengine.tos.comm.common.BucketType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.GenericInput;

public class CreateBucketV2Input extends GenericInput {
    private String bucket;
    private ACLType acl;
    private String grantFullControl;
    private String grantRead;
    private String grantReadAcp;
    private String grantWrite;
    private String grantWriteAcp;
    private StorageClassType storageClass;
    private AzRedundancyType azRedundancy;
    private String projectName;
    private BucketType bucketType;

    public CreateBucketV2Input() {
    }

    public CreateBucketV2Input(String bucket) {
        this.bucket = bucket;
    }

    public String getBucket() {
        return bucket;
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

    public String getGrantWrite() {
        return grantWrite;
    }

    public String getGrantWriteAcp() {
        return grantWriteAcp;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public AzRedundancyType getAzRedundancy() {
        return azRedundancy;
    }

    public CreateBucketV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public CreateBucketV2Input setAcl(ACLType acl) {
        this.acl = acl;
        return this;
    }

    public CreateBucketV2Input setGrantFullControl(String grantFullControl) {
        this.grantFullControl = grantFullControl;
        return this;
    }

    public CreateBucketV2Input setGrantRead(String grantRead) {
        this.grantRead = grantRead;
        return this;
    }

    public CreateBucketV2Input setGrantReadAcp(String grantReadAcp) {
        this.grantReadAcp = grantReadAcp;
        return this;
    }

    public CreateBucketV2Input setGrantWrite(String grantWrite) {
        this.grantWrite = grantWrite;
        return this;
    }

    public CreateBucketV2Input setGrantWriteAcp(String grantWriteAcp) {
        this.grantWriteAcp = grantWriteAcp;
        return this;
    }

    public CreateBucketV2Input setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public CreateBucketV2Input setAzRedundancy(AzRedundancyType azRedundancy) {
        this.azRedundancy = azRedundancy;
        return this;
    }

    public String getProjectName() {
        return projectName;
    }

    public CreateBucketV2Input setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public BucketType getBucketType() {
        return bucketType;
    }

    public CreateBucketV2Input setBucketType(BucketType bucketType) {
        this.bucketType = bucketType;
        return this;
    }

    @Override
    public String toString() {
        return "CreateBucketV2Input{" +
                "bucket='" + bucket + '\'' +
                ", acl=" + acl +
                ", grantFullControl='" + grantFullControl + '\'' +
                ", grantRead='" + grantRead + '\'' +
                ", grantReadAcp='" + grantReadAcp + '\'' +
                ", grantWrite='" + grantWrite + '\'' +
                ", grantWriteAcp='" + grantWriteAcp + '\'' +
                ", storageClass=" + storageClass +
                ", azRedundancy=" + azRedundancy +
                ", projectName='" + projectName + '\'' +
                ", bucketType=" + bucketType +
                '}';
    }

    public static CreateBucketInputV2Builder builder() {
        return new CreateBucketInputV2Builder();
    }

    public static final class CreateBucketInputV2Builder {
        private String bucket;
        private ACLType acl;
        private String grantFullControl;
        private String grantRead;
        private String grantReadAcp;
        private String grantWrite;
        private String grantWriteAcp;
        private StorageClassType storageClass;
        private AzRedundancyType azRedundancy;
        private String projectName;
        private BucketType bucketType;

        private CreateBucketInputV2Builder() {
        }

        public CreateBucketInputV2Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public CreateBucketInputV2Builder acl(ACLType acl) {
            this.acl = acl;
            return this;
        }

        public CreateBucketInputV2Builder grantFullControl(String grantFullControl) {
            this.grantFullControl = grantFullControl;
            return this;
        }

        public CreateBucketInputV2Builder grantRead(String grantRead) {
            this.grantRead = grantRead;
            return this;
        }

        public CreateBucketInputV2Builder grantReadAcp(String grantReadAcp) {
            this.grantReadAcp = grantReadAcp;
            return this;
        }

        public CreateBucketInputV2Builder grantWrite(String grantWrite) {
            this.grantWrite = grantWrite;
            return this;
        }

        public CreateBucketInputV2Builder grantWriteAcp(String grantWriteAcp) {
            this.grantWriteAcp = grantWriteAcp;
            return this;
        }

        public CreateBucketInputV2Builder storageClass(StorageClassType storageClass) {
            this.storageClass = storageClass;
            return this;
        }

        public CreateBucketInputV2Builder azRedundancy(AzRedundancyType azRedundancy) {
            this.azRedundancy = azRedundancy;
            return this;
        }

        public CreateBucketInputV2Builder projectName(String projectName) {
            this.projectName = projectName;
            return this;
        }

        public CreateBucketInputV2Builder bucketType(BucketType bucketType) {
            this.bucketType = bucketType;
            return this;
        }
        public CreateBucketV2Input build() {
            CreateBucketV2Input createBucketInputV2 = new CreateBucketV2Input();
            createBucketInputV2.bucket = this.bucket;
            createBucketInputV2.acl = this.acl;
            createBucketInputV2.grantWriteAcp = this.grantWriteAcp;
            createBucketInputV2.grantReadAcp = this.grantReadAcp;
            createBucketInputV2.grantRead = this.grantRead;
            createBucketInputV2.grantFullControl = this.grantFullControl;
            createBucketInputV2.grantWrite = this.grantWrite;
            createBucketInputV2.storageClass = this.storageClass;
            createBucketInputV2.azRedundancy = this.azRedundancy;
            createBucketInputV2.projectName = this.projectName;
            createBucketInputV2.bucketType = this.bucketType;
            return createBucketInputV2;
        }
    }
}

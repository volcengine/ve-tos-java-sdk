package com.volcengine.tos.model.object;

public class GetObjectACLV2Input {
    private String bucket;
    private String key;
    private String versionID;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getVersionID() {
        return versionID;
    }

    public GetObjectACLV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public GetObjectACLV2Input setKey(String key) {
        this.key = key;
        return this;
    }

    public GetObjectACLV2Input setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectACLInputV2{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                '}';
    }

    public static GetObjectACLInputV2Builder builder() {
        return new GetObjectACLInputV2Builder();
    }

    public static final class GetObjectACLInputV2Builder {
        private String bucket;
        private String key;
        private String versionID;

        private GetObjectACLInputV2Builder() {
        }

        public GetObjectACLInputV2Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectACLInputV2Builder key(String key) {
            this.key = key;
            return this;
        }

        public GetObjectACLInputV2Builder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public GetObjectACLV2Input build() {
            GetObjectACLV2Input getObjectACLInputV2 = new GetObjectACLV2Input();
            getObjectACLInputV2.versionID = this.versionID;
            getObjectACLInputV2.key = this.key;
            getObjectACLInputV2.bucket = this.bucket;
            return getObjectACLInputV2;
        }
    }
}

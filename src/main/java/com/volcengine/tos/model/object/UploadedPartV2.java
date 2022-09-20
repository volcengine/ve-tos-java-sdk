package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UploadedPartV2 {
    @JsonProperty("PartNumber")
    private int partNumber;
    @JsonProperty("ETag")
    private String etag;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias("LastModified")
    private Date lastModified;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias("Size")
    private long size;

    public int getPartNumber() {
        return partNumber;
    }

    public UploadedPartV2 setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public UploadedPartV2 setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public UploadedPartV2 setLastModified(Date lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public long getSize() {
        return size;
    }

    public UploadedPartV2 setSize(long size) {
        this.size = size;
        return this;
    }

    @Override
    public String toString() {
        return "UploadedPartV2{" +
                "partNumber=" + partNumber +
                ", etag='" + etag + '\'' +
                ", lastModified=" + lastModified +
                ", size=" + size +
                '}';
    }

    public static UploadedPartV2Builder builder() {
        return new UploadedPartV2Builder();
    }

    public static final class UploadedPartV2Builder {
        private int partNumber;
        private String etag;
        private Date lastModified;
        private long size;

        private UploadedPartV2Builder() {
        }

        public UploadedPartV2Builder partNumber(int partNumber) {
            this.partNumber = partNumber;
            return this;
        }

        public UploadedPartV2Builder etag(String etag) {
            this.etag = etag;
            return this;
        }

        public UploadedPartV2Builder lastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public UploadedPartV2Builder size(long size) {
            this.size = size;
            return this;
        }

        public UploadedPartV2 build() {
            UploadedPartV2 uploadedPartV2 = new UploadedPartV2();
            uploadedPartV2.setPartNumber(partNumber);
            uploadedPartV2.setEtag(etag);
            uploadedPartV2.setLastModified(lastModified);
            uploadedPartV2.setSize(size);
            return uploadedPartV2;
        }
    }
}

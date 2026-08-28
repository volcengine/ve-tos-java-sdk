package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class PutBucketTrashInput extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Trash")
    private BucketTrash trash;

    public String getBucket() {
        return bucket;
    }

    public PutBucketTrashInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public BucketTrash getTrash() {
        return trash;
    }

    public PutBucketTrashInput setTrash(BucketTrash trash) {
        this.trash = trash;
        return this;
    }

    @Override
    public String toString() {
        return "PutBucketTrashInput{" +
                "bucket='" + bucket + '\'' +
                ", trash=" + trash +
                '}';
    }

    public static PutBucketTrashInputBuilder builder() {
        return new PutBucketTrashInputBuilder();
    }

    public static final class PutBucketTrashInputBuilder {
        private String bucket;
        private BucketTrash trash;

        private PutBucketTrashInputBuilder() {
        }

        public PutBucketTrashInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PutBucketTrashInputBuilder trash(BucketTrash trash) {
            this.trash = trash;
            return this;
        }

        public PutBucketTrashInput build() {
            return new PutBucketTrashInput().setBucket(bucket).setTrash(trash);
        }
    }
}

package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;
import java.util.List;

public class DeleteMultiObjectsV2Input {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Objects")
    private List<ObjectTobeDeleted> objects;
    @JsonProperty("Quiet")
    private boolean quiet;

    public DeleteMultiObjectsV2Input() {
    }

    public boolean isQuiet() {
        return quiet;
    }

    public String getBucket() {
        return bucket;
    }

    public List<ObjectTobeDeleted> getObjects() {
        return objects;
    }

    public DeleteMultiObjectsV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public DeleteMultiObjectsV2Input setObjects(List<ObjectTobeDeleted> objects) {
        this.objects = objects;
        return this;
    }

    public DeleteMultiObjectsV2Input setQuiet(boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    public static DeleteMultiObjectsInputBuilder builder() {
        return new DeleteMultiObjectsInputBuilder();
    }

    @Override
    public String toString() {
        return "DeleteMultiObjectsInput{" +
                "bucket='" + bucket + '\'' +
                ", objects=" + Arrays.toString(objects.toArray()) +
                ", quiet=" + quiet +
                '}';
    }

    public static final class DeleteMultiObjectsInputBuilder {
        private String bucket;
        private List<ObjectTobeDeleted> objects;
        private boolean quiet;

        private DeleteMultiObjectsInputBuilder() {
        }

        public DeleteMultiObjectsInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public DeleteMultiObjectsInputBuilder objects(List<ObjectTobeDeleted> objects) {
            this.objects = objects;
            return this;
        }

        public DeleteMultiObjectsInputBuilder quiet(boolean quiet) {
            this.quiet = quiet;
            return this;
        }

        public DeleteMultiObjectsV2Input build() {
            DeleteMultiObjectsV2Input deleteMultiObjectsInput = new DeleteMultiObjectsV2Input();
            deleteMultiObjectsInput.quiet = this.quiet;
            deleteMultiObjectsInput.objects = this.objects;
            deleteMultiObjectsInput.bucket = this.bucket;
            return deleteMultiObjectsInput;
        }
    }
}

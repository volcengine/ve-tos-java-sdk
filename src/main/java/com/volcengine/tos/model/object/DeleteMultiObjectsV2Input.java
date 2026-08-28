package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

import java.util.Arrays;
import java.util.List;

public class DeleteMultiObjectsV2Input extends GenericInput {
    @JsonIgnore
    private String bucket;
    @JsonProperty("Objects")
    private List<ObjectTobeDeleted> objects;
    @JsonProperty("Quiet")
    private boolean quiet;
    @JsonIgnore
    private boolean recursive;
    @JsonIgnore
    private boolean skipTrash;

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

    public boolean isRecursive() {
        return recursive;
    }

    public boolean isSkipTrash() {
        return skipTrash;
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

    public DeleteMultiObjectsV2Input setRecursive(boolean recursive) {
        this.recursive = recursive;
        return this;
    }

    public DeleteMultiObjectsV2Input setSkipTrash(boolean skipTrash) {
        this.skipTrash = skipTrash;
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
                ", recursive=" + recursive +
                ", skipTrash=" + skipTrash +
                '}';
    }

    public static final class DeleteMultiObjectsInputBuilder {
        private String bucket;
        private List<ObjectTobeDeleted> objects;
        private boolean quiet;
        private boolean recursive;
        private boolean skipTrash;

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

        public DeleteMultiObjectsInputBuilder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        public DeleteMultiObjectsInputBuilder skipTrash(boolean skipTrash) {
            this.skipTrash = skipTrash;
            return this;
        }

        public DeleteMultiObjectsV2Input build() {
            DeleteMultiObjectsV2Input deleteMultiObjectsInput = new DeleteMultiObjectsV2Input();
            deleteMultiObjectsInput.quiet = this.quiet;
            deleteMultiObjectsInput.objects = this.objects;
            deleteMultiObjectsInput.bucket = this.bucket;
            deleteMultiObjectsInput.recursive = this.recursive;
            deleteMultiObjectsInput.skipTrash = this.skipTrash;
            return deleteMultiObjectsInput;
        }
    }
}

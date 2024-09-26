package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class RenameObjectInput extends GenericInput {
    private String bucket;
    private String key;
    private String newKey;
    private boolean recursiveMkdir;
    private boolean forbidOverwrite;

    public String getBucket() {
        return bucket;
    }

    public RenameObjectInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public RenameObjectInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getNewKey() {
        return newKey;
    }

    public RenameObjectInput setNewKey(String newKey) {
        this.newKey = newKey;
        return this;
    }

    public boolean isRecursiveMkdir() {
        return recursiveMkdir;
    }

    public RenameObjectInput setRecursiveMkdir(boolean recursiveMkdir) {
        this.recursiveMkdir = recursiveMkdir;
        return this;
    }

    public boolean isForbidOverwrite() {
        return forbidOverwrite;
    }

    public RenameObjectInput setForbidOverwrite(boolean forbidOverwrite) {
        this.forbidOverwrite = forbidOverwrite;
        return this;
    }

    @Override
    public String toString() {
        return "RenameObjectInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", newKey='" + newKey + '\'' +
                ", recursiveMkdir=" + recursiveMkdir +
                ", forbidOverwrite=" + forbidOverwrite +
                '}';
    }

    public static RenameObjectInputBuilder builder() {
        return new RenameObjectInputBuilder();
    }

    public static final class RenameObjectInputBuilder {
        private String bucket;
        private String key;
        private String newKey;
        private boolean recursiveMkdir;
        private boolean forbidOverwrite;

        private RenameObjectInputBuilder() {
        }

        public RenameObjectInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public RenameObjectInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public RenameObjectInputBuilder newKey(String newKey) {
            this.newKey = newKey;
            return this;
        }

        public RenameObjectInputBuilder recursiveMkdir(boolean recursiveMkdir) {
            this.recursiveMkdir = recursiveMkdir;
            return this;
        }

        public RenameObjectInputBuilder forbidOverwrite(boolean forbidOverwrite) {
            this.forbidOverwrite = forbidOverwrite;
            return this;
        }

        public RenameObjectInput build() {
            RenameObjectInput renameObjectInput = new RenameObjectInput();
            renameObjectInput.setBucket(bucket);
            renameObjectInput.setKey(key);
            renameObjectInput.setNewKey(newKey);
            renameObjectInput.setRecursiveMkdir(recursiveMkdir);
            renameObjectInput.setForbidOverwrite(forbidOverwrite);
            return renameObjectInput;
        }
    }
}

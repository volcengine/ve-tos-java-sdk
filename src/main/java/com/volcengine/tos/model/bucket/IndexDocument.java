package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexDocument {
    @JsonProperty("Suffix")
    private String suffix;
    @JsonProperty("ForbiddenSubDir")
    private boolean forbiddenSubDir;

    public String getSuffix() {
        return suffix;
    }

    public IndexDocument setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public boolean isForbiddenSubDir() {
        return forbiddenSubDir;
    }

    public IndexDocument setForbiddenSubDir(boolean forbiddenSubDir) {
        this.forbiddenSubDir = forbiddenSubDir;
        return this;
    }

    @Override
    public String toString() {
        return "IndexDocument{" +
                "suffix='" + suffix + '\'' +
                ", forbiddenSubDir=" + forbiddenSubDir +
                '}';
    }

    public static IndexDocumentBuilder builder() {
        return new IndexDocumentBuilder();
    }

    public static final class IndexDocumentBuilder {
        private String suffix;
        private boolean forbiddenSubDir;

        private IndexDocumentBuilder() {
        }

        public IndexDocumentBuilder suffix(String suffix) {
            this.suffix = suffix;
            return this;
        }

        public IndexDocumentBuilder forbiddenSubDir(boolean forbiddenSubDir) {
            this.forbiddenSubDir = forbiddenSubDir;
            return this;
        }

        public IndexDocument build() {
            IndexDocument indexDocument = new IndexDocument();
            indexDocument.setSuffix(suffix);
            indexDocument.setForbiddenSubDir(forbiddenSubDir);
            return indexDocument;
        }
    }
}

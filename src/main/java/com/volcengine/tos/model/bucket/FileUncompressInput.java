package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class FileUncompressInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("Input")
    private FileUncompressSource input;

    @JsonProperty("FileUncompressConfig")
    private FileUncompressConfig fileUncompressConfig;

    @JsonProperty("Output")
    private FileUncompressTarget output;

    public String getBucket() {
        return bucket;
    }

    public FileUncompressInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public FileUncompressSource getInput() {
        return input;
    }

    public FileUncompressInput setInput(FileUncompressSource input) {
        this.input = input;
        return this;
    }

    public FileUncompressConfig getFileUncompressConfig() {
        return fileUncompressConfig;
    }

    public FileUncompressInput setFileUncompressConfig(FileUncompressConfig fileUncompressConfig) {
        this.fileUncompressConfig = fileUncompressConfig;
        return this;
    }

    public FileUncompressTarget getOutput() {
        return output;
    }

    public FileUncompressInput setOutput(FileUncompressTarget output) {
        this.output = output;
        return this;
    }

    @Override
    public String toString() {
        return "FileUncompressInput{" +
                "bucket='" + bucket + '\'' +
                ", input=" + input +
                ", fileUncompressConfig=" + fileUncompressConfig +
                ", output=" + output +
                '}';
    }

    public static FileUncompressInputBuilder builder() {
        return new FileUncompressInputBuilder();
    }

    public static final class FileUncompressInputBuilder {
        private String bucket;
        private FileUncompressSource input;
        private FileUncompressConfig fileUncompressConfig;
        private FileUncompressTarget output;

        private FileUncompressInputBuilder() {
        }

        public FileUncompressInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public FileUncompressInputBuilder input(FileUncompressSource input) {
            this.input = input;
            return this;
        }

        public FileUncompressInputBuilder fileUncompressConfig(FileUncompressConfig fileUncompressConfig) {
            this.fileUncompressConfig = fileUncompressConfig;
            return this;
        }

        public FileUncompressInputBuilder output(FileUncompressTarget output) {
            this.output = output;
            return this;
        }

        public FileUncompressInput build() {
            FileUncompressInput input = new FileUncompressInput();
            input.setBucket(bucket);
            input.setInput(this.input);
            input.setFileUncompressConfig(this.fileUncompressConfig);
            input.setOutput(this.output);
            return input;
        }
    }
}

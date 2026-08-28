package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class FileCompressInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonProperty("Input")
    private FileCompressSource input;

    @JsonProperty("FileCompressConfig")
    private FileCompressConfig fileCompressConfig;

    @JsonProperty("Output")
    private FileCompressTarget output;

    public String getBucket() {
        return bucket;
    }

    public FileCompressInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public FileCompressSource getInput() {
        return input;
    }

    public FileCompressInput setInput(FileCompressSource input) {
        this.input = input;
        return this;
    }

    public FileCompressConfig getFileCompressConfig() {
        return fileCompressConfig;
    }

    public FileCompressInput setFileCompressConfig(FileCompressConfig fileCompressConfig) {
        this.fileCompressConfig = fileCompressConfig;
        return this;
    }

    public FileCompressTarget getOutput() {
        return output;
    }

    public FileCompressInput setOutput(FileCompressTarget output) {
        this.output = output;
        return this;
    }

    @Override
    public String toString() {
        return "FileCompressInput{" +
                "bucket='" + bucket + '\'' +
                ", input=" + input +
                ", fileCompressConfig=" + fileCompressConfig +
                ", output=" + output +
                '}';
    }

    public static FileCompressInputBuilder builder() {
        return new FileCompressInputBuilder();
    }

    public static final class FileCompressInputBuilder {
        private String bucket;
        private FileCompressSource input;
        private FileCompressConfig fileCompressConfig;
        private FileCompressTarget output;

        private FileCompressInputBuilder() {
        }

        public FileCompressInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public FileCompressInputBuilder input(FileCompressSource input) {
            this.input = input;
            return this;
        }

        public FileCompressInputBuilder fileCompressConfig(FileCompressConfig fileCompressConfig) {
            this.fileCompressConfig = fileCompressConfig;
            return this;
        }

        public FileCompressInputBuilder output(FileCompressTarget output) {
            this.output = output;
            return this;
        }

        public FileCompressInput build() {
            FileCompressInput input = new FileCompressInput();
            input.setBucket(bucket);
            input.setInput(this.input);
            input.setFileCompressConfig(this.fileCompressConfig);
            input.setOutput(this.output);
            return input;
        }
    }
}

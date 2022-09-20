package com.volcengine.tos.model.object;

import java.io.InputStream;

public class PutObjectInput {
    private PutObjectBasicInput putObjectBasicInput;
    private InputStream content;

    public PutObjectInput() {
    }

    public PutObjectBasicInput getPutObjectBasicInput() {
        return putObjectBasicInput;
    }

    public InputStream getContent() {
        return content;
    }

    public PutObjectInput setPutObjectBasicInput(PutObjectBasicInput putObjectBasicInput) {
        this.putObjectBasicInput = putObjectBasicInput;
        return this;
    }

    public PutObjectInput setContent(InputStream content) {
        this.content = content;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectInput{" +
                "putObjectBasicInput=" + putObjectBasicInput +
                ", content=" + content +
                '}';
    }

    public static PutObjectInputBuilder builder() {
        return new PutObjectInputBuilder();
    }

    public static final class PutObjectInputBuilder {
        private PutObjectBasicInput putObjectBasicInput;
        private InputStream content;

        private PutObjectInputBuilder() {
        }

        public PutObjectInputBuilder putObjectBasicInput(PutObjectBasicInput putObjectBasicInput) {
            this.putObjectBasicInput = putObjectBasicInput;
            return this;
        }

        public PutObjectInputBuilder content(InputStream content) {
            this.content = content;
            return this;
        }

        public PutObjectInput build() {
            PutObjectInput putObjectInput = new PutObjectInput();
            putObjectInput.setPutObjectBasicInput(putObjectBasicInput);
            putObjectInput.setContent(content);
            return putObjectInput;
        }
    }
}

package com.volcengine.tos.model.object;

import java.io.InputStream;

public class UploadPartV2Input {
    private UploadPartBasicInput uploadPartBasicInput;
    private transient InputStream content;
    private long contentLength;

    public UploadPartBasicInput getUploadPartBasicInput() {
        return uploadPartBasicInput;
    }

    public InputStream getContent() {
        return content;
    }

    public long getContentLength() {
        return contentLength;
    }

    public UploadPartV2Input setUploadPartBasicInput(UploadPartBasicInput uploadPartBasicInput) {
        this.uploadPartBasicInput = uploadPartBasicInput;
        return this;
    }

    public UploadPartV2Input setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public UploadPartV2Input setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartV2Input{" +
                "uploadPartBasicInput=" + uploadPartBasicInput +
                ", contentLength=" + contentLength +
                '}';
    }

    public static UploadPartV2InputBuilder builder() {
        return new UploadPartV2InputBuilder();
    }

    public static final class UploadPartV2InputBuilder {
        private UploadPartBasicInput uploadPartBasicInput;
        private transient InputStream content;
        private long contentLength;

        private UploadPartV2InputBuilder() {
        }

        public UploadPartV2InputBuilder uploadPartBasicInput(UploadPartBasicInput uploadPartBasicInput) {
            this.uploadPartBasicInput = uploadPartBasicInput;
            return this;
        }

        public UploadPartV2InputBuilder content(InputStream content) {
            this.content = content;
            return this;
        }

        public UploadPartV2InputBuilder contentLength(long contentLength) {
            this.contentLength = contentLength;
            return this;
        }

        public UploadPartV2Input build() {
            UploadPartV2Input uploadPartV2Input = new UploadPartV2Input();
            uploadPartV2Input.uploadPartBasicInput = this.uploadPartBasicInput;
            uploadPartV2Input.content = this.content;
            uploadPartV2Input.contentLength = this.contentLength;
            return uploadPartV2Input;
        }
    }
}

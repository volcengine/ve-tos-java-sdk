package com.volcengine.tos.model.object;

import java.io.File;
import java.io.FileInputStream;

public class UploadPartFromFileInput {
    private UploadPartBasicInput uploadPartBasicInput;
    private String filePath;
    private File file;
    private FileInputStream fileInputStream;
    private long offset;
    private long partSize;

    public UploadPartBasicInput getUploadPartBasicInput() {
        return uploadPartBasicInput;
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return file;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    public long getOffset() {
        return offset;
    }

    public long getPartSize() {
        return partSize;
    }

    public UploadPartFromFileInput setUploadPartBasicInput(UploadPartBasicInput uploadPartBasicInput) {
        this.uploadPartBasicInput = uploadPartBasicInput;
        return this;
    }

    public UploadPartFromFileInput setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public UploadPartFromFileInput setFile(File file) {
        this.file = file;
        return this;
    }

    public UploadPartFromFileInput setFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
        return this;
    }

    public UploadPartFromFileInput setOffset(long offset) {
        this.offset = offset;
        return this;
    }

    public UploadPartFromFileInput setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartFromFileInput{" +
                "uploadPartBasicInput=" + uploadPartBasicInput +
                ", filePath='" + filePath + '\'' +
                ", offset=" + offset +
                ", partSize=" + partSize +
                '}';
    }

    public static UploadPartFromFileInputBuilder builder() {
        return new UploadPartFromFileInputBuilder();
    }

    public static final class UploadPartFromFileInputBuilder {
        private UploadPartBasicInput uploadPartBasicInput;
        private String filePath;
        private File file;
        private FileInputStream fileInputStream;
        private long offset;
        private long partSize;

        private UploadPartFromFileInputBuilder() {
        }

        public UploadPartFromFileInputBuilder uploadPartBasicInput(UploadPartBasicInput uploadPartBasicInput) {
            this.uploadPartBasicInput = uploadPartBasicInput;
            return this;
        }

        public UploadPartFromFileInputBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public UploadPartFromFileInputBuilder file(File file) {
            this.file = file;
            return this;
        }

        public UploadPartFromFileInputBuilder fileInputStream(FileInputStream fileInputStream) {
            this.fileInputStream = fileInputStream;
            return this;
        }

        public UploadPartFromFileInputBuilder offset(long offset) {
            this.offset = offset;
            return this;
        }

        public UploadPartFromFileInputBuilder partSize(long partSize) {
            this.partSize = partSize;
            return this;
        }

        public UploadPartFromFileInput build() {
            UploadPartFromFileInput uploadPartFromFileInput = new UploadPartFromFileInput();
            uploadPartFromFileInput.filePath = this.filePath;
            uploadPartFromFileInput.file = this.file;
            uploadPartFromFileInput.fileInputStream = this.fileInputStream;
            uploadPartFromFileInput.uploadPartBasicInput = this.uploadPartBasicInput;
            uploadPartFromFileInput.offset = this.offset;
            uploadPartFromFileInput.partSize = this.partSize;
            return uploadPartFromFileInput;
        }
    }
}

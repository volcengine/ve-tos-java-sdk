package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;

import java.io.File;
import java.io.FileInputStream;

public class PutObjectFromFileInput {
    private PutObjectBasicInput putObjectBasicInput = new PutObjectBasicInput();
    private String filePath;
    private File file;
    private FileInputStream fileInputStream;

    public PutObjectFromFileInput() {
    }

    public PutObjectFromFileInput setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public PutObjectFromFileInput setFile(File file) {
        this.file = file;
        return this;
    }

    public PutObjectFromFileInput setFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
        return this;
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

    public String getBucket() {
        return putObjectBasicInput.getBucket();
    }

    public PutObjectFromFileInput setBucket(String bucket) {
        this.putObjectBasicInput.setBucket(bucket);
        return this;
    }

    public String getKey() {
        return putObjectBasicInput.getKey();
    }

    public PutObjectFromFileInput setKey(String key) {
        this.putObjectBasicInput.setKey(key);
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return putObjectBasicInput.getOptions();
    }

    public PutObjectFromFileInput setOptions(ObjectMetaRequestOptions options) {
        this.putObjectBasicInput.setOptions(options);
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return putObjectBasicInput.getDataTransferListener();
    }

    public PutObjectFromFileInput setDataTransferListener(DataTransferListener dataTransferListener) {
        this.putObjectBasicInput.setDataTransferListener(dataTransferListener);
        return this;
    }

    public RateLimiter getRateLimiter() {
        return putObjectBasicInput.getRateLimiter();
    }

    public PutObjectFromFileInput setRateLimiter(RateLimiter rateLimiter) {
        this.putObjectBasicInput.setRateLimiter(rateLimiter);
        return this;
    }

    public String getCallback() {
        return putObjectBasicInput.getCallback();
    }

    public PutObjectFromFileInput setCallback(String callback) {
        this.putObjectBasicInput.setCallback(callback);
        return this;
    }

    public String getCallbackVar() {
        return putObjectBasicInput.getCallbackVar();
    }

    public PutObjectFromFileInput setCallbackVar(String callbackVar) {
        this.putObjectBasicInput.setCallbackVar(callbackVar);
        return this;
    }

    public boolean isForbidOverwrite() {
        return this.putObjectBasicInput.isForbidOverwrite();
    }

    public PutObjectFromFileInput setForbidOverwrite(boolean forbidOverwrite) {
        this.putObjectBasicInput.setForbidOverwrite(forbidOverwrite);
        return this;
    }

    public String getIfMatch() {
        return this.putObjectBasicInput.getIfMatch();
    }

    public PutObjectFromFileInput setIfMatch(String ifMatch) {
        this.putObjectBasicInput.setIfMatch(ifMatch);
        return this;
    }

    public static PutObjectFromFileInputBuilder builder() {
        return new PutObjectFromFileInputBuilder();
    }

    @Override
    public String toString() {
        return "PutObjectFromFileInput{" +
                "bucket='" + getBucket() + '\'' +
                ", key='" + getKey() + '\'' +
                ", options=" + getOptions() +
                ", dataTransferListener=" + getDataTransferListener() +
                ", rateLimit=" + getRateLimiter() +
                ", callback='" + getCallback() + '\'' +
                ", callbackVar='" + getCallbackVar() + '\'' +
                '}';
    }

    public static final class PutObjectFromFileInputBuilder {
        private PutObjectBasicInput putObjectBasicInput = new PutObjectBasicInput();
        private String filePath;
        private File file;
        private FileInputStream fileInputStream;

        private PutObjectFromFileInputBuilder() {
        }

        @Deprecated
        public PutObjectFromFileInputBuilder putObjectBasicInput(PutObjectBasicInput putObjectBasicInput) {
            this.putObjectBasicInput = putObjectBasicInput;
            return this;
        }

        public PutObjectFromFileInputBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public PutObjectFromFileInputBuilder file(File file) {
            this.file = file;
            return this;
        }

        public PutObjectFromFileInputBuilder fileInputStream(FileInputStream fileInputStream) {
            this.fileInputStream = fileInputStream;
            return this;
        }

        public PutObjectFromFileInputBuilder bucket(String bucket) {
            this.putObjectBasicInput.setBucket(bucket);
            return this;
        }

        public PutObjectFromFileInputBuilder key(String key) {
            this.putObjectBasicInput.setKey(key);
            return this;
        }

        public PutObjectFromFileInputBuilder options(ObjectMetaRequestOptions options) {
            this.putObjectBasicInput.setOptions(options);
            return this;
        }

        public PutObjectFromFileInputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.putObjectBasicInput.setDataTransferListener(dataTransferListener);
            return this;
        }

        public PutObjectFromFileInputBuilder rateLimiter(RateLimiter rateLimiter) {
            this.putObjectBasicInput.setRateLimiter(rateLimiter);
            return this;
        }

        public PutObjectFromFileInputBuilder callback(String callback) {
            this.putObjectBasicInput.setCallback(callback);
            return this;
        }

        public PutObjectFromFileInputBuilder callbackVar(String callbackVar) {
            this.putObjectBasicInput.setCallbackVar(callbackVar);
            return this;
        }

        public PutObjectFromFileInputBuilder forbidOverwrite(boolean forbidOverwrite) {
            this.putObjectBasicInput.setForbidOverwrite(forbidOverwrite);
            return this;
        }

        public PutObjectFromFileInputBuilder ifMatch(String ifMatch) {
            this.putObjectBasicInput.setIfMatch(ifMatch);
            return this;
        }

        public PutObjectFromFileInput build() {
            PutObjectFromFileInput putObjectFromFileInput = new PutObjectFromFileInput();
            putObjectFromFileInput.setPutObjectBasicInput(putObjectBasicInput);
            putObjectFromFileInput.setFilePath(filePath);
            putObjectFromFileInput.setFile(file);
            putObjectFromFileInput.setFileInputStream(fileInputStream);
            return putObjectFromFileInput;
        }
    }

    @Deprecated
    public PutObjectFromFileInput(PutObjectBasicInput putObjectBasicInput, String filePath) {
        this.putObjectBasicInput = putObjectBasicInput;
        this.filePath = filePath;
    }

    @Deprecated
    public PutObjectFromFileInput(PutObjectBasicInput putObjectBasicInput, File file) {
        this.putObjectBasicInput = putObjectBasicInput;
        this.file = file;
    }

    @Deprecated
    public PutObjectFromFileInput(PutObjectBasicInput putObjectBasicInput, FileInputStream fileInputStream) {
        this.putObjectBasicInput = putObjectBasicInput;
        this.fileInputStream = fileInputStream;
    }

    @Deprecated
    public PutObjectFromFileInput setPutObjectBasicInput(PutObjectBasicInput putObjectBasicInput) {
        this.putObjectBasicInput = putObjectBasicInput;
        return this;
    }

    @Deprecated
    public PutObjectBasicInput getPutObjectBasicInput() {
        return putObjectBasicInput;
    }
}

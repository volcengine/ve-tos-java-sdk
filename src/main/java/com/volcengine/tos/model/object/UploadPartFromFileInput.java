package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.model.GenericInput;

import java.io.File;
import java.io.FileInputStream;

public class UploadPartFromFileInput extends GenericInput {
    private UploadPartBasicInput uploadPartBasicInput = new UploadPartBasicInput();
    private String filePath;
    private File file;
    private FileInputStream fileInputStream;
    private long offset;
    private long partSize;

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

    public String getBucket() {
        return uploadPartBasicInput.getBucket();
    }

    public UploadPartFromFileInput setBucket(String bucket) {
        this.uploadPartBasicInput.setBucket(bucket);
        return this;
    }

    public String getKey() {
        return uploadPartBasicInput.getKey();
    }

    public UploadPartFromFileInput setKey(String key) {
        this.uploadPartBasicInput.setKey(key);
        return this;
    }

    public String getUploadID() {
        return uploadPartBasicInput.getUploadID();
    }

    public UploadPartFromFileInput setUploadID(String uploadID) {
        this.uploadPartBasicInput.setUploadID(uploadID);
        return this;
    }

    public int getPartNumber() {
        return uploadPartBasicInput.getPartNumber();
    }

    public UploadPartFromFileInput setPartNumber(int partNumber) {
        this.uploadPartBasicInput.setPartNumber(partNumber);
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return uploadPartBasicInput.getOptions();
    }

    public UploadPartFromFileInput setOptions(ObjectMetaRequestOptions options) {
        this.uploadPartBasicInput.setOptions(options);
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return uploadPartBasicInput.getDataTransferListener();
    }

    public UploadPartFromFileInput setDataTransferListener(DataTransferListener dataTransferListener) {
        this.uploadPartBasicInput.setDataTransferListener(dataTransferListener);
        return this;
    }

    public RateLimiter getRateLimiter() {
        return uploadPartBasicInput.getRateLimiter();
    }

    public UploadPartFromFileInput setRateLimiter(RateLimiter rateLimiter) {
        this.uploadPartBasicInput.setRateLimiter(rateLimiter);
        return this;
    }

    @Override
    public String toString() {
        return "UploadPartFromFileInput{" +
                "bucket='" + getBucket() + '\'' +
                ", key='" + getKey() + '\'' +
                ", uploadID='" + getUploadID() + '\'' +
                ", partNumber=" + getPartNumber() +
                ", options=" + getOptions() +
                ", dataTransferListener=" + getDataTransferListener() +
                ", rateLimit=" + getRateLimiter() +
                ", filePath='" + filePath + '\'' +
                ", offset=" + offset +
                ", partSize=" + partSize +
                '}';
    }

    public static UploadPartFromFileInputBuilder builder() {
        return new UploadPartFromFileInputBuilder();
    }

    public static final class UploadPartFromFileInputBuilder {
        private UploadPartBasicInput uploadPartBasicInput = new UploadPartBasicInput();
        private String filePath;
        private File file;
        private FileInputStream fileInputStream;
        private long offset;
        private long partSize;

        private UploadPartFromFileInputBuilder() {
        }

        @Deprecated
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

        public UploadPartFromFileInputBuilder bucket(String bucket) {
            this.uploadPartBasicInput.setBucket(bucket);
            return this;
        }

        public UploadPartFromFileInputBuilder key(String key) {
            this.uploadPartBasicInput.setKey(key);
            return this;
        }

        public UploadPartFromFileInputBuilder uploadID(String uploadID) {
            this.uploadPartBasicInput.setUploadID(uploadID);
            return this;
        }

        public UploadPartFromFileInputBuilder partNumber(int partNumber) {
            this.uploadPartBasicInput.setPartNumber(partNumber);
            return this;
        }

        public UploadPartFromFileInputBuilder options(ObjectMetaRequestOptions options) {
            this.uploadPartBasicInput.setOptions(options);
            return this;
        }

        public UploadPartFromFileInputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.uploadPartBasicInput.setDataTransferListener(dataTransferListener);
            return this;
        }

        public UploadPartFromFileInputBuilder rateLimiter(RateLimiter rateLimiter) {
            this.uploadPartBasicInput.setRateLimiter(rateLimiter);
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

    @Deprecated
    public UploadPartFromFileInput setUploadPartBasicInput(UploadPartBasicInput uploadPartBasicInput) {
        this.uploadPartBasicInput = uploadPartBasicInput;
        return this;
    }

    @Deprecated
    public UploadPartBasicInput getUploadPartBasicInput() {
        return uploadPartBasicInput;
    }
}

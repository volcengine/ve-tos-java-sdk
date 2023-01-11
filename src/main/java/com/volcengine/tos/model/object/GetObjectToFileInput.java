package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;

import java.io.File;
import java.util.Date;

public class GetObjectToFileInput {
    private GetObjectV2Input getObjectInputV2 = new GetObjectV2Input();
    private String filePath;
    private File file;

    public GetObjectToFileInput() {
    }

    public String getBucket() {
        return getObjectInputV2.getBucket();
    }

    public GetObjectToFileInput setBucket(String bucket) {
        this.getObjectInputV2.setBucket(bucket);
        return this;
    }

    public String getKey() {
        return getObjectInputV2.getKey();
    }

    public GetObjectToFileInput setKey(String key) {
        this.getObjectInputV2.setKey(key);
        return this;
    }

    public String getVersionID() {
        return getObjectInputV2.getVersionID();
    }

    public GetObjectToFileInput setVersionID(String versionID) {
        this.getObjectInputV2.setVersionID(versionID);
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return getObjectInputV2.getOptions();
    }

    public GetObjectToFileInput setOptions(ObjectMetaRequestOptions options) {
        this.getObjectInputV2.setOptions(options);
        return this;
    }

    public String getResponseCacheControl() {
        return getObjectInputV2.getResponseCacheControl();
    }

    public GetObjectToFileInput setResponseCacheControl(String responseCacheControl) {
        this.getObjectInputV2.setResponseCacheControl(responseCacheControl);
        return this;
    }

    public String getResponseContentDisposition() {
        return getObjectInputV2.getResponseContentDisposition();
    }

    public GetObjectToFileInput setResponseContentDisposition(String responseContentDisposition) {
        this.getObjectInputV2.setResponseContentDisposition(responseContentDisposition);
        return this;
    }

    public String getResponseContentEncoding() {
        return getObjectInputV2.getResponseContentEncoding();
    }

    public GetObjectToFileInput setResponseContentEncoding(String responseContentEncoding) {
        this.getObjectInputV2.setResponseContentEncoding(responseContentEncoding);
        return this;
    }

    public String getResponseContentLanguage() {
        return getObjectInputV2.getResponseContentLanguage();
    }

    public GetObjectToFileInput setResponseContentLanguage(String responseContentLanguage) {
        this.getObjectInputV2.setResponseContentLanguage(responseContentLanguage);
        return this;
    }

    public String getResponseContentType() {
        return getObjectInputV2.getResponseContentType();
    }

    public GetObjectToFileInput setResponseContentType(String responseContentType) {
        this.getObjectInputV2.setResponseContentType(responseContentType);
        return this;
    }

    public Date getResponseExpires() {
        return getObjectInputV2.getResponseExpires();
    }

    public GetObjectToFileInput setResponseExpires(Date responseExpires) {
        this.getObjectInputV2.setResponseExpires(responseExpires);
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return getObjectInputV2.getDataTransferListener();
    }

    public GetObjectToFileInput setDataTransferListener(DataTransferListener dataTransferListener) {
        this.getObjectInputV2.setDataTransferListener(dataTransferListener);
        return this;
    }

    public RateLimiter getRateLimiter() {
        return getObjectInputV2.getRateLimiter();
    }

    public GetObjectToFileInput setRateLimiter(RateLimiter rateLimiter) {
        this.getObjectInputV2.setRateLimiter(rateLimiter);
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return file;
    }

    public GetObjectToFileInput setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public GetObjectToFileInput setFile(File file) {
        this.file = file;
        return this;
    }

    public static GetObjectToFileInputBuilder builder() {
        return new GetObjectToFileInputBuilder();
    }

    @Override
    public String toString() {
        return "GetObjectToFileInput{" +
                "bucket='" + getBucket() + '\'' +
                ", key='" + getKey() + '\'' +
                ", versionID='" + getVersionID() + '\'' +
                ", options=" + getOptions() +
                ", responseCacheControl='" + getResponseCacheControl() + '\'' +
                ", responseContentDisposition='" + getResponseContentDisposition() + '\'' +
                ", responseContentEncoding='" + getResponseContentEncoding() + '\'' +
                ", responseContentLanguage='" + getResponseContentLanguage() + '\'' +
                ", responseContentType='" + getResponseContentType() + '\'' +
                ", responseExpires=" + getResponseExpires() +
                ", dataTransferListener=" + getDataTransferListener() +
                ", rateLimiter=" + getRateLimiter() +
                ", filePath='" + filePath + '\'' +
                '}';
    }

    public static final class GetObjectToFileInputBuilder {
        private GetObjectV2Input getObjectInputV2 = new GetObjectV2Input();
        private String filePath;
        private File file;

        private GetObjectToFileInputBuilder() {
        }

        @Deprecated
        public GetObjectToFileInputBuilder getObjectInputV2(GetObjectV2Input getObjectInputV2) {
            this.getObjectInputV2 = getObjectInputV2;
            return this;
        }

        public GetObjectToFileInputBuilder bucket(String bucket) {
            this.getObjectInputV2.setBucket(bucket);
            return this;
        }

        public GetObjectToFileInputBuilder key(String key) {
            this.getObjectInputV2.setKey(key);
            return this;
        }

        public GetObjectToFileInputBuilder versionID(String versionID) {
            this.getObjectInputV2.setVersionID(versionID);
            return this;
        }

        public GetObjectToFileInputBuilder options(ObjectMetaRequestOptions options) {
            this.getObjectInputV2.setOptions(options);
            return this;
        }

        public GetObjectToFileInputBuilder responseCacheControl(String responseCacheControl) {
            this.getObjectInputV2.setResponseCacheControl(responseCacheControl);
            return this;
        }

        public GetObjectToFileInputBuilder responseContentDisposition(String responseContentDisposition) {
            this.getObjectInputV2.setResponseContentDisposition(responseContentDisposition);
            return this;
        }

        public GetObjectToFileInputBuilder responseContentEncoding(String responseContentEncoding) {
            this.getObjectInputV2.setResponseContentEncoding(responseContentEncoding);
            return this;
        }

        public GetObjectToFileInputBuilder responseContentLanguage(String responseContentLanguage) {
            this.getObjectInputV2.setResponseContentLanguage(responseContentLanguage);
            return this;
        }

        public GetObjectToFileInputBuilder responseContentType(String responseContentType) {
            this.getObjectInputV2.setResponseContentType(responseContentType);
            return this;
        }

        public GetObjectToFileInputBuilder responseExpires(Date responseExpires) {
            this.getObjectInputV2.setResponseExpires(responseExpires);
            return this;
        }

        public GetObjectToFileInputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.getObjectInputV2.setDataTransferListener(dataTransferListener);
            return this;
        }

        public GetObjectToFileInputBuilder rateLimiter(RateLimiter rateLimiter) {
            this.getObjectInputV2.setRateLimiter(rateLimiter);
            return this;
        }

        public GetObjectToFileInputBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public GetObjectToFileInputBuilder file(File file) {
            this.file = file;
            return this;
        }

        public GetObjectToFileInput build() {
            GetObjectToFileInput getObjectToFileInput = new GetObjectToFileInput(getObjectInputV2, filePath);
            getObjectToFileInput.setFile(file);
            return getObjectToFileInput;
        }
    }

    @Deprecated
    public GetObjectToFileInput(GetObjectV2Input getObjectInputV2, String filePath) {
        this.getObjectInputV2 = getObjectInputV2;
        this.filePath = filePath;
    }

    @Deprecated
    public GetObjectToFileInput(GetObjectV2Input getObjectInputV2, File file) {
        this.getObjectInputV2 = getObjectInputV2;
        this.file = file;
    }

    @Deprecated
    public GetObjectV2Input getGetObjectInputV2() {
        return getObjectInputV2;
    }

    @Deprecated
    public GetObjectToFileInput setGetObjectInputV2(GetObjectV2Input getObjectInputV2) {
        this.getObjectInputV2 = getObjectInputV2;
        return this;
    }
}

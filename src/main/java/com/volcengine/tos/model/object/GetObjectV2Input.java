package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.internal.util.DateConverter;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetObjectV2Input {
    private String bucket;
    private String key;
    private String versionID;

    private ObjectMetaRequestOptions options;

    private String responseCacheControl;
    private String responseContentDisposition;
    private String responseContentEncoding;
    private String responseContentLanguage;
    private String responseContentType;
    private Date responseExpires;

    /**
     * 进度条
     */
    private DataTransferListener dataTransferListener;

    public String getBucket() {
        return bucket;
    }

    public GetObjectV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public GetObjectV2Input setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public GetObjectV2Input setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public GetObjectV2Input setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public String getResponseCacheControl() {
        return responseCacheControl;
    }

    public GetObjectV2Input setResponseCacheControl(String responseCacheControl) {
        this.responseCacheControl = responseCacheControl;
        return this;
    }

    public String getResponseContentDisposition() {
        return responseContentDisposition;
    }

    public GetObjectV2Input setResponseContentDisposition(String responseContentDisposition) {
        this.responseContentDisposition = responseContentDisposition;
        return this;
    }

    public String getResponseContentEncoding() {
        return responseContentEncoding;
    }

    public GetObjectV2Input setResponseContentEncoding(String responseContentEncoding) {
        this.responseContentEncoding = responseContentEncoding;
        return this;
    }

    public String getResponseContentLanguage() {
        return responseContentLanguage;
    }

    public GetObjectV2Input setResponseContentLanguage(String responseContentLanguage) {
        this.responseContentLanguage = responseContentLanguage;
        return this;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public GetObjectV2Input setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
        return this;
    }

    public Date getResponseExpires() {
        return responseExpires;
    }

    public GetObjectV2Input setResponseExpires(Date responseExpires) {
        this.responseExpires = responseExpires;
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public GetObjectV2Input setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }

    public Map<String, String> getAllSettedHeaders() {
        Map<String, String> allHeaders = new HashMap<>(options == null ? Collections.emptyMap() : options.headers());
        addRespHeaders(allHeaders);
        return allHeaders;
    }

    private void addRespHeaders(Map<String, String> allHeaders) {
        if (this.responseCacheControl != null) {
            allHeaders.put(TosHeader.HEADER_RESPONSE_CACHE_CONTROL, responseCacheControl);
        }
        if (this.responseContentLanguage != null) {
            allHeaders.put(TosHeader.HEADER_RESPONSE_CONTENT_LANGUAGE, responseContentLanguage);
        }
        if (this.responseContentDisposition != null) {
            allHeaders.put(TosHeader.HEADER_RESPONSE_CONTENT_DISPOSITION, responseContentDisposition);
        }
        if (this.responseContentType != null) {
            allHeaders.put(TosHeader.HEADER_RESPONSE_CONTENT_TYPE, responseContentType);
        }
        if (this.responseContentEncoding != null) {
            allHeaders.put(TosHeader.HEADER_RESPONSE_CONTENT_ENCODING, responseContentEncoding);
        }
        if (this.responseExpires != null) {
            allHeaders.put(TosHeader.HEADER_RESPONSE_EXPIRES, DateConverter.dateToRFC1123String(responseExpires));
        }
    }

    @Override
    public String toString() {
        return "GetObjectV2Input{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", options=" + options +
                ", responseCacheControl='" + responseCacheControl + '\'' +
                ", responseContentDisposition='" + responseContentDisposition + '\'' +
                ", responseContentEncoding='" + responseContentEncoding + '\'' +
                ", responseContentLanguage='" + responseContentLanguage + '\'' +
                ", responseContentType='" + responseContentType + '\'' +
                ", responseExpires=" + responseExpires +
                ", dataTransferListener=" + dataTransferListener +
                '}';
    }

    public static GetObjectV2InputBuilder builder() {
        return new GetObjectV2InputBuilder();
    }

    public static final class GetObjectV2InputBuilder {
        private String bucket;
        private String key;
        private String versionID;
        private ObjectMetaRequestOptions options;
        private String responseCacheControl;
        private String responseContentDisposition;
        private String responseContentEncoding;
        private String responseContentLanguage;
        private String responseContentType;
        private Date responseExpires;
        private DataTransferListener dataTransferListener;

        private GetObjectV2InputBuilder() {
        }

        public GetObjectV2InputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public GetObjectV2InputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public GetObjectV2InputBuilder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public GetObjectV2InputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public GetObjectV2InputBuilder responseCacheControl(String responseCacheControl) {
            this.responseCacheControl = responseCacheControl;
            return this;
        }

        public GetObjectV2InputBuilder responseContentDisposition(String responseContentDisposition) {
            this.responseContentDisposition = responseContentDisposition;
            return this;
        }

        public GetObjectV2InputBuilder responseContentEncoding(String responseContentEncoding) {
            this.responseContentEncoding = responseContentEncoding;
            return this;
        }

        public GetObjectV2InputBuilder responseContentLanguage(String responseContentLanguage) {
            this.responseContentLanguage = responseContentLanguage;
            return this;
        }

        public GetObjectV2InputBuilder responseContentType(String responseContentType) {
            this.responseContentType = responseContentType;
            return this;
        }

        public GetObjectV2InputBuilder responseExpires(Date responseExpires) {
            this.responseExpires = responseExpires;
            return this;
        }

        public GetObjectV2InputBuilder dataTransferListener(DataTransferListener dataTransferListener) {
            this.dataTransferListener = dataTransferListener;
            return this;
        }

        public GetObjectV2Input build() {
            GetObjectV2Input getObjectV2Input = new GetObjectV2Input();
            getObjectV2Input.bucket = this.bucket;
            getObjectV2Input.key = this.key;
            getObjectV2Input.dataTransferListener = this.dataTransferListener;
            getObjectV2Input.versionID = this.versionID;
            getObjectV2Input.options = this.options;
            getObjectV2Input.responseCacheControl = this.responseCacheControl;
            getObjectV2Input.responseContentDisposition = this.responseContentDisposition;
            getObjectV2Input.responseContentType = this.responseContentType;
            getObjectV2Input.responseContentEncoding = this.responseContentEncoding;
            getObjectV2Input.responseContentLanguage = this.responseContentLanguage;
            getObjectV2Input.responseExpires = this.responseExpires;
            return getObjectV2Input;
        }
    }
}

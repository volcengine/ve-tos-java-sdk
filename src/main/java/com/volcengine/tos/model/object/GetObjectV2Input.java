package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.DocPreviewDstType;
import com.volcengine.tos.comm.common.DocPreviewSrcType;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.GenericInput;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GetObjectV2Input extends GenericInput {
    private String bucket;
    private String key;
    private String versionID;
    private String range;

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

    /**
     * 客户端限速，单位 Byte/s
     */
    private RateLimiter rateLimiter;

    private String process;

    private int docPage;

    private DocPreviewSrcType srcType;

    private DocPreviewDstType dstType;

    /**
     * 图片另存为参数
     */
    private String saveBucket;

    /**
     * 图片另存为参数
     */
    private String saveObject;


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

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public GetObjectV2Input setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    public String getRange() {
        return range;
    }

    public GetObjectV2Input setRange(String range) {
        this.range = range;
        return this;
    }

    public String getProcess() {
        return process;
    }

    public GetObjectV2Input setProcess(String process) {
        this.process = process;
        return this;
    }

    public int getDocPage() {
        return docPage;
    }

    public GetObjectV2Input setDocPage(int docPage) {
        this.docPage = docPage;
        return this;
    }

    public DocPreviewSrcType getSrcType() {
        return srcType;
    }

    public GetObjectV2Input setSrcType(DocPreviewSrcType srcType) {
        this.srcType = srcType;
        return this;
    }

    public DocPreviewDstType getDstType() {
        return dstType;
    }

    public GetObjectV2Input setDstType(DocPreviewDstType dstType) {
        this.dstType = dstType;
        return this;
    }

    public String getSaveBucket() {
        return saveBucket;
    }

    public GetObjectV2Input setSaveBucket(String saveBucket) {
        this.saveBucket = saveBucket;
        return this;
    }

    public String getSaveObject() {
        return saveObject;
    }

    public GetObjectV2Input setSaveObject(String saveObject) {
        this.saveObject = saveObject;
        return this;
    }

    public Map<String, String> getAllSettedHeaders() {
        Map<String, String> allHeaders = new HashMap<>(options == null ? Collections.emptyMap() : options.headers());
        if (StringUtils.isNotEmpty(range)) {
            // will overwrite the Range header set in options.
            allHeaders.put(TosHeader.HEADER_RANGE, range);
        }
        return allHeaders;
    }

    @Override
    public String toString() {
        return "GetObjectV2Input{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", range='" + range + '\'' +
                ", options=" + options +
                ", responseCacheControl='" + responseCacheControl + '\'' +
                ", responseContentDisposition='" + responseContentDisposition + '\'' +
                ", responseContentEncoding='" + responseContentEncoding + '\'' +
                ", responseContentLanguage='" + responseContentLanguage + '\'' +
                ", responseContentType='" + responseContentType + '\'' +
                ", responseExpires=" + responseExpires +
                ", dataTransferListener=" + dataTransferListener +
                ", rateLimiter=" + rateLimiter +
                ", process='" + process + '\'' +
                ", saveBucket='" + saveBucket + '\'' +
                ", saveObject='" + saveObject + '\'' +
                '}';
    }

    public static GetObjectV2InputBuilder builder() {
        return new GetObjectV2InputBuilder();
    }

    public static final class GetObjectV2InputBuilder {
        private String bucket;
        private String key;
        private String versionID;
        private String range;
        private ObjectMetaRequestOptions options;
        private String responseCacheControl;
        private String responseContentDisposition;
        private String responseContentEncoding;
        private String responseContentLanguage;
        private String responseContentType;
        private Date responseExpires;
        private DataTransferListener dataTransferListener;
        private RateLimiter rateLimiter;
        private String process;
        private int docPage;
        private DocPreviewSrcType srcType;
        private DocPreviewDstType dstType;
        private String saveBucket;
        private String saveObject;

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

        public GetObjectV2InputBuilder range(String range) {
            this.range = range;
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

        public GetObjectV2InputBuilder rateLimiter(RateLimiter rateLimiter) {
            this.rateLimiter = rateLimiter;
            return this;
        }

        public GetObjectV2InputBuilder process(String process) {
            this.process = process;
            return this;
        }

        public GetObjectV2InputBuilder docPage(int docPage) {
            this.docPage = docPage;
            return this;
        }

        public GetObjectV2InputBuilder srcType(DocPreviewSrcType srcType) {
            this.srcType = srcType;
            return this;
        }

        public GetObjectV2InputBuilder dstType(DocPreviewDstType dstType) {
            this.dstType = dstType;
            return this;
        }

        public GetObjectV2InputBuilder saveBucket(String saveBucket) {
            this.saveBucket = saveBucket;
            return this;
        }

        public GetObjectV2InputBuilder saveObject(String saveObject) {
            this.saveObject = saveObject;
            return this;
        }

        public GetObjectV2Input build() {
            GetObjectV2Input getObjectV2Input = new GetObjectV2Input();
            getObjectV2Input.bucket = this.bucket;
            getObjectV2Input.key = this.key;
            getObjectV2Input.range = this.range;
            getObjectV2Input.dataTransferListener = this.dataTransferListener;
            getObjectV2Input.versionID = this.versionID;
            getObjectV2Input.options = this.options;
            getObjectV2Input.responseCacheControl = this.responseCacheControl;
            getObjectV2Input.responseContentDisposition = this.responseContentDisposition;
            getObjectV2Input.responseContentType = this.responseContentType;
            getObjectV2Input.responseContentEncoding = this.responseContentEncoding;
            getObjectV2Input.responseContentLanguage = this.responseContentLanguage;
            getObjectV2Input.responseExpires = this.responseExpires;
            getObjectV2Input.rateLimiter = this.rateLimiter;
            getObjectV2Input.process = this.process;
            getObjectV2Input.docPage = this.docPage;
            getObjectV2Input.srcType = this.srcType;
            getObjectV2Input.dstType = this.dstType;
            getObjectV2Input.saveBucket = this.saveBucket;
            getObjectV2Input.saveObject = this.saveObject;
            return getObjectV2Input;
        }
    }
}

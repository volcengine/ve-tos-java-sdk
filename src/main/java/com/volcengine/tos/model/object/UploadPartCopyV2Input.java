package com.volcengine.tos.model.object;

import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.internal.model.HttpRange;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.internal.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadPartCopyV2Input {
    private String bucket;
    private String key;
    private String uploadID;
    private int partNumber;

    private String sourceBucket;
    private String sourceKey;
    private String sourceVersionID;

    /**
     * for http common header: "Range"
     * only supported in uploadPartCopy method
     */
    private HttpRange copySourceRange;

    /**
     * for copy object option header: "x-tos-copy-source-if-match"
     */
    private String copySourceIfMatch;
    /**
     * for copy object option header: "x-tos-copy-source-if-modified-since", format in RFC1123 GMT
     */
    private Date copySourceIfModifiedSinceDate;
    /**
     * for copy object option header: "x-tos-copy-source-if-none-match"
     */
    private String copySourceIfNoneMatch;
    /**
     * for copy object option header: "x-tos-copy-source-if-unmodified-since", format in RFC1123 GMT
     */
    private Date copySourceIfUnmodifiedSinceDate;
    /**
     * for copy object option header: "x-tos-copy-source-server-side-encryption-customer-algorithm", value=AES256
     */
    private String copySourceSSECAlgorithm;
    /**
     * for copy object option header: "x-tos-copy-source-server-side-encryption-customer-key"
     */
    private String copySourceSSECKey;
    /**
     * for copy object option header: "x-tos-copy-source-server-side-encryption-customer-key-MD5"
     */
    private String copySourceSSECKeyMD5;

    private ObjectMetaRequestOptions options;

    private Map<String, String> headers;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getUploadID() {
        return uploadID;
    }

    public int getPartNumber() {
        return partNumber;
    }

    public String getSourceBucket() {
        return sourceBucket;
    }

    public String getSourceKey() {
        return sourceKey;
    }

    public String getSourceVersionID() {
        return sourceVersionID;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public UploadPartCopyV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public UploadPartCopyV2Input setKey(String key) {
        this.key = key;
        return this;
    }

    public UploadPartCopyV2Input setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public UploadPartCopyV2Input setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public UploadPartCopyV2Input setSourceBucket(String sourceBucket) {
        this.sourceBucket = sourceBucket;
        return this;
    }

    public UploadPartCopyV2Input setSourceKey(String sourceKey) {
        this.sourceKey = sourceKey;
        return this;
    }

    public UploadPartCopyV2Input setSourceVersionID(String sourceVersionID) {
        this.sourceVersionID = sourceVersionID;
        return this;
    }

    public UploadPartCopyV2Input setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public long getCopySourceRangeStart() {
        return copySourceRange == null ? 0 : copySourceRange.getStart();
    }

    public long getCopySourceRangeEnd() {
        return copySourceRange == null ? 0 : copySourceRange.getEnd();
    }

    public UploadPartCopyV2Input setCopySourceRange(long copySourceRangeStart, long copySourceRangeEnd) {
        this.copySourceRange = new HttpRange().setStart(copySourceRangeStart).setEnd(copySourceRangeEnd);
        return this;
    }

    public String getCopySourceIfMatch() {
        return copySourceIfMatch;
    }

    public UploadPartCopyV2Input setCopySourceIfMatch(String copySourceIfMatch) {
        this.copySourceIfMatch = copySourceIfMatch;
        return this;
    }

    public Date getCopySourceIfModifiedSinceDate() {
        return copySourceIfModifiedSinceDate;
    }

    public UploadPartCopyV2Input setCopySourceIfModifiedSinceDate(Date copySourceIfModifiedSinceDate) {
        this.copySourceIfModifiedSinceDate = copySourceIfModifiedSinceDate;
        return this;
    }

    public String getCopySourceIfNoneMatch() {
        return copySourceIfNoneMatch;
    }

    public UploadPartCopyV2Input setCopySourceIfNoneMatch(String copySourceIfNoneMatch) {
        this.copySourceIfNoneMatch = copySourceIfNoneMatch;
        return this;
    }

    public Date getCopySourceIfUnmodifiedSinceDate() {
        return copySourceIfUnmodifiedSinceDate;
    }

    public UploadPartCopyV2Input setCopySourceIfUnmodifiedSinceDate(Date copySourceIfUnmodifiedSinceDate) {
        this.copySourceIfUnmodifiedSinceDate = copySourceIfUnmodifiedSinceDate;
        return this;
    }

    public String getCopySourceSSECAlgorithm() {
        return copySourceSSECAlgorithm;
    }

    public UploadPartCopyV2Input setCopySourceSSECAlgorithm(String copySourceSSECAlgorithm) {
        this.copySourceSSECAlgorithm = copySourceSSECAlgorithm;
        return this;
    }

    public String getCopySourceSSECKey() {
        return copySourceSSECKey;
    }

    public UploadPartCopyV2Input setCopySourceSSECKey(String copySourceSSECKey) {
        this.copySourceSSECKey = copySourceSSECKey;
        return this;
    }

    public String getCopySourceSSECKeyMD5() {
        return copySourceSSECKeyMD5;
    }

    public UploadPartCopyV2Input setCopySourceSSECKeyMD5(String copySourceSSECKeyMD5) {
        this.copySourceSSECKeyMD5 = copySourceSSECKeyMD5;
        return this;
    }

    public Map<String, String> getAllSettedHeaders() {
        if (this.copySourceRange != null) {
            withHeader(TosHeader.HEADER_COPY_SOURCE_RANGE, this.copySourceRange.toString());
        }
        withHeader(TosHeader.HEADER_COPY_SOURCE_IF_MATCH, copySourceIfMatch);
        withHeader(TosHeader.HEADER_COPY_SOURCE_IF_NONE_MATCH, copySourceIfNoneMatch);
        if (StringUtils.isNotEmpty(copySourceSSECAlgorithm)) {
            if (Consts.customServerSideEncryptionAlgorithmList.contains(copySourceSSECAlgorithm)) {
                withHeader(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM, copySourceSSECAlgorithm);
            } else {
                throw new IllegalArgumentException("invalid copySourceSSECAlgorithm input, only support AES256");
            }
        }
        withHeader(TosHeader.HEADER_COPY_SOURCE_SSE_CUSTOMER_KEY, copySourceSSECKey);
        withHeader(TosHeader.HEADER_COPY_SOURCE_SSE_CUSTOMER_KEY_MD5, copySourceSSECKeyMD5);
        if (copySourceIfModifiedSinceDate != null) {
            withHeader(key, DateConverter.dateToRFC1123String(copySourceIfModifiedSinceDate));
        }
        if (copySourceIfUnmodifiedSinceDate != null) {
            withHeader(key, DateConverter.dateToRFC1123String(copySourceIfUnmodifiedSinceDate));
        }
        if (options != null) {
            headers.putAll(options.headers());
        }
        return headers;
    }

    private void withHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        if(value != null && value.length() != 0){
            value = TosUtils.tryEncodeValue(key, value);
            this.headers.put(key, value);
        }
    }

    @Override
    public String toString() {
        return "UploadPartCopyV2Input{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", partNumber=" + partNumber +
                ", sourceBucket='" + sourceBucket + '\'' +
                ", sourceKey='" + sourceKey + '\'' +
                ", sourceVersionID='" + sourceVersionID + '\'' +
                ", copySourceRange=" + copySourceRange +
                ", copySourceIfMatch='" + copySourceIfMatch + '\'' +
                ", copySourceIfModifiedSinceDate=" + copySourceIfModifiedSinceDate +
                ", copySourceIfNoneMatch='" + copySourceIfNoneMatch + '\'' +
                ", copySourceIfUnmodifiedSinceDate=" + copySourceIfUnmodifiedSinceDate +
                ", copySourceSSECAlgorithm='" + copySourceSSECAlgorithm + '\'' +
                ", copySourceSSECKey='" + copySourceSSECKey + '\'' +
                ", copySourceSSECKeyMD5='" + copySourceSSECKeyMD5 + '\'' +
                ", options=" + options +
                '}';
    }

    public static UploadPartCopyV2InputBuilder builder() {
        return new UploadPartCopyV2InputBuilder();
    }

    public static final class UploadPartCopyV2InputBuilder {
        private String bucket;
        private String key;
        private String uploadID;
        private int partNumber;
        private String sourceBucket;
        private String sourceKey;
        private String sourceVersionID;
        private HttpRange range;
        private String copySourceIfMatch;
        private Date copySourceIfModifiedSinceDate;
        private String copySourceIfNoneMatch;
        private Date copySourceIfUnmodifiedSinceDate;
        private String copySourceSSECAlgorithm;
        private String copySourceSSECKey;
        private String copySourceSSECKeyMD5;
        private ObjectMetaRequestOptions options;

        private UploadPartCopyV2InputBuilder() {
        }

        public UploadPartCopyV2InputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public UploadPartCopyV2InputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public UploadPartCopyV2InputBuilder uploadID(String uploadID) {
            this.uploadID = uploadID;
            return this;
        }

        public UploadPartCopyV2InputBuilder partNumber(int partNumber) {
            this.partNumber = partNumber;
            return this;
        }

        public UploadPartCopyV2InputBuilder sourceBucket(String sourceBucket) {
            this.sourceBucket = sourceBucket;
            return this;
        }

        public UploadPartCopyV2InputBuilder sourceKey(String sourceKey) {
            this.sourceKey = sourceKey;
            return this;
        }

        public UploadPartCopyV2InputBuilder sourceVersionID(String sourceVersionID) {
            this.sourceVersionID = sourceVersionID;
            return this;
        }

        public UploadPartCopyV2InputBuilder copySourceRange(long copySourceRangeStart, long copySourceRangeEnd) {
            this.range = new HttpRange().setStart(copySourceRangeStart).setEnd(copySourceRangeEnd);
            return this;
        }

        public UploadPartCopyV2InputBuilder copySourceIfMatch(String copySourceIfMatch) {
            this.copySourceIfMatch = copySourceIfMatch;
            return this;
        }

        public UploadPartCopyV2InputBuilder copySourceIfModifiedSinceDate(Date copySourceIfModifiedSinceDate) {
            this.copySourceIfModifiedSinceDate = copySourceIfModifiedSinceDate;
            return this;
        }

        public UploadPartCopyV2InputBuilder copySourceIfNoneMatch(String copySourceIfNoneMatch) {
            this.copySourceIfNoneMatch = copySourceIfNoneMatch;
            return this;
        }

        public UploadPartCopyV2InputBuilder copySourceIfUnmodifiedSinceDate(Date copySourceIfUnmodifiedSinceDate) {
            this.copySourceIfUnmodifiedSinceDate = copySourceIfUnmodifiedSinceDate;
            return this;
        }

        public UploadPartCopyV2InputBuilder copySourceSSECAlgorithm(String copySourceSSECAlgorithm) {
            this.copySourceSSECAlgorithm = copySourceSSECAlgorithm;
            return this;
        }

        public UploadPartCopyV2InputBuilder copySourceSSECKey(String copySourceSSECKey) {
            this.copySourceSSECKey = copySourceSSECKey;
            return this;
        }

        public UploadPartCopyV2InputBuilder copySourceSSECKeyMD5(String copySourceSSECKeyMD5) {
            this.copySourceSSECKeyMD5 = copySourceSSECKeyMD5;
            return this;
        }

        public UploadPartCopyV2InputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public UploadPartCopyV2Input build() {
            UploadPartCopyV2Input uploadPartCopyV2Input = new UploadPartCopyV2Input();
            uploadPartCopyV2Input.setBucket(bucket);
            uploadPartCopyV2Input.setKey(key);
            uploadPartCopyV2Input.setUploadID(uploadID);
            uploadPartCopyV2Input.setPartNumber(partNumber);
            uploadPartCopyV2Input.setSourceBucket(sourceBucket);
            uploadPartCopyV2Input.setSourceKey(sourceKey);
            uploadPartCopyV2Input.setSourceVersionID(sourceVersionID);
            uploadPartCopyV2Input.setCopySourceIfMatch(copySourceIfMatch);
            uploadPartCopyV2Input.setCopySourceIfModifiedSinceDate(copySourceIfModifiedSinceDate);
            uploadPartCopyV2Input.setCopySourceIfNoneMatch(copySourceIfNoneMatch);
            uploadPartCopyV2Input.setCopySourceIfUnmodifiedSinceDate(copySourceIfUnmodifiedSinceDate);
            uploadPartCopyV2Input.setCopySourceSSECAlgorithm(copySourceSSECAlgorithm);
            uploadPartCopyV2Input.setCopySourceSSECKey(copySourceSSECKey);
            uploadPartCopyV2Input.setCopySourceSSECKeyMD5(copySourceSSECKeyMD5);
            if (range != null) {
                uploadPartCopyV2Input.copySourceRange = range;
            }
            uploadPartCopyV2Input.setOptions(options);
            return uploadPartCopyV2Input;
        }
    }
}

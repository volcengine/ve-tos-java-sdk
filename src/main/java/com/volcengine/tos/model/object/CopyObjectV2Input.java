package com.volcengine.tos.model.object;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.MetadataDirectiveType;
import com.volcengine.tos.comm.common.TaggingDirectiveType;
import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.GenericInput;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CopyObjectV2Input extends GenericInput {
    /**
     * the bucket copy to
     */
    private String bucket;
    /**
     * the object copy to
     */
    private String key;
    /**
     * the bucket copy from
     */
    private String srcBucket;
    /**
     * the object copy from
     */
    private String srcKey;

    /**
     * the object version copy from
     */
    private String srcVersionID;

    /**
     * for copy object option header: "x-tos-copy-source-if-match"
     */
    private String copySourceIfMatch;
    /**
     * for copy object option header: "x-tos-copy-source-if-modified-since", format in RFC1123 GMT
     */
    private Date copySourceIfModifiedSince;
    /**
     * for copy object option header: "x-tos-copy-source-if-none-match"
     */
    private String copySourceIfNoneMatch;
    /**
     * for copy object option header: "x-tos-copy-source-if-unmodified-since", format in RFC1123 GMT
     */
    private Date copySourceIfUnmodifiedSince;
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

    /**
     * for copy object option header: "x-tos-metadata-directive"
     */
    private MetadataDirectiveType metadataDirective;

    private ObjectMetaRequestOptions options;

    private boolean forbidOverwrite;
    private String ifMatch;
    private String tagging;
    private TaggingDirectiveType taggingDirective;

    private Map<String, String> headers;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getSrcBucket() {
        return srcBucket;
    }

    public String getSrcKey() {
        return srcKey;
    }

    public String getSrcVersionID() {
        return srcVersionID;
    }

    public ObjectMetaRequestOptions getOptions() {
        return options;
    }

    public Map<String, String> getAllSettedHeaders() {
        withHeader(TosHeader.HEADER_COPY_SOURCE_IF_MATCH, copySourceIfMatch);
        withHeader(TosHeader.HEADER_COPY_SOURCE_IF_NONE_MATCH, copySourceIfNoneMatch);
        if (StringUtils.isNotEmpty(copySourceSSECAlgorithm)) {
            if (Consts.CUSTOM_SERVER_SIDE_ENCRYPTION_ALGORITHM_LIST.contains(copySourceSSECAlgorithm)) {
                withHeader(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM, copySourceSSECAlgorithm);
            } else {
                throw new TosClientException("invalid copySourceSSECAlgorithm input, only support AES256", null);
            }
        }
        withHeader(TosHeader.HEADER_COPY_SOURCE_SSE_CUSTOMER_KEY, copySourceSSECKey);
        withHeader(TosHeader.HEADER_COPY_SOURCE_SSE_CUSTOMER_KEY_MD5, copySourceSSECKeyMD5);
        withHeader(TosHeader.HEADER_METADATA_DIRECTIVE,
                metadataDirective == null ? null : metadataDirective.toString());
        if (copySourceIfModifiedSince != null) {
            withHeader(key, DateConverter.dateToRFC1123String(copySourceIfModifiedSince));
        }
        if (copySourceIfUnmodifiedSince != null) {
            withHeader(key, DateConverter.dateToRFC1123String(copySourceIfUnmodifiedSince));
        }
        if (options != null) {
            headers.putAll(options.headers());
        }
        withHeader(TosHeader.HEADER_X_IF_MATCH, ifMatch);
        if (forbidOverwrite) {
            withHeader(TosHeader.HEADER_FORBID_OVERWRITE, "true");
        }
        withHeader(TosHeader.HEADER_TAGGING, tagging);
        withHeader(TosHeader.HEADER_TAGGING_DIRECTIVE, taggingDirective == null ? null : taggingDirective.getType());
        return headers;
    }

    private void withHeader(String key, String value) {
        if (this.headers == null) {
            this.headers = new HashMap<>();
        }
        if(value != null && value.length() != 0){
            this.headers.put(key, value);
        }
    }

    public CopyObjectV2Input setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public CopyObjectV2Input setKey(String key) {
        this.key = key;
        return this;
    }

    public CopyObjectV2Input setSrcBucket(String srcBucket) {
        this.srcBucket = srcBucket;
        return this;
    }

    public CopyObjectV2Input setSrcKey(String srcKey) {
        this.srcKey = srcKey;
        return this;
    }

    public CopyObjectV2Input setSrcVersionID(String srcVersionID) {
        this.srcVersionID = srcVersionID;
        return this;
    }

    public CopyObjectV2Input setOptions(ObjectMetaRequestOptions options) {
        this.options = options;
        return this;
    }

    public String getCopySourceIfMatch() {
        return copySourceIfMatch;
    }

    public CopyObjectV2Input setCopySourceIfMatch(String copySourceIfMatch) {
        this.copySourceIfMatch = copySourceIfMatch;
        return this;
    }

    public Date getCopySourceIfModifiedSince() {
        return copySourceIfModifiedSince;
    }

    public CopyObjectV2Input setCopySourceIfModifiedSince(Date copySourceIfModifiedSince) {
        this.copySourceIfModifiedSince = copySourceIfModifiedSince;
        return this;
    }

    public String getCopySourceIfNoneMatch() {
        return copySourceIfNoneMatch;
    }

    public CopyObjectV2Input setCopySourceIfNoneMatch(String copySourceIfNoneMatch) {
        this.copySourceIfNoneMatch = copySourceIfNoneMatch;
        return this;
    }

    public Date getCopySourceIfUnmodifiedSince() {
        return copySourceIfUnmodifiedSince;
    }

    public CopyObjectV2Input setCopySourceIfUnmodifiedSince(Date copySourceIfUnmodifiedSince) {
        this.copySourceIfUnmodifiedSince = copySourceIfUnmodifiedSince;
        return this;
    }

    public String getCopySourceSSECAlgorithm() {
        return copySourceSSECAlgorithm;
    }

    public CopyObjectV2Input setCopySourceSSECAlgorithm(String copySourceSSECAlgorithm) {
        this.copySourceSSECAlgorithm = copySourceSSECAlgorithm;
        return this;
    }

    public String getCopySourceSSECKey() {
        return copySourceSSECKey;
    }

    public CopyObjectV2Input setCopySourceSSECKey(String copySourceSSECKey) {
        this.copySourceSSECKey = copySourceSSECKey;
        return this;
    }

    public String getCopySourceSSECKeyMD5() {
        return copySourceSSECKeyMD5;
    }

    public CopyObjectV2Input setCopySourceSSECKeyMD5(String copySourceSSECKeyMD5) {
        this.copySourceSSECKeyMD5 = copySourceSSECKeyMD5;
        return this;
    }

    public MetadataDirectiveType getMetadataDirective() {
        return metadataDirective;
    }

    public CopyObjectV2Input setMetadataDirective(MetadataDirectiveType metadataDirective) {
        this.metadataDirective = metadataDirective;
        return this;
    }

    public boolean isForbidOverwrite() {
        return forbidOverwrite;
    }

    public CopyObjectV2Input setForbidOverwrite(boolean forbidOverwrite) {
        this.forbidOverwrite = forbidOverwrite;
        return this;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public CopyObjectV2Input setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    public String getTagging() {
        return tagging;
    }

    public CopyObjectV2Input setTagging(String tagging) {
        this.tagging = tagging;
        return this;
    }

    public TaggingDirectiveType getTaggingDirective() {
        return taggingDirective;
    }

    public CopyObjectV2Input setTaggingDirective(TaggingDirectiveType taggingDirective) {
        this.taggingDirective = taggingDirective;
        return this;
    }

    public static CopyObjectV2InputBuilder builder() {
        return new CopyObjectV2InputBuilder();
    }

    @Override
    public String toString() {
        return "CopyObjectV2Input{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", srcBucket='" + srcBucket + '\'' +
                ", srcKey='" + srcKey + '\'' +
                ", srcVersionID='" + srcVersionID + '\'' +
                ", copySourceIfMatch='" + copySourceIfMatch + '\'' +
                ", copySourceIfModifiedSince=" + copySourceIfModifiedSince +
                ", copySourceIfNoneMatch='" + copySourceIfNoneMatch + '\'' +
                ", copySourceIfUnmodifiedSince=" + copySourceIfUnmodifiedSince +
                ", copySourceSSECAlgorithm='" + copySourceSSECAlgorithm + '\'' +
                ", copySourceSSECKey='" + copySourceSSECKey + '\'' +
                ", copySourceSSECKeyMD5='" + copySourceSSECKeyMD5 + '\'' +
                ", metadataDirective=" + metadataDirective +
                ", options=" + options +
                '}';
    }

    public static final class CopyObjectV2InputBuilder {
        private String bucket;
        private String key;
        private String srcBucket;
        private String srcKey;
        private String srcVersionID;
        private String copySourceIfMatch;
        private Date copySourceIfModifiedSince;
        private String copySourceIfNoneMatch;
        private Date copySourceIfUnmodifiedSince;
        private String copySourceSSECAlgorithm;
        private String copySourceSSECKey;
        private String copySourceSSECKeyMD5;
        private MetadataDirectiveType metadataDirective;
        private ObjectMetaRequestOptions options;
        private boolean forbidOverwrite;
        private String ifMatch;
        private String tagging;
        private TaggingDirectiveType taggingDirective;

        private CopyObjectV2InputBuilder() {
        }

        public CopyObjectV2InputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public CopyObjectV2InputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public CopyObjectV2InputBuilder srcBucket(String srcBucket) {
            this.srcBucket = srcBucket;
            return this;
        }

        public CopyObjectV2InputBuilder srcKey(String srcKey) {
            this.srcKey = srcKey;
            return this;
        }

        public CopyObjectV2InputBuilder srcVersionID(String srcVersionID) {
            this.srcVersionID = srcVersionID;
            return this;
        }

        public CopyObjectV2InputBuilder copySourceIfMatch(String copySourceIfMatch) {
            this.copySourceIfMatch = copySourceIfMatch;
            return this;
        }

        public CopyObjectV2InputBuilder copySourceIfModifiedSince(Date copySourceIfModifiedSince) {
            this.copySourceIfModifiedSince = copySourceIfModifiedSince;
            return this;
        }

        public CopyObjectV2InputBuilder copySourceIfNoneMatch(String copySourceIfNoneMatch) {
            this.copySourceIfNoneMatch = copySourceIfNoneMatch;
            return this;
        }

        public CopyObjectV2InputBuilder copySourceIfUnmodifiedSince(Date copySourceIfUnmodifiedSince) {
            this.copySourceIfUnmodifiedSince = copySourceIfUnmodifiedSince;
            return this;
        }

        public CopyObjectV2InputBuilder copySourceSSECAlgorithm(String copySourceSSECAlgorithm) {
            this.copySourceSSECAlgorithm = copySourceSSECAlgorithm;
            return this;
        }

        public CopyObjectV2InputBuilder copySourceSSECKey(String copySourceSSECKey) {
            this.copySourceSSECKey = copySourceSSECKey;
            return this;
        }

        public CopyObjectV2InputBuilder copySourceSSECKeyMD5(String copySourceSSECKeyMD5) {
            this.copySourceSSECKeyMD5 = copySourceSSECKeyMD5;
            return this;
        }

        public CopyObjectV2InputBuilder metadataDirective(MetadataDirectiveType metadataDirective) {
            this.metadataDirective = metadataDirective;
            return this;
        }

        public CopyObjectV2InputBuilder options(ObjectMetaRequestOptions options) {
            this.options = options;
            return this;
        }

        public CopyObjectV2InputBuilder forbidOverwrite(boolean forbidOverwrite) {
            this.forbidOverwrite = forbidOverwrite;
            return this;
        }

        public CopyObjectV2InputBuilder ifMatch(String ifMatch) {
            this.ifMatch = ifMatch;
            return this;
        }

        public CopyObjectV2InputBuilder tagging(String tagging) {
            this.tagging = tagging;
            return this;
        }

        public CopyObjectV2InputBuilder taggingDirective(TaggingDirectiveType taggingDirective) {
            this.taggingDirective = taggingDirective;
            return this;
        }

        public CopyObjectV2Input build() {
            CopyObjectV2Input copyObjectV2Input = new CopyObjectV2Input();
            copyObjectV2Input.setBucket(bucket);
            copyObjectV2Input.setKey(key);
            copyObjectV2Input.setSrcBucket(srcBucket);
            copyObjectV2Input.setSrcKey(srcKey);
            copyObjectV2Input.setSrcVersionID(srcVersionID);
            copyObjectV2Input.setCopySourceIfMatch(copySourceIfMatch);
            copyObjectV2Input.setCopySourceIfModifiedSince(copySourceIfModifiedSince);
            copyObjectV2Input.setCopySourceIfNoneMatch(copySourceIfNoneMatch);
            copyObjectV2Input.setCopySourceIfUnmodifiedSince(copySourceIfUnmodifiedSince);
            copyObjectV2Input.setCopySourceSSECAlgorithm(copySourceSSECAlgorithm);
            copyObjectV2Input.setCopySourceSSECKey(copySourceSSECKey);
            copyObjectV2Input.setCopySourceSSECKeyMD5(copySourceSSECKeyMD5);
            copyObjectV2Input.setMetadataDirective(metadataDirective);
            copyObjectV2Input.setOptions(options);
            copyObjectV2Input.setIfMatch(ifMatch);
            copyObjectV2Input.setForbidOverwrite(forbidOverwrite);
            copyObjectV2Input.setTagging(tagging);
            copyObjectV2Input.setTaggingDirective(taggingDirective);
            return copyObjectV2Input;
        }
    }
}

package com.volcengine.tos.model.object;

import java.util.List;

public class PreSignedPostSignatureInput {
    private String bucket;
    private String key;
    private long expires;
    private List<PostSignatureCondition> conditions;
    private ContentLengthRange contentLengthRange;

    public String getBucket() {
        return bucket;
    }

    public PreSignedPostSignatureInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public PreSignedPostSignatureInput setKey(String key) {
        this.key = key;
        return this;
    }

    public long getExpires() {
        return expires;
    }

    public PreSignedPostSignatureInput setExpires(long expires) {
        this.expires = expires;
        return this;
    }

    public List<PostSignatureCondition> getConditions() {
        return conditions;
    }

    public PreSignedPostSignatureInput setConditions(List<PostSignatureCondition> conditions) {
        this.conditions = conditions;
        return this;
    }

    public ContentLengthRange getContentLengthRange() {
        return contentLengthRange;
    }

    public PreSignedPostSignatureInput setContentLengthRange(ContentLengthRange contentLengthRange) {
        this.contentLengthRange = contentLengthRange;
        return this;
    }

    @Override
    public String toString() {
        return "PreSignedPostSignatureInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", expires=" + expires +
                ", conditions=" + conditions +
                ", contentLengthRange=" + contentLengthRange +
                '}';
    }

    public static PreSignedPostSignatureInputBuilder builder() {
        return new PreSignedPostSignatureInputBuilder();
    }

    public static final class PreSignedPostSignatureInputBuilder {
        private String bucket;
        private String key;
        private long expires;
        private List<PostSignatureCondition> conditions;
        private ContentLengthRange contentLengthRange;

        private PreSignedPostSignatureInputBuilder() {
        }

        public PreSignedPostSignatureInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PreSignedPostSignatureInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public PreSignedPostSignatureInputBuilder expires(long expires) {
            this.expires = expires;
            return this;
        }

        public PreSignedPostSignatureInputBuilder conditions(List<PostSignatureCondition> conditions) {
            this.conditions = conditions;
            return this;
        }

        public PreSignedPostSignatureInputBuilder contentLengthRange(ContentLengthRange contentLengthRange) {
            this.contentLengthRange = contentLengthRange;
            return this;
        }

        public PreSignedPostSignatureInput build() {
            PreSignedPostSignatureInput preSignedPostSignatureInput = new PreSignedPostSignatureInput();
            preSignedPostSignatureInput.setBucket(bucket);
            preSignedPostSignatureInput.setKey(key);
            preSignedPostSignatureInput.setExpires(expires);
            preSignedPostSignatureInput.setConditions(conditions);
            preSignedPostSignatureInput.setContentLengthRange(contentLengthRange);
            return preSignedPostSignatureInput;
        }
    }
}

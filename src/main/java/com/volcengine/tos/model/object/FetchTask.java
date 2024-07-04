package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.internal.util.TosUtils;

import java.util.List;
import java.util.Map;

public class FetchTask {
    @JsonProperty("URL")
    private String url;
    @JsonProperty("IgnoreSameKey")
    private boolean ignoreSameKey;
    @JsonProperty("ContentMD5")
    private String contentMD5;
    @JsonProperty("Bucket")
    private String bucket;
    @JsonProperty("Object")
    private String key;
    @JsonProperty("CallbackURL")
    private String callbackUrl;
    @JsonProperty("CallbackHost")
    private String callbackHost;
    @JsonProperty("CallbackBodyType")
    private String callbackBodyType;
    @JsonProperty("CallbackBody")
    private String callbackBody;
    @JsonProperty("StorageClass")
    private StorageClassType storageClass;
    @JsonProperty("Acl")
    private ACLType acl;
    @JsonProperty("GrantFullControl")
    private String grantFullControl;
    @JsonProperty("GrantRead")
    private String grantRead;
    @JsonProperty("GrantReadAcp")
    private String grantReadAcp;
    @JsonProperty("GrantWriteAcp")
    private String grantWriteAcp;
    @JsonProperty("SSECAlgorithm")
    private String ssecAlgorithm;
    @JsonProperty("SSECKey")
    private String ssecKey;
    @JsonProperty("SSECKeyMd5")
    private String ssecKeyMD5;
    @JsonProperty("UserMeta")
    private List<Map<String, String>> userMeta;
    @JsonIgnore
    private boolean disableEncodingMeta;

    public String getUrl() {
        return url;
    }

    public FetchTask setUrl(String url) {
        this.url = url;
        return this;
    }

    public boolean isIgnoreSameKey() {
        return ignoreSameKey;
    }

    public FetchTask setIgnoreSameKey(boolean ignoreSameKey) {
        this.ignoreSameKey = ignoreSameKey;
        return this;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public FetchTask setContentMD5(String contentMD5) {
        this.contentMD5 = contentMD5;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public FetchTask setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public FetchTask setKey(String key) {
        this.key = key;
        return this;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public FetchTask setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
        return this;
    }

    public String getCallbackHost() {
        return callbackHost;
    }

    public FetchTask setCallbackHost(String callbackHost) {
        this.callbackHost = callbackHost;
        return this;
    }

    public String getCallbackBodyType() {
        return callbackBodyType;
    }

    public FetchTask setCallbackBodyType(String callbackBodyType) {
        this.callbackBodyType = callbackBodyType;
        return this;
    }

    public String getCallbackBody() {
        return callbackBody;
    }

    public FetchTask setCallbackBody(String callbackBody) {
        this.callbackBody = callbackBody;
        return this;
    }

    public StorageClassType getStorageClass() {
        return storageClass;
    }

    public FetchTask setStorageClass(StorageClassType storageClass) {
        this.storageClass = storageClass;
        return this;
    }

    public ACLType getAcl() {
        return acl;
    }

    public FetchTask setAcl(ACLType acl) {
        this.acl = acl;
        return this;
    }

    public String getGrantFullControl() {
        return grantFullControl;
    }

    public FetchTask setGrantFullControl(String grantFullControl) {
        this.grantFullControl = grantFullControl;
        return this;
    }

    public String getGrantRead() {
        return grantRead;
    }

    public FetchTask setGrantRead(String grantRead) {
        this.grantRead = grantRead;
        return this;
    }

    public String getGrantReadAcp() {
        return grantReadAcp;
    }

    public FetchTask setGrantReadAcp(String grantReadAcp) {
        this.grantReadAcp = grantReadAcp;
        return this;
    }

    public String getGrantWriteAcp() {
        return grantWriteAcp;
    }

    public FetchTask setGrantWriteAcp(String grantWriteAcp) {
        this.grantWriteAcp = grantWriteAcp;
        return this;
    }

    public String getSsecAlgorithm() {
        return ssecAlgorithm;
    }

    public FetchTask setSsecAlgorithm(String ssecAlgorithm) {
        this.ssecAlgorithm = ssecAlgorithm;
        return this;
    }

    public String getSsecKey() {
        return ssecKey;
    }

    public FetchTask setSsecKey(String ssecKey) {
        this.ssecKey = ssecKey;
        return this;
    }

    public String getSsecKeyMD5() {
        return ssecKeyMD5;
    }

    public FetchTask setSsecKeyMD5(String ssecKeyMD5) {
        this.ssecKeyMD5 = ssecKeyMD5;
        return this;
    }

    public Map<String, String> getMeta() {
        return TosUtils.parseMeta(this.userMeta, disableEncodingMeta);
    }

    @Override
    public String toString() {
        return "FetchTask{" +
                "url='" + url + '\'' +
                ", ignoreSameKey=" + ignoreSameKey +
                ", contentMD5='" + contentMD5 + '\'' +
                ", bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", callbackUrl='" + callbackUrl + '\'' +
                ", callbackHost='" + callbackHost + '\'' +
                ", callbackBodyType='" + callbackBodyType + '\'' +
                ", callbackBody='" + callbackBody + '\'' +
                ", storageClass=" + storageClass +
                ", acl=" + acl +
                ", grantFullControl='" + grantFullControl + '\'' +
                ", grantRead='" + grantRead + '\'' +
                ", grantReadAcp='" + grantReadAcp + '\'' +
                ", grantWriteAcp='" + grantWriteAcp + '\'' +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKey='" + ssecKey + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                ", meta=" + getMeta() +
                '}';
    }
}

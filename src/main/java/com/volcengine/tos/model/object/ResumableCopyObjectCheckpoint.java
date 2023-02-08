package com.volcengine.tos.model.object;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ResumableCopyObjectCheckpoint {
    private String bucket;
    private String key;
    private String srcBucket;
    private String srcKey;
    private String srcVersionID;
    private long partSize;
    private String uploadID;

    private String copySourceIfMatch;
    private Date copySourceIfModifiedSince;
    private String copySourceIfNoneMatch;
    private Date copySourceIfUnModifiedSince;
    private String copySourceSSECAlgorithm;
    private String copySourceSSECKeyMD5;

    private String ssecAlgorithm;
    private String ssecKeyMD5;
    private String encodingType;

    private CopySourceObjectInfo copySourceObjectInfo;
    private List<CopyPartInfo> copyPartInfoList;

    public boolean isValid(CopySourceObjectInfo copySourceObjectInfo, String bucket, String objectKey,
                           String srcBucket, String srcKey, String srcVersionID) {
        if (StringUtils.isEmpty(this.uploadID) || !StringUtils.equals(this.bucket, bucket)
                || !StringUtils.equals(this.key, objectKey) || !StringUtils.equals(this.srcBucket, srcBucket)
                || !StringUtils.equals(this.srcKey, srcKey) || !StringUtils.equals(this.srcVersionID, srcVersionID)
                || copySourceObjectInfo == null || this.copySourceObjectInfo == null) {
            return false;
        }
        return this.copySourceObjectInfo.equals(copySourceObjectInfo);
    }

    public synchronized void writeToFile(String checkpointFile) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(checkpointFile)) {
            fos.write(TosUtils.getJsonMapper().writeValueAsBytes(this));
        } catch (JsonProcessingException e) {
            throw new TosClientException("tos: unable to do serialization", e);
        }
    }

    public String getBucket() {
        return bucket;
    }

    public ResumableCopyObjectCheckpoint setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public ResumableCopyObjectCheckpoint setKey(String key) {
        this.key = key;
        return this;
    }

    public String getSrcBucket() {
        return srcBucket;
    }

    public ResumableCopyObjectCheckpoint setSrcBucket(String srcBucket) {
        this.srcBucket = srcBucket;
        return this;
    }

    public String getSrcKey() {
        return srcKey;
    }

    public ResumableCopyObjectCheckpoint setSrcKey(String srcKey) {
        this.srcKey = srcKey;
        return this;
    }

    public String getSrcVersionID() {
        return srcVersionID;
    }

    public ResumableCopyObjectCheckpoint setSrcVersionID(String srcVersionID) {
        this.srcVersionID = srcVersionID;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public ResumableCopyObjectCheckpoint setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public ResumableCopyObjectCheckpoint setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getCopySourceIfMatch() {
        return copySourceIfMatch;
    }

    public ResumableCopyObjectCheckpoint setCopySourceIfMatch(String copySourceIfMatch) {
        this.copySourceIfMatch = copySourceIfMatch;
        return this;
    }

    public Date getCopySourceIfModifiedSince() {
        return copySourceIfModifiedSince;
    }

    public ResumableCopyObjectCheckpoint setCopySourceIfModifiedSince(Date copySourceIfModifiedSince) {
        this.copySourceIfModifiedSince = copySourceIfModifiedSince;
        return this;
    }

    public String getCopySourceIfNoneMatch() {
        return copySourceIfNoneMatch;
    }

    public ResumableCopyObjectCheckpoint setCopySourceIfNoneMatch(String copySourceIfNoneMatch) {
        this.copySourceIfNoneMatch = copySourceIfNoneMatch;
        return this;
    }

    public Date getCopySourceIfUnModifiedSince() {
        return copySourceIfUnModifiedSince;
    }

    public ResumableCopyObjectCheckpoint setCopySourceIfUnModifiedSince(Date copySourceIfUnModifiedSince) {
        this.copySourceIfUnModifiedSince = copySourceIfUnModifiedSince;
        return this;
    }

    public String getCopySourceSSECAlgorithm() {
        return copySourceSSECAlgorithm;
    }

    public ResumableCopyObjectCheckpoint setCopySourceSSECAlgorithm(String copySourceSSECAlgorithm) {
        this.copySourceSSECAlgorithm = copySourceSSECAlgorithm;
        return this;
    }

    public String getCopySourceSSECKeyMD5() {
        return copySourceSSECKeyMD5;
    }

    public ResumableCopyObjectCheckpoint setCopySourceSSECKeyMD5(String copySourceSSECKeyMD5) {
        this.copySourceSSECKeyMD5 = copySourceSSECKeyMD5;
        return this;
    }

    public String getSsecAlgorithm() {
        return ssecAlgorithm;
    }

    public ResumableCopyObjectCheckpoint setSsecAlgorithm(String ssecAlgorithm) {
        this.ssecAlgorithm = ssecAlgorithm;
        return this;
    }

    public String getSsecKeyMD5() {
        return ssecKeyMD5;
    }

    public ResumableCopyObjectCheckpoint setSsecKeyMD5(String ssecKeyMD5) {
        this.ssecKeyMD5 = ssecKeyMD5;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public ResumableCopyObjectCheckpoint setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    public CopySourceObjectInfo getCopySourceObjectInfo() {
        return copySourceObjectInfo;
    }

    public ResumableCopyObjectCheckpoint setCopySourceObjectInfo(CopySourceObjectInfo copySourceObjectInfo) {
        this.copySourceObjectInfo = copySourceObjectInfo;
        return this;
    }

    public List<CopyPartInfo> getCopyPartInfoList() {
        return copyPartInfoList;
    }

    public ResumableCopyObjectCheckpoint setCopyPartInfoList(List<CopyPartInfo> copyPartInfoList) {
        this.copyPartInfoList = copyPartInfoList;
        return this;
    }

    @Override
    public String toString() {
        return "ResumableCopyObjectCheckpoint{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", srcBucket='" + srcBucket + '\'' +
                ", srcKey='" + srcKey + '\'' +
                ", srcVersionID='" + srcVersionID + '\'' +
                ", partSize=" + partSize +
                ", uploadID='" + uploadID + '\'' +
                ", copySourceIfMatch='" + copySourceIfMatch + '\'' +
                ", copySourceIfModifiedSince=" + copySourceIfModifiedSince +
                ", copySourceIfNoneMatch='" + copySourceIfNoneMatch + '\'' +
                ", copySourceIfUnModifiedSince=" + copySourceIfUnModifiedSince +
                ", copySourceSSECAlgorithm='" + copySourceSSECAlgorithm + '\'' +
                ", copySourceSSECKeyMD5='" + copySourceSSECKeyMD5 + '\'' +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                ", encodingType='" + encodingType + '\'' +
                ", copySourceObjectInfo=" + copySourceObjectInfo +
                ", copyPartInfoList=" + copyPartInfoList +
                '}';
    }
}

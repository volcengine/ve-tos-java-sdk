package com.volcengine.tos.model.object;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class DownloadFileCheckpoint {
    private String bucket;
    private String key;
    private String versionID;
    private long partSize;
    private String ifMatch;
    private Date ifModifiedSince;
    private String ifNoneMatch;
    private Date ifUnModifiedSince;
    private String ssecAlgorithm;
    private String ssecKeyMD5;
    private DownloadObjectInfo downloadObjectInfo;
    private DownloadFileInfo downloadFileInfo;
    private List<DownloadPartInfo> downloadPartInfos;

    public boolean isValid(String bucket, String objectKey, String downloadFilePath, String etag) {
        if (!StringUtils.equals(this.bucket, bucket) ||
                !StringUtils.equals(this.key, objectKey)) {
            return false;
        }
        if (downloadFileInfo != null && !StringUtils.equals(downloadFileInfo.getFilePath(), downloadFilePath)) {
            return false;
        }
        return downloadObjectInfo == null || StringUtils.equals(downloadObjectInfo.getEtag(), etag);
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

    public DownloadFileCheckpoint setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public DownloadFileCheckpoint setKey(String key) {
        this.key = key;
        return this;
    }

    public String getVersionID() {
        return versionID;
    }

    public DownloadFileCheckpoint setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public DownloadFileCheckpoint setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public DownloadFileCheckpoint setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    public Date getIfModifiedSince() {
        return ifModifiedSince;
    }

    public DownloadFileCheckpoint setIfModifiedSince(Date ifModifiedSince) {
        this.ifModifiedSince = ifModifiedSince;
        return this;
    }

    public String getIfNoneMatch() {
        return ifNoneMatch;
    }

    public DownloadFileCheckpoint setIfNoneMatch(String ifNoneMatch) {
        this.ifNoneMatch = ifNoneMatch;
        return this;
    }

    public Date getIfUnModifiedSince() {
        return ifUnModifiedSince;
    }

    public DownloadFileCheckpoint setIfUnModifiedSince(Date ifUnModifiedSince) {
        this.ifUnModifiedSince = ifUnModifiedSince;
        return this;
    }

    public String getSsecAlgorithm() {
        return ssecAlgorithm;
    }

    public DownloadFileCheckpoint setSsecAlgorithm(String ssecAlgorithm) {
        this.ssecAlgorithm = ssecAlgorithm;
        return this;
    }

    public String getSsecKeyMD5() {
        return ssecKeyMD5;
    }

    public DownloadFileCheckpoint setSsecKeyMD5(String ssecKeyMD5) {
        this.ssecKeyMD5 = ssecKeyMD5;
        return this;
    }

    public DownloadObjectInfo getDownloadObjectInfo() {
        return downloadObjectInfo;
    }

    public DownloadFileCheckpoint setDownloadObjectInfo(DownloadObjectInfo downloadObjectInfo) {
        this.downloadObjectInfo = downloadObjectInfo;
        return this;
    }

    public DownloadFileInfo getDownloadFileInfo() {
        return downloadFileInfo;
    }

    public DownloadFileCheckpoint setDownloadFileInfo(DownloadFileInfo downloadFileInfo) {
        this.downloadFileInfo = downloadFileInfo;
        return this;
    }

    public List<DownloadPartInfo> getDownloadPartInfos() {
        return downloadPartInfos;
    }

    public DownloadFileCheckpoint setDownloadPartInfos(List<DownloadPartInfo> downloadPartInfos) {
        this.downloadPartInfos = downloadPartInfos;
        return this;
    }

    @Override
    public String toString() {
        return "DownloadFileCheckpoint{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", partSize=" + partSize +
                ", ifMatch='" + ifMatch + '\'' +
                ", ifModifiedSince=" + ifModifiedSince +
                ", ifNoneMatch='" + ifNoneMatch + '\'' +
                ", ifUnModifiedSince=" + ifUnModifiedSince +
                ", ssecAlgorithm='" + ssecAlgorithm + '\'' +
                ", ssecKeyMD5='" + ssecKeyMD5 + '\'' +
                ", downloadObjectInfo=" + downloadObjectInfo +
                ", downloadFileInfo=" + downloadFileInfo +
                ", downloadPartInfo=" + downloadPartInfos +
                '}';
    }
}

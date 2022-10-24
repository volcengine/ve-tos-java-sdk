package com.volcengine.tos.model.object;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class UploadFileV2Checkpoint {
    private String bucket;
    private String key;
    private String uploadID;
    private String sseAlgorithm;
    private String sseKeyMd5;
    private String contentType;
    /**
     * the file path to upload
     */
    private String filePath;
    private long lastModified;
    private long fileSize;
    private String encodingType;
    private List<UploadPartInfo> uploadPartInfos;

    public boolean isValid(long uploadFileSize, long uploadFileLastModifiedTime,
                           String bucket, String objectKey, String uploadFilePath) {
        if (StringUtils.isEmpty(this.uploadID) ||
                !StringUtils.equals(this.bucket, bucket) ||
                !StringUtils.equals(this.key, objectKey) ||
                !StringUtils.equals(this.filePath, uploadFilePath)) {
            return false;
        }
        return this.fileSize== uploadFileSize && this.lastModified == uploadFileLastModifiedTime;
    }

    public synchronized void writeToFile(String checkpointFile) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(checkpointFile)) {
            fos.write(TosUtils.JSON.writeValueAsBytes(this));
        } catch (JsonProcessingException e) {
            throw new TosClientException("tos: unable to do serialization", e);
        }
    }

    public String getBucket() {
        return bucket;
    }

    public UploadFileV2Checkpoint setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public UploadFileV2Checkpoint setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public UploadFileV2Checkpoint setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getSseAlgorithm() {
        return sseAlgorithm;
    }

    public UploadFileV2Checkpoint setSseAlgorithm(String sseAlgorithm) {
        this.sseAlgorithm = sseAlgorithm;
        return this;
    }

    public String getSseKeyMd5() {
        return sseKeyMd5;
    }

    public UploadFileV2Checkpoint setSseKeyMd5(String sseKeyMd5) {
        this.sseKeyMd5 = sseKeyMd5;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public UploadFileV2Checkpoint setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }

    public UploadFileV2Checkpoint setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public long getLastModified() {
        return lastModified;
    }

    public UploadFileV2Checkpoint setLastModified(long lastModified) {
        this.lastModified = lastModified;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public UploadFileV2Checkpoint setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public List<UploadPartInfo> getUploadPartInfos() {
        return uploadPartInfos;
    }

    public UploadFileV2Checkpoint setUploadPartInfos(List<UploadPartInfo> uploadPartInfos) {
        this.uploadPartInfos = uploadPartInfos;
        return this;
    }

    public String getEncodingType() {
        return encodingType;
    }

    public UploadFileV2Checkpoint setEncodingType(String encodingType) {
        this.encodingType = encodingType;
        return this;
    }

    @Override
    public String toString() {
        return "UploadFileV2Checkpoint{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", sseAlgorithm='" + sseAlgorithm + '\'' +
                ", sseKeyMd5='" + sseKeyMd5 + '\'' +
                ", contentType='" + contentType + '\'' +
                ", filePath='" + filePath + '\'' +
                ", lastModified=" + lastModified +
                ", fileSize=" + fileSize +
                ", encodingType='" + encodingType + '\'' +
                ", uploadPartInfos=" + uploadPartInfos +
                '}';
    }
}

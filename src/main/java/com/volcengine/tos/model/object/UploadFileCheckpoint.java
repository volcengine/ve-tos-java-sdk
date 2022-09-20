package com.volcengine.tos.model.object;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class UploadFileCheckpoint implements Serializable{
    private String bucket;
    private String key;
    private long partSize;
    private String uploadID;
    private String sseAlgorithm;
    private String sseKeyMd5;
    private UploadFileInfo fileInfo;
    private List<UploadFilePartInfo> uploadFilePartInfoList;

    public boolean isValid(long uploadFileSize, long uploadFileLastModifiedTime,
                           String bucket, String objectKey, String uploadFilePath) {
        if (StringUtils.isEmpty(this.uploadID) ||
                !StringUtils.equals(this.bucket, bucket) ||
                !StringUtils.equals(this.key, objectKey) ||
                !StringUtils.equals(this.fileInfo.getFilePath(), uploadFilePath)) {
            return false;
        }
        return this.fileInfo.getFileSize() == uploadFileSize &&
                this.fileInfo.getLastModified() == uploadFileLastModifiedTime;
    }

    public synchronized void writeToFile(String checkpointFile, ObjectMapper om) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(checkpointFile))
        {
            fos.write(om.writeValueAsBytes(this));
        }
    }

    public String getBucket() {
        return bucket;
    }

    public UploadFileCheckpoint setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public UploadFileCheckpoint setKey(String key) {
        this.key = key;
        return this;
    }

    public long getPartSize() {
        return partSize;
    }

    public UploadFileCheckpoint setPartSize(long partSize) {
        this.partSize = partSize;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public UploadFileCheckpoint setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getSseAlgorithm() {
        return sseAlgorithm;
    }

    public UploadFileCheckpoint setSseAlgorithm(String sseAlgorithm) {
        this.sseAlgorithm = sseAlgorithm;
        return this;
    }

    public String getSseKeyMd5() {
        return sseKeyMd5;
    }

    public UploadFileCheckpoint setSseKeyMd5(String sseKeyMd5) {
        this.sseKeyMd5 = sseKeyMd5;
        return this;
    }

    public UploadFileInfo getFileInfo() {
        return fileInfo;
    }

    public UploadFileCheckpoint setFileInfo(UploadFileInfo fileInfo) {
        this.fileInfo = fileInfo;
        return this;
    }

    public List<UploadFilePartInfo> getPartInfoList() {
        return uploadFilePartInfoList;
    }

    public UploadFileCheckpoint setPartInfoList(List<UploadFilePartInfo> uploadFilePartInfoList) {
        this.uploadFilePartInfoList = uploadFilePartInfoList;
        return this;
    }

    @Override
    public String toString() {
        return "UploadFileCheckpoint{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", partSize=" + partSize +
                ", uploadID='" + uploadID + '\'' +
                ", sseAlgorithm='" + sseAlgorithm + '\'' +
                ", sseKeyMd5='" + sseKeyMd5 + '\'' +
                ", fileInfo=" + fileInfo +
                ", uploadFilePartInfoList=" + uploadFilePartInfoList +
                '}';
    }
}


package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

public class CompleteMultipartUploadInput {
    public static class InnerCompleteMultipartUploadInput {
        @JsonProperty("Parts")
        private InnerUploadedPart[] parts;
        public InnerCompleteMultipartUploadInput(int length) {
            this.parts = new InnerUploadedPart[length];
        }
        public InnerUploadedPart[] getParts(){
            return parts;
        }

        public InnerCompleteMultipartUploadInput setParts(InnerUploadedPart[] parts) {
            this.parts = parts;
            return this;
        }

        public void setPartsByIdx(InnerUploadedPart part, int i){
            if (i >= this.parts.length) {
                return;
            }
            this.parts[i] = part;
        }
        @Override
        public String toString(){
            return "{["+parts[0]+" "+parts[1]+"]}";
        }
    }
    private String key;
    private String uploadID;
    private MultipartUploadedPart[] uploadedParts;

    public CompleteMultipartUploadInput(String key, String uploadID, MultipartUploadedPart[] uploadedParts) {
        this.key = key;
        this.uploadID = uploadID;
        this.uploadedParts = uploadedParts;
    }

    public int getUploadedPartsLength(){
        return this.uploadedParts.length;
    }

    public MultipartUploadedPart[] getUploadedParts() {
        return uploadedParts;
    }

    public String getKey() {
        return key;
    }

    public String getUploadID() {
        return uploadID;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setUploadID(String uploadID) {
        this.uploadID = uploadID;
    }

    public void setUploadedParts(MultipartUploadedPart[] uploadedParts) {
        this.uploadedParts = uploadedParts;
    }

    @Override
    public String toString() {
        return "CompleteMultipartUploadInput{" +
                "key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'' +
                ", uploadedParts=" + Arrays.toString(uploadedParts) +
                '}';
    }
}

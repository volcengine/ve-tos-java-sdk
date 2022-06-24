package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcengine.tos.TosClientException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CompleteMultipartUploadInput {
    private static class InnerCompleteMultipartUploadInput {
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
    private List<MultipartUploadedPart> multiUploadedParts = Collections.emptyList();

    public CompleteMultipartUploadInput() {
    }

    @Deprecated
    public CompleteMultipartUploadInput(String key, String uploadID, MultipartUploadedPart[] uploadedParts) {
        this.key = key;
        this.uploadID = uploadID;
        this.uploadedParts = uploadedParts;
    }

    public int getUploadedPartsLength(){
        if (this.uploadedParts == null || this.uploadedParts.length == 0) {
            return multiUploadedParts.size();
        }
        return this.uploadedParts.length;
    }

    public String getKey() {
        return key;
    }

    public String getUploadID() {
        return uploadID;
    }

    public CompleteMultipartUploadInput setKey(String key) {
        this.key = key;
        return this;
    }

    public CompleteMultipartUploadInput setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    @Deprecated
    public MultipartUploadedPart[] getUploadedParts() {
        return uploadedParts;
    }

    @Deprecated
    public void setUploadedParts(MultipartUploadedPart[] uploadedParts) {
        this.uploadedParts = uploadedParts;
    }

    public List<MultipartUploadedPart> getMultiUploadedParts() {
        return multiUploadedParts;
    }

    public CompleteMultipartUploadInput setMultiUploadedParts(List<MultipartUploadedPart> multiUploadedParts) {
        this.multiUploadedParts = multiUploadedParts;
        return this;
    }

    private InnerCompleteMultipartUploadInput setMultiPart(MultipartUploadedPart[] uploadedParts) {
        if (uploadedParts == null || uploadedParts.length == 0) {
            return null;
        }
        InnerCompleteMultipartUploadInput multipart = new InnerCompleteMultipartUploadInput(uploadedParts.length);
        for (int i = 0; i < uploadedParts.length; i++) {
            if (uploadedParts[i] != null) {
                multipart.setPartsByIdx(uploadedParts[i].uploadedPart(), i);
            } else {
                return null;
            }
        }
        Arrays.sort(multipart.getParts());
        return multipart;
    }

    private InnerCompleteMultipartUploadInput setMultiPart(List<MultipartUploadedPart> uploadedParts) {
        if (uploadedParts == null || uploadedParts.size() == 0) {
            return null;
        }
        InnerCompleteMultipartUploadInput multipart = new InnerCompleteMultipartUploadInput(uploadedParts.size());
        for (int i = 0; i < uploadedParts.size(); i++) {
            if (uploadedParts.get(i) != null) {
                multipart.setPartsByIdx(uploadedParts.get(i).uploadedPart(), i);
            } else {
                return null;
            }
        }
        Arrays.sort(multipart.getParts());
        return multipart;
    }

    public byte[] getUploadedPartData(ObjectMapper json) throws TosClientException {
        int partsNum = getUploadedPartsLength();
        if (partsNum == 0) {
            throw new TosClientException("tos: MultipartUploadedPart is null, please check your input first.", null);
        }
        InnerCompleteMultipartUploadInput multipart = null;
        if (getUploadedParts() != null && getUploadedParts().length != 0) {
            // input with array
            multipart = setMultiPart(getUploadedParts());
        } else if (getMultiUploadedParts().size() != 0) {
            // input with list
            multipart = setMultiPart(getMultiUploadedParts());
        }
        if (multipart == null) {
            throw new TosClientException("tos: can not get MultipartUploadedPart, please check your input first.", null);
        }
        byte[] data;
        try{
            data = json.writeValueAsBytes(multipart);
        } catch (JsonProcessingException jpe) {
            throw new TosClientException("tos: json parse exception", jpe);
        }
        return data;
    }

    @Override
    public String toString() {
        String base = "CompleteMultipartUploadInput{" +
                "key='" + key + '\'' +
                ", uploadID='" + uploadID + '\'';
        if (uploadedParts != null && uploadedParts.length != 0) {
            return base + ", uploadedParts=" + Arrays.toString(uploadedParts) + '}';
        } else {
            return base + ", uploadedParts=" + Arrays.toString(multiUploadedParts.toArray()) + '}';
        }
    }
}

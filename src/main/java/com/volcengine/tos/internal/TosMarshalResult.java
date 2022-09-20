package com.volcengine.tos.internal;

public class TosMarshalResult {
    private String contentMD5;
    private byte[] data;
    public TosMarshalResult(String contentMD5, byte[] data){
        this.contentMD5 = contentMD5;
        this.data = data;
    }

    public String getContentMD5() {
        return contentMD5;
    }

    public byte[] getData() {
        return data;
    }
}

package com.volcengine.tos;

import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

public class TosResponse implements AutoCloseable, Serializable {
    private int statusCode;
    private long contentLength;

    private Map<String, String> headers = Collections.emptyMap();
    @JsonIgnore
    private transient InputStream inputStream;

    public RequestInfo RequestInfo() {
        return new RequestInfo(this.getRequesID(), this.headers);
    }

    public String getRequesID() {
        return headers.get(TosHeader.HEADER_REQUEST_ID);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public TosResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public long getContentLength() {
        return contentLength;
    }

    public TosResponse setContentLength(long contentLength) {
        this.contentLength = contentLength;
        return this;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public long getInputStreamContentLength() throws IOException{
        return inputStream.available() == 0 ? -1 : inputStream.available();
    }
    public TosResponse setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        return this;
    }


    public Map<String, String> getHeaders() {
        return headers;
    }

    public TosResponse setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    @Override
    public String toString() {
        return "TosResponse{" +
                "statusCode=" + statusCode +
                ", contentLength=" + contentLength +
                ", headers=" + headers +
                '}';
    }
}

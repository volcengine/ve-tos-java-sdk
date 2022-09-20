package com.volcengine.tos.model;

import java.util.Map;

public class RequestInfo {
    private String requestId;
    private String id2;
    private int statusCode;
    private Map<String, String> header;

    @Deprecated
    public RequestInfo(String requestId, Map<String, String> header) {
        this.requestId = requestId;
        this.header = header;
    }

    public RequestInfo(String requestId, String id2, int statusCode, Map<String, String> header) {
        this.requestId = requestId;
        this.id2 = id2;
        this.statusCode = statusCode;
        this.header = header;
    }

    public String getRequestId() {
        return requestId;
    }

    @Deprecated
    public RequestInfo setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public String getId2() {
        return id2;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    @Deprecated
    public RequestInfo setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +
                "requestId='" + requestId + '\'' +
                ", id2='" + id2 + '\'' +
                ", statusCode=" + statusCode +
                ", header=" + header +
                '}';
    }
}

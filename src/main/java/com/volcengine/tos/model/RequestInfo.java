package com.volcengine.tos.model;

import java.util.Map;

public class RequestInfo {
    private String requestId;
    private Map<String, String> header;

    public RequestInfo(String requestId, Map<String, String> header) {
        this.requestId = requestId;
        this.header = header;
    }

    public String getRequestId() {
        return requestId;
    }

    public RequestInfo setRequestId(String requestId) {
        this.requestId = requestId;
        return this;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public RequestInfo setHeader(Map<String, String> header) {
        this.header = header;
        return this;
    }

    @Override
    public String toString() {
        return "RequestInfo{" +
                "requestId='" + requestId + '\'' +
                ", header=" + header +
                '}';
    }
}

package com.volcengine.tos.internal;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServerExceptionJson{
    private int statusCode;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Message")
    private String message;
    @JsonProperty("RequestId")
    private String requestID;
    @JsonProperty("HostId")
    private String hostID;

    public int getStatusCode() {
        return statusCode;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getRequestID() {
        return requestID;
    }

    public String getHostID() {
        return hostID;
    }
}


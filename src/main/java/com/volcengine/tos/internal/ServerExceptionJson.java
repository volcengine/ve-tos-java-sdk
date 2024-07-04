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

    @JsonProperty("EC")
    private String ec;
    @JsonProperty("Key")
    private String key;

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

    public String getEc() {
        return ec;
    }

    public String getKey() {
        return key;
    }
}


package com.volcengine.tos;

import java.io.Serializable;

public class TosServerException extends TosException implements Serializable {
    private int statusCode;
    private String code;
    private String message;
    private String requestID;
    private String hostID;

    public TosServerException(int statusCode) {
        this.statusCode = statusCode;
    }

    public TosServerException(int statusCode, String code, String message, String requestID, String hostID) {
        super();
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
        this.requestID = requestID;
        this.hostID = hostID;
    }

    public TosServerException setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public TosServerException setCode(String code) {
        this.code = code;
        return this;
    }

    public TosServerException setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getRequestID() {
        return requestID;
    }

    public TosServerException setRequestID(String requestID) {
        this.requestID = requestID;
        return this;
    }

    public String getHostID() {
        return hostID;
    }

    public TosServerException setHostID(String hostID) {
        this.hostID = hostID;
        return this;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "TosServerException{" +
                "statusCode=" + statusCode +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", requestID='" + requestID + '\'' +
                ", hostID='" + hostID + '\'' +
                '}';
    }
}

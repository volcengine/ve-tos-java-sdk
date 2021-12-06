package com.volcengine.tos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

class ServerExceptionJson{
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

class TosServerException extends TosException implements Serializable {

    private static final String UTF8 = "UTF-8";
    private static final int UNKNOWN_ERROR_CODE = -1;
    private static final ObjectMapper JSON = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    private int statusCode;
    private String code;
    private String message;
    private String requestID;
    private String hostID;

    TosServerException(int statusCode, String code, String message, String requestID, String hostID) {
        super();
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
        this.requestID = requestID;
        this.hostID = hostID;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String getCode() {
        return code;
    }

    static void checkException(TosResponse res, int okCode, int ...okCodes) throws TosServerException, UnexpectedStatusCodeException, IOException {
        if (res.getStatusCode() == okCode) {
            return;
        }
        for (int code : okCodes) {
            if (res.getStatusCode() == code) {
                return;
            }
        }
        if (res.getStatusCode() >= HttpStatus.SC_BAD_REQUEST){
            String s = IOUtils.toString(res.getInputStream(), StandardCharsets.UTF_8);
            if (s.length() > 0) {
                ServerExceptionJson se = JSON.readValue(s, new TypeReference<ServerExceptionJson>(){});
                throw new TosServerException(res.getStatusCode(), se.getCode(), se.getMessage(), se.getRequestID(), se.getHostID());
            }
        }
        throw new UnexpectedStatusCodeException(res.getStatusCode(), okCode, okCodes).withRequestID(res.getRequesID());
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

package com.volcengine.tos;

import java.util.ArrayList;
import java.util.List;

public class UnexpectedStatusCodeException extends TosException {
    private int statusCode;
    private List<Integer> expectedCodes = new ArrayList<>();
    private String requestID;

    public UnexpectedStatusCodeException(int statusCode, int expectedCode, String requestID){
        super();
        this.statusCode = statusCode;
        this.expectedCodes.add(expectedCode);
        this.requestID = requestID;
    }

    public UnexpectedStatusCodeException(int statusCode, List<Integer> expectedCodes, String requestID){
        super();
        this.statusCode = statusCode;
        this.expectedCodes.addAll(expectedCodes);
        this.requestID = requestID;
    }

    public UnexpectedStatusCodeException withRequestID(String requestID){
        this.requestID = requestID;
        return this;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    public List<Integer> getExpectedCodes() {
        return expectedCodes;
    }

    @Deprecated
    public String getRequestID() {
        return requestID;
    }

    @Override
    public String toString() {
        return "UnexpectedStatusCodeException{" +
                "statusCode=" + statusCode +
                ", expectedCodes=" + expectedCodes +
                ", requestID='" + requestID + '\'' +
                '}';
    }
}

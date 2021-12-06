package com.volcengine.tos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class UnexpectedStatusCodeException extends TosException{
    private int statusCode;
    private List<Integer> expectedCodes = new ArrayList<>();
    private String requestID;

    public UnexpectedStatusCodeException(int statusCode, int expectedCode, int ...expectedCodes){
        super();
        this.statusCode = statusCode;
        this.expectedCodes.add(expectedCode);
        this.expectedCodes.addAll(Arrays.stream(expectedCodes).boxed().collect(Collectors.toList()));
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

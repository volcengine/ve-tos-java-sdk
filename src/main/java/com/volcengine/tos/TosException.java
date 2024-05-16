package com.volcengine.tos;

public class TosException extends RuntimeException {

    private String requestUrl;

    public TosException() {
    }

    public TosException(Throwable cause) {
        super(cause);
    }

    public TosException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode(){
        return "";
    }

    public int getStatusCode(){
        return 0;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public TosException setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
        return this;
    }
}

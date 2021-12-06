package com.volcengine.tos;

public class TosClientException extends TosException{

    public TosClientException(String message, Exception cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "TosClientException{" +
                "cause=" + getCause() +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}

package com.volcengine.tos.util;

public class TosClientRuntimeException extends RuntimeException{
    public TosClientRuntimeException(String message, Exception cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        return "TosClientRuntimeException{" +
                "cause=" + getCause() +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}

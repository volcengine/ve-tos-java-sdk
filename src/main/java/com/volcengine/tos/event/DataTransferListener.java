package com.volcengine.tos.event;

public interface DataTransferListener {
    /**
     * dataTransferStatusChange is the callback function in data transfer progress
     * @param status
     */
    void dataTransferStatusChange(DataTransferStatus status);
}

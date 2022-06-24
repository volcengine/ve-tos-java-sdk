package com.volcengine.tos.event;

public enum DataTransferType {
    /**
     * Data transfer started.
     */
    DATA_TRANSFER_STARTED,
    /**
     * Data transfer read or write.
     */
    DATA_TRANSFER_RW,
    /**
     * Data transfer succeed.
     */
    DATA_TRANSFER_SUCCEED,
    /**
     * Data transfer failed.
     */
    DATA_TRANSFER_FAILED
}

package com.volcengine.tos.comm.event;

public enum UploadEventType {
    UploadEventCreateMultipartUploadSucceed,
    UploadEventCreateMultipartUploadFailed,
    UploadEventUploadPartSucceed,
    UploadEventUploadPartFailed,
    UploadEventUploadPartAborted,
    UploadEventCompleteMultipartUploadSucceed,
    UploadEventCompleteMultipartUploadFailed
}

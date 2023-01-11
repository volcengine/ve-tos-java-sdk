package com.volcengine.tos.comm.event;

public enum CopyEventType {
    CopyEventCreateMultipartUploadSucceed,
    CopyEventCreateMultipartUploadFailed,
    CopyEventUploadPartCopySucceed,
    CopyEventUploadPartCopyFailed,
    CopyEventUploadPartCopyAborted,
    CopyEventCompleteMultipartUploadSucceed,
    CopyEventCompleteMultipartUploadFailed
}

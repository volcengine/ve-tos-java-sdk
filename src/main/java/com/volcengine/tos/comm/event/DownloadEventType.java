package com.volcengine.tos.comm.event;

public enum DownloadEventType {
    DownloadEventCreateTempFileSucceed,
    DownloadEventCreateTempFileFailed,
    DownloadEventDownloadPartSucceed,
    DownloadEventDownloadPartFailed,
    DownloadEventDownloadPartAborted,
    DownloadEventRenameTempFileSucceed,
    DownloadEventRenameTempFileFailed;
}

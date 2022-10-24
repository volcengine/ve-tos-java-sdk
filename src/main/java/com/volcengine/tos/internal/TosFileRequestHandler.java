package com.volcengine.tos.internal;

import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.*;

public interface TosFileRequestHandler {
    GetObjectToFileOutput getObjectToFile(GetObjectToFileInput input) throws TosException;

    PutObjectFromFileOutput putObjectFromFile(PutObjectFromFileInput input) throws TosException;

    UploadPartFromFileOutput uploadPartFromFile(UploadPartFromFileInput input) throws TosException;

    UploadFileV2Output uploadFile(UploadFileV2Input input) throws TosException;

    DownloadFileOutput downloadFile(DownloadFileInput input) throws TosException;
}

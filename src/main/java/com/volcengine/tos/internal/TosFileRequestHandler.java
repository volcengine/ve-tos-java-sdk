package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.internal.model.CRC64Checksum;
import com.volcengine.tos.internal.taskman.DownloadFileTaskHandler;
import com.volcengine.tos.internal.taskman.UploadFileTaskHandler;
import com.volcengine.tos.internal.util.FileUtils;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.*;

import java.io.*;
import java.util.zip.CheckedInputStream;

public class TosFileRequestHandler {
    private RequestHandler fileHandler;
    private TosObjectRequestHandler objectHandler;
    private TosRequestFactory factory;
    private boolean enableCrcCheck;

    public TosFileRequestHandler(TosObjectRequestHandler objectHandler, Transport transport,
                                 TosRequestFactory factory) {
        this.fileHandler = new RequestHandler(transport);
        this.objectHandler = objectHandler;
        this.factory = factory;
    }

    public TosObjectRequestHandler getObjectHandler() {
        return objectHandler;
    }

    public TosFileRequestHandler setObjectHandler(TosObjectRequestHandler objectHandler) {
        this.objectHandler = objectHandler;
        return this;
    }

    public TosRequestFactory getFactory() {
        return factory;
    }

    public TosFileRequestHandler setFactory(TosRequestFactory factory) {
        this.factory = factory;
        return this;
    }

    public boolean isEnableCrcCheck() {
        return enableCrcCheck;
    }

    public TosFileRequestHandler setEnableCrcCheck(boolean enableCrcCheck) {
        this.enableCrcCheck = enableCrcCheck;
        return this;
    }

    public TosFileRequestHandler setTransport(Transport transport) {
        if (this.fileHandler == null) {
            this.fileHandler = new RequestHandler(transport);
        } else {
            this.fileHandler.setTransport(transport);
        }
        return this;
    }

    public Transport getTransport() {
        if (this.fileHandler != null) {
            return this.fileHandler.getTransport();
        }
        return null;
    }

    public GetObjectToFileOutput getObjectToFile(GetObjectToFileInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectToFileInput");
        GetObjectV2Output output = objectHandler.getObject(input.getGetObjectInputV2());
        String filePath = getFilePath(input);
        String newFilePath = FileUtils.parseFilePath(filePath, input.getGetObjectInputV2().getKey());
        if (StringUtils.isEmpty(newFilePath)) {
            // the key ends with "/", need to download a directory
            return null;
        }
        File srcFile = new File(newFilePath);
        File tmpFile = new File(newFilePath + Consts.TEMP_FILE_SUFFIX);
        if (output.getContent() != null) {
            try(FileOutputStream writer = new FileOutputStream(tmpFile);
                InputStream inputStream = this.enableCrcCheck ?
                        new CheckedInputStream(output.getContent(), new CRC64Checksum()) : output.getContent()) {
                int once = 0;
                byte[] buffer = new byte[4096];
                while ((once = inputStream.read(buffer)) > 0) {
                    writer.write(buffer, 0, once);
                }
            } catch (IOException e) {
                throw new TosClientException("tos: write data to local file failed", e);
            }
        } else {
            try{
                tmpFile.createNewFile();
            } catch (IOException e) {
                throw new TosClientException("tos: create new local file failed", e);
            }
        }
        if (!tmpFile.renameTo(srcFile)) {
            throw new TosClientException("tos: move temp file to dst file failed, src: " + tmpFile.getPath()
                    + ", dst: " + srcFile.getPath(), null);
        }
        return new GetObjectToFileOutput(output.getGetObjectBasicOutput());
    }

    private String getFilePath(GetObjectToFileInput input) {
        String filePath = input.getFilePath();
        if (StringUtils.isEmpty(filePath)) {
            File file = input.getFile();
            if (file == null) {
                throw new TosClientException("tos: file path is null", null);
            }
            filePath = file.getPath();
        }
        return filePath;
    }

    public PutObjectFromFileOutput putObjectFromFile(PutObjectFromFileInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectFromFileInput");
        InputStream content = FileUtils.getFileContent(input.getFileInputStream(), input.getFile(), input.getFilePath());
        PutObjectOutput putObjectOutput = objectHandler.putObject(PutObjectInput.builder()
                .putObjectBasicInput(input.getPutObjectBasicInput())
                .content(content)
                .build());
        return new PutObjectFromFileOutput(putObjectOutput);
    }

    public UploadPartFromFileOutput uploadPartFromFile(UploadPartFromFileInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "UploadPartFromFileInput");
        InputStream content = FileUtils.getBoundedFileContent(input.getFileInputStream(), input.getFile(),
                input.getFilePath(), input.getOffset(), input.getPartSize());
        UploadPartV2Output uploadPartV2Output = objectHandler.uploadPart(UploadPartV2Input.builder()
                .uploadPartBasicInput(input.getUploadPartBasicInput())
                .content(content)
                .contentLength(input.getPartSize())
                .build());
        return new UploadPartFromFileOutput(uploadPartV2Output);
    }

    public UploadFileV2Output uploadFile(UploadFileV2Input input) throws TosException {
        UploadFileTaskHandler handler = new UploadFileTaskHandler(input, this.objectHandler, this.enableCrcCheck);
        handler.initTask();
        handler.dispatch();
        return handler.handle();
    }

    public DownloadFileOutput downloadFile(DownloadFileInput input) throws TosException {
        DownloadFileTaskHandler handler = new DownloadFileTaskHandler(input, this.objectHandler, this.enableCrcCheck);
        handler.initTask();
        handler.dispatch();
        return handler.handle();
    }
}

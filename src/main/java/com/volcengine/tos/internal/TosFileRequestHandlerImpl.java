package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.internal.util.FileUtils;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.model.object.*;

import java.io.*;

public class TosFileRequestHandlerImpl implements TosFileRequestHandler {
    private RequestHandler fileHandler;
    private TosObjectRequestHandler objectHandler;
    private TosRequestFactory factory;

    public TosFileRequestHandlerImpl(TosObjectRequestHandler objectHandler, Transport transport,
                                     TosRequestFactory factory) {
        this.fileHandler = new RequestHandler(transport);
        this.objectHandler = objectHandler;
        this.factory = factory;
    }

    @Override
    public GetObjectToFileOutput getObjectToFile(GetObjectToFileInput input) throws TosException {
        ParamsChecker.isValidInput(input, "GetObjectToFileInput");
        GetObjectV2Output output = objectHandler.getObject(input.getGetObjectInputV2());
        if (output.getContent() != null) {
            File file = input.getFile();
            if (file == null) {
                file = new File(input.getFilePath());
            }
            try(FileOutputStream writer = new FileOutputStream(file)) {
                int once = 0;
                byte[] buffer = new byte[4096];
                InputStream inputStream = output.getContent();
                while ((once = inputStream.read(buffer)) > 0) {
                    writer.write(buffer, 0, once);
                }
            } catch (IOException e) {
                throw new TosClientException("write data to local file failed", e);
            }
        }
        return new GetObjectToFileOutput(output.getGetObjectBasicOutput());
    }

    @Override
    public PutObjectFromFileOutput putObjectFromFile(PutObjectFromFileInput input) throws TosException {
        ParamsChecker.isValidInput(input, "PutObjectFromFileInput");
        InputStream content = null;
        try{
            content = FileUtils.getFileContent(input.getFileInputStream(), input.getFile(), input.getFilePath());
        } catch (FileNotFoundException e) {
            throw new TosClientException("get file content failed", e);
        }
        PutObjectOutput putObjectOutput = objectHandler.putObject(PutObjectInput.builder()
                .putObjectBasicInput(input.getPutObjectBasicInput())
                .content(content)
                .build());
        return new PutObjectFromFileOutput(putObjectOutput);
    }

    @Override
    public UploadPartFromFileOutput uploadPartFromFile(UploadPartFromFileInput input) throws TosException {
        ParamsChecker.isValidInput(input, "UploadPartFromFileInput");
        InputStream content = null;
        try{
            content = FileUtils.getBoundedFileContent(input.getFileInputStream(), input.getFile(),
                    input.getFilePath(), input.getOffset(), input.getPartSize());
        } catch (IOException e) {
            throw new TosClientException("get file content failed", e);
        }
        UploadPartV2Output uploadPartV2Output = objectHandler.uploadPart(UploadPartV2Input.builder()
                .uploadPartBasicInput(input.getUploadPartBasicInput())
                .content(content)
                .contentLength(input.getPartSize())
                .build());
        return new UploadPartFromFileOutput(uploadPartV2Output);
    }
}

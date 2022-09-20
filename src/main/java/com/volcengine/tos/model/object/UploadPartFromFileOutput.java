package com.volcengine.tos.model.object;

public class UploadPartFromFileOutput {
    private UploadPartV2Output uploadPartV2Output;

    public UploadPartFromFileOutput(UploadPartV2Output uploadPartV2Output) {
        this.uploadPartV2Output = uploadPartV2Output;
    }

    public UploadPartV2Output getUploadPartV2Output() {
        return uploadPartV2Output;
    }

    @Override
    public String toString() {
        return "UploadPartFromFileOutput{" +
                "uploadPartV2Output=" + uploadPartV2Output +
                '}';
    }
}

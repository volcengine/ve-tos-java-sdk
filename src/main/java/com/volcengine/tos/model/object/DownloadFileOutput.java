package com.volcengine.tos.model.object;

public class DownloadFileOutput {
    private HeadObjectV2Output output;

    public HeadObjectV2Output getOutput() {
        return output;
    }

    public DownloadFileOutput setOutput(HeadObjectV2Output output) {
        this.output = output;
        return this;
    }

    @Override
    public String toString() {
        return "DownloadFileOutput{" +
                "output=" + output +
                '}';
    }
}

package com.volcengine.tos.model.object;

public class PutObjectFromFileOutput {
    private PutObjectOutput putObjectOutput;

    public PutObjectFromFileOutput() {
    }

    public PutObjectFromFileOutput(PutObjectOutput putObjectOutput) {
        this.putObjectOutput = putObjectOutput;
    }

    public PutObjectOutput getPutObjectOutput() {
        return putObjectOutput;
    }

    public PutObjectFromFileOutput setPutObjectOutput(PutObjectOutput putObjectOutput) {
        this.putObjectOutput = putObjectOutput;
        return this;
    }

    @Override
    public String toString() {
        return "PutObjectFromFileOutput{" +
                "putObjectOutput=" + putObjectOutput +
                '}';
    }
}

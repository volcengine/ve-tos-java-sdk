package com.volcengine.tos.model.object;

public class HeadObjectV2Output {
    private GetObjectBasicOutput headObjectBasicOutput;

    public HeadObjectV2Output(GetObjectBasicOutput getObjectBasicOutput) {
        this.headObjectBasicOutput = getObjectBasicOutput;
    }

    public GetObjectBasicOutput getHeadObjectBasicOutput() {
        return headObjectBasicOutput;
    }

    public HeadObjectV2Output setHeadObjectBasicOutput(GetObjectBasicOutput headObjectBasicOutput) {
        this.headObjectBasicOutput = headObjectBasicOutput;
        return this;
    }

    @Override
    public String toString() {
        return "HeadObjectOutputV2{" +
                "headObjectBasicOutput=" + headObjectBasicOutput +
                '}';
    }
}

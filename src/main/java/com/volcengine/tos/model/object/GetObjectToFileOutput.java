package com.volcengine.tos.model.object;

public class GetObjectToFileOutput {
    private GetObjectBasicOutput getObjectBasicOutput;

    public GetObjectToFileOutput(GetObjectBasicOutput getObjectBasicOutput) {
        this.getObjectBasicOutput = getObjectBasicOutput;
    }

    public GetObjectBasicOutput getGetObjectBasicOutput() {
        return getObjectBasicOutput;
    }

    public GetObjectToFileOutput setGetObjectBasicOutput(GetObjectBasicOutput getObjectBasicOutput) {
        this.getObjectBasicOutput = getObjectBasicOutput;
        return this;
    }

    @Override
    public String toString() {
        return "GetObjectToFileOutput{" +
                "getObjectBasicOutput=" + getObjectBasicOutput +
                '}';
    }
}

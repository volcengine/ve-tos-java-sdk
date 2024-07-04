package com.volcengine.tos.model.object;

import com.volcengine.tos.model.GenericInput;

public class GetFetchTaskInput extends GenericInput {
    private String bucket;
    private String taskId;

    public String getBucket() {
        return bucket;
    }

    public GetFetchTaskInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public GetFetchTaskInput setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    @Override
    public String toString() {
        return "GetFetchTaskInput{" +
                "bucket='" + bucket + '\'' +
                ", taskId='" + taskId + '\'' +
                '}';
    }
}

package com.volcengine.tos.model.bucket;

public class ListBucketsV2Input {
    private String projectName;

    public String getProjectName() {
        return projectName;
    }

    public ListBucketsV2Input setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }
}

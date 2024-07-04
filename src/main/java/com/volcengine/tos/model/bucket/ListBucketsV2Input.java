package com.volcengine.tos.model.bucket;

import com.volcengine.tos.comm.common.BucketType;
import com.volcengine.tos.model.GenericInput;

public class ListBucketsV2Input extends GenericInput {
    private String projectName;
    private BucketType bucketType;

    public String getProjectName() {
        return projectName;
    }

    public ListBucketsV2Input setProjectName(String projectName) {
        this.projectName = projectName;
        return this;
    }

    public BucketType getBucketType() {
        return bucketType;
    }

    public ListBucketsV2Input setBucketType(BucketType bucketType) {
        this.bucketType = bucketType;
        return this;
    }
}

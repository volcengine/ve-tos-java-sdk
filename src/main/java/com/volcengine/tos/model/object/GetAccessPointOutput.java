package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.RequestInfo;

public class GetAccessPointOutput extends AccessPoint {

    @JsonIgnore
    private RequestInfo requestInfo;

    public GetAccessPointOutput() {
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetAccessPointOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    @Override
    public String toString() {
        return "GetAccessPointOutput{" +
                "requestInfo=" + requestInfo +
                ", name='" + getName() + '\'' +
                ", alias='" + getAlias() + '\'' +
                ", bucket='" + getBucket() + '\'' +
                ", bucketAccountId='" + getBucketAccountId() + '\'' +
                ", accessPointType='" + getAccessPointType() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", networkOrigin='" + getNetworkOrigin() + '\'' +
                ", vpcId='" + getVpcId() + '\'' +
                ", accessPointTrn='" + getAccessPointTrn() + '\'' +
                ", creationDate='" + getCreationDate() + '\'' +
                ", endpoints=" + getEndpoints() +
                '}';
    }
}

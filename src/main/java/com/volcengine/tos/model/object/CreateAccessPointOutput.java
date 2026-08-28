package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

public class CreateAccessPointOutput {

    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("AccessPointTrn")
    private String accessPointTrn;

    @JsonProperty("Alias")
    private String alias;

    public CreateAccessPointOutput() {
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public CreateAccessPointOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public String getAccessPointTrn() {
        return accessPointTrn;
    }

    public CreateAccessPointOutput setAccessPointTrn(String accessPointTrn) {
        this.accessPointTrn = accessPointTrn;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public CreateAccessPointOutput setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    @Override
    public String toString() {
        return "CreateAccessPointOutput{" +
                "requestInfo=" + requestInfo +
                ", accessPointTrn='" + accessPointTrn + '\'' +
                ", alias='" + alias + '\'' +
                '}';
    }
}

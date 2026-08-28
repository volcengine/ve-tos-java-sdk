package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class ListBindAcceleratorForAccessPointOutput {

    @JsonIgnore
    private RequestInfo requestInfo;

    @JsonProperty("Accelerators")
    private List<Accelerator> accelerators;

    public ListBindAcceleratorForAccessPointOutput() {
    }

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public ListBindAcceleratorForAccessPointOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<Accelerator> getAccelerators() {
        return accelerators;
    }

    public ListBindAcceleratorForAccessPointOutput setAccelerators(List<Accelerator> accelerators) {
        this.accelerators = accelerators;
        return this;
    }

    @Override
    public String toString() {
        return "ListBindAcceleratorForAccessPointOutput{" +
                "requestInfo=" + requestInfo +
                ", accelerators=" + accelerators +
                '}';
    }
}

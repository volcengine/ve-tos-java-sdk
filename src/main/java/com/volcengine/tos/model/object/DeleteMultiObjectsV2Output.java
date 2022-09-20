package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class DeleteMultiObjectsV2Output {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Deleted")
    private List<Deleted> deleteds;
    @JsonProperty("Error")
    private List<DeleteError> errors;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteMultiObjectsV2Output requestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<Deleted> getDeleteds() {
        return deleteds;
    }

    public DeleteMultiObjectsV2Output deleteds(List<Deleted> deleteds) {
        this.deleteds = deleteds;
        return this;
    }

    public List<DeleteError> getErrors() {
        return errors;
    }

    public DeleteMultiObjectsV2Output errors(List<DeleteError> errors) {
        this.errors = errors;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteMultiObjectsV2Output{" +
                "requestInfo=" + requestInfo +
                ", deleteds=" + deleteds +
                ", errors=" + errors +
                '}';
    }
}

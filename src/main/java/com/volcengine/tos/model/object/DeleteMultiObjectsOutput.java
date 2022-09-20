package com.volcengine.tos.model.object;

import com.volcengine.tos.model.RequestInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@Deprecated
public class DeleteMultiObjectsOutput {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Deleted")
    private Deleted[] deleteds;
    @JsonProperty("Error")
    private DeleteError[] errors;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public DeleteMultiObjectsOutput setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public Deleted[] getDeleteds() {
        return deleteds;
    }

    public DeleteMultiObjectsOutput setDeleteds(Deleted[] deleteds) {
        this.deleteds = deleteds;
        return this;
    }

    public DeleteError[] getErrors() {
        return errors;
    }

    public DeleteMultiObjectsOutput setErrors(DeleteError[] errors) {
        this.errors = errors;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteMultiObjectsOutput{" +
                "requestInfo=" + requestInfo +
                ", deleteds=" + Arrays.toString(deleteds) +
                ", errors=" + Arrays.toString(errors) +
                '}';
    }
}

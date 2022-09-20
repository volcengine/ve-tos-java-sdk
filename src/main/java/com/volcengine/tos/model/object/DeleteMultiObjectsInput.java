package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Arrays;

@Deprecated
public class DeleteMultiObjectsInput {
    @JsonProperty("Quiet")
    private boolean quiet;
    @JsonProperty("Objects")
    private ObjectTobeDeleted[] objectTobeDeleteds;

    public DeleteMultiObjectsInput() {
    }

    public DeleteMultiObjectsInput(ObjectTobeDeleted[] objectTobeDeleteds, boolean quiet) {
        this.objectTobeDeleteds = objectTobeDeleteds;
        this.quiet = quiet;
    }

    public ObjectTobeDeleted[] getObjectTobeDeleteds() {
        return objectTobeDeleteds;
    }

    public DeleteMultiObjectsInput setObjectTobeDeleteds(ObjectTobeDeleted[] objectTobeDeleteds) {
        this.objectTobeDeleteds = objectTobeDeleteds;
        return this;
    }

    public boolean isQuiet() {
        return quiet;
    }

    public DeleteMultiObjectsInput setQuiet(boolean quiet) {
        this.quiet = quiet;
        return this;
    }

    @Override
    public String toString() {
        return "DeleteMultiObjectsInput{" +
                "quiet=" + quiet +
                ", objectTobeDeleteds=" + Arrays.toString(objectTobeDeleteds) +
                '}';
    }

}

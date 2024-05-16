package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.StatusType;

public class LifecycleRuleFilter {
    @JsonProperty("ObjectSizeGreaterThan")
    private int objectSizeGreaterThan;
    @JsonProperty("GreaterThanIncludeEqual")
    private StatusType GreaterThanIncludeEqual;
    @JsonProperty("ObjectSizeLessThan")
    private int objectSizeLessThan;
    @JsonProperty("LessThanIncludeEqual")
    private StatusType LessThanIncludeEqual;

    public int getObjectSizeGreaterThan() {
        return objectSizeGreaterThan;
    }

    public LifecycleRuleFilter setObjectSizeGreaterThan(int objectSizeGreaterThan) {
        this.objectSizeGreaterThan = objectSizeGreaterThan;
        return this;
    }

    public StatusType getGreaterThanIncludeEqual() {
        return GreaterThanIncludeEqual;
    }

    public LifecycleRuleFilter setGreaterThanIncludeEqual(StatusType greaterThanIncludeEqual) {
        GreaterThanIncludeEqual = greaterThanIncludeEqual;
        return this;
    }

    public int getObjectSizeLessThan() {
        return objectSizeLessThan;
    }

    public LifecycleRuleFilter setObjectSizeLessThan(int objectSizeLessThan) {
        this.objectSizeLessThan = objectSizeLessThan;
        return this;
    }

    public StatusType getLessThanIncludeEqual() {
        return LessThanIncludeEqual;
    }

    public LifecycleRuleFilter setLessThanIncludeEqual(StatusType lessThanIncludeEqual) {
        LessThanIncludeEqual = lessThanIncludeEqual;
        return this;
    }
}

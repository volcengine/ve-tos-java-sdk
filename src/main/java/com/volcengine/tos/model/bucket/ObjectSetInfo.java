package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.object.TagSet;

public class ObjectSetInfo {
    @JsonProperty("ObjectSetName")
    private String objectSetName;

    @JsonProperty("TagSet")
    private TagSet tagSet;

    public String getObjectSetName() {
        return objectSetName;
    }

    public ObjectSetInfo setObjectSetName(String objectSetName) {
        this.objectSetName = objectSetName;
        return this;
    }

    public TagSet getTagSet() {
        return tagSet;
    }

    public ObjectSetInfo setTagSet(TagSet tagSet) {
        this.tagSet = tagSet;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectSetInfo{" +
                "objectSetName='" + objectSetName + '\'' +
                ", tagSet=" + tagSet +
                '}';
    }
}

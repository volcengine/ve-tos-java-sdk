package com.volcengine.tos.model.acl;

import com.fasterxml.jackson.annotation.JsonProperty;

@Deprecated
public class Grantee {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DisplayName")
    private String displayName;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("Canned")
    private String uri;

    public String getId() {
        return id;
    }

    public Grantee setId(String id) {
        this.id = id;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Grantee setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getType() {
        return type;
    }

    public Grantee setType(String type) {
        this.type = type;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public Grantee setUri(String uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public String toString() {
        return "Grantee{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", type='" + type + '\'' +
                ", uri='" + uri + '\'' +
                '}';
    }
}

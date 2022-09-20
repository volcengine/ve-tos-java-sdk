package com.volcengine.tos.model.acl;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Owner {
    @JsonProperty("ID")
    private String id;
    @JsonProperty("DisplayName")
    private String displayName;

    public String getId() {
        return id;
    }

    public Owner setId(String id) {
        this.id = id;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Owner setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                '}';
    }

    public static OwnerBuilder builder() {
        return new OwnerBuilder();
    }

    public static final class OwnerBuilder {
        private String id;
        private String displayName;

        private OwnerBuilder() {
        }

        public OwnerBuilder id(String id) {
            this.id = id;
            return this;
        }

        public OwnerBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Owner build() {
            Owner owner = new Owner();
            owner.setId(id);
            owner.setDisplayName(displayName);
            return owner;
        }
    }
}

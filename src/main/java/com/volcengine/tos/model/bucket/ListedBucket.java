package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
public class ListedBucket implements Serializable {
    @JsonProperty("CreationDate")
    private String creationDate;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("Location")
    private String location;
    @JsonProperty("ExtranetEndpoint")
    private String extranetEndpoint;
    @JsonProperty("IntranetEndpoint")
    private String intranetEndpoint;

    public String getCreationDate() {
        return creationDate;
    }

    public ListedBucket setCreationDate(String creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public String getName() {
        return name;
    }

    public ListedBucket setName(String name) {
        this.name = name;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public ListedBucket setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getExtranetEndpoint() {
        return extranetEndpoint;
    }

    public ListedBucket setExtranetEndpoint(String extranetEndpoint) {
        this.extranetEndpoint = extranetEndpoint;
        return this;
    }

    public String getIntranetEndpoint() {
        return intranetEndpoint;
    }

    public ListedBucket setIntranetEndpoint(String intranetEndpoint) {
        this.intranetEndpoint = intranetEndpoint;
        return this;
    }

    @Override
    public String toString() {
        return "ListedBucket{" +
                "creationDate='" + creationDate + '\'' +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", extranetEndpoint='" + extranetEndpoint + '\'' +
                ", intranetEndpoint='" + intranetEndpoint + '\'' +
                '}';
    }
}

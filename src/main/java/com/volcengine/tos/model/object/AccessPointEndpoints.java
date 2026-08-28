package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessPointEndpoints {

    @JsonProperty("IntranetEndpoint")
    private String intranetEndpoint;

    @JsonProperty("ExtranetEndpoint")
    private String extranetEndpoint;

    public AccessPointEndpoints() {
    }

    public String getIntranetEndpoint() {
        return intranetEndpoint;
    }

    public AccessPointEndpoints setIntranetEndpoint(String intranetEndpoint) {
        this.intranetEndpoint = intranetEndpoint;
        return this;
    }

    public String getExtranetEndpoint() {
        return extranetEndpoint;
    }

    public AccessPointEndpoints setExtranetEndpoint(String extranetEndpoint) {
        this.extranetEndpoint = extranetEndpoint;
        return this;
    }

    @Override
    public String toString() {
        return "AccessPointEndpoints{" +
                "intranetEndpoint='" + intranetEndpoint + '\'' +
                ", extranetEndpoint='" + extranetEndpoint + '\'' +
                '}';
    }
}

package com.volcengine.tos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class GenericInput {
    @JsonIgnore
    protected Date requestDate;
    @JsonIgnore
    protected String requestHost;

    public Date getRequestDate() {
        return requestDate;
    }

    public GenericInput setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
        return this;
    }

    public String getRequestHost() {
        return requestHost;
    }

    public GenericInput setRequestHost(String requestHost) {
        this.requestHost = requestHost;
        return this;
    }
}

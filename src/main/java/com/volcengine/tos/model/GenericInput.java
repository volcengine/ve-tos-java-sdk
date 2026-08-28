package com.volcengine.tos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.internal.util.StringUtils;

import java.util.*;

public class GenericInput {
    @JsonIgnore
    protected Date requestDate;
    @JsonIgnore
    protected String requestHost;

    private Map<String, String> requestHeaders;
    private Map<String, String> requestQuery;
    private static final Set<String> DISALLOWED_HEADERS = new HashSet<>(Arrays.asList(
            "content-length", "host", "connection", "x-tos-date",
            "range", "transfer-encoding", "authorization", "date"
    ));

    public GenericInput setRequestHeader(String key, String value) {
        if (DISALLOWED_HEADERS.contains(key.toLowerCase())) {
            return this;
        }

        if (this.requestHeaders == null) {
            this.requestHeaders = new HashMap<>();
        }

        if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(key)) {
            requestHeaders.put(key.toLowerCase(), value);
        } else {
            throw new IllegalArgumentException("Invalid header key or value");
        }
        return this;
    }

    public Map<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public GenericInput setRequestQuery(String key, String value) {
        if (this.requestQuery == null) {
            this.requestQuery = new HashMap<>();
        }

        if (StringUtils.isNotEmpty(value) && StringUtils.isNotEmpty(key)) {
            requestQuery.put(key, value);
        } else {
            throw new IllegalArgumentException("Invalid header key or value");
        }
        return this;

    }

    public Map<String, String> getRequestQuery() {
        return requestQuery;
    }

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

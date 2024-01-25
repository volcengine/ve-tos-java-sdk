package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.RequestInfo;

import java.util.List;

public class GetBucketNotificationType2Output {
    @JsonIgnore
    private RequestInfo requestInfo;
    @JsonProperty("Rules")
    private List<NotificationRule> rules;
    @JsonProperty("Version")
    private String version;

    public RequestInfo getRequestInfo() {
        return requestInfo;
    }

    public GetBucketNotificationType2Output setRequestInfo(RequestInfo requestInfo) {
        this.requestInfo = requestInfo;
        return this;
    }

    public List<NotificationRule> getRules() {
        return rules;
    }

    public GetBucketNotificationType2Output setRules(List<NotificationRule> rules) {
        this.rules = rules;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public GetBucketNotificationType2Output setVersion(String version) {
        this.version = version;
        return this;
    }

    @Override
    public String toString() {
        return "GetBucketNotificationType2Output{" +
                "requestInfo=" + requestInfo +
                ", rules=" + rules +
                ", version='" + version + '\'' +
                '}';
    }
}

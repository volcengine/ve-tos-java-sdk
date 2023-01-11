package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CloudFunctionConfiguration {
    @JsonProperty("RuleId")
    private String id;
    @JsonProperty("Events")
    private List<String> events;
    @JsonProperty("Filter")
    private Filter filter;
    @JsonProperty("CloudFunction")
    private String cloudFunction;

    public String getId() {
        return id;
    }

    public CloudFunctionConfiguration setId(String id) {
        this.id = id;
        return this;
    }

    public List<String> getEvents() {
        return events;
    }

    public CloudFunctionConfiguration setEvents(List<String> events) {
        this.events = events;
        return this;
    }

    public Filter getFilter() {
        return filter;
    }

    public CloudFunctionConfiguration setFilter(Filter filter) {
        this.filter = filter;
        return this;
    }

    public String getCloudFunction() {
        return cloudFunction;
    }

    public CloudFunctionConfiguration setCloudFunction(String cloudFunction) {
        this.cloudFunction = cloudFunction;
        return this;
    }

    @Override
    public String toString() {
        return "CloudFunctionConfiguration{" +
                "id='" + id + '\'' +
                ", events=" + events +
                ", filter=" + filter +
                ", cloudFunction='" + cloudFunction + '\'' +
                '}';
    }

    public static CloudFunctionConfigurationBuilder builder() {
        return new CloudFunctionConfigurationBuilder();
    }

    public static final class CloudFunctionConfigurationBuilder {
        private String id;
        private List<String> events;
        private Filter filter;
        private String cloudFunction;

        private CloudFunctionConfigurationBuilder() {
        }

        public CloudFunctionConfigurationBuilder id(String id) {
            this.id = id;
            return this;
        }

        public CloudFunctionConfigurationBuilder events(List<String> events) {
            this.events = events;
            return this;
        }

        public CloudFunctionConfigurationBuilder filter(Filter filter) {
            this.filter = filter;
            return this;
        }

        public CloudFunctionConfigurationBuilder cloudFunction(String cloudFunction) {
            this.cloudFunction = cloudFunction;
            return this;
        }

        public CloudFunctionConfiguration build() {
            CloudFunctionConfiguration cloudFunctionConfiguration = new CloudFunctionConfiguration();
            cloudFunctionConfiguration.setId(id);
            cloudFunctionConfiguration.setEvents(events);
            cloudFunctionConfiguration.setFilter(filter);
            cloudFunctionConfiguration.setCloudFunction(cloudFunction);
            return cloudFunctionConfiguration;
        }
    }
}

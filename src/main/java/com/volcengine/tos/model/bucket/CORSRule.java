package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CORSRule {
    @JsonProperty("AllowedOrigins")
    private List<String> allowedOrigins;
    @JsonProperty("AllowedMethods")
    private List<String> allowedMethods;
    @JsonProperty("AllowedHeaders")
    private List<String> allowedHeaders;
    @JsonProperty("ExposeHeaders")
    private List<String> exposeHeaders;
    @JsonProperty("MaxAgeSeconds")
    private int maxAgeSeconds;
    @JsonProperty("ResponseVary")
    private boolean responseVary;

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public CORSRule setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
        return this;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public CORSRule setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
        return this;
    }

    public List<String> getAllowedHeaders() {
        return allowedHeaders;
    }

    public CORSRule setAllowedHeaders(List<String> allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
        return this;
    }

    public List<String> getExposeHeaders() {
        return exposeHeaders;
    }

    public CORSRule setExposeHeaders(List<String> exposeHeaders) {
        this.exposeHeaders = exposeHeaders;
        return this;
    }

    public int getMaxAgeSeconds() {
        return maxAgeSeconds;
    }

    public CORSRule setMaxAgeSeconds(int maxAgeSeconds) {
        this.maxAgeSeconds = maxAgeSeconds;
        return this;
    }

    public boolean isResponseVary() {
        return responseVary;
    }

    public CORSRule setResponseVary(boolean responseVary) {
        this.responseVary = responseVary;
        return this;
    }

    @Override
    public String toString() {
        return "CORSRule{" +
                "allowedOrigins=" + allowedOrigins +
                ", allowedMethods=" + allowedMethods +
                ", allowedHeaders=" + allowedHeaders +
                ", exposeHeaders=" + exposeHeaders +
                ", maxAgeSeconds=" + maxAgeSeconds +
                ", responseVary=" + responseVary +
                '}';
    }

    public static CORSRuleBuilder builder() {
        return new CORSRuleBuilder();
    }

    public static final class CORSRuleBuilder {
        private List<String> allowedOrigins;
        private List<String> allowedMethods;
        private List<String> allowedHeaders;
        private List<String> exposeHeaders;
        private int maxAgeSeconds;
        private boolean responseVary;

        private CORSRuleBuilder() {
        }

        public CORSRuleBuilder allowedOrigins(List<String> allowedOrigins) {
            this.allowedOrigins = allowedOrigins;
            return this;
        }

        public CORSRuleBuilder allowedMethods(List<String> allowedMethods) {
            this.allowedMethods = allowedMethods;
            return this;
        }

        public CORSRuleBuilder allowedHeaders(List<String> allowedHeaders) {
            this.allowedHeaders = allowedHeaders;
            return this;
        }

        public CORSRuleBuilder exposeHeaders(List<String> exposeHeaders) {
            this.exposeHeaders = exposeHeaders;
            return this;
        }

        public CORSRuleBuilder maxAgeSeconds(int maxAgeSeconds) {
            this.maxAgeSeconds = maxAgeSeconds;
            return this;
        }

        public CORSRuleBuilder responseVary(boolean responseVary) {
            this.responseVary = responseVary;
            return this;
        }

        public CORSRule build() {
            CORSRule cORSRule = new CORSRule();
            cORSRule.setAllowedOrigins(allowedOrigins);
            cORSRule.setAllowedMethods(allowedMethods);
            cORSRule.setAllowedHeaders(allowedHeaders);
            cORSRule.setExposeHeaders(exposeHeaders);
            cORSRule.setMaxAgeSeconds(maxAgeSeconds);
            cORSRule.setResponseVary(responseVary);
            return cORSRule;
        }
    }
}

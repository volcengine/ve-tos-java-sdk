package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RestoreJobParameters {
    @JsonProperty("Tier")
    private String tier;

    public String getTier() {
        return tier;
    }

    public RestoreJobParameters setTier(String tier) {
        this.tier = tier;
        return this;
    }

    @Override
    public String toString() {
        return "RestoreJobParameters{" +
                "tier='" + tier + '\'' +
                '}';
    }

    public static RestoreJobParametersBuilder builder() {
        return new RestoreJobParametersBuilder();
    }

    public static final class RestoreJobParametersBuilder {
        private String tier;

        private RestoreJobParametersBuilder() {
        }

        public RestoreJobParametersBuilder tier(String tier) {
            this.tier = tier;
            return this;
        }

        public RestoreJobParameters build() {
            RestoreJobParameters restoreJobParameters = new RestoreJobParameters();
            restoreJobParameters.setTier(tier);
            return restoreJobParameters;
        }
    }
}

package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.comm.common.TierType;

public class RestoreJobParameters {
    @JsonProperty("Tier")
    private TierType tier;

    public TierType getTier() {
        return tier;
    }

    public RestoreJobParameters setTier(TierType tier) {
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
        private TierType tier;

        private RestoreJobParametersBuilder() {
        }

        public RestoreJobParametersBuilder tier(TierType tier) {
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

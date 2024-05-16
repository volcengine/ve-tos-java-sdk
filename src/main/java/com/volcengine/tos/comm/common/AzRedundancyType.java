package com.volcengine.tos.comm.common;

import com.volcengine.tos.internal.util.StringUtils;

public enum AzRedundancyType {
    /**
     * 单 az
     */
    AZ_REDUNDANCY_SINGLE_AZ("single-az"),
    /**
     * 多 az
     */
    AZ_REDUNDANCY_MULTI_AZ("multi-az"),

    AZ_REDUNDANCY_UNKNOWN("unknown");

    public static AzRedundancyType parse(String input) {
        if (StringUtils.isEmpty(input)) {
            return null;
        }

        if ("single-az".equals(input)) {
            return AZ_REDUNDANCY_SINGLE_AZ;
        }
        if ("multi-az".equals(input)) {
            return AZ_REDUNDANCY_MULTI_AZ;
        }
        return null;
    }

    private String az;
    private AzRedundancyType(String az) {
        this.az = az;
    }

    @Override
    public String toString() {
        return az;
    }
}

package com.volcengine.tos.comm.common;

public enum AzRedundancyType {
    /**
     * 单 az
     */
    AZ_REDUNDANCY_SINGLE_AZ("single-az"),
    /**
     * 多 az
     */
    AZ_REDUNDANCY_MULTI_AZ("multi-az");
    private String az;
    private AzRedundancyType(String az) {
        this.az = az;
    }

    @Override
    public String toString() {
        return az;
    }
}

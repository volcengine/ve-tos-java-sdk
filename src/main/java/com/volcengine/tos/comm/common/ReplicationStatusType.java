package com.volcengine.tos.comm.common;

import com.volcengine.tos.internal.util.StringUtils;

public enum ReplicationStatusType {
    REPLICATION_STATUS_PENDING("PENDING"),
    REPLICATION_STATUS_COMPLETE("COMPLETE"),
    REPLICATION_STATUS_FAILED("FAILED"),
    REPLICATION_STATUS_REPLICA("REPLICA");

    public static ReplicationStatusType parse(String input){
        if (StringUtils.isEmpty(input)) {
            return null;
        }
        if ("PENDING".equals(input)) {
            return REPLICATION_STATUS_PENDING;
        }
        if ("COMPLETE".equals(input)) {
            return REPLICATION_STATUS_COMPLETE;
        }
        if ("FAILED".equals(input)) {
            return REPLICATION_STATUS_FAILED;
        }
        if ("REPLICA".equals(input)) {
            return REPLICATION_STATUS_REPLICA;
        }
        return null;
    }

    private String type;

    private ReplicationStatusType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}

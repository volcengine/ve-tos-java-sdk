package com.volcengine.tos.internal.model;

import java.util.zip.Checksum;

public interface TosRepeatableChecksum extends Checksum {
    /**
     * mark the current computed checksum value,
     * reset the checksum sum value to the lastChecksumValue
     * while calling reset() method in the CheckedInputStream.
     */
    void markCurrentValue();
}

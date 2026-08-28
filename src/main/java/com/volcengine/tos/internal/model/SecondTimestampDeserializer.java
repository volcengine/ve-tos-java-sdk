package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.util.Date;

public class SecondTimestampDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt)
            throws IOException {
        // 读取秒级时间戳并转换为Date
        long timestamp = p.getLongValue() * 1000;
        return new Date(timestamp);
    }
}

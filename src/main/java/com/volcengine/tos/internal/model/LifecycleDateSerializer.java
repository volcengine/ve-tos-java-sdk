package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.volcengine.tos.internal.util.DateConverter;

import java.io.IOException;
import java.util.Date;

public class LifecycleDateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(DateConverter.dateToRFC3339String(value));
    }
}

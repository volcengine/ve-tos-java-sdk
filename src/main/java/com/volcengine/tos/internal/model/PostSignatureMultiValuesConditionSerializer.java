package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.PostSignatureMultiValuesCondition;

import java.io.IOException;

public class PostSignatureMultiValuesConditionSerializer extends JsonSerializer<PostSignatureMultiValuesCondition> {
    @Override
    public void serialize(PostSignatureMultiValuesCondition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null || StringUtils.isEmpty(value.getOperator())) {
            gen.writeNull();
        } else {
            String key = value.getKey();
            if (key == null || !key.startsWith("$")) {
                key = "$" + key;
            }
            gen.writeStartArray();
            gen.writeString(value.getOperator());
            gen.writeString(key);
            if (value.getValue() == null) {
                gen.writeNull();
            } else {
                int size = value.getValue().size();
                gen.writeArray(value.getValue().toArray(new String[size]), 0, size);
            }
            gen.writeEndArray();
        }
    }
}

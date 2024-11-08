package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.volcengine.tos.internal.util.SigningUtils;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.PostSignatureCondition;

import java.io.IOException;

public class PostSignatureConditionSerializer extends JsonSerializer<PostSignatureCondition> {
    @Override
    public void serialize(PostSignatureCondition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else if (value.getOperator() == null) {
            gen.writeStartObject();
            gen.writeStringField(value.getKey(), value.getValue());
            gen.writeEndObject();
        } else if (StringUtils.equals(value.getOperator(), SigningUtils.signConditionRange)) {
            gen.writeStartArray();
            gen.writeObject(value.getOperator());
            gen.writeObject(Long.parseLong(value.getKey()));
            gen.writeObject(Long.parseLong(value.getValue()));
            gen.writeEndArray();
        } else {
            String key = value.getKey();
            if (key == null || !key.startsWith("$")) {
                key = "$" + key;
            }
            gen.writeArray(new String[]{value.getOperator(), key, value.getValue()}, 0, 3);
        }
    }
}

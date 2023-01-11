package com.volcengine.tos.internal.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.internal.util.SigningUtils;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.PolicySignatureCondition;

import java.io.IOException;

public class PolicySignatureConditionSerializer extends JsonSerializer<PolicySignatureCondition> {
    @Override
    public void serialize(PolicySignatureCondition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
        } else if (value.getOperator() == null){
            gen.writeStartObject();
            gen.writeStringField(value.getKey(), value.getValue());
            gen.writeEndObject();
        } else if (StringUtils.equals(value.getOperator(), SigningUtils.signConditionStartsWith)
        || StringUtils.equals(value.getOperator(), SigningUtils.signConditionEq)) {
            gen.writeArray(new String[]{value.getOperator(), "$" + value.getKey(), value.getValue()}, 0, 3);
        } else {
            throw new TosClientException("tos: invalid operator in condition, it should be 'starts-with' or 'eq'.", null);
        }
    }
}

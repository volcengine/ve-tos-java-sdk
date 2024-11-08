package com.volcengine.tos.model.object;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.volcengine.tos.internal.model.PostSignatureMultiValuesConditionSerializer;

import java.util.List;

@JsonSerialize(using = PostSignatureMultiValuesConditionSerializer.class)
public class PostSignatureMultiValuesCondition {
    private String key;
    private List<String> value;
    private String operator;

    public PostSignatureMultiValuesCondition(){

    }

    public PostSignatureMultiValuesCondition(String key, List<String> value, String operator) {
        this.key = key;
        this.value = value;
        this.operator = operator;
    }

    public String getKey() {
        return key;
    }

    public PostSignatureMultiValuesCondition setKey(String key) {
        this.key = key;
        return this;
    }

    public List<String> getValue() {
        return value;
    }

    public PostSignatureMultiValuesCondition setValue(List<String> value) {
        this.value = value;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public PostSignatureMultiValuesCondition setOperator(String operator) {
        this.operator = operator;
        return this;
    }
}

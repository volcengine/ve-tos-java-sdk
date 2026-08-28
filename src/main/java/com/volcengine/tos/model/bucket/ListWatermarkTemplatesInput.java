package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.volcengine.tos.model.GenericInput;

public class ListWatermarkTemplatesInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    public String getBucket() {
        return bucket;
    }

    public ListWatermarkTemplatesInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    @Override
    public String toString() {
        return "ListWatermarkTemplatesInput{" +
                "bucket='" + bucket + '\'' +
                '}';
    }

    public static ListWatermarkTemplatesInputBuilder builder() {
        return new ListWatermarkTemplatesInputBuilder();
    }

    public static class ListWatermarkTemplatesInputBuilder {
        private String bucket;

        public ListWatermarkTemplatesInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public ListWatermarkTemplatesInput build() {
            ListWatermarkTemplatesInput input = new ListWatermarkTemplatesInput();
            input.setBucket(bucket);
            return input;
        }
    }
}

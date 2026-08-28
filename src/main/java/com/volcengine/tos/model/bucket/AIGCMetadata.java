package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AIGCMetadata {
    @JsonProperty("Label")
    private String label;
    
    @JsonProperty("ContentProducer")
    private String contentProducer;
    
    @JsonProperty("ProduceID")
    private String produceID;
    
    @JsonProperty("ContentPropagator")
    private String contentPropagator;
    
    @JsonProperty("PropagateID")
    private String propagateID;
    
    @JsonProperty("ReservedCode1")
    private String reservedCode1;
    
    @JsonProperty("ReservedCode2")
    private String reservedCode2;

    public String getLabel() {
        return label;
    }

    public AIGCMetadata setLabel(String label) {
        this.label = label;
        return this;
    }

    public String getContentProducer() {
        return contentProducer;
    }

    public AIGCMetadata setContentProducer(String contentProducer) {
        this.contentProducer = contentProducer;
        return this;
    }

    public String getProduceID() {
        return produceID;
    }

    public AIGCMetadata setProduceID(String produceID) {
        this.produceID = produceID;
        return this;
    }

    public String getContentPropagator() {
        return contentPropagator;
    }

    public AIGCMetadata setContentPropagator(String contentPropagator) {
        this.contentPropagator = contentPropagator;
        return this;
    }

    public String getPropagateID() {
        return propagateID;
    }

    public AIGCMetadata setPropagateID(String propagateID) {
        this.propagateID = propagateID;
        return this;
    }

    public String getReservedCode1() {
        return reservedCode1;
    }

    public AIGCMetadata setReservedCode1(String reservedCode1) {
        this.reservedCode1 = reservedCode1;
        return this;
    }

    public String getReservedCode2() {
        return reservedCode2;
    }

    public AIGCMetadata setReservedCode2(String reservedCode2) {
        this.reservedCode2 = reservedCode2;
        return this;
    }

    @Override
    public String toString() {
        return "AIGCMetadata{" +
                "label='" + label + '\'' +
                ", contentProducer='" + contentProducer + '\'' +
                ", produceID='" + produceID + '\'' +
                ", contentPropagator='" + contentPropagator + '\'' +
                ", propagateID='" + propagateID + '\'' +
                ", reservedCode1='" + reservedCode1 + '\'' +
                ", reservedCode2='" + reservedCode2 + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String label;
        private String contentProducer;
        private String produceID;
        private String contentPropagator;
        private String propagateID;
        private String reservedCode1;
        private String reservedCode2;

        public Builder label(String label) {
            this.label = label;
            return this;
        }

        public Builder contentProducer(String contentProducer) {
            this.contentProducer = contentProducer;
            return this;
        }

        public Builder produceID(String produceID) {
            this.produceID = produceID;
            return this;
        }

        public Builder contentPropagator(String contentPropagator) {
            this.contentPropagator = contentPropagator;
            return this;
        }

        public Builder propagateID(String propagateID) {
            this.propagateID = propagateID;
            return this;
        }

        public Builder reservedCode1(String reservedCode1) {
            this.reservedCode1 = reservedCode1;
            return this;
        }

        public Builder reservedCode2(String reservedCode2) {
            this.reservedCode2 = reservedCode2;
            return this;
        }

        public AIGCMetadata build() {
            AIGCMetadata aigcMetadata = new AIGCMetadata();
            aigcMetadata.setLabel(label);
            aigcMetadata.setContentProducer(contentProducer);
            aigcMetadata.setProduceID(produceID);
            aigcMetadata.setContentPropagator(contentPropagator);
            aigcMetadata.setPropagateID(propagateID);
            aigcMetadata.setReservedCode1(reservedCode1);
            aigcMetadata.setReservedCode2(reservedCode2);
            return aigcMetadata;
        }
    }
}
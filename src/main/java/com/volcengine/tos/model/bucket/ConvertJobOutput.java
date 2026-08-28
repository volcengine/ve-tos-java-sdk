package com.volcengine.tos.model.bucket;
import com.fasterxml.jackson.annotation.JsonProperty;
public class ConvertJobOutput {
    @JsonProperty("Region")
    private String region;
    
    @JsonProperty("Bucket")
    private String bucket;
    
    @JsonProperty("Object")
    private String object;
    
    public String getRegion() {
        return region;
    }
    
    public ConvertJobOutput setRegion(String region) {
        this.region = region;
        return this;
    }
    
    public String getBucket() {
        return bucket;
    }
    
    public ConvertJobOutput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }
    
    public String getObject() {
        return object;
    }
    
    public ConvertJobOutput setObject(String object) {
        this.object = object;
        return this;
    }
    public static ConvertJobOutputBuilder builder() {
        return new ConvertJobOutputBuilder();
    }
    public static class ConvertJobOutputBuilder {
        private String region;
        private String bucket;
        private String object;
        public ConvertJobOutputBuilder region(String region) {
            this.region = region;
            return this;
        }
        public ConvertJobOutputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }
        public ConvertJobOutputBuilder object(String object) {
            this.object = object;
            return this;
        }
        public ConvertJobOutput build() {
            ConvertJobOutput output = new ConvertJobOutput();
            output.setRegion(region);
            output.setBucket(bucket);
            output.setObject(object);
            return output;
        }
    }
}
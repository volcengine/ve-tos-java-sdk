package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.volcengine.tos.model.GenericInput;

public class PointCloudCompressInput extends GenericInput {
    @JsonIgnore
    private String bucket;

    @JsonIgnore
    private String key;

    @JsonProperty("format")
    private String format;

    @JsonProperty("method")
    private String method;

    @JsonProperty("fields")
    private String fields;

    @JsonProperty("lib")
    private String lib;

    @JsonProperty("point-resolution")
    private Double pointResolution;

    @JsonProperty("octree-resolution")
    private Double octreeResolution;

    @JsonProperty("down-sampling")
    private Integer downSampling;

    public String getBucket() {
        return bucket;
    }

    public PointCloudCompressInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public PointCloudCompressInput setKey(String key) {
        this.key = key;
        return this;
    }

    public String getFormat() {
        return format;
    }

    public PointCloudCompressInput setFormat(String format) {
        this.format = format;
        return this;
    }

    public String getMethod() {
        return method;
    }

    public PointCloudCompressInput setMethod(String method) {
        this.method = method;
        return this;
    }

    public String getFields() {
        return fields;
    }

    public PointCloudCompressInput setFields(String fields) {
        this.fields = fields;
        return this;
    }

    public String getLib() {
        return lib;
    }

    public PointCloudCompressInput setLib(String lib) {
        this.lib = lib;
        return this;
    }

    public Double getPointResolution() {
        return pointResolution;
    }

    public PointCloudCompressInput setPointResolution(Double pointResolution) {
        this.pointResolution = pointResolution;
        return this;
    }

    public Double getOctreeResolution() {
        return octreeResolution;
    }

    public PointCloudCompressInput setOctreeResolution(Double octreeResolution) {
        this.octreeResolution = octreeResolution;
        return this;
    }

    public Integer getDownSampling() {
        return downSampling;
    }

    public PointCloudCompressInput setDownSampling(Integer downSampling) {
        this.downSampling = downSampling;
        return this;
    }

    @Override
    public String toString() {
        return "PointCloudCompressInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", format='" + format + '\'' +
                ", method='" + method + '\'' +
                ", fields='" + fields + '\'' +
                ", lib='" + lib + '\'' +
                ", pointResolution=" + pointResolution +
                ", octreeResolution=" + octreeResolution +
                ", downSampling=" + downSampling +
                '}';
    }

    public static PointCloudCompressInputBuilder builder() {
        return new PointCloudCompressInputBuilder();
    }

    public static final class PointCloudCompressInputBuilder {
        private String bucket;
        private String key;
        private String format;
        private String method;
        private String fields;
        private String lib;
        private Double pointResolution;
        private Double octreeResolution;
        private Integer downSampling;

        private PointCloudCompressInputBuilder() {
        }

        public PointCloudCompressInputBuilder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public PointCloudCompressInputBuilder key(String key) {
            this.key = key;
            return this;
        }

        public PointCloudCompressInputBuilder format(String format) {
            this.format = format;
            return this;
        }

        public PointCloudCompressInputBuilder method(String method) {
            this.method = method;
            return this;
        }

        public PointCloudCompressInputBuilder fields(String fields) {
            this.fields = fields;
            return this;
        }

        public PointCloudCompressInputBuilder lib(String lib) {
            this.lib = lib;
            return this;
        }

        public PointCloudCompressInputBuilder pointResolution(Double pointResolution) {
            this.pointResolution = pointResolution;
            return this;
        }

        public PointCloudCompressInputBuilder octreeResolution(Double octreeResolution) {
            this.octreeResolution = octreeResolution;
            return this;
        }

        public PointCloudCompressInputBuilder downSampling(Integer downSampling) {
            this.downSampling = downSampling;
            return this;
        }

        public PointCloudCompressInput build() {
            PointCloudCompressInput input = new PointCloudCompressInput();
            input.setBucket(bucket);
            input.setKey(key);
            input.setFormat(format);
            input.setMethod(method);
            input.setFields(fields);
            input.setLib(lib);
            input.setPointResolution(pointResolution);
            input.setOctreeResolution(octreeResolution);
            input.setDownSampling(downSampling);
            return input;
        }
    }
}

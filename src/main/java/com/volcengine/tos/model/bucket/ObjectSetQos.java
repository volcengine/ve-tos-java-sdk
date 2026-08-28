package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ObjectSetQos {
    @JsonProperty("ReadsQps")
    private Integer readsQps;
    @JsonProperty("WritesQps")
    private Integer writesQps;
    @JsonProperty("ListQps")
    private Integer listQps;
    @JsonProperty("ReadsRate")
    private Integer readsRate;
    @JsonProperty("WritesRate")
    private Integer writesRate;

    public Integer getReadsQps() {
        return readsQps;
    }

    public ObjectSetQos setReadsQps(Integer readsQps) {
        this.readsQps = readsQps;
        return this;
    }

    public Integer getWritesQps() {
        return writesQps;
    }

    public ObjectSetQos setWritesQps(Integer writesQps) {
        this.writesQps = writesQps;
        return this;
    }

    public Integer getListQps() {
        return listQps;
    }

    public ObjectSetQos setListQps(Integer listQps) {
        this.listQps = listQps;
        return this;
    }

    public Integer getReadsRate() {
        return readsRate;
    }

    public ObjectSetQos setReadsRate(Integer readsRate) {
        this.readsRate = readsRate;
        return this;
    }

    public Integer getWritesRate() {
        return writesRate;
    }

    public ObjectSetQos setWritesRate(Integer writesRate) {
        this.writesRate = writesRate;
        return this;
    }

    @Override
    public String toString() {
        return "ObjectSetQos{" +
                "readsQps=" + readsQps +
                ", writesQps=" + writesQps +
                ", listQps=" + listQps +
                ", readsRate=" + readsRate +
                ", writesRate=" + writesRate +
                '}';
    }

    public static ObjectSetQosBuilder builder() {
        return new ObjectSetQosBuilder();
    }

    public static final class ObjectSetQosBuilder {
        private Integer readsQps;
        private Integer writesQps;
        private Integer listQps;
        private Integer readsRate;
        private Integer writesRate;

        private ObjectSetQosBuilder() {
        }

        public ObjectSetQosBuilder readsQps(Integer readsQps) {
            this.readsQps = readsQps;
            return this;
        }

        public ObjectSetQosBuilder writesQps(Integer writesQps) {
            this.writesQps = writesQps;
            return this;
        }

        public ObjectSetQosBuilder listQps(Integer listQps) {
            this.listQps = listQps;
            return this;
        }

        public ObjectSetQosBuilder readsRate(Integer readsRate) {
            this.readsRate = readsRate;
            return this;
        }

        public ObjectSetQosBuilder writesRate(Integer writesRate) {
            this.writesRate = writesRate;
            return this;
        }

        public ObjectSetQos build() {
            ObjectSetQos qos = new ObjectSetQos();
            qos.setReadsQps(readsQps);
            qos.setWritesQps(writesQps);
            qos.setListQps(listQps);
            qos.setReadsRate(readsRate);
            qos.setWritesRate(writesRate);
            return qos;
        }
    }
}

package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Progress {
    @JsonProperty("HistoricalObject")
    private double historicalObject;
    @JsonProperty("NewObject")
    private String newObject;

    public double getHistoricalObject() {
        return historicalObject;
    }

    public Progress setHistoricalObject(double historicalObject) {
        this.historicalObject = historicalObject;
        return this;
    }

    public String getNewObject() {
        return newObject;
    }

    public Progress setNewObject(String newObject) {
        this.newObject = newObject;
        return this;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "historicalObject=" + historicalObject +
                ", newObject='" + newObject + '\'' +
                '}';
    }

    public static ProgressBuilder builder() {
        return new ProgressBuilder();
    }

    public static final class ProgressBuilder {
        private double historicalObject;
        private String newObject;

        private ProgressBuilder() {
        }

        public ProgressBuilder historicalObject(double historicalObject) {
            this.historicalObject = historicalObject;
            return this;
        }

        public ProgressBuilder newObject(String newObject) {
            this.newObject = newObject;
            return this;
        }

        public Progress build() {
            Progress progress = new Progress();
            progress.setHistoricalObject(historicalObject);
            progress.setNewObject(newObject);
            return progress;
        }
    }
}

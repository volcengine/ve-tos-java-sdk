package com.volcengine.tos.model.bucket;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FetchHeaderToMetaDataRules {
    @JsonProperty("SourceHeader")
    private String sourceHeader;
    @JsonProperty("MetaDataSuffix")
    private String metaDataSuffix;

    public String getSourceHeader() {
        return sourceHeader;
    }

    public FetchHeaderToMetaDataRules setSourceHeader(String sourceHeader) {
        this.sourceHeader = sourceHeader;
        return this;
    }

    public String getMetaDataSuffix() {
        return metaDataSuffix;
    }

    public FetchHeaderToMetaDataRules setMetaDataSuffix(String metaDataSuffix) {
        this.metaDataSuffix = metaDataSuffix;
        return this;
    }

    @Override
    public String toString() {
        return "FetchHeaderToMetaDataRules{" +
                "sourceHeader='" + sourceHeader + '\'' +
                ", metaDataSuffix='" + metaDataSuffix + '\'' +
                '}';
    }
}

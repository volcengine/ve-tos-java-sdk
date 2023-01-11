package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
@Deprecated
public class InnerUploadedPart implements Comparable<InnerUploadedPart>{
    @JsonProperty("PartNumber")
    int partNumber;
    @JsonProperty("ETag")
    String etag;
    InnerUploadedPart(int partNumber, String etag){
        this.partNumber = partNumber;
        this.etag = etag;
    }

    public InnerUploadedPart setPartNumber(int partNumber) {
        this.partNumber = partNumber;
        return this;
    }

    public String getEtag() {
        return etag;
    }

    public InnerUploadedPart setEtag(String etag) {
        this.etag = etag;
        return this;
    }

    @Override
    public int compareTo(InnerUploadedPart o) {
        return this.partNumber - o.partNumber;
    }

    public int getPartNumber() {
        return partNumber;
    }

    @Override
    public String toString(){
        return "{"+partNumber+" "+etag+"}";
    }
}

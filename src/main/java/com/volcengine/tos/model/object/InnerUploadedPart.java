package com.volcengine.tos.model.object;

import com.fasterxml.jackson.annotation.JsonProperty;
@Deprecated
class InnerUploadedPart implements Comparable<InnerUploadedPart>{
    @JsonProperty("PartNumber")
    int partNumber;
    @JsonProperty("ETag")
    String etag;
    InnerUploadedPart(int partNumber, String etag){
        this.partNumber = partNumber;
        this.etag = etag;
    }

    @Override
    public int compareTo(InnerUploadedPart o) {
        return this.partNumber - o.partNumber;
    }

    int getPartNumber() {
        return partNumber;
    }

    @Override
    public String toString(){
        return "{"+partNumber+" "+etag+"}";
    }
}

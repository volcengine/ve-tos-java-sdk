package com.volcengine.tos.internal.model;

public class HttpRange {
    private long start;
    private long end;

    public long getStart() {
        return start;
    }

    public HttpRange setStart(long start) {
        this.start = start;
        return this;
    }

    public long getEnd() {
        return end;
    }

    public HttpRange setEnd(long end) {
        this.end = end;
        return this;
    }

    @Override
    public String toString(){
        if (start < 0 && end < 0) {
            return "bytes=0-";
        } else if (start > 0 && end > 0 && start > end) {
            return "bytes=0-";
        } else if (start < 0) {
            return "bytes=0-" + end;
        } else if (end < 0) {
            return "bytes=" + start + "-";
        } else {
            return "bytes=" + start + "-" + end;
        }
    }
}

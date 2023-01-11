package com.volcengine.tos.model.object;

public class ContentLengthRange {
    private long rangeStart;
    private long rangeEnd;

    public long getRangeStart() {
        return rangeStart;
    }

    public ContentLengthRange setRangeStart(long rangeStart) {
        this.rangeStart = rangeStart;
        return this;
    }

    public long getRangeEnd() {
        return rangeEnd;
    }

    public ContentLengthRange setRangeEnd(long rangeEnd) {
        this.rangeEnd = rangeEnd;
        return this;
    }

    @Override
    public String toString() {
        return "ContentLengthRange{" +
                "rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                '}';
    }

    public static ContentLengthRangeBuilder builder() {
        return new ContentLengthRangeBuilder();
    }

    public static final class ContentLengthRangeBuilder {
        private long rangeStart;
        private long rangeEnd;

        private ContentLengthRangeBuilder() {
        }

        public ContentLengthRangeBuilder rangeStart(long rangeStart) {
            this.rangeStart = rangeStart;
            return this;
        }

        public ContentLengthRangeBuilder rangeEnd(long rangeEnd) {
            this.rangeEnd = rangeEnd;
            return this;
        }

        public ContentLengthRange build() {
            ContentLengthRange contentLengthRange = new ContentLengthRange();
            contentLengthRange.setRangeStart(rangeStart);
            contentLengthRange.setRangeEnd(rangeEnd);
            return contentLengthRange;
        }
    }
}

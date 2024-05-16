package com.volcengine.tos.comm.common;

public enum DocPreviewDstType {
    DOC_PREVIEW_DST_TYPE_PDF("pdf"),
    DOC_PREVIEW_DST_TYPE_HTML("html"),
    DOC_PREVIEW_DST_TYPE_PNG("png"),
    DOC_PREVIEW_DST_TYPE_JPG("jpg");

    private String type;

    private DocPreviewDstType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}

package com.volcengine.tos.comm.common;

public enum DocPreviewSrcType {
    DOC_PREVIEW_SRC_TYPE_DOC("doc"),
    DOC_PREVIEW_SRC_TYPE_DOCX("docx"),
    DOC_PREVIEW_SRC_TYPE_PPT("ppt"),
    DOC_PREVIEW_SRC_TYPE_PPTX("pptx"),
    DOC_PREVIEW_SRC_TYPE_XLS("xls"),
    DOC_PREVIEW_SRC_TYPE_XLSX("xlsx");

    private String type;

    private DocPreviewSrcType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}

package com.volcengine.tos.internal;

public interface RequestOptionsBuilder {
    /**
     * properties setting for building a Tos Request
     *
     * @param builder RequestBuilder
     */
    void withOption(RequestBuilder builder);
}

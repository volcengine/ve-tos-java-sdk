package com.volcengine.tos;

public interface RequestOptionsBuilder {
    /**
     * properties setting for building a Tos Request
     *
     * @param builder RequestBuilder
     */
    void withOption(RequestBuilder builder);
}

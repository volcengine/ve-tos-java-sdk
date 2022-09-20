package com.volcengine.tos.internal;

import java.io.InputStream;
import java.util.Map;

public interface TosRequestFactory {
    /**
     * create a RequestBuilder for request
     * @param bucket bucket name
     * @param object object name
     * @param headers http option headers
     * @return RequestBuilder
     */
    RequestBuilder init(String bucket, String object, Map<String, String> headers);

    /**
     * build a TosRequest from RequestBuilder
     * @param builder a RequestBuilder that has been set up
     * @param method http method
     * @param content content to put in a http body
     * @return TosRequest
     */
    TosRequest build(RequestBuilder builder, String method, InputStream content);

    /**
     * build a TosRequest from RequestBuilder in the copy source case
     * @param builder a RequestBuilder that has been set up
     * @param method http method
     * @param srcBucket the source bucket name for copying
     * @param srcObject the source object name for copying
     * @return TosRequest
     */
    TosRequest buildWithCopy(RequestBuilder builder, String method, String srcBucket, String srcObject);
}

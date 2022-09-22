package com.volcengine.tos;

import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.RequestBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;


public class RequestOptionsTest {
    @Test
    void propertiesTest(){
        RequestBuilder rb = new RequestBuilder().setQuery(new HashMap<>()).setHeaders(new HashMap<>());
        RequestOptions.withContentType("type").withOption(rb);
        Assert.assertEquals("type", rb.getHeaders().get(TosHeader.HEADER_CONTENT_TYPE));

        RequestOptions.withAutoRecognizeContentType(false).withOption(rb);
        Assert.assertFalse(rb.isAutoRecognizeContentType());
    }
}

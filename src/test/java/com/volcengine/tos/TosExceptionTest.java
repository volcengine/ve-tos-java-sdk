package com.volcengine.tos;

import com.volcengine.tos.comm.Code;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class TosExceptionTest {
    @Test
    void UnexpectedStatusCodeExceptionTest(){
        UnexpectedStatusCodeException e = new UnexpectedStatusCodeException(HttpStatus.SC_BAD_REQUEST, HttpStatus.SC_OK);
        Assert.assertNotNull(e);
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, e.getStatusCode());
        Assert.assertEquals(1, e.getExpectedCodes().size());
        Assert.assertEquals(HttpStatus.SC_OK, e.getExpectedCodes().get(0).intValue());

        e = e.withRequestID("xxx");
        Assert.assertNotNull(e);
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, e.getStatusCode());
        Assert.assertEquals(1, e.getExpectedCodes().size());
        Assert.assertEquals(HttpStatus.SC_OK, e.getExpectedCodes().get(0).intValue());
        Assert.assertEquals("xxx", e.getRequestID());

        TosException te = e;
        Assert.assertNotNull(te);
        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, te.getStatusCode());

        Consts.LOG.info(te.toString());
    }

    @Test
    void TosServerExceptionTest(){
        TosServerException se = new TosServerException(HttpStatus.SC_INTERNAL_SERVER_ERROR, Code.INTERNAL_ERROR,
                "1234", "2h3c", "1.1.1.1");
        Assert.assertNotNull(se);
        Assert.assertEquals(Code.INTERNAL_ERROR, se.getCode());
        Assert.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, se.getStatusCode());

        TosException te = se;
        Assert.assertNotNull(te);
        Assert.assertEquals(Code.INTERNAL_ERROR, te.getCode());
        Assert.assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, te.getStatusCode());

        Consts.LOG.info(te.toString());
    }

    @Test
    void TosClientExceptionTest(){
        TosClientException ce = new TosClientException("xxx", new IOException("aaa"));
        Assert.assertNotNull(ce);
        Assert.assertTrue(ce.getCause() instanceof IOException);
        Assert.assertEquals("xxx", ce.getMessage());
        Consts.LOG.info(ce.toString());

        TosException te = ce;
        Assert.assertNotNull(te);
        Assert.assertTrue(te.getCause() instanceof IOException);
        Assert.assertEquals("xxx", te.getMessage());
        Assert.assertEquals("", ce.getCode());
        Assert.assertEquals(0, ce.getStatusCode());
        Consts.LOG.info("exception: ", te);
    }
}

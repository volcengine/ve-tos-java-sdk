package com.volcengine.tos;

import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;

public class TosExceptionTest {
    @Test
    void unexpectedStatusCodeExceptionTest(){
        UnexpectedStatusCodeException e = new UnexpectedStatusCodeException(HttpStatus.BAD_REQUEST, HttpStatus.OK, "xxx");
        Assert.assertNotNull(e);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        Assert.assertEquals(1, e.getExpectedCodes().size());
        Assert.assertEquals(HttpStatus.OK, e.getExpectedCodes().get(0).intValue());

        Assert.assertNotNull(e);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, e.getStatusCode());
        Assert.assertEquals(1, e.getExpectedCodes().size());
        Assert.assertEquals(HttpStatus.OK, e.getExpectedCodes().get(0).intValue());
        Assert.assertEquals("xxx", e.getRequestID());

        TosException te = e;
        Assert.assertNotNull(te);
        Assert.assertEquals(HttpStatus.BAD_REQUEST, te.getStatusCode());

        Consts.LOG.info(te.toString());
    }

    @Test
    void TosServerExceptionTest(){
        TosServerException se = new TosServerException(HttpStatus.INTERNAL_SERVER_ERROR, Code.INTERNAL_ERROR,
                "1234", "2h3c", "1.1.1.1");
        Assert.assertNotNull(se);
        Assert.assertEquals(se.getCode(), Code.INTERNAL_ERROR);
        Assert.assertEquals(se.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(se.getMessage(), "1234");

        Assert.assertEquals(((TosException) se).getCode(), Code.INTERNAL_ERROR);
        Assert.assertEquals(((TosException) se).getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
        Assert.assertEquals(se.getMessage(), "1234");

        Consts.LOG.info(((TosException) se).toString());
    }

    @Test
    void TosClientExceptionTest(){
        TosClientException ce = new TosClientException("xxx", new IOException("aaa"));
        Assert.assertNotNull(ce);
        Assert.assertTrue(ce.getCause() instanceof IOException);
        Assert.assertEquals("xxx", ce.getMessage());
        Consts.LOG.info(ce.toString());

        Assert.assertTrue(ce.getCause() instanceof IOException);
        Assert.assertEquals("xxx", ce.getMessage());
        Assert.assertEquals("", ce.getCode());
        Assert.assertEquals(0, ce.getStatusCode());
        Consts.LOG.info("exception: ", ce);
    }
}

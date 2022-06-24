package com.volcengine.tos.io;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class InputStreamTypeTest {
    @Test
    void TestTosInputStreamType() throws ClassNotFoundException, FileNotFoundException {
        InputStream is = new ByteArrayInputStream("aaa".getBytes());
        TosRepeatableInputStream tis = new TosRepeatableInputStream(is, -1);
        Assert.assertEquals(tis.getOriginInputStream().getClass(), Class.forName("java.io.ByteArrayInputStream"));

        tis = new TosRepeatableInputStream(is, 20);
        Assert.assertEquals(tis.getOriginInputStream().getClass(), Class.forName("java.io.ByteArrayInputStream"));

        BufferedInputStream ibs = new BufferedInputStream(is);
        tis = new TosRepeatableInputStream(ibs, 20);
        Assert.assertEquals(tis.getOriginInputStream().getClass(), Class.forName("java.io.BufferedInputStream"));

        InputStream fis = new FileInputStream("pom.xml");
        tis = new TosRepeatableInputStream(fis, 20);
        Assert.assertEquals(tis.getOriginInputStream().getClass(), Class.forName("com.volcengine.tos.io.TosRepeatableInputStream$WrappedBufferedInputStream"));
    }
}

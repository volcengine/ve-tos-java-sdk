package com.volcengine.tos.transport;

import com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream;
import com.volcengine.tos.comm.io.TosRepeatableFileInputStream;
import okhttp3.MediaType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;

public class RequestBodyTest {
    @Test
    void inputStreamTypeTest() throws ClassNotFoundException, FileNotFoundException {
        InputStream is = new ByteArrayInputStream("aaa".getBytes());
        InputStreamRequestBody body = new InputStreamRequestBody(MediaType.parse("application/json"), is, 20);
        Assert.assertEquals(body.getInputStream().getClass(), Class.forName("com.volcengine.tos.comm.io.TosRepeatableInputStream"));

        is = new TosRepeatableFileInputStream(new File("pom.xml"));
        body = new InputStreamRequestBody(MediaType.parse("application/json"), is, 20);
        Assert.assertEquals(body.getInputStream().getClass(), Class.forName("com.volcengine.tos.comm.io.TosRepeatableFileInputStream"));

        is = new TosRepeatableBoundedFileInputStream(new FileInputStream("pom.xml"), 10);
        body = new InputStreamRequestBody(MediaType.parse("application/json"), is, 20);
        Assert.assertEquals(body.getInputStream().getClass(), Class.forName("com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream"));
    }
}

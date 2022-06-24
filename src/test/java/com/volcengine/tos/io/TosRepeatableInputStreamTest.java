package com.volcengine.tos.io;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;

public class TosRepeatableInputStreamTest {
    @Test
    public void TestTosRepeatableInputStream() {
        String data = "Java SDK for TOS Service";
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data.getBytes());
             TosRepeatableInputStream tis = new TosRepeatableInputStream(bis, data.length())){
            Assert.assertTrue(tis.markSupported());
            tis.mark(12);
            Assert.assertEquals(tis.available(), data.length());
            byte[] out = new byte[20];
            int ret = tis.read(out, 0, 10);
            Assert.assertEquals(ret, 10);
            ret = tis.read();
            Assert.assertEquals(ret, 'o');
            Assert.assertEquals(tis.available(), data.length() - 11);
            tis.reset();
            Assert.assertEquals(tis.available(), data.length());
            ret = tis.read(out, 0, 12);
            Assert.assertEquals(ret, 12);
            tis.mark(12);
            ret = tis.read();
            ret = tis.read();
            Assert.assertEquals(ret, 'T');
            tis.reset();
            Assert.assertEquals(tis.available(), data.length() - 12);
        } catch (IOException e) {
            Assert.fail();
        }
    }

    @Test
    public void TestTosRepeatableFileInputStream() {
        String path = "pom.xml";
        try (FileInputStream inputFile = new FileInputStream(path);
             TosRepeatableFileInputStream tfis = new TosRepeatableFileInputStream(inputFile)){
            int size = inputFile.available();
            Assert.assertEquals(size, tfis.available());
            tfis.mark(10);
            tfis.read();
            tfis.read();
            Assert.assertEquals(size - 2, tfis.available());
            tfis.reset();
            Assert.assertEquals(size, tfis.available());
            tfis.skip(size / 2);
            tfis.mark(10);
            Assert.assertEquals(size - size / 2, tfis.available());
            tfis.read();
            tfis.read();
            tfis.reset();
            Assert.assertEquals(size - size / 2, tfis.available());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void TestTosRepeatableBoundedFileInputStream() {
        String path = "pom.xml";
        try (FileInputStream inputFile = new FileInputStream(path);
             TosRepeatableBoundedFileInputStream tfis = new TosRepeatableBoundedFileInputStream(inputFile, 10)){
            int size = inputFile.available();
            Assert.assertEquals(size, tfis.available());
            tfis.mark(10);
            byte[] out = new byte[10];
            int ret = tfis.read(out, 0, 10);
            Assert.assertEquals(ret, 10);
            ret = tfis.read();
            Assert.assertEquals(ret, -1);
            tfis.reset();
            Assert.assertEquals(size, tfis.available());
        }catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}

package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.internal.model.CRC64Checksum;
import com.volcengine.tos.internal.model.CheckCrc64AutoInputStream;
import com.volcengine.tos.internal.model.SimpleDataTransferListenInputStream;
import com.volcengine.tos.internal.util.CRC64Utils;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.TosObjectInputStream;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.CheckedInputStream;

public class CRC64Test  {
    @Test
    void crc64Test() throws IOException {
        String data1 = "0123456789ABCDEFGabcdefg";
        String data2 = "This is a test of the crc64 algorithm.";
        String data3 = "中文测试&*（#*¥&（（fjwdeoh";
        CRC64Checksum crc64;

        crc64 = new CRC64Checksum();
        crc64.update(data1.getBytes(), 0, data1.length());
        long pat = Long.parseLong("-2573508052881018777");
        Assert.assertEquals(pat, crc64.getValue());
        Assert.assertEquals(CRC64Utils.longToUnsignedLongString(crc64.getValue()), "15873236020828532839");

        crc64.reset();
        crc64.update(data2.getBytes(), 0, data2.length());
        pat = Long.parseLong("4479640849766114640");
        Assert.assertEquals(pat, crc64.getValue());
        Assert.assertEquals(CRC64Utils.longToUnsignedLongString(crc64.getValue()), "4479640849766114640");

        crc64.reset();
        byte[] data3InByteArray = StringUtils.toByteArray(new ByteArrayInputStream(data3.getBytes(StandardCharsets.UTF_8)));
        crc64.update(data3InByteArray, 0, data3InByteArray.length);
        pat = Long.parseLong("324545339191807349");
        Assert.assertEquals(pat, crc64.getValue());
        Assert.assertEquals(CRC64Utils.longToUnsignedLongString(crc64.getValue()), "324545339191807349");

        // test consume InputStream
        crc64.reset();
        try(InputStream in = new ByteArrayInputStream(data3.getBytes(StandardCharsets.UTF_8));
            CheckedInputStream checkedInputStream = new CheckedInputStream(in, crc64)) {
            byte[] b = new byte[128];
            while ((checkedInputStream.read(b)) != -1) {
                // read data
            }
            Assert.assertEquals(pat, crc64.getValue());
            Assert.assertEquals(CRC64Utils.longToUnsignedLongString(crc64.getValue()), "324545339191807349");
            Assert.assertEquals(checkedInputStream.getChecksum().getValue(), pat);
        }

        // test combine
        String total = data1 + data2;
        CRC64Checksum crc1 = new CRC64Checksum();
        crc1.update(data1.getBytes(), 0, data1.length());

        CRC64Checksum crc2 = new CRC64Checksum();
        crc2.update(data2.getBytes(), 0, data2.length());

        CRC64Checksum crc3 = new CRC64Checksum();
        crc3.update(total.getBytes(), 0, total.length());

        CRC64Checksum crc4 = CRC64Utils.combine(crc1, crc2, data2.length());
        Assert.assertEquals(crc3.getValue(), crc4.getValue());

        CRC64Checksum crc5 = CRC64Utils.combine(crc1, crc2, 0);
        Assert.assertEquals(crc1.getValue(), crc5.getValue());
    }

    @Test
    void autoCheckCrc64InputStreamTest() {
        String data2 = "This is a test of the crc64 algorithm.";
        String data2Crc64 = "4479640849766114640";
        ByteArrayInputStream in = new ByteArrayInputStream(data2.getBytes());
        try(CheckCrc64AutoInputStream crc64AutoInputStream =
                    new CheckCrc64AutoInputStream(in, new CRC64Checksum(), data2Crc64)) {
            crc64AutoInputStream.read();
        } catch (IOException e) {
            Assert.fail();
        } catch (TosClientException e) {
            Assert.assertTrue(e.getMessage().contains("tos: crc of entire file mismatch"));
        }

        in = new ByteArrayInputStream(data2.getBytes());
        try(CheckCrc64AutoInputStream crc64AutoInputStream =
                    new CheckCrc64AutoInputStream(in, new CRC64Checksum(), data2Crc64)) {
            byte[] tmp = new byte[data2.length() / 2];
            crc64AutoInputStream.read(tmp);
        } catch (IOException e) {
            Assert.fail();
        } catch (TosClientException e) {
            Assert.assertTrue(e.getMessage().contains("tos: crc of entire file mismatch"));
        }

        in = new ByteArrayInputStream(data2.getBytes());
        try(CheckCrc64AutoInputStream crc64AutoInputStream =
                    new CheckCrc64AutoInputStream(in, new CRC64Checksum(), data2Crc64)) {
            byte[] tmp = new byte[data2.length()];
            crc64AutoInputStream.read(tmp);
        } catch (IOException | TosClientException e) {
            Assert.fail();
        }

        in = new ByteArrayInputStream(data2.getBytes());
        try(CheckCrc64AutoInputStream crc64AutoInputStream =
                    new CheckCrc64AutoInputStream(in, new CRC64Checksum(), data2Crc64)) {
            byte[] tmp = new byte[data2.length()/2];
            crc64AutoInputStream.read(tmp);
            crc64AutoInputStream.skip(data2.length()/4);
            crc64AutoInputStream.read(tmp);
        } catch (IOException | TosClientException e) {
            Assert.fail();
        }
    }

//    @Test
//    void wrapInputStreamTest() {
//        DataTransferListener listener = System.out::println;
//        InputStream stream = new ByteArrayInputStream("hello world".getBytes());
//        stream = new CheckedInputStream(stream, new CRC64Checksum());
//        stream = new SimpleDataTransferListenInputStream(stream, listener, 100);
//        stream = new TosObjectInputStream(stream);
//        ((CheckedInputStream) stream).getChecksum().getValue();
//    }
}
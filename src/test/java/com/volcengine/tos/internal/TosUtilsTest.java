package com.volcengine.tos.internal;

import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.TosUtils;
import org.testng.annotations.Test;

import org.testng.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TosUtilsTest {
    @Test
    public void uriEncodeTest(){
        String out = TosUtils.uriEncode("23i23+___", true);
        Assert.assertEquals(out, "23i23%2B___");

        out = TosUtils.uriEncode("23i23 ___", true);
        Assert.assertEquals(out, "23i23%20___");

        out = TosUtils.uriEncode("23i23 /___", true);
        Assert.assertEquals(out, "23i23%20%2F___");

        out = TosUtils.uriEncode("23i23 /___", false);
        Assert.assertEquals(out, "23i23%20/___");

        out = TosUtils.uriEncode("/中文测试/", true);
        Assert.assertEquals(out, "%2F%E4%B8%AD%E6%96%87%E6%B5%8B%E8%AF%95%2F");
    }

    @Test
    void dateConvertTest(){
        String rfc1123String = "Tue, 23 Aug 2022 16:50:43 GMT";
//        String rfc1123String = "Fri, 02 Sep 2022 11:04:56 GMT";
        Date date = DateConverter.rfc1123StringToDate(rfc1123String);
        Assert.assertEquals(date.toInstant().getEpochSecond(), 1661273443);

        String formattedRFC1123 = DateConverter.dateToRFC1123String(date);
        Assert.assertEquals(formattedRFC1123, rfc1123String);

        String iso8601String = "2022-08-23T16:50:43.000Z";
        date = DateConverter.iso8601StringToDate(iso8601String);
        Assert.assertEquals(date.toInstant().getEpochSecond(), 1661273443);

        String formattedISO8601 = DateConverter.dateToISO8601String(date);
        Assert.assertEquals(formattedISO8601, iso8601String);

        Date now = new Date();
        System.out.println(DateConverter.dateToRFC3339String(now));
    }

    @Test
    void supportedRegionTest() {
        Map<String, List<String>> supportedRegion = TosUtils.getSupportedRegion();
        Assert.assertEquals(supportedRegion.size(), 3);
        Assert.assertEquals(supportedRegion.get("cn-beijing").get(0), "tos-cn-beijing.volces.com");
        Assert.assertEquals(supportedRegion.get("cn-shanghai").get(0), "tos-cn-shanghai.volces.com");
        Assert.assertEquals(supportedRegion.get("cn-guangzhou").get(0), "tos-cn-guangzhou.volces.com");
    }

    @Test
    void ipTest() {
        String addr = null;
        Assert.assertFalse(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "localhost";
        Assert.assertTrue(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "127.0.0.1";
        Assert.assertTrue(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "127.0.0.256";
        Assert.assertFalse(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "0::0::0";
        Assert.assertFalse(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "fe80::1551:1234:fbb:fcc3";
        Assert.assertFalse(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "[fe80::1551:1234:fbb:fcc3]";
        Assert.assertTrue(ParamsChecker.isLocalhostOrIpAddress(addr));
    }
}

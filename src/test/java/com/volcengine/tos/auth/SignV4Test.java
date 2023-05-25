package com.volcengine.tos.auth;

import com.volcengine.tos.Consts;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.internal.TosRequest;
import com.volcengine.tos.internal.util.DateConverter;
import okhttp3.HttpUrl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SignV4Test {
    static final DateTimeFormatter iso8601Layout = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");

    @Test
    public void timeParseTest(){
        OffsetDateTime now = Instant.now().atOffset(ZoneOffset.UTC);
        String date = now.format(iso8601Layout);
        Consts.LOG.info("Format Date: {}", date);
        Consts.LOG.info("Format Local Date: {}", LocalDateTime.now().format(iso8601Layout));
        String date1 = "20210921T173420Z";
        LocalDateTime parse = LocalDateTime.parse(date1, iso8601Layout);
        Consts.LOG.info("Parse Local Date: {}", parse);
    }

    @Test
    public void signV4Test(){
        Date now = new Date();
        String date = DateConverter.dateToFullIso8601Str(now);
        String shortDate = DateConverter.dateToShortDateStr(now);

        Credentials cred = new StaticCredentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
        SignV4 sv = new SignV4(cred, "cn-north-1");

        HttpUrl url = HttpUrl.parse("https://test.tos.com:8080/test.txt");
        Assert.assertNotNull(url);

        TosRequest req = new TosRequest();
        req.setMethod(HttpMethod.GET);
        req.setHost(url.host()+":"+url.port());
        req.setPath(url.encodedPath());
        req.setQuery(new HashMap<String, String>());
        req.setHeaders(new HashMap<String, String>());

        Map<String, String> query = sv.signQuery(req, 3600);
        Assert.assertEquals(6, query.size());
        String dateSignedStr = query.get("X-Tos-Date");
        Date dateSigned = DateConverter.fullIso8601StrToDate(dateSignedStr);
        Assert.assertNotNull(dateSigned);
//        System.out.println("time: " + dateSigned.getTime() + ", now: " + now.getTime() + ", diff: " + (dateSigned.getTime() - now.getTime()));
        Assert.assertTrue(dateSigned.getTime()/1000 - now.getTime()/1000 < 2);
        Assert.assertEquals("TOS4-HMAC-SHA256", query.get("X-Tos-Algorithm"));
        Assert.assertEquals("host", query.get("X-Tos-SignedHeaders"));
        Assert.assertEquals(String.format("AKIAIOSFODNN7EXAMPLE/%s/cn-north-1/tos/request", shortDate), query.get("X-Tos-Credential"));
        Assert.assertEquals("3600", query.get("X-Tos-Expires"));

        Map<String, String> header = sv.signHeader(req);
        Assert.assertEquals(3, header.size());
        Assert.assertTrue(header.get("Authorization").startsWith(String.format("TOS4-HMAC-SHA256 Credential=" +
                "AKIAIOSFODNN7EXAMPLE/%s/cn-north-1/tos/request,SignedHeaders=date;host;x-tos-date,Signature=", shortDate)));
        dateSignedStr = header.get("X-Tos-Date");
        dateSigned = DateConverter.fullIso8601StrToDate(dateSignedStr);
        Assert.assertNotNull(dateSigned);
        Assert.assertTrue(dateSigned.getTime()/1000 - now.getTime()/1000 < 2);
        Assert.assertNull(header.get("X-Tos-Content-Sha256"));
    }
}

package com.volcengine.tos;

import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.SignV4;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.comm.HttpMethod;
import okhttp3.HttpUrl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class SignV4Test {
    static final DateTimeFormatter iso8601Layout = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'");
    @Test
    public void URIEncodeTest(){
        String out = SignV4.uriEncode("23i23+___", true);
        Assert.assertEquals(out, "23i23%2B___");

        out = SignV4.uriEncode("23i23 ___", true);
        Assert.assertEquals(out, "23i23%20___");

        out = SignV4.uriEncode("23i23 /___", true);
        Assert.assertEquals(out, "23i23%20%2F___");

        out = SignV4.uriEncode("23i23 /___", false);
        Assert.assertEquals(out, "23i23%20/___");

        out = SignV4.uriEncode("/中文测试/", true);
        Assert.assertEquals(out, "%2F%E4%B8%AD%E6%96%87%E6%B5%8B%E8%AF%95%2F");
    }

    @Test
    public void TimeParseTest(){
        OffsetDateTime now = Instant.now().atOffset(ZoneOffset.UTC);
        String date = now.format(iso8601Layout);
        Consts.LOG.info("Format Date: {}", date);
        Consts.LOG.info("Format Local Date: {}", LocalDateTime.now().format(iso8601Layout));
        String date1 = "20210921T173420Z";
        LocalDateTime parse = LocalDateTime.parse(date1, iso8601Layout);
        Consts.LOG.info("Parse Local Date: {}", parse);
    }

    @Test
    public void AlgV4Test(){
        String date = "20210721T104454Z";
        LocalDateTime parse = LocalDateTime.parse(date, iso8601Layout);

        Credentials cred = new StaticCredentials("AKIAIOSFODNN7EXAMPLE", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
        SignV4 sv = new SignV4(cred, "cn-north-1");
        sv.setNow(() -> Instant.ofEpochSecond(1626864294));

        HttpUrl url = HttpUrl.parse("https://test.tos.com:8080/test.txt");
        Assert.assertNotNull(url);

        TosRequest req = new TosRequest();
        req.setMethod(HttpMethod.GET);
        req.setHost(url.host()+":"+url.port());
        req.setPath(url.encodedPath());
        req.setQuery(new HashMap<>());
        req.setHeaders(new HashMap<>());

        Map<String, String> header = sv.signHeader(req);
        Assert.assertEquals(3, header.size());
        Assert.assertEquals("TOS4-HMAC-SHA256 Credential=AKIAIOSFODNN7EXAMPLE/20210721/cn-north-1/tos/request,SignedHeaders=date;" +
                        "host;x-tos-date,Signature=8851df83fd33af6fb4a28addbe938d88139715dca85bbb43ee739c3eb58eddf2"
                , header.get("Authorization"));
        Assert.assertEquals(date, header.get("X-Tos-Date"));
        Assert.assertEquals(date, header.get("Date"));
        Assert.assertNull(header.get("X-Tos-Content-Sha256"));
    }
}

package com.volcengine.tos.auth;

import com.volcengine.tos.Consts;
import com.volcengine.tos.internal.TosRequest;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.internal.util.TosUtils;
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

        Map<String, String> query = sv.signQuery(req, Duration.ofHours(1));
        Assert.assertEquals(6, query.size());
        Assert.assertEquals(date, query.get("X-Tos-Date"));
        Assert.assertEquals("TOS4-HMAC-SHA256", query.get("X-Tos-Algorithm"));
        Assert.assertEquals("host", query.get("X-Tos-SignedHeaders"));
        Assert.assertEquals("AKIAIOSFODNN7EXAMPLE/20210721/cn-north-1/tos/request", query.get("X-Tos-Credential"));
        Assert.assertEquals("3600", query.get("X-Tos-Expires"));
        Assert.assertEquals("decc75e2b2d453117f81e53954eb2cd3a2f56db2951e9b2257863db4c4921111", query.get("X-Tos-Signature"));

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

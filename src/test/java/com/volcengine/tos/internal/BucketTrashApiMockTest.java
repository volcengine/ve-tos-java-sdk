package com.volcengine.tos.internal;

import com.volcengine.tos.TOSClientConfiguration;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.StatusType;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.bucket.BucketTrash;
import com.volcengine.tos.model.bucket.BucketTrashPrefixRule;
import com.volcengine.tos.model.bucket.GetBucketTrashInput;
import com.volcengine.tos.model.bucket.GetBucketTrashOutput;
import com.volcengine.tos.model.bucket.PutBucketTrashInput;
import com.volcengine.tos.transport.TransportConfig;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;

public class BucketTrashApiMockTest {
    private static String md5Base64(byte[] data) throws Exception {
        return Base64.getEncoder().encodeToString(MessageDigest.getInstance("MD5").digest(data));
    }

    @Test
    public void bucketTrashApisBuildCorrectRequestsAndParseResponse() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setResponseCode(200));
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("{\"Trash\":{\"TrashPath\":\".Trash/\",\"CleanInterval\":1,\"Status\":\"Enabled\",\"PrefixMatchRules\":[{\"PrefixList\":[\"source-a/\",\"source-b/\"],\"TrashPath\":\"archive/\",\"CleanInterval\":2}]}}"));
            server.start();

            String endpoint = server.url("/").toString();
            TOSV2 client = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder()
                    .region("cn-beijing")
                    .endpoint(endpoint.substring(0, endpoint.length() - 1))
                    .transportConfig(TransportConfig.builder().maxRetryCount(0).build())
                    .build());
            BucketTrash trash = new BucketTrash().setTrashPath(".Trash/").setCleanInterval(1)
                    .setStatus(StatusType.STATUS_ENABLED).setPrefixMatchRules(Collections.singletonList(
                            new BucketTrashPrefixRule().setPrefixList(Arrays.asList("source-a/", "source-b/"))
                                    .setTrashPath("archive/").setCleanInterval(2)));

            client.putBucketTrash(new PutBucketTrashInput().setBucket("bucketname").setTrash(trash));
            RecordedRequest request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            Assert.assertEquals(request.getRequestUrl().queryParameter("trash"), "");
            byte[] body = request.getBody().clone().readByteArray();
            Assert.assertEquals(request.getHeader(TosHeader.HEADER_CONTENT_MD5), md5Base64(body));
            Assert.assertEquals(TosUtils.getJsonMapper().readTree(body),
                    TosUtils.getJsonMapper().readTree("{\"Trash\":{\"TrashPath\":\".Trash/\",\"CleanInterval\":1,\"Status\":\"Enabled\",\"PrefixMatchRules\":[{\"PrefixList\":[\"source-a/\",\"source-b/\"],\"TrashPath\":\"archive/\",\"CleanInterval\":2}]}}"));

            GetBucketTrashOutput output = client.getBucketTrash(new GetBucketTrashInput().setBucket("bucketname"));
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            Assert.assertEquals(request.getRequestUrl().queryParameter("trash"), "");
            Assert.assertEquals(request.getBody().size(), 0);
            Assert.assertEquals(output.getTrash().getTrashPath(), ".Trash/");
            Assert.assertEquals(output.getTrash().getCleanInterval(), Integer.valueOf(1));
            Assert.assertEquals(output.getTrash().getStatus(), StatusType.STATUS_ENABLED);
            Assert.assertEquals(output.getTrash().getPrefixMatchRules().get(0).getPrefixList(),
                    Arrays.asList("source-a/", "source-b/"));
            Assert.assertEquals(output.getTrash().getPrefixMatchRules().get(0).getTrashPath(), "archive/");
            Assert.assertEquals(output.getTrash().getPrefixMatchRules().get(0).getCleanInterval(), Integer.valueOf(2));
        }
    }
}

package com.volcengine.tos.internal;

import com.fasterxml.jackson.databind.JsonNode;
import com.volcengine.tos.TOSClientConfiguration;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.StatusType;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.TagSet;
import com.volcengine.tos.transport.TransportConfig;
import okhttp3.HttpUrl;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class ObjectSetApiMockTest {

    private static String md5Base64(byte[] data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Base64.getEncoder().encodeToString(md.digest(data));
    }

    private static void assertNoBody(RecordedRequest request) {
        Assert.assertEquals(request.getBody().size(), 0);
    }

    private static void assertContentMd5(RecordedRequest request) throws Exception {
        byte[] body = request.getBody().clone().readByteArray();
        Assert.assertEquals(request.getHeader(TosHeader.HEADER_CONTENT_MD5), md5Base64(body));
    }

    private static JsonNode parseJson(byte[] data) throws Exception {
        return TosUtils.getJsonMapper().readTree(data);
    }

    @Test
    public void objectSetApis_buildCorrectRequests() throws Exception {
        try (MockWebServer server = new MockWebServer()) {
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("[{\"capname\":\"cap1\",\"endpoint\":[\"e1\"],\"s3endpoint\":[\"s3e1\"]}]")
            );
            server.enqueue(new MockResponse().setResponseCode(200));
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .addHeader(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP, "true")
                    .setBody("{\"Rules\":[{\"ID\":\"id\",\"Prefix\":\"p\",\"Status\":\"Enabled\"}]}")
            );
            server.enqueue(new MockResponse().setResponseCode(204));
            server.enqueue(new MockResponse().setResponseCode(200));
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .addHeader(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP, "false")
                    .setBody("{\"ObjectSetTagRules\":[{\"Tag\":{\"Key\":\"k\",\"Value\":\"v\"},\"Rules\":[{\"ID\":\"id\",\"Prefix\":\"p\",\"Status\":\"Enabled\"}]}]}")
            );
            server.enqueue(new MockResponse().setResponseCode(204));
            server.enqueue(new MockResponse().setResponseCode(200));
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("{\"PathLevel\":3,\"CustomDelimiter\":\"/\",\"EnableDefaultObjectSet\":true,\"Qos\":{\"ReadsQps\":1000,\"WritesQps\":1000,\"ListQps\":1000,\"ReadsRate\":1000,\"WritesRate\":1000},\"StorageQuota\":\"1024\"}")
            );
            server.enqueue(new MockResponse().setResponseCode(200));
            server.enqueue(new MockResponse().setResponseCode(204));
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("{\"IsTruncated\":false,\"NextMarker\":\"Next\",\"ObjectSets\":[{\"ObjectSetName\":\"a/b/c/\",\"TagSet\":{\"Tags\":[{\"Key\":\"key1\",\"Value\":\"value1\"}]}}]}")
            );
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("{\"ObjectSetName\":\"a/b/c/\",\"TagSet\":{\"Tags\":[{\"Key\":\"key1\",\"Value\":\"value1\"}]}}")
            );
            server.enqueue(new MockResponse().setResponseCode(200));
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("{\"ObjectSetName\":\"a/b/c/\",\"TagSet\":{\"Tags\":[{\"Key\":\"key1\",\"Value\":\"value1\"}]}}")
            );
            server.enqueue(new MockResponse().setResponseCode(200));
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("{\"Rules\":[{\"Tag\":{\"Key\":\"k\",\"Value\":\"v\"},\"Qos\":{\"ReadsQps\":1000,\"WritesQps\":1000,\"ListQps\":1000,\"ReadsRate\":1000,\"WritesRate\":1000},\"StorageQuota\":\"1024\"}]}")
            );
            server.enqueue(new MockResponse().setResponseCode(204));
            server.enqueue(new MockResponse().setResponseCode(200));
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("{\"StorageQuota\":\"1024\"}")
            );
            server.enqueue(new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody("{\"TotalStorageStat\":{\"Storage\":\"1024\",\"ObjectCount\":10},\"StandardStorageStat\":{\"Storage\":\"512\",\"ObjectCount\":5},\"IAStorageStat\":{\"Storage\":\"256\",\"ObjectCount\":3},\"ArchiveFrStorageStat\":{\"Storage\":\"128\",\"ObjectCount\":1},\"ArchiveStorageStat\":{\"Storage\":\"64\",\"ObjectCount\":1},\"ColdArchiveStat\":{\"Storage\":\"32\",\"ObjectCount\":0},\"DeepColdArchiveStorageStat\":{\"Storage\":\"0\",\"ObjectCount\":0},\"IntelligentTieringStorageStats\":{\"HighFreqStorageStat\":{\"Storage\":\"0\",\"ObjectCount\":0},\"LowFreqStorageStat\":{\"Storage\":\"0\",\"ObjectCount\":0},\"ArchiveStorageStat\":{\"Storage\":\"0\",\"ObjectCount\":0}}}")
            );

            server.start();

            TransportConfig transportConfig = TransportConfig.builder()
                    .maxRetryCount(0)
                    .readTimeoutMills(1000)
                    .writeTimeoutMills(1000)
                    .except100ContinueThreshold(0)
                    .build();

            String endpoint = server.url("/").toString();
            if (endpoint.endsWith("/")) {
                endpoint = endpoint.substring(0, endpoint.length() - 1);
            }

            TOSClientConfiguration conf = TOSClientConfiguration.builder()
                    .region("cn-beijing")
                    .endpoint(endpoint)
                    .transportConfig(transportConfig)
                    .build();
            TOSV2 client = new TOSV2ClientBuilder().build(conf);

            String bucket = "bucketname";
            String objectSetName = "a/b/c/";

            // 1) GetObjectSetEndpoint
            client.getObjectSetEndpoint(GetObjectSetEndpointInput.builder().bucket(bucket).objectSetName(objectSetName).build());
            RecordedRequest request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            HttpUrl url = request.getRequestUrl();
            Assert.assertTrue(url.encodedPath().equals("/" + bucket + "/") || url.encodedPath().equals("/" + bucket + "%2F"));
            Assert.assertEquals(url.queryParameter("objectsetendpoint"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), objectSetName);
            assertNoBody(request);

            // 2) PutObjectSetLifecycle
            LifecycleRule rule = new LifecycleRule().setId("id").setPrefix("p").setStatus(StatusType.STATUS_ENABLED);
            client.putObjectSetLifecycle(PutObjectSetLifecycleInput.builder()
                    .bucket(bucket)
                    .objectSetName(objectSetName)
                    .rules(Collections.singletonList(rule))
                    .allowSameActionOverlap(true)
                    .build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectset-lifecycle"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), objectSetName);
            Assert.assertEquals(request.getHeader(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP), "true");
            assertContentMd5(request);
            Assert.assertEquals(parseJson(request.getBody().clone().readByteArray()),
                    parseJson("{\"Rules\":[{\"ID\":\"id\",\"Prefix\":\"p\",\"Status\":\"Enabled\"}]}".getBytes(StandardCharsets.UTF_8)));

            // 3) GetObjectSetLifecycle
            client.getObjectSetLifecycle(GetObjectSetLifecycleInput.builder().bucket(bucket).objectSetName(objectSetName).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectset-lifecycle"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), objectSetName);
            assertNoBody(request);

            // 4) DeleteObjectSetLifecycle
            client.deleteObjectSetLifecycle(DeleteObjectSetLifecycleInput.builder().bucket(bucket).objectSetName(objectSetName).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "DELETE");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectset-lifecycle"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), objectSetName);
            assertNoBody(request);

            // 5) PutObjectSetLifecycleByTag
            ObjectSetTagLifecycleRule tagRule = new ObjectSetTagLifecycleRule()
                    .setTag(new Tag().setKey("k").setValue("v"))
                    .setRules(Collections.singletonList(rule));
            client.putObjectSetLifecycleByTag(PutObjectSetLifecycleByTagInput.builder()
                    .bucket(bucket)
                    .objectSetTagRules(Collections.singletonList(tagRule))
                    .allowSameActionOverlap(true)
                    .build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectset-lifecycle-bytag"), "");
            Assert.assertEquals(request.getHeader(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP), "true");
            assertContentMd5(request);

            // 6) GetObjectSetLifecycleByTag
            client.getObjectSetLifecycleByTag(GetObjectSetLifecycleByTagInput.builder().bucket(bucket).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectset-lifecycle-bytag"), "");
            assertNoBody(request);

            // 7) DeleteObjectSetLifecycleByTag
            client.deleteObjectSetLifecycleByTag(DeleteObjectSetLifecycleByTagInput.builder().bucket(bucket).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "DELETE");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectset-lifecycle-bytag"), "");
            assertNoBody(request);

            // 8) PutBucketObjectSetConfiguration
            ObjectSetQos qos = ObjectSetQos.builder().readsQps(1000).writesQps(1000).listQps(1000).readsRate(1000).writesRate(1000).build();
            client.putBucketObjectSetConfiguration(PutBucketObjectSetConfigurationInput.builder()
                    .bucket(bucket)
                    .pathLevel(3)
                    .customDelimiter("/")
                    .enableDefaultObjectSet(true)
                    .qos(qos)
                    .storageQuota("1024")
                    .build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsetconfiguration"), "");
            assertContentMd5(request);

            // 9) GetBucketObjectSetConfiguration
            client.getBucketObjectSetConfiguration(GetBucketObjectSetConfigurationInput.builder().bucket(bucket).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsetconfiguration"), "");
            assertNoBody(request);

            // Common TagSet
            TagSet tagSet = TagSet.builder().tags(Collections.singletonList(new Tag().setKey("key1").setValue("value1"))).build();

            // 10) PutObjectSet
            client.putObjectSet(PutObjectSetInput.builder().bucket(bucket).objectSetName(objectSetName).tagSet(tagSet).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectset"), "");
            assertContentMd5(request);

            // 11) DeleteObjectSet
            client.deleteObjectSet(DeleteObjectSetInput.builder().bucket(bucket).objectSetName(objectSetName).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "DELETE");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectset"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), objectSetName);
            assertNoBody(request);

            // 12) ListObjectSets
            client.listObjectSets(ListObjectSetsInput.builder()
                    .bucket(bucket)
                    .prefix("a/b/")
                    .tags("key1=value1")
                    .maxKeys(100)
                    .marker("marker")
                    .build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsets"), "");
            Assert.assertEquals(url.queryParameter("prefix"), "a/b/");
            Assert.assertEquals(url.queryParameter("tags"), "key1=value1");
            Assert.assertEquals(url.queryParameter("max-keys"), "100");
            Assert.assertEquals(url.queryParameter("marker"), "marker");
            assertNoBody(request);

            // 13) GetObjectSet
            client.getObjectSet(GetObjectSetInput.builder().bucket(bucket).objectSetName(objectSetName).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectset"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), objectSetName);
            assertNoBody(request);

            // 14) PutObjectSetTagging
            client.putObjectSetTagging(PutObjectSetTaggingInput.builder().bucket(bucket).objectSetName(objectSetName).tagSet(tagSet).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsettagging"), "");
            assertContentMd5(request);

            // 15) GetObjectSetTagging
            client.getObjectSetTagging(GetObjectSetTaggingInput.builder().bucket(bucket).objectSetName(objectSetName).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsettagging"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), objectSetName);
            assertNoBody(request);

            // 16) PutObjectSetQuotaByTag
            ObjectSetQuotaByTagRule quotaRule = new ObjectSetQuotaByTagRule()
                    .setTag(new Tag().setKey("k").setValue("v"))
                    .setQos(qos)
                    .setStorageQuota("1024");
            client.putObjectSetQuotaByTag(PutObjectSetQuotaByTagInput.builder().bucket(bucket).rules(Collections.singletonList(quotaRule)).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsetquotabytag"), "");
            assertContentMd5(request);

            // 17) GetObjectSetQuotaByTag
            client.getObjectSetQuotaByTag(GetObjectSetQuotaByTagInput.builder().bucket(bucket).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsetquotabytag"), "");
            assertNoBody(request);

            // 18) DeleteObjectSetQuotaByTag
            client.deleteObjectSetQuotaByTag(DeleteObjectSetQuotaByTagInput.builder().bucket(bucket).build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "DELETE");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsetquotabytag"), "");
            assertNoBody(request);

            // 19) PutObjectSetQuota
            client.putObjectSetQuota(PutObjectSetQuotaInput.builder().bucket(bucket).objectSetName("a/b/c").storageQuota("1024").build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "PUT");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsetquota"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), "a/b/c");
            assertContentMd5(request);

            // 20) GetObjectSetQuota
            client.getObjectSetQuota(GetObjectSetQuotaInput.builder().bucket(bucket).objectSetName("a/b/c").build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsetquota"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), "a/b/c");
            assertNoBody(request);

            // 21) GetObjectSetStorage
            client.getObjectSetStorage(GetObjectSetStorageInput.builder().bucket(bucket).objectSetName("a/b/c").build());
            request = server.takeRequest();
            Assert.assertEquals(request.getMethod(), "GET");
            url = request.getRequestUrl();
            Assert.assertEquals(url.queryParameter("objectsetstorage"), "");
            Assert.assertEquals(url.queryParameter("ObjectSetName"), "a/b/c");
            assertNoBody(request);
        }
    }
}

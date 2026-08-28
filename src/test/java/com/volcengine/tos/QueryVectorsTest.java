package com.volcengine.tos;

import com.volcengine.tos.comm.common.DataType;
import com.volcengine.tos.comm.common.DistanceMetricType;
import com.volcengine.tos.model.vectors.*;
import com.volcengine.tos.model.vectors.Vector;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeTest;

import static org.testng.Assert.*;
import org.testng.Assert;


import java.util.*;

public class QueryVectorsTest {
    private static final TOSVectors client = new TOSVectorsClientBuilder().build(Consts.region, Consts.vectorsEndpoint, Consts.accessKey, Consts.secretKey);
    private static final String accountId = Consts.accountId;

    private static final String testVectorBucketName = "test-" + System.currentTimeMillis();
    private static final String testIndexName = "test-query-vectors-index";

    private static void waitForFoundVectors(String vectorBucketName, String indexName, List<String> keys){
        /* 轮询直到查到向量 */
        long deadline = System.currentTimeMillis() + 120_000;
        List<Vector> found = new ArrayList<>();
        while (System.currentTimeMillis() < deadline) {
            GetVectorsInput input = GetVectorsInput.builder()
                    .vectorBucketName(vectorBucketName)
                    .indexName(indexName)
                    .accountId(accountId)
                    .keys(keys)
                    .returnData(true)
                    .build();
            GetVectorsOutput output = client.getVectors(input);
            Consts.LOG.info("get vectors output: {}", output);
            if (output.getVectors() != null && !output.getVectors().isEmpty()) {
                found = output.getVectors();
                Consts.LOG.info("found vectors: {}", found);
                break;
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }


    @BeforeTest
    public static void setup() throws Exception {
        // 创建测试用的向量存储桶
        CreateVectorBucketInput createBucketInput = CreateVectorBucketInput.builder()
                .vectorBucketName(testVectorBucketName)
                .build();
        client.createVectorBucket(createBucketInput);

        CreateIndexInput createIndexInput = CreateIndexInput.builder()
                .vectorBucketName(testVectorBucketName)
                .accountId(accountId)
                .indexName(testIndexName)
                .dimension(128)
                .dataType(DataType.DATA_TYPE_FLOAT32)
                .distanceMetric(DistanceMetricType.DISTANCE_METRIC_COSINE)
                .build();
        client.createIndex(createIndexInput);

        // 准备测试向量数据
        List<Vector> testVectors = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float[] vectorData = new float[128];
            for (int j = 0; j < 128; j++) {
                vectorData[j] = (float) Math.random();
            }
            
            Map<String, Object> metadata = new HashMap<>();
            metadata.put("category", i % 2 == 0 ? "A" : "B");
            metadata.put("timestamp", System.currentTimeMillis() - i * 1000);
            metadata.put("index", i);

            Vector vector = Vector.builder()
                    .key("test-vector-" + i)
                    .data(VectorData.builder().float32(vectorData).build())
                    .metadata(metadata)
                    .build();
            testVectors.add(vector);
        }

        // 上传测试向量
        PutVectorsInput putVectorsInput = PutVectorsInput.builder()
                .vectorBucketName(testVectorBucketName)
                .accountId(accountId)
                .indexName(testIndexName)
                .vectors(testVectors)
                .build();

        PutVectorsOutput putVectorsOutput = client.putVectors(putVectorsInput);
        Consts.LOG.info("put vectors output: {}", putVectorsOutput);

        waitForFoundVectors(testVectorBucketName, testIndexName, Arrays.asList("test-vector-0", "test-vector-1"));
    }

    @AfterTest
    public static void cleanup() throws Exception {
        // 删除测试索引
        DeleteIndexInput deleteIndexInput = DeleteIndexInput.builder()
                .vectorBucketName(testVectorBucketName)
                .accountId(accountId)
                .indexName(testIndexName)
                .build();
        try {
            client.deleteIndex(deleteIndexInput);
        } catch (Exception e) {
            // 忽略删除索引的错误
        }

        // 删除测试存储桶
        DeleteVectorBucketInput deleteBucketInput = DeleteVectorBucketInput.builder()
                .vectorBucketName(testVectorBucketName)
                .build();
        try {
            client.deleteVectorBucket(deleteBucketInput);
        } catch (Exception e) {
            // 忽略删除存储桶的错误
        }
    }

    @Test
    public void testQueryVectorsBasic() throws Exception {
        // 准备查询向量
        float[] queryVector = new float[128];
        for (int i = 0; i < 128; i++) {
            queryVector[i] = (float) Math.random();
        }

        QueryVectorsInput input = QueryVectorsInput.builder()
                .vectorBucketName(testVectorBucketName)
                .accountId(accountId)
                .indexName(testIndexName)
                .topK(10)
                .queryVector(VectorData.builder().float32(queryVector).build())
                .build();

        QueryVectorsOutput output = client.queryVectors(input);

        assertNotNull(output);
        assertNotNull(output.getRequestInfo());
        assertEquals(output.getRequestInfo().getStatusCode(), 200);
        assertNotNull(output.getVectors());
        assertFalse(output.getVectors().isEmpty());

        // 验证返回的向量结构
        DistanceVector vector = output.getVectors().get(0);
        assertNotNull(vector.getKey());
        assertNotNull(vector.getData());
        assertNull(vector.getDistance());
        assertNull(vector.getMetadata());
    }

    @Test
    public void testQueryVectorsWithFilter() throws Exception {
        // 准备查询向量
        float[] queryVector = new float[128];
        for (int i = 0; i < 128; i++) {
            queryVector[i] = (float) Math.random();
        }

        Map<String, Object> filter = new HashMap<>();
        filter.put("category", "A");

        QueryVectorsInput input = QueryVectorsInput.builder()
                .vectorBucketName(testVectorBucketName)
                .accountId(accountId)
                .indexName(testIndexName)
                .returnDistance(true)
                .returnMetadata(true)
                .topK(10)
                .queryVector(VectorData.builder().float32(queryVector).build())
                .filter(filter)
                .build();

        QueryVectorsOutput output = client.queryVectors(input);

        assertNotNull(output);
        assertNotNull(output.getVectors());

        // 验证过滤后的结果
        for (DistanceVector vector : output.getVectors()) {
            assertEquals("A", vector.getMetadata().get("category"));
        }
    }

    @Test
    public void testQueryVectorsWithoutDistance() throws Exception {
        // 准备查询向量
        float[] queryVector = new float[128];
        for (int i = 0; i < 128; i++) {
            queryVector[i] = (float) Math.random();
        }

        QueryVectorsInput input = QueryVectorsInput.builder()
                .vectorBucketName(testVectorBucketName)
                .accountId(accountId)
                .indexName(testIndexName)
                .returnDistance(false)
                .returnMetadata(true)
                .topK(3)
                .queryVector(VectorData.builder().float32(queryVector).build())
                .filter(new HashMap<>())
                .build();

        QueryVectorsOutput output = client.queryVectors(input);

        assertNotNull(output);
        assertNotNull(output.getVectors());

        DistanceVector vector = output.getVectors().get(0);
        assertNotNull(vector.getKey());
        assertNotNull(vector.getData());
        assertNotNull(vector.getMetadata());
        // distance字段应该存在，但值可能为0
        assertNull(vector.getDistance());
    }

    @Test
    public void testQueryVectorsWithoutMetadata() throws Exception {
        // 准备查询向量
        float[] queryVector = new float[128];
        for (int i = 0; i < 128; i++) {
            queryVector[i] = (float) Math.random();
        }

        QueryVectorsInput input = QueryVectorsInput.builder()
                .vectorBucketName(testVectorBucketName)
                .accountId(accountId)
                .indexName(testIndexName)
                .returnDistance(true)
                .returnMetadata(false)
                .topK(3)
                .queryVector(VectorData.builder().float32(queryVector).build())
                .filter(new HashMap<>())
                .build();

        QueryVectorsOutput output = client.queryVectors(input);

        assertNotNull(output);
        assertNotNull(output.getVectors());

        DistanceVector vector = output.getVectors().get(0);
        assertNotNull(vector.getKey());
        assertNotNull(vector.getData());
        assertTrue(vector.getDistance() >= 0);
        // metadata字段应该存在，但可能为空
        assertTrue(vector.getMetadata() == null || vector.getMetadata().isEmpty());
    }

    @Test
    public void testQueryVectorsDifferentTopK() throws Exception {
        // 准备查询向量
        float[] queryVector = new float[128];
        for (int i = 0; i < 128; i++) {
            queryVector[i] = (float) Math.random();
        }

        // 测试不同的topK值
        int[] topKValues = {1, 3, 5, 10};

        for (int topK : topKValues) {
            QueryVectorsInput input = QueryVectorsInput.builder()
                    .vectorBucketName(testVectorBucketName)
                    .accountId(accountId)
                    .indexName(testIndexName)
                    .returnDistance(true)
                    .returnMetadata(true)
                    .topK(topK)
                    .queryVector(VectorData.builder().float32(queryVector).build())
                    .filter(new HashMap<>())
                    .build();

            QueryVectorsOutput output = client.queryVectors(input);

            assertNotNull(output);
            assertNotNull(output.getVectors());
            assertTrue(output.getVectors().size() <= topK);
        }
    }

    @Test
    public void testQueryVectorsEmptyResult() throws Exception {
        // 准备查询向量
        float[] queryVector = new float[128];
        for (int i = 0; i < 128; i++) {
            queryVector[i] = (float) Math.random();
        }

        Map<String, Object> filter = new HashMap<>();
        filter.put("category", "NonExistentCategory");

        QueryVectorsInput input = QueryVectorsInput.builder()
                .vectorBucketName(testVectorBucketName)
                .accountId(accountId)
                .indexName(testIndexName)
                .returnDistance(true)
                .returnMetadata(true)
                .topK(5)
                .queryVector(VectorData.builder().float32(queryVector).build())
                .filter(filter)
                .build();

        QueryVectorsOutput output = client.queryVectors(input);

        assertNotNull(output);
        // 空结果时，vectors 可能为 null 或空列表
        assertTrue(output.getVectors() == null || output.getVectors().isEmpty());
    }
}

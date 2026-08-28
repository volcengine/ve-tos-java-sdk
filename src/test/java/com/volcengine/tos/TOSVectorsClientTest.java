package com.volcengine.tos;

import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.credential.EnvCredentialsProvider;
import com.volcengine.tos.credential.StaticCredentialsProvider;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.bucket.CreateBucketV2Input;
import com.volcengine.tos.model.bucket.GetBucketPolicyInput;
import com.volcengine.tos.model.bucket.ListBucketsV2Input;
import com.volcengine.tos.model.bucket.ListBucketsV2Output;
import com.volcengine.tos.model.vectors.*;
import com.volcengine.tos.comm.common.DataType;
import com.volcengine.tos.comm.common.DistanceMetricType;
import com.volcengine.tos.model.vectors.Vector;
import com.volcengine.tos.session.Session;
import com.volcengine.tos.session.SessionOptions;
import com.volcengine.tos.transport.TransportConfig;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
import org.testng.Assert;

import java.time.Instant;
import java.util.*;

public class TOSVectorsClientTest {
    private static final TOSVectors client = new TOSVectorsClientBuilder().build(Consts.region, Consts.vectorsEndpoint, Consts.accessKey, Consts.secretKey);
    private static final String accountId = Consts.accountId;

    private static void waitForFoundVectors(String vectorBucketName, String indexName){
        /* 轮询直到查到向量 */
        long deadline = System.currentTimeMillis() + 120_000;
        List<Vector> found = new ArrayList<>();
        while (System.currentTimeMillis() < deadline) {
            GetVectorsInput input = GetVectorsInput.builder()
                    .vectorBucketName(vectorBucketName)
                    .indexName(indexName)
                    .accountId(accountId)
                    .keys(Arrays.asList("vector1", "vector2"))
                    .returnData(true)
                    .build();
            GetVectorsOutput output = client.getVectors(input);
            Consts.LOG.info("get vectors output: {}", output);
            if (output.getVectors() != null && !output.getVectors().isEmpty()) {
                found = output.getVectors();
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

    @Test
    public void VectorBucketCrudTest() {
        String TEST_VECTOR_BUCKET = "java-sdk-" + System.currentTimeMillis();
        try {
            // 首先创建向量桶
            CreateVectorBucketInput createInput = CreateVectorBucketInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .build();
            CreateVectorBucketOutput createOutput = client.createVectorBucket(createInput);
            assertNotNull(createOutput);
            assertEquals(createOutput.getRequestInfo().getStatusCode(), 200);
            assertFalse(createOutput.getRequestInfo().getRequestId().isEmpty());

            // 测试获取向量桶信息
            GetVectorBucketInput getInput = GetVectorBucketInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .accountId(Consts.accountId)
                    .build();

            GetVectorBucketOutput getOutput = client.getVectorBucket(getInput);

            assertNotNull(getOutput);
            assertNotNull(getOutput.getRequestInfo());
            assertEquals(getOutput.getRequestInfo().getStatusCode(), 200);
            assertNotNull(getOutput.getVectorBucket());
            assertEquals(getOutput.getVectorBucket().getVectorBucketName(), TEST_VECTOR_BUCKET);
            assertNotNull(getOutput.getVectorBucket().getCreationTime());
            assertEquals(new Date().getDay(), getOutput.getVectorBucket().getCreationTime().getDay());
            assertNotNull(getOutput.getVectorBucket().getVectorBucketTrn());
            assertNotNull(getOutput.getVectorBucket().getProjectName());

             // 最后删除向量桶
            DeleteVectorBucketInput deleteInput = DeleteVectorBucketInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .accountId(Consts.accountId)
                    .build();
            DeleteVectorBucketOutput deleteOutput = client.deleteVectorBucket(deleteInput);
            assertNotNull(deleteOutput);
            assertEquals(deleteOutput.getRequestInfo().getStatusCode(), 200);

            try {
                // 再次获取向量桶信息，应该失败
                GetVectorBucketOutput getOutputAfterDelete = client.getVectorBucket(getInput);
                fail("Get vector bucket after delete should fail");
            } catch (TosException e) {
                Consts.LOG.info("Get vector bucket after delete failed as expected: " + e.getMessage());
                // 预期的异常，向量桶已被删除
                assertEquals(e.getStatusCode(), 404);
                Assert.assertEquals(e.getCode(), "VectorBucketNotFound");
            }
        } catch (TosException e) {
            fail("Get vector bucket failed: " + e.getMessage());
        }
    }

    @Test
    public void ListVectorBucketsTest() {
        String TEST_VECTOR_BUCKET = "java-sdk-" + System.currentTimeMillis();
        try {
            // 首先创建向量桶
            CreateVectorBucketInput createInput = CreateVectorBucketInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .build();
            CreateVectorBucketOutput createOutput = client.createVectorBucket(createInput);
            assertNotNull(createOutput);
            assertEquals(createOutput.getRequestInfo().getStatusCode(), 200);

            // 测试列举向量桶
            ListVectorBucketsInput listInput = ListVectorBucketsInput.builder()
                    .build();
            
            ListVectorBucketsOutput listOutput = client.listVectorBuckets(listInput);
            
            assertNotNull(listOutput);
            assertEquals(listOutput.getRequestInfo().getStatusCode(), 200);
            assertNotNull(listOutput.getVectorBuckets());
            assertFalse(listOutput.getVectorBuckets().isEmpty());
            
            // 验证返回的存储桶列表中包含刚创建的存储桶
            boolean foundBucket = false;
            for (com.volcengine.tos.model.vectors.VectorBucket bucket : listOutput.getVectorBuckets()) {
                assertNotNull(bucket.getVectorBucketTrn());
                assertNotNull(bucket.getCreationTime());
                assertNotNull(bucket.getProjectName());
                assertNotNull(bucket.getVectorBucketName());
                if (TEST_VECTOR_BUCKET.equals(bucket.getVectorBucketName())) {
                    foundBucket = true;
                }
            }
            assertTrue(foundBucket, "Created bucket should be found in list");

            // 清理测试数据
            DeleteVectorBucketInput deleteInput = DeleteVectorBucketInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .accountId(Consts.accountId)
                    .build();
            DeleteVectorBucketOutput deleteOutput = client.deleteVectorBucket(deleteInput);
            assertNotNull(deleteOutput);
            assertEquals(deleteOutput.getRequestInfo().getStatusCode(), 200);
            
        } catch (TosException e) {
            fail("List vector buckets failed: " + e.getMessage());
        }
    }

    @Test
    public void ListVectorBucketsPaginationTest() {
        // 测试分页列举向量桶
        ListVectorBucketsInput listInputPage1 = ListVectorBucketsInput.builder()
                .maxResults(2)
                .build();
        ListVectorBucketsOutput listOutputPage1 = client.listVectorBuckets(listInputPage1);

        assertNotNull(listOutputPage1);
        assertEquals(listOutputPage1.getVectorBuckets().size(), 2);
        assertNotNull(listOutputPage1.getNextToken());


        // 测试使用nextToken分页列举向量桶
        ListVectorBucketsInput listInputPage2 = ListVectorBucketsInput.builder()
                .maxResults(2)
                .nextToken(listOutputPage1.getNextToken())
                .build();
        ListVectorBucketsOutput listOutputPage2 = client.listVectorBuckets(listInputPage2);

        assertNotNull(listOutputPage2);
        assertEquals(listOutputPage2.getVectorBuckets().size(), 2);
        assertNotNull(listOutputPage2.getNextToken());

        // 验证listOutputPage1和listOutputPage2没有重复的bucket
        List<String> page1BucketNames = new ArrayList<>();
        for (com.volcengine.tos.model.vectors.VectorBucket bucket : listOutputPage1.getVectorBuckets()) {
            page1BucketNames.add(bucket.getVectorBucketName());
        }
        
        List<String> page2BucketNames = new ArrayList<>();
        for (com.volcengine.tos.model.vectors.VectorBucket bucket : listOutputPage2.getVectorBuckets()) {
            page2BucketNames.add(bucket.getVectorBucketName());
        }
        
        // 检查两个页面之间没有重复的bucket名称
        for (String bucketName : page1BucketNames) {
            assertFalse(page2BucketNames.contains(bucketName), 
                "Found duplicate bucket in page2: " + bucketName + ". Page1 buckets: " + page1BucketNames + ", Page2 buckets: " + page2BucketNames);
        }
    }

    @Test
    public void ListVectorPrefixTest(){
        String PREFIX1 = "test-prefix-1";
        String PREFIX2 = "test-prefix-2";
        String TEST_VECTOR_BUCKET1 = PREFIX1 + System.currentTimeMillis();
        String TEST_VECTOR_BUCKET2 = PREFIX2 + System.currentTimeMillis();


        CreateVectorBucketInput input = CreateVectorBucketInput.builder().vectorBucketName(TEST_VECTOR_BUCKET1).build();
        client.createVectorBucket(input);

        input = CreateVectorBucketInput.builder().vectorBucketName(TEST_VECTOR_BUCKET2).build();
        client.createVectorBucket(input);

        // 测试列举向量桶前缀
        ListVectorBucketsInput listInput = ListVectorBucketsInput.builder()
                .prefix(PREFIX1)
                .build();
        ListVectorBucketsOutput listOutput = client.listVectorBuckets(listInput);

        assertNotNull(listOutput);
        for (VectorBucket bucket : listOutput.getVectorBuckets()) {
            assertTrue(bucket.getVectorBucketName().startsWith(PREFIX1));
        }

       List<String> bucketNames = Arrays.asList(TEST_VECTOR_BUCKET1, TEST_VECTOR_BUCKET2);
       for (String bkt : bucketNames) {
           DeleteVectorBucketInput deleteInput = DeleteVectorBucketInput.builder()
                    .vectorBucketName(bkt)
                    .accountId(Consts.accountId)
                    .build();
            client.deleteVectorBucket(deleteInput);
       }

    }

    @Test
    public void VectorIndexCrudTest(){
        String TEST_VECTOR_BUCKET = "java-sdk-" + System.currentTimeMillis();
        CreateVectorBucketInput input = CreateVectorBucketInput.builder().vectorBucketName(TEST_VECTOR_BUCKET).build();
        client.createVectorBucket(input);
        // 创建索引
        String TEST_INDEX_COSINE_NAME = "test-index-cosine" + System.currentTimeMillis();
        String TEST_INDEX_EUCLIDEAN_NAME = "test-index-euclidean" + System.currentTimeMillis();
        CreateIndexInput createIndexInput = CreateIndexInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .indexName(TEST_INDEX_COSINE_NAME).accountId(accountId)
                .dataType(DataType.DATA_TYPE_FLOAT32)
                .dimension(128)
                .distanceMetric(DistanceMetricType.DISTANCE_METRIC_COSINE)
                .metadataConfiguration(new MetadataConfiguration().setNonFilterableMetadataKeys(Arrays.asList("key1", "key2")))
                .build();
        CreateIndexOutput createIndexOutput = client.createIndex(createIndexInput);
        Assert.assertNotNull(createIndexOutput);
        Assert.assertEquals(createIndexOutput.getRequestInfo().getStatusCode(), 200);

        GetIndexInput getIndexInput = GetIndexInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .indexName(TEST_INDEX_COSINE_NAME)
                .accountId(accountId)
                .build();

        GetIndexOutput getIndexOutput = client.getIndex(getIndexInput);
        Assert.assertNotNull(getIndexOutput);
        Assert.assertEquals(getIndexOutput.getRequestInfo().getStatusCode(), 200);
        Assert.assertNotNull(getIndexOutput.getIndex());
        Assert.assertNotNull(getIndexOutput.getIndex().getCreationTime());
        Assert.assertEquals(getIndexOutput.getIndex().getCreationTime().getDay(), new Date().getDay());
        Assert.assertFalse(getIndexOutput.getIndex().getIndexTrn().isEmpty());
        Assert.assertEquals(getIndexOutput.getIndex().getIndexName(), TEST_INDEX_COSINE_NAME);
        Assert.assertEquals(getIndexOutput.getIndex().getDistanceMetric(), DistanceMetricType.DISTANCE_METRIC_COSINE);
        Assert.assertEquals(getIndexOutput.getIndex().getDataType(), DataType.DATA_TYPE_FLOAT32);
        Assert.assertEquals(getIndexOutput.getIndex().getDimension(), 128);
        Assert.assertEquals(getIndexOutput.getIndex().getMetadataConfiguration().getNonFilterableMetadataKeys(), Arrays.asList("key1", "key2"));

        DeleteIndexInput deleteIndexInput = DeleteIndexInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .indexName(TEST_INDEX_COSINE_NAME)
                .accountId(accountId)
                .build();
        DeleteIndexOutput deleteIndexOutput = client.deleteIndex(deleteIndexInput);
        Assert.assertNotNull(deleteIndexOutput);
        Assert.assertEquals(deleteIndexOutput.getRequestInfo().getStatusCode(), 200);

        try {
            client.getIndex(getIndexInput);
            fail("Get index after delete should fail");
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), 404);
        }

        CreateIndexInput createIndexInputEuclidean = CreateIndexInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .indexName(TEST_INDEX_EUCLIDEAN_NAME).accountId(accountId)
                .dataType(DataType.DATA_TYPE_FLOAT32)
                .dimension(128)
                .distanceMetric(DistanceMetricType.DISTANCE_METRIC_EUCLIDEAN)
                .build();
        client.createIndex(createIndexInputEuclidean);

        GetIndexInput getIndexInputEuclidean = GetIndexInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .indexName(TEST_INDEX_EUCLIDEAN_NAME)
                .accountId(accountId)
                .build();
        GetIndexOutput getIndexOutputEuclidean = client.getIndex(getIndexInputEuclidean);
        Assert.assertNotNull(getIndexOutputEuclidean);
        Assert.assertEquals(getIndexOutputEuclidean.getRequestInfo().getStatusCode(), 200);
        Assert.assertNotNull(getIndexOutputEuclidean.getIndex());
        Assert.assertEquals(getIndexOutputEuclidean.getIndex().getIndexName(), TEST_INDEX_EUCLIDEAN_NAME);
        Assert.assertEquals(getIndexOutputEuclidean.getIndex().getDistanceMetric(), DistanceMetricType.DISTANCE_METRIC_EUCLIDEAN);

        DeleteIndexInput deleteIndexInputEuclidean = DeleteIndexInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .indexName(TEST_INDEX_EUCLIDEAN_NAME)
                .accountId(accountId)
                .build();
        client.deleteIndex(deleteIndexInputEuclidean);

        DeleteVectorBucketInput deleteVectorBucketInput = DeleteVectorBucketInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .accountId(accountId)
                .build();
        client.deleteVectorBucket(deleteVectorBucketInput);
    }

    @Test
    public void ListIndexesTest() {
        String TEST_VECTOR_BUCKET = "test-list-indexes-" + System.currentTimeMillis();
        CreateVectorBucketInput input = CreateVectorBucketInput.builder().vectorBucketName(TEST_VECTOR_BUCKET).build();
        client.createVectorBucket(input);
        
        // 创建几个测试索引用于列表测试
        String[] indexNames = {
            "test-index-1-" + System.currentTimeMillis(),
            "test-index-2-" + System.currentTimeMillis(),
            "prefix-index-1-" + System.currentTimeMillis(),
            "prefix-index-2-" + System.currentTimeMillis(),
        };
        
        for (String indexName : indexNames) {
            CreateIndexInput createIndexInput = CreateIndexInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .indexName(indexName).accountId(accountId)
                    .dataType(DataType.DATA_TYPE_FLOAT32)
                    .dimension(128)
                    .distanceMetric(DistanceMetricType.DISTANCE_METRIC_EUCLIDEAN)
                    .build();
            CreateIndexOutput createIndexOutput = client.createIndex(createIndexInput);
            Assert.assertNotNull(createIndexOutput);
            Assert.assertEquals(createIndexOutput.getRequestInfo().getStatusCode(), 200);
        }
        
        // 测试基本参数列表
        ListIndexesInput listIndexesInput = ListIndexesInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .accountId(accountId)
                .build();
        
        ListIndexesOutput listIndexesOutput = client.listIndexes(listIndexesInput);
        Assert.assertNotNull(listIndexesOutput);
        Assert.assertEquals(listIndexesOutput.getRequestInfo().getStatusCode(), 200);
        Assert.assertNotNull(listIndexesOutput.getIndexes());
        assertEquals(listIndexesOutput.getIndexes().size(), 4);

        IndexSummary index = listIndexesOutput.getIndexes().get(0);
        Assert.assertNotNull(index.getCreationTime());
        Assert.assertNotNull(index.getIndexName());
        Assert.assertFalse(index.getIndexTrn().isEmpty());
        Assert.assertNotNull(index.getVectorBucketName());
        Assert.assertEquals(index.getVectorBucketName(), TEST_VECTOR_BUCKET);
        
        // 测试maxResults参数限制结果
        ListIndexesInput listIndexesInputWithLimit = ListIndexesInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .accountId(accountId)
                .maxResults(2)
                .build();
        
        ListIndexesOutput listIndexesOutputWithLimit = client.listIndexes(listIndexesInputWithLimit);
        Assert.assertNotNull(listIndexesOutputWithLimit);
        Assert.assertEquals(listIndexesOutputWithLimit.getRequestInfo().getStatusCode(), 200);
        Assert.assertNotNull(listIndexesOutputWithLimit.getIndexes());
        assertEquals(listIndexesOutputWithLimit.getIndexes().size(), 2);
        
        // 测试prefix参数过滤结果
        ListIndexesInput listIndexesInputWithPrefix = ListIndexesInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .accountId(accountId)
                .prefix("prefix-")
                .build();
        
        ListIndexesOutput listIndexesOutputWithPrefix = client.listIndexes(listIndexesInputWithPrefix);
        Assert.assertNotNull(listIndexesOutputWithPrefix);
        Assert.assertEquals(listIndexesOutputWithPrefix.getRequestInfo().getStatusCode(), 200);
        Assert.assertNotNull(listIndexesOutputWithPrefix.getIndexes());
        
        // 验证所有返回的索引名称都以指定前缀开头
        for (IndexSummary indexItem : listIndexesOutputWithPrefix.getIndexes()) {
            Assert.assertTrue(indexItem.getIndexName().startsWith("prefix-"));
        }
        
        // 测试分页功能
        ListIndexesInput firstPageInput = ListIndexesInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .accountId(accountId)
                .maxResults(2)
                .build();
        
        ListIndexesOutput firstPageOutput = client.listIndexes(firstPageInput);
        Assert.assertNotNull(firstPageOutput);
        Assert.assertEquals(firstPageOutput.getRequestInfo().getStatusCode(), 200);
        Assert.assertNotNull(firstPageOutput.getIndexes());
        Assert.assertEquals(firstPageOutput.getIndexes().size(), 2);
        Assert.assertNotNull(firstPageOutput.getNextToken());
        
        // 如果有nextToken，继续列举下一页
        ListIndexesInput secondPageInput = ListIndexesInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .accountId(accountId)
                .maxResults(2)
                .nextToken(firstPageOutput.getNextToken())
                .build();

        ListIndexesOutput secondPageOutput = client.listIndexes(secondPageInput);
        Assert.assertNotNull(secondPageOutput);
        Assert.assertEquals(secondPageOutput.getRequestInfo().getStatusCode(), 200);
        Assert.assertNotNull(secondPageOutput.getIndexes());
        Assert.assertTrue(secondPageOutput.getIndexes().size() <= 2);

        // 验证第一页和第二页数据不重复
        List<String> firstPageNames = new ArrayList<>();
        for (IndexSummary indexItem : firstPageOutput.getIndexes()) {
            firstPageNames.add(indexItem.getIndexName());
        }
        List<String> secondPageNames = new ArrayList<>();
        for (IndexSummary indexItem : secondPageOutput.getIndexes()) {
            secondPageNames.add(indexItem.getIndexName());
        }

        for (String name : firstPageNames) {
            Assert.assertFalse(secondPageNames.contains(name));
        }
        
        // 清理：删除测试索引和向量桶
        for (String indexName : indexNames) {
            DeleteIndexInput deleteIndexInput = DeleteIndexInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .indexName(indexName)
                    .accountId(accountId)
                    .build();
            client.deleteIndex(deleteIndexInput);
        }
        
        DeleteVectorBucketInput deleteVectorBucketInput = DeleteVectorBucketInput.builder()
                .vectorBucketName(TEST_VECTOR_BUCKET)
                .accountId(accountId)
                .build();
        client.deleteVectorBucket(deleteVectorBucketInput);
    }

    @Test
    public void ListVectorsTest() {
        String TEST_VECTOR_BUCKET = "test-list-vectors-" + System.currentTimeMillis();
        String TEST_INDEX_NAME = "test-index-" + System.currentTimeMillis();
        
        try {
            // 创建向量桶
            CreateVectorBucketInput createBucketInput = CreateVectorBucketInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .build();
            CreateVectorBucketOutput createBucketOutput = client.createVectorBucket(createBucketInput);
            assertNotNull(createBucketOutput);
            assertEquals(createBucketOutput.getRequestInfo().getStatusCode(), 200);

            // 创建索引
            CreateIndexInput createIndexInput = CreateIndexInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .indexName(TEST_INDEX_NAME)
                    .accountId(accountId)
                    .dataType(DataType.DATA_TYPE_FLOAT32)
                    .dimension(128)
                    .distanceMetric(DistanceMetricType.DISTANCE_METRIC_EUCLIDEAN)
                    .build();
            CreateIndexOutput createIndexOutput = client.createIndex(createIndexInput);
            assertNotNull(createIndexOutput);
            assertEquals(createIndexOutput.getRequestInfo().getStatusCode(), 200);

            // 批量创建测试向量
            int testVectorsCount = 15;
            List<Vector> testVectors = new ArrayList<>();
            for (int i = 0; i < testVectorsCount; i++) {
                float[] vectorData = new float[128];
                for (int j = 0; j < 128; j++) {
                    vectorData[j] = (float) Math.random();
                }
                
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("index", i);
                metadata.put("category", i % 2 == 0 ? "electronics" : "clothing");
                metadata.put("timestamp", System.currentTimeMillis());
                
                Vector vector = Vector.builder()
                        .key(String.format("test-vector-%03d-%d", i, System.currentTimeMillis()))
                        .data(VectorData.builder().float32(vectorData).build())
                        .metadata(metadata)
                        .build();
                testVectors.add(vector);
            }

            // 批量上传向量
            PutVectorsInput putVectorsInput = PutVectorsInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .accountId(accountId)
                    .indexName(TEST_INDEX_NAME)
                    .vectors(testVectors)
                    .build();
            PutVectorsOutput putVectorsOutput = client.putVectors(putVectorsInput);
            assertNotNull(putVectorsOutput);
            assertEquals(putVectorsOutput.getRequestInfo().getStatusCode(), 200);

             // 等待向量写入完成
             waitForFoundVectors(TEST_VECTOR_BUCKET, TEST_INDEX_NAME);

            // 测试分页列举向量
            int pageSize = 5;
            
            // 第一页
            ListVectorsInput page1Input = ListVectorsInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .accountId(accountId)
                    .indexName(TEST_INDEX_NAME)
                    .maxResults(pageSize)
                    .build();
            
            ListVectorsOutput page1Output = client.listVectors(page1Input);
            assertNotNull(page1Output);
            assertEquals(page1Output.getRequestInfo().getStatusCode(), 200);
            assertNotNull(page1Output.getVectors());
            assertEquals(page1Output.getVectors().size(), pageSize);
            assertNotNull(page1Output.getNextToken());
            for (Vector vector : page1Output.getVectors()) {
                assertNotNull(vector.getKey());
                assertNull(vector.getData());
                assertNull(vector.getMetadata());
            }

            // 第二页
            ListVectorsInput page2Input = ListVectorsInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .accountId(accountId)
                    .indexName(TEST_INDEX_NAME)
                    .maxResults(pageSize)
                    .nextToken(page1Output.getNextToken())
                    .returnData(true)
                    .returnMetadata(false)
                    .build();
            
            ListVectorsOutput page2Output = client.listVectors(page2Input);
            assertNotNull(page2Output);
            assertEquals(page2Output.getRequestInfo().getStatusCode(), 200);
            assertNotNull(page2Output.getVectors());
            assertEquals(page2Output.getVectors().size(), pageSize);
            assertNotNull(page2Output.getNextToken());
            for (Vector vector : page2Output.getVectors()) {
                assertNotNull(vector.getKey());
                assertNotNull(vector.getData());
                assertNull(vector.getMetadata());
            }

            // 验证没有重复向量
            List<String> page1Keys = new ArrayList<>();
            for (Vector vector : page1Output.getVectors()) {
                page1Keys.add(vector.getKey());
            }
            List<String> page2Keys = new ArrayList<>();
            for (Vector vector : page2Output.getVectors()) {
                page2Keys.add(vector.getKey());
            }
            
            for (String key : page1Keys) {
                assertFalse(page2Keys.contains(key), "Found duplicate vector key between pages");
            }

            // 第三页（最后一页）
            ListVectorsInput page3Input = ListVectorsInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .accountId(accountId)
                    .indexName(TEST_INDEX_NAME)
                    .maxResults(pageSize)
                    .nextToken(page2Output.getNextToken())
                    .returnData(false)
                    .returnMetadata(true)
                    .build();
            
            ListVectorsOutput page3Output = client.listVectors(page3Input);
            assertNotNull(page3Output);
            assertEquals(page3Output.getRequestInfo().getStatusCode(), 200);
            assertNotNull(page3Output.getVectors());
            assertFalse(page3Output.getVectors().isEmpty());
            for (Vector vector : page3Output.getVectors()) {
                assertNotNull(vector.getKey());
                assertNull(vector.getData());
                assertNotNull(vector.getMetadata());
            }

            // 测试不带可选参数列举向量
            ListVectorsInput basicInput = ListVectorsInput.builder()
                    .vectorBucketName(TEST_VECTOR_BUCKET)
                    .accountId(accountId)
                    .indexName(TEST_INDEX_NAME)
                    .build();
            
            ListVectorsOutput basicOutput = client.listVectors(basicInput);
            assertNotNull(basicOutput);
            assertEquals(basicOutput.getRequestInfo().getStatusCode(), 200);
            assertNotNull(basicOutput.getVectors());
            assertTrue(basicOutput.getVectors().size() >= testVectorsCount);
        } catch (TosException e) {
            fail("List vectors test failed: " + e.getMessage());
        } finally {
            // 清理测试环境
            try {
                // 删除索引
                DeleteIndexInput deleteIndexInput = DeleteIndexInput.builder()
                        .vectorBucketName(TEST_VECTOR_BUCKET)
                        .indexName(TEST_INDEX_NAME)
                        .accountId(accountId)
                        .build();
                client.deleteIndex(deleteIndexInput);
                
                // 删除向量桶
                DeleteVectorBucketInput deleteBucketInput = DeleteVectorBucketInput.builder()
                        .vectorBucketName(TEST_VECTOR_BUCKET)
                        .accountId(accountId)
                        .build();
                client.deleteVectorBucket(deleteBucketInput);
            } catch (TosException e) {
                Consts.LOG.info("Cleanup failed (expected): " + e.getMessage());
            }
        }
    }

    @Test
    void putVectorBucketPolicyTest() {
        String TEST_BUCKET_NAME = "java-sdk" + System.currentTimeMillis();

        // 创建测试向量存储桶
        CreateVectorBucketInput createVectorBucketInput = CreateVectorBucketInput.builder()
                .vectorBucketName(TEST_BUCKET_NAME)
                .build();
        client.createVectorBucket(createVectorBucketInput);

        // 测试设置存储桶策略
        String policy = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":[\"\"],\"Action\":\"tosvectors:GetVectorBucket\",\"Resource\":\"trn:tosvectors:"+ Consts.region +":"+ accountId + ":bucket/" + TEST_BUCKET_NAME + "\"}]}";

        PutVectorBucketPolicyInput putPolicyInput = PutVectorBucketPolicyInput.builder()
                .vectorBucketName(TEST_BUCKET_NAME)
                .accountId(accountId)
                .policy(policy)
                .build();

        PutVectorBucketPolicyOutput putPolicyOutput = client.putVectorBucketPolicy(putPolicyInput);
        Assert.assertEquals(putPolicyOutput.getRequestInfo().getStatusCode(), 200);

        GetVectorBucketPolicyInput getPolicyInput = GetVectorBucketPolicyInput.builder()
                .vectorBucketName(TEST_BUCKET_NAME)
                .accountId(accountId)
                .build();

        GetVectorBucketPolicyOutput getPolicyOutput = client.getVectorBucketPolicy(getPolicyInput);
        Assert.assertEquals(getPolicyOutput.getRequestInfo().getStatusCode(), 200);
        Consts.LOG.info("getPolicyOutput: {}", getPolicyOutput.getPolicy());
        Assert.assertTrue(getPolicyOutput.getPolicy().contains("\"Effect\":\"Allow\""));
        Assert.assertTrue(getPolicyOutput.getPolicy().contains("\"Action\":\"tosvectors:GetVectorBucket\""));
        Assert.assertTrue(getPolicyOutput.getPolicy().contains("\"Resource\":\"trn:tosvectors:"+ Consts.region +":"+ accountId + ":bucket/" + TEST_BUCKET_NAME + "\""));

        DeleteVectorBucketPolicyInput deletePolicyInput = DeleteVectorBucketPolicyInput.builder()
                .vectorBucketName(TEST_BUCKET_NAME)
                .accountId(accountId)
                .build();
        DeleteVectorBucketPolicyOutput deletePolicyOutput = client.deleteVectorBucketPolicy(deletePolicyInput);
        Assert.assertEquals(deletePolicyOutput.getRequestInfo().getStatusCode(), 200);

        try {
            client.getVectorBucketPolicy(getPolicyInput);
            Assert.fail("Get vector bucket policy should fail after delete");
        } catch (TosException e) {
            Assert.assertEquals(e.getStatusCode(), 404);
        }

        // 清理测试资源
        DeleteVectorBucketInput deleteVectorBucketInput = DeleteVectorBucketInput.builder()
                .vectorBucketName(TEST_BUCKET_NAME)
                .accountId(accountId)
                .build();
        client.deleteVectorBucket(deleteVectorBucketInput);
    }

    @Test
    void vectorBucketNameValidTest() {
        String longLengthBucketName = StringUtils.randomString(33);
        List<String> bucketNameInvalidList = Arrays.asList(null, "", "1", longLengthBucketName);
        for (String name : bucketNameInvalidList) {
            try {
                CreateVectorBucketInput input = CreateVectorBucketInput.builder().vectorBucketName(name).build();
                client.createVectorBucket(input);
            } catch (Exception e) {
                Assert.assertEquals(e.getMessage(), "invalid vector bucket name, the length must be [3, 32]");
            }
        }
        try {
            GetVectorBucketInput input = GetVectorBucketInput.builder().vectorBucketName("-dasd").build();
            client.getVectorBucket(input);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "invalid vector bucket name, the vector bucket name can be neither starting with '-' nor ending with '-'");
        }
        bucketNameInvalidList = Arrays.asList("&*(%^&", "ABCD", "中文测试");
        for (String name : bucketNameInvalidList) {
            Consts.LOG.info("create index name: {}", name);
            try {
               DeleteVectorBucketInput input = DeleteVectorBucketInput.builder().vectorBucketName(name).build();
                client.deleteVectorBucket(input);
            } catch (Exception e) {
                Assert.assertEquals(e.getMessage(), "invalid vector bucket name, the character set is illegal");
            }
        }
    }

    @Test
    void accountIdValidTest() {
        String validAccountId = "1233a124";
        try {
            GetVectorBucketInput input = GetVectorBucketInput.builder().vectorBucketName("abcd").accountId(validAccountId).build();
            client.getVectorBucket(input);
        } catch (Exception e) {
            Assert.assertEquals(e.getMessage(), "invalid account id, the account id must be a number");
        }
    }

    @Test
    void testUserAgent() {
        Map<String, String> m = new HashMap<>();
        m.put("cloud_type", "aliyun");
        m.put("cloud_region", "hangzhou");
        TOSVectors cli = new TOSVectorsClientBuilder().build(TOSVectorsClientConfiguration.builder().region(Consts.region).endpoint(Consts.vectorsEndpoint)
                .credentialsProvider(new StaticCredentialsProvider(Consts.accessKey, Consts.secretKey)).
                transportConfig(new TransportConfig())
                .userAgentProductName("EMR").userAgentSoftName("Hadoop")
                .userAgentSoftVersion("v3.0.0").userAgentCustomizedKeyValues(m).build());
        ListVectorBucketsInput input = ListVectorBucketsInput.builder().build();
        ListVectorBucketsOutput o = cli.listVectorBuckets(input);
        Assert.assertTrue(o.getRequestInfo().getRequestId().length() > 0);

        input.setRequestDate(new Date());
        o = cli.listVectorBuckets(input);
        Assert.assertTrue(o.getRequestInfo().getRequestId().length() > 0);

        try {
            input.setRequestDate(Date.from(Instant.now().minusSeconds(3600)));
            cli.listVectorBuckets(input);
            Assert.assertTrue(false);
        } catch (TosServerException ex) {
            Assert.assertEquals(ex.getStatusCode(), 403);
            Assert.assertEquals(ex.getCode(), "RequestTimeTooSkewed");
        }

        input = new ListVectorBucketsInput();
        cli = new TOSVectorsClientBuilder().build(TOSVectorsClientConfiguration.builder().region(Consts.region).endpoint(Consts.vectorsEndpoint)
                .credentialsProvider(new EnvCredentialsProvider()).
                transportConfig(new TransportConfig()).build());
        try {
            o = cli.listVectorBuckets(input);
            Assert.assertTrue(o.getRequestInfo().getRequestId().length() > 0);
        } catch (TosServerException ex) {
            Assert.assertTrue(ex.getRequestID().length() > 0);
        }

        ListVectorBucketsOutput output = client.listVectorBuckets(input);
        Assert.assertTrue(output.getRequestInfo().getRequestId().length() > 0);

        input.setRequestHost("www.baidu.com");
        try {
            client.listVectorBuckets(input);
            Assert.assertTrue(false);
        } catch (TosServerException ex) {
            Assert.assertTrue(ex.getRequestID().length() > 0);
            Assert.assertEquals(ex.getStatusCode(), 412);
        }
    }
}

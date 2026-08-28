package com.volcengine.tos;

import com.volcengine.tos.comm.common.DataType;
import com.volcengine.tos.comm.common.DistanceMetricType;
import com.volcengine.tos.model.vectors.*;
import com.volcengine.tos.model.vectors.Vector;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class TOSVectorsTest {
    private static TOSVectors client = new TOSVectorsClientBuilder().build(Consts.region, Consts.vectorsEndpoint, Consts.accessKey, Consts.secretKey);
    private static String accountId = Consts.accountId;

    public static float[] createRandomFloatArray(int length) {
        float[] arr = new float[length];
        Random rand = new Random();
        for (int i = 0; i < length; i++) {
            arr[i] = rand.nextFloat();
        }
        return arr;
    }

    @Test
    void tosVectorsCrudTest() {
        String TEST_BUCKET_NAME = "test-vectors-bucket" + System.currentTimeMillis();
        String TEST_INDEX_NAME = "test-vectors-index" + System.currentTimeMillis();
        CreateVectorBucketInput createVectorBucketInput = CreateVectorBucketInput.builder()
                .vectorBucketName(TEST_BUCKET_NAME)
                .build();
        client.createVectorBucket(createVectorBucketInput);

        CreateIndexInput createIndexInput = CreateIndexInput.builder()
                .vectorBucketName(TEST_BUCKET_NAME)
                .indexName(TEST_INDEX_NAME)
                .accountId(accountId)
                .dataType(DataType.DATA_TYPE_FLOAT32)
                .dimension(128)
                .distanceMetric(DistanceMetricType.DISTANCE_METRIC_COSINE)
                .build();
        client.createIndex(createIndexInput);

        Map<String, Object> metaData1 = new HashMap<>();
        metaData1.put("category", "electronics");
        metaData1.put("timestamp", System.currentTimeMillis());
        metaData1.put("source", "user-upload");

        Map<String, Object> metaData2 = new HashMap<>();
        metaData2.put("category", "clothing");
        metaData2.put("timestamp", System.currentTimeMillis());
        metaData2.put("source", "batch-import");

        VectorData vectorData1 = VectorData.builder().float32(createRandomFloatArray(128)).build();
        VectorData vectorData2 = VectorData.builder().float32(createRandomFloatArray(128)).build();


        List<Vector> vectors = Arrays.asList(
                Vector.builder().key("vector1").data(vectorData1).metadata(metaData1).build(),
                Vector.builder().key("vector2").data(vectorData2).metadata(metaData2).build()
        );

        PutVectorsInput putVectorsInput = PutVectorsInput.builder()
                .vectorBucketName(TEST_BUCKET_NAME)
                .indexName(TEST_INDEX_NAME)
                .accountId(accountId)
                .vectors(vectors)
                .build();
        PutVectorsOutput putVectorsOutput = client.putVectors(putVectorsInput);
        Assert.assertEquals(putVectorsOutput.getRequestInfo().getStatusCode(), 200);

        /* 轮询直到查到向量 */
        long deadline = System.currentTimeMillis() + 120_000;
        List<Vector> found = new ArrayList<>();
        while (System.currentTimeMillis() < deadline) {
            GetVectorsInput input = GetVectorsInput.builder()
                    .vectorBucketName(TEST_BUCKET_NAME)
                    .indexName(TEST_INDEX_NAME)
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

        Assert.assertEquals(found.get(0).getKey(), "vector1");
        Assert.assertNull(found.get(0).getMetadata());
        Assert.assertEquals(found.get(0).getData().getFloat32(), vectorData1.getFloat32());

        GetVectorsInput getVectorsOutputWithMetaDataInput = GetVectorsInput.builder()
                    .vectorBucketName(TEST_BUCKET_NAME)
                    .indexName(TEST_INDEX_NAME)
                    .accountId(accountId)
                    .keys(Arrays.asList("vector1", "vector2"))
                    .returnMetadata(true)
                    .build();

        GetVectorsOutput getVectorsOutputWithMetaDataOutput = client.getVectors(getVectorsOutputWithMetaDataInput);
        Assert.assertEquals(getVectorsOutputWithMetaDataOutput.getVectors().get(0).getMetadata(), metaData1);
        Assert.assertEquals(getVectorsOutputWithMetaDataOutput.getVectors().get(1).getMetadata(), metaData2);
        Assert.assertNull(getVectorsOutputWithMetaDataOutput.getVectors().get(0).getData());
        Assert.assertNull(getVectorsOutputWithMetaDataOutput.getVectors().get(1).getData());


        DeleteVectorsInput deleteVectorsInput = DeleteVectorsInput.builder()
                    .vectorBucketName(TEST_BUCKET_NAME)
                    .indexName(TEST_INDEX_NAME)
                    .accountId(accountId)
                    .keys(Arrays.asList("vector1", "vector2"))
                    .build();
        DeleteVectorsOutput deleteVectorsOutput = client.deleteVectors(deleteVectorsInput);
        Assert.assertEquals(deleteVectorsOutput.getRequestInfo().getStatusCode(), 200);

        GetVectorsInput getVectorsInputAfterDelete = GetVectorsInput.builder()
                    .vectorBucketName(TEST_BUCKET_NAME)
                    .indexName(TEST_INDEX_NAME)
                    .accountId(accountId)
                    .keys(Arrays.asList("vector1", "vector2"))
                    .returnData(true)
                    .build();
        /* 轮询直到查到向量 */
        long deadline2 = System.currentTimeMillis() + 120_000;
        boolean deleteSuccess = false;
        while (System.currentTimeMillis() < deadline2) {
            GetVectorsOutput getVectorsOutputAfterDelete = client.getVectors(getVectorsInputAfterDelete);
            Consts.LOG.info("get vectors output after delete: {}", getVectorsOutputAfterDelete);
             if (getVectorsOutputAfterDelete.getVectors() == null || getVectorsOutputAfterDelete.getVectors().isEmpty()) {
                 deleteSuccess = true;
                 break;
             }

             try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }
        Assert.assertTrue(deleteSuccess);

        DeleteIndexInput deleteIndexInput = DeleteIndexInput.builder()
                    .vectorBucketName(TEST_BUCKET_NAME)
                    .indexName(TEST_INDEX_NAME)
                    .accountId(accountId)
                    .build();
        client.deleteIndex(deleteIndexInput);

        DeleteVectorBucketInput deleteVectorBucketInput = DeleteVectorBucketInput.builder()
                    .vectorBucketName(TEST_BUCKET_NAME)
                    .accountId(accountId)
                    .build();
        client.deleteVectorBucket(deleteVectorBucketInput);
    }

//    @Test
//    void clearVectorBucket(){
//        ListVectorBucketsOutput listVectorBucketsOutput = client.listVectorBuckets(ListVectorBucketsInput.builder().accountID(accountId).maxResults(100).build());
//        for (VectorBucket vectorBucketInfo : listVectorBucketsOutput.getVectorBuckets()) {
//            ListIndexesInput listIndexesInput = ListIndexesInput.builder()
//                    .vectorBucketName(vectorBucketInfo.getVectorBucketName())
//                    .accountID(accountId)
//                    .maxResults(100)
//                    .build();
//             ListIndexesOutput listIndexesOutput = client.listIndexes(listIndexesInput);
//             if (listIndexesOutput.getIndexes() != null) {
//                 for (IndexSummary indexInfo : listIndexesOutput.getIndexes()) {
//                     DeleteIndexInput deleteIndexInput = DeleteIndexInput.builder()
//                             .vectorBucketName(vectorBucketInfo.getVectorBucketName())
//                             .indexName(indexInfo.getIndexName())
//                             .accountID(accountId)
//                             .build();
//                     client.deleteIndex(deleteIndexInput);
//                 }
//             }
//             DeleteVectorBucketInput deleteVectorBucketInput = DeleteVectorBucketInput.builder()
//                     .vectorBucketName(vectorBucketInfo.getVectorBucketName())
//                     .accountID(accountId)
//                     .build();
//             try {
//                 client.deleteVectorBucket(deleteVectorBucketInput);
//             } catch (TosServerException e) {
//                 Consts.LOG.error("delete vector bucket failed, vector bucket name: {}, error: {}", vectorBucketInfo.getVectorBucketName(), e);
//             }
//        }
//    }
}

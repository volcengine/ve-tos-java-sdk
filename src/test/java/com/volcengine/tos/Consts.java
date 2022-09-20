package com.volcengine.tos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consts {
    public static final Logger LOG = LoggerFactory.getLogger(Consts.class);
    public static final ObjectMapper JSON = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

    // boe
    public static final String bucket = System.getenv("TEST_BUCKET");
    public static final String endpoint = System.getenv("ENDPOINT");
    public static final String region = System.getenv("REGION");
    public static final String accessKey = System.getenv("ACCESS_KEY");
    public static final String secretKey = System.getenv("SECRET_KEY");
    public static final String bucketCopy = System.getenv("TEST_BUCKET_COPY");

    // for internal package uts
    public static final String internalBucketCrudPrefix = "internal-bucket-crud-test-prefix-";
    public static final String internalObjectCrudPrefix = "internal-object-crud-test-prefix-";
    public static final String internalObjectListPrefix = "internal-object-list-test-prefix-";
    public static final String internalObjectListVersionsPrefix = "internal-object-list-versions-test-prefix-";
    public static final String internalObjectAppendPrefix = "internal-object-append-test-prefix-";
    public static final String internalObjectCopyPrefix = "internal-object-copy-test-prefix-";
    public static final String internalObjectMultiPartPrefix = "internal-object-multipart-test-prefix-";

    // for integration test
    public static final String bucketCrudPrefix = "bucket-crud-test-prefix-";
    public static final String objectCrudPrefix = "object-crud-test-prefix-";
    public static final String objectListPrefix = "object-list-test-prefix-";
    public static final String objectListVersionsPrefix = "object-list-versions-test-prefix-";
    public static final String objectAppendPrefix = "object-append-test-prefix-";
    public static final String objectMultiPartPrefix = "object-multipart-test-prefix-";
}

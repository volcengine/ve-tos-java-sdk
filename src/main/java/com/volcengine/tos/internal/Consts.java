package com.volcengine.tos.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface Consts {
    int DEFAULT_READ_BUFFER_SIZE = 8192;

    String SCHEME_HTTP = "http";
    String SCHEME_HTTPS = "https";
    String SDK_NAME = "ve-tos-java-sdk";
    String SDK_VERSION = "v2.1.1";

    int MIN_PART_NUM = 1;
    int MAX_PART_NUM = 10000;
    int MIN_PART_SIZE = 5 * 1024 * 1024;


    int MAX_OBJECT_KEY_LENGTH = 696;
    int MAX_BUCKET_NAME_LENGTH = 63;
    int MIN_BUCKET_NAME_LENGTH = 3;

    Set<String> customServerSideEncryptionAlgorithmList = new HashSet<>(Collections.singletonList("AES256"));
}

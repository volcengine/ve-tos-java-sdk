package com.volcengine.tos.internal;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public interface Consts {
    int DEFAULT_READ_BUFFER_SIZE = 8192;
    int DEFAULT_TOS_BUFFER_STREAM_SIZE = 512 * 1024;

    String SCHEME_HTTP = "http";
    String SCHEME_HTTPS = "https";
    String SDK_NAME = "ve-tos-java-sdk";
    String SDK_VERSION = "v2.8.6";
    String OS_NAME = System.getProperty("os.name");
    String OS_ARCH = System.getProperty("os.arch");
    String JAVA_VERSION = System.getProperty("java.version", "0");

    String SDK_LOG_NAMESPACE = "com.volcengine.tos";
    String TOS_CHUNKED = "tos-chunked";
    String STREAMING_UNSIGNED_PAYLOAD_TRAILER = "STREAMING-UNSIGNED-PAYLOAD-TRAILER";
    String TOS_RAW_TRAILER = "tos-raw-trailer";
    int DEFAULT_TOS_CHUNK_SIZE = 64 * 1024;

    int MIN_PART_NUM = 1;
    int MAX_PART_NUM = 10000;
    int MIN_PART_SIZE = 5 * 1024 * 1024;
    long MAX_PART_SIZE = 5L * 1024 * 1024 * 1024;
    int DEFAULT_PART_SIZE = 20 * 1024 * 1024;

    int MAX_TASK_NUM = 1000;
    int MIN_TASK_NUM = 1;

    int MAX_OBJECT_KEY_LENGTH = 696;
    int MAX_BUCKET_NAME_LENGTH = 63;
    int MIN_BUCKET_NAME_LENGTH = 3;

    int DEFAULT_MIN_RATE_LIMITER_CAPACITY = 10 * 1024;
    int DEFAULT_MIN_RATE_LIMITER_RATE = 1024;

    int DEFAULT_PROGRESS_CALLBACK_SIZE = 512 * 1024;

    String TEMP_FILE_SUFFIX = ".temp";
    String UPLOAD_CHECKPOINT_FILE_SUFFIX = ".upload";
    String DOWNLOAD_CHECKPOINT_FILE_SUFFIX = ".download";
    String COPY_CHECKPOINT_FILE_SUFFIX = ".copy";

    /**
     * URL_MODE_DEFAULT url pattern is http(s)://{bucket}.domain/{object}
     */
    int URL_MODE_DEFAULT = 0;
    /**
     * URL_MODE_PATH url pattern is http(s)://domain/{bucket}/{object}
     */
    int URL_MODE_PATH = 1;
    /**
     * URL_MODE_CUSTOM_DOMAIN url pattern is http(s)://domain/{object}
     */
    int URL_MODE_CUSTOM_DOMAIN = 2;

    int DEFAULT_HTTPS_PORT = 443;

    Set<String> CUSTOM_SERVER_SIDE_ENCRYPTION_ALGORITHM_LIST = new HashSet<>(Collections.singletonList("AES256"));

    String USE_COMPLETE_ALL = "yes";

    boolean DEFAULT_AUTO_RECOGNIZE_CONTENT_TYPE = true;
    boolean DEFAULT_ENABLE_CRC = true;
    boolean DEFAULT_DISABLE_TRAILER_HEADER = true;
    boolean DEFAULT_ENABLE_VERIFY_SSL = true;

    int DEFAULT_MAX_CONNECTIONS = 1024;
    int DEFAULT_IDLE_CONNECTION_TIME_MILLS = 60000;
    int DEFAULT_CONNECT_TIMEOUT_MILLS = 10000;
    int DEFAULT_READ_TIMEOUT_MILLS = 30000;
    int DEFAULT_WRITE_TIMEOUT_MILLS = 30000;
    int DEFAULT_MAX_RETRY_COUNT = 3;
    int DEFAULT_EXPECT_100_CONTINUE_THRESHOLD = 65536;
    int DEFAULT_HIGH_LATENCY_LOG_THRESHOLD = 100;

    String DEFAULT_USER_AGENT_ITEM = "undefined";
    String SYMLINK = "Symlink";

    // EC Code
    String EcObjForbidOverwriteErr = "0017-00000004"; // 目标对象已经存在，不允许覆盖
    String EcNoSuchObjectErr = "0017-00000003"; // 对象不存在
}

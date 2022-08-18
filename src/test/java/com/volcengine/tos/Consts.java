package com.volcengine.tos;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Consts {
    public static final Logger LOG = LoggerFactory.getLogger(Consts.class);
    public static final ObjectMapper JSON = new ObjectMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    public static final String bucket = "";
    public static final String endpoint = "";
    public static final String region = "";
    public static final String accessKey = "";
    public static final String secretKey = "";
    public static final String bucketCopy = "";
}

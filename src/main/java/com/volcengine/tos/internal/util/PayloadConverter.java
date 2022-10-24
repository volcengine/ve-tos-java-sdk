package com.volcengine.tos.internal.util;

import org.apache.commons.codec.binary.Base64;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.internal.TosMarshalResult;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.volcengine.tos.internal.util.TosUtils.JSON;

public class PayloadConverter {
    public static TosMarshalResult serializePayloadAndComputeMD5(Object input) throws TosClientException {
        byte[] content;
        String contentMD5;
        try{
            content = JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(input);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(content);
            contentMD5 = new String(Base64.encodeBase64(bytes));
        } catch (JsonProcessingException | NoSuchAlgorithmException e){
            throw new TosClientException("tos: unable to do serialization", e);
        }
        return new TosMarshalResult(contentMD5, content);
    }

    public static TosMarshalResult serializePayload(Object input) throws TosClientException {
        byte[] content;
        try{
            content = JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(input);
        } catch (JsonProcessingException e){
            throw new TosClientException("tos: unable to do serialization", e);
        }
        return new TosMarshalResult("", content);
    }

    public static <T> T parsePayload(InputStream reader, TypeReference<T> valueTypeRef) throws TosClientException {
        try{
            return JSON.readValue(reader, valueTypeRef);
        } catch (IOException e){
            throw new TosClientException("tos: unable to do deserialization", e);
        }
    }
}

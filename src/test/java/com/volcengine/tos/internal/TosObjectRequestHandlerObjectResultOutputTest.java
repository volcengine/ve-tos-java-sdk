package com.volcengine.tos.internal;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.model.object.CopyObjectV2Output;
import com.volcengine.tos.model.object.FetchObjectOutput;
import com.volcengine.tos.model.object.UploadPartCopyV2Input;
import com.volcengine.tos.model.object.UploadPartCopyV2Output;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TosObjectRequestHandlerObjectResultOutputTest {
    private static final String SERVER_EXCEPTION_BODY = "{\n" +
            "  \"Code\": \"NoSuchKey\",\n" +
            "  \"Message\": \"The specified key does not exist.\",\n" +
            "  \"RequestId\": \"server-request-id\",\n" +
            "  \"HostId\": \"server-host-id\",\n" +
            "  \"EC\": \"0017-00000001\",\n" +
            "  \"Key\": \"src-key\"\n" +
            "}";

    @Test
    void buildCopyObjectV2OutputWithServerExceptionJsonBodyShouldThrow() throws Exception {
        Throwable cause = invokeAndGetCause("buildCopyObjectV2Output",
                new Class<?>[]{TosResponse.class}, response(SERVER_EXCEPTION_BODY));

        assertServerException(cause);
    }

    @Test
    void buildUploadPartCopyV2OutputWithServerExceptionJsonBodyShouldThrow() throws Exception {
        UploadPartCopyV2Input input = new UploadPartCopyV2Input().setPartNumber(1);

        Throwable cause = invokeAndGetCause("buildUploadPartCopyV2Output",
                new Class<?>[]{UploadPartCopyV2Input.class, TosResponse.class}, input, response(SERVER_EXCEPTION_BODY));

        assertServerException(cause);
    }

    @Test
    void buildFetchObjectOutputWithServerExceptionJsonBodyShouldThrow() throws Exception {
        Throwable cause = invokeAndGetCause("buildFetchObjectOutput",
                new Class<?>[]{TosResponse.class}, response(SERVER_EXCEPTION_BODY));

        assertServerException(cause);
    }

    @Test
    void buildCopyObjectV2OutputWithValidSuccessBodyShouldReturnOutput() throws Exception {
        CopyObjectV2Output output = (CopyObjectV2Output) invoke("buildCopyObjectV2Output",
                new Class<?>[]{TosResponse.class}, response("{\"ETag\":\"\\\"etag-copy\\\"\"}"));

        Assert.assertEquals(output.getEtag(), "\"etag-copy\"");
        Assert.assertEquals(output.getRequestInfo().getRequestId(), "response-request-id");
        Assert.assertEquals(output.getVersionID(), "version-id");
        Assert.assertEquals(output.getHashCrc64ecma(), "crc64");
    }

    @Test
    void buildUploadPartCopyV2OutputWithValidSuccessBodyShouldReturnOutput() throws Exception {
        UploadPartCopyV2Input input = new UploadPartCopyV2Input().setPartNumber(7);

        UploadPartCopyV2Output output = (UploadPartCopyV2Output) invoke("buildUploadPartCopyV2Output",
                new Class<?>[]{UploadPartCopyV2Input.class, TosResponse.class}, input,
                response("{\"ETag\":\"\\\"etag-part\\\"\"}"));

        Assert.assertEquals(output.getEtag(), "\"etag-part\"");
        Assert.assertEquals(output.getPartNumber(), 7);
        Assert.assertEquals(output.getRequestInfo().getRequestId(), "response-request-id");
        Assert.assertEquals(output.getServerSideEncryption(), "sse");
        Assert.assertEquals(output.getServerSideEncryptionKeyID(), "sse-key-id");
    }

    @Test
    void buildFetchObjectOutputWithValidSuccessBodyShouldReturnOutput() throws Exception {
        FetchObjectOutput output = (FetchObjectOutput) invoke("buildFetchObjectOutput",
                new Class<?>[]{TosResponse.class}, response("{\"ETag\":\"\\\"etag-fetch\\\"\"}"));

        Assert.assertEquals(output.getEtag(), "\"etag-fetch\"");
        Assert.assertEquals(output.getRequestInfo().getRequestId(), "response-request-id");
        Assert.assertEquals(output.getVersionID(), "version-id");
        Assert.assertEquals(output.getSsecAlgorithm(), "ssec-algorithm");
        Assert.assertEquals(output.getSsecKeyMD5(), "ssec-key-md5");
    }

    @Test
    void buildFetchObjectOutputWithInvalidBodyShouldThrowClientException() throws Exception {
        Throwable cause = invokeAndGetCause("buildFetchObjectOutput",
                new Class<?>[]{TosResponse.class}, response("{}"));

        Assert.assertTrue(cause instanceof TosClientException);
    }

    private static Object invoke(String methodName, Class<?>[] parameterTypes, Object... args) throws Exception {
        Method method = TosObjectRequestHandler.class.getDeclaredMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(new TosObjectRequestHandler(null, null), args);
    }

    private static Throwable invokeAndGetCause(String methodName, Class<?>[] parameterTypes, Object... args) throws Exception {
        try {
            invoke(methodName, parameterTypes, args);
            Assert.fail("Expected invocation to throw");
            return null;
        } catch (InvocationTargetException e) {
            return e.getCause();
        }
    }

    private static TosResponse response(String body) {
        Map<String, String> headers = new HashMap<>();
        headers.put(TosHeader.HEADER_REQUEST_ID.toLowerCase(), "response-request-id");
        headers.put(TosHeader.HEADER_ID_2.toLowerCase(), "response-id-2");
        headers.put(TosHeader.HEADER_VERSIONID.toLowerCase(), "version-id");
        headers.put(TosHeader.HEADER_COPY_SOURCE_VERSION_ID.toLowerCase(), "source-version-id");
        headers.put(TosHeader.HEADER_CRC64.toLowerCase(), "crc64");
        headers.put(TosHeader.HEADER_SSE.toLowerCase(), "sse");
        headers.put(TosHeader.HEADER_SSE_KEY_ID.toLowerCase(), "sse-key-id");
        headers.put(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM.toLowerCase(), "ssec-algorithm");
        headers.put(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5.toLowerCase(), "ssec-key-md5");
        return new TosResponse().setStatusCode(HttpStatus.OK).setHeaders(headers)
                .setInputStream(new ByteArrayInputStream(body.getBytes(StandardCharsets.UTF_8)));
    }

    private static void assertServerException(Throwable cause) {
        Assert.assertTrue(cause instanceof TosServerException);
        TosServerException ex = (TosServerException) cause;
        Assert.assertEquals(ex.getStatusCode(), HttpStatus.OK);
        Assert.assertEquals(ex.getCode(), "NoSuchKey");
        Assert.assertEquals(ex.getMessage(), "The specified key does not exist.");
        Assert.assertEquals(ex.getRequestID(), "server-request-id");
        Assert.assertEquals(ex.getHostID(), "server-host-id");
        Assert.assertEquals(ex.getEc(), "0017-00000001");
        Assert.assertEquals(ex.getKey(), "src-key");
    }
}

package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.UnexpectedStatusCodeException;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpStatus;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

class RequestHandler {
    private Transport transport;
    private static final ObjectMapper JSON = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    RequestHandler(Transport transport) {
        this.transport = transport;
    }

    protected  <T> T doRequest(TosRequest request, int expectedCode, Action<TosResponse, T> action) throws TosException {
        try (TosResponse res = doRequest(request)) {
            if (containExpectedCodes(res.getStatusCode(), expectedCode)) {
                return action.apply(res);
            }
            checkException(res);
            throw new UnexpectedStatusCodeException(res.getStatusCode(), expectedCode, res.getRequesID());
        } catch (IOException e) {
            throw new TosClientException("close failed", e);
        }
    }

    protected <T> T doRequest(TosRequest request, List<Integer> expectedCodes, Action<TosResponse, T> action) throws TosException {
        try (TosResponse res = doRequest(request, expectedCodes)) {
            return action.apply(res);
        } catch (IOException e) {
            throw new TosClientException("close failed", e);
        }
    }

    protected TosResponse doRequest(TosRequest request, List<Integer> expectedCodes) {
        TosResponse response = doRequest(request);
        for (int code : expectedCodes) {
            if (containExpectedCodes(response.getStatusCode(), code)) {
                return response;
            }
        }
        checkException(response);
        throw new UnexpectedStatusCodeException(response.getStatusCode(), expectedCodes, response.getRequesID());
    }

    private TosResponse doRequest(TosRequest request) throws TosException {
        // add retries
        TosResponse res;
        try{
            res = transport.roundTrip(request);
        } catch (IOException e){
            throw new TosClientException("request exception", e);
        }
        return res;
    }

    protected boolean containExpectedCodes(int statusCode, int expectedCode) {
        return statusCode == expectedCode;
    }

    protected void checkException(TosResponse res) {
        if (res.getStatusCode() < HttpStatus.MULTIPLE_CHOICE) {
            // < 300, no exception
            return;
        }
        // other cases, throw exception
        String s = null;
        try{
            s = IOUtils.toString(res.getInputStream(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new TosClientException("read response body failed", e);
        }
        if (s.length() > 0) {
            ServerExceptionJson se = null;
            try{
                se = JSON.readValue(s, new TypeReference<ServerExceptionJson>(){});
            } catch (JsonProcessingException e) {
                if (res.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    throw new TosClientException("bad request" + s, null);
                }
                throw new TosClientException("parse server exception failed"+ s, null);
            }
            throw new TosServerException(res.getStatusCode(), se.getCode(), se.getMessage(), se.getRequestID(), se.getHostID());
        }
        // head 不返回 body，此处特殊处理
        if (res.getStatusCode() == HttpStatus.NOT_FOUND) {
            // 针对 head 404 场景
            throw new TosServerException(res.getStatusCode(), Code.NOT_FOUND, "", res.getRequesID(), "");
        }
        if (res.getStatusCode() == HttpStatus.FORBIDDEN) {
            // 针对 head 403 场景
            throw new TosServerException(res.getStatusCode(), Code.FORBIDDEN, "", res.getRequesID(), "");
        }
    }
}

@FunctionalInterface
interface Action<T, R> {
    R apply(T t) throws IOException, TosException;
}
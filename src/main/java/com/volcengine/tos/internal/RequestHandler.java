package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.UnexpectedStatusCodeException;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.IOException;
import java.util.List;

class RequestHandler {
    private Transport transport;

    RequestHandler(Transport transport) {
        this.transport = transport;
    }

    public Transport getTransport() {
        return transport;
    }

    public RequestHandler setTransport(Transport transport) {
        this.transport = transport;
        return this;
    }

    protected <T> T doRequest(TosRequest request, int expectedCode, Action<TosResponse, T> action) {
        try (TosResponse res = doRequest(request)) {
            if (containExpectedCode(res.getStatusCode(), expectedCode)) {
                return action.apply(res);
            }
            checkException(res);
            throw new UnexpectedStatusCodeException(res.getStatusCode(), expectedCode, res.getRequesID());
        } catch (IOException e) {
            throw new TosClientException("tos: close body failed", e);
        }
    }

    protected <T> T doRequest(TosRequest request, List<Integer> expectedCodes, Action<TosResponse, T> action) {
        try (TosResponse res = doRequest(request, expectedCodes)) {
            return action.apply(res);
        } catch (IOException e) {
            throw new TosClientException("tos: close body failed", e);
        }
    }

    protected TosResponse doRequest(TosRequest request, List<Integer> expectedCodes) {
        TosResponse response = doRequest(request);
        for (int code : expectedCodes) {
            if (containExpectedCode(response.getStatusCode(), code)) {
                return response;
            }
        }
        checkException(response);
        throw new UnexpectedStatusCodeException(response.getStatusCode(), expectedCodes, response.getRequesID());
    }

    private TosResponse doRequest(TosRequest request) {
        TosResponse res;
        try{
            res = transport.roundTrip(request);
        } catch (IOException e){
            throw new TosClientException("tos: request exception", e);
        } finally {
            if (request.getContent() != null) {
                try {
                    request.getContent().close();
                } catch (IOException e) {
                    TosUtils.getLogger().debug("tos: close request body failed, {}", e.toString());
                }
            }
        }
        return res;
    }

    protected boolean containExpectedCode(int statusCode, int expectedCode) {
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
            s = StringUtils.toString(res.getInputStream());
        } catch (IOException e) {
            throw new TosClientException("tos: read response body failed", e);
        }
        if (s.length() > 0) {
            ServerExceptionJson se = null;
            try{
                se = TosUtils.getJsonMapper().readValue(s, new TypeReference<ServerExceptionJson>(){});
            } catch (JsonProcessingException e) {
                if (res.getStatusCode() == HttpStatus.BAD_REQUEST) {
                    throw new TosClientException("tos: bad request" + s, null);
                }
                throw new TosClientException("tos: parse server exception failed"+ s, null);
            }
            throw new TosServerException(res.getStatusCode(), se.getCode(), se.getMessage(), se.getRequestID(), se.getHostID());
        }
        // head请求服务端报错时不返回body，以下特殊处理
        // 404、403场景给 message 赋值，这两种场景比较常见
        if (res.getStatusCode() == HttpStatus.NOT_FOUND) {
            // 针对 head 404 场景
            throw new TosServerException(res.getStatusCode(), Code.NOT_FOUND, "", res.getRequesID(), "");
        }
        if (res.getStatusCode() == HttpStatus.FORBIDDEN) {
            // 针对 head 403 场景
            throw new TosServerException(res.getStatusCode(), Code.FORBIDDEN, "", res.getRequesID(), "");
        }
        // 2.5.1 版本后统一抛出TosServerException，之前版本会抛UnexpectedStatusCodeException
        throw new TosServerException(res.getStatusCode(), "", "", res.getRequesID(), "");
    }
}

@FunctionalInterface
interface Action<T, R> {
    R apply(T t);
}
package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.UnexpectedStatusCodeException;
import com.volcengine.tos.comm.Code;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    // 特殊场景下 server 会返回非常规状态码，此时不能直接抛 UnexpectedStatusCodeException
    protected <T> T doRequest(TosRequest request,
                              int expectedCode, List<Integer> unexpectedCodes,
                              Action<TosResponse, T> action) {
        try (TosResponse res = doRequest(request)) {
            if (containExpectedCode(res.getStatusCode(), expectedCode)) {
                return action.apply(res);
            }
            boolean containUnexpectedCode = false;
            for (int code : unexpectedCodes) {
                if (containUnexpectedCode(res.getStatusCode(), code)) {
                    containUnexpectedCode = true;
                    break;
                }
            }
            if (containUnexpectedCode) {
                String s = StringUtils.toString(res.getInputStream(), "response body");
                checkException(s, res.getStatusCode(), res.getRequesID(), res.getHeaders());
            } else {
                checkException(res);
            }
            throw new UnexpectedStatusCodeException(res.getStatusCode(), expectedCode, res.getRequesID());
        } catch (IOException e) {
            throw new TosClientException("tos: close body failed", e);
        }
    }

    protected <T> T doRequest(TosRequest request, List<Integer> expectedCodes, Action<TosResponse, T> action) {
        try (TosResponse res = doRequest(request, expectedCodes)) {
            return action.apply(res);
        } catch (IOException e) {
            throw new TosClientException("tos: close body failed", e).setRequestUrl(request.toURL().toString());
        } catch (TosException e) {
            throw e.setRequestUrl(request.toURL().toString());
        }
    }

    protected TosResponse doRequest(TosRequest request, List<Integer> expectedCodes) {
        TosResponse response = doRequest(request);
        for (int code : expectedCodes) {
            if (containExpectedCode(response.getStatusCode(), code)) {
                return response;
            }
        }
        try{
            checkException(response);
            throw new UnexpectedStatusCodeException(response.getStatusCode(), expectedCodes, response.getRequesID());
        }finally {
            try {
                response.close();
            } catch (IOException e) {}
        }
    }

    private TosResponse doRequest(TosRequest request) {
        TosResponse res;
        try{
            res = transport.roundTrip(request);
        } catch (IOException e) {
            throw new TosClientException("tos: request exception", e);
        } catch (IllegalArgumentException e) {
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

    protected boolean containUnexpectedCode(int statusCode, int unexpectedCode) {
        return statusCode == unexpectedCode;
    }

    protected void checkException(TosResponse res) {
        if (res.getStatusCode() < HttpStatus.MULTIPLE_CHOICE) {
            // < 300, no exception
            return;
        }
        // other cases, throw exception
        String s = StringUtils.toString(res.getInputStream(), "response body");
        checkException(s, res.getStatusCode(), res.getRequesID(), res.getHeaders());
    }

    private static void checkException(String rspBody, int statusCode, String reqId, Map<String, String> headers) {
        if (StringUtils.isNotEmpty(rspBody)) {
            ServerExceptionJson se = null;
            try{
                se = TosUtils.getJsonMapper().readValue(rspBody, new TypeReference<ServerExceptionJson>(){});
            } catch (JsonProcessingException e) {
                if (statusCode == HttpStatus.BAD_REQUEST) {
                    throw new TosClientException("tos: bad request" + rspBody, null);
                }
                throw new TosClientException("tos: parse server exception failed"+ rspBody, null);
            }
            throw new TosServerException(statusCode, se.getCode(), se.getMessage(), se.getRequestID(), se.getHostID())
                    .setEc(se.getEc()).setKey(se.getKey());
        }
        String ec = headers.get(TosHeader.HEADER_EC.toLowerCase());
        // head请求服务端报错时不返回body，以下特殊处理
        // 404、403场景给 message 赋值，这两种场景比较常见
        if (statusCode == HttpStatus.NOT_FOUND) {
            // 针对 head 404 场景
            throw new TosServerException(statusCode, Code.NOT_FOUND, "", reqId, "").setEc(ec);
        }
        if (statusCode == HttpStatus.FORBIDDEN) {
            // 针对 head 403 场景
            throw new TosServerException(statusCode, Code.FORBIDDEN, "", reqId, "").setEc(ec);
        }
        // 2.5.1 版本后统一抛出TosServerException，之前版本会抛UnexpectedStatusCodeException
        throw new TosServerException(statusCode, "", "", reqId, "").setEc(ec);
    }
}

@FunctionalInterface
interface Action<T, R> {
    R apply(T t);
}
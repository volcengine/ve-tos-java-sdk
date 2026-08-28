package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.util.PayloadConverter;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.SigningUtils;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.GenericInput;
import com.volcengine.tos.model.vectors.CreateIndexInput;
import com.volcengine.tos.model.vectors.CreateIndexOutput;
import com.volcengine.tos.model.vectors.CreateVectorBucketInput;
import com.volcengine.tos.model.vectors.CreateVectorBucketOutput;
import com.volcengine.tos.model.vectors.DeleteIndexInput;
import com.volcengine.tos.model.vectors.DeleteIndexOutput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketInput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketOutput;
import com.volcengine.tos.model.vectors.DeleteVectorsInput;
import com.volcengine.tos.model.vectors.DeleteVectorsOutput;
import com.volcengine.tos.model.vectors.GetIndexInput;
import com.volcengine.tos.model.vectors.GetIndexOutput;
import com.volcengine.tos.model.vectors.GetVectorBucketInput;
import com.volcengine.tos.model.vectors.GetVectorBucketOutput;
import com.volcengine.tos.model.vectors.GetVectorsInput;
import com.volcengine.tos.model.vectors.GetVectorsOutput;
import com.volcengine.tos.model.vectors.ListIndexesInput;
import com.volcengine.tos.model.vectors.ListIndexesOutput;
import com.volcengine.tos.model.vectors.ListVectorsInput;
import com.volcengine.tos.model.vectors.ListVectorsOutput;
import com.volcengine.tos.model.vectors.ListVectorBucketsInput;
import com.volcengine.tos.model.vectors.ListVectorBucketsOutput;
import com.volcengine.tos.model.vectors.PutVectorsInput;
import com.volcengine.tos.model.vectors.PutVectorsOutput;
import com.volcengine.tos.model.vectors.PutVectorBucketPolicyInput;
import com.volcengine.tos.model.vectors.PutVectorBucketPolicyOutput;
import com.volcengine.tos.model.vectors.GetVectorBucketPolicyInput;
import com.volcengine.tos.model.vectors.GetVectorBucketPolicyOutput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketPolicyInput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketPolicyOutput;
import com.volcengine.tos.model.vectors.QueryVectorsInput;
import com.volcengine.tos.model.vectors.QueryVectorsOutput;

import java.io.ByteArrayInputStream;
import java.time.ZoneOffset;
import java.util.Map;

public class TosVectorsRequestHandler {
    private RequestHandler vectorsHandler;
    private TosRequestFactory factory;

    private RequestBuilder handleGenericInput(RequestBuilder builder, GenericInput input) {
        if (StringUtils.isNotEmpty(input.getRequestHost())) {
            builder = builder.withHeader(TosHeader.HEADER_HOST, input.getRequestHost());
        }
        if (input.getRequestDate() != null) {
            builder = builder.withHeader(SigningUtils.v4Date,
                    SigningUtils.iso8601Layout.format(input.getRequestDate().toInstant().atOffset(ZoneOffset.UTC)));
        }

        if (input.getRequestHeaders() != null && !input.getRequestHeaders().isEmpty()) {
            Map<String, String> headers = builder.getHeaders();
            for (Map.Entry<String, String> entry : input.getRequestHeaders().entrySet()) {
                if (!containsKeyIgnoreCase(headers, entry.getKey())) {
                    builder = builder.withHeader(entry.getKey(), entry.getValue());
                }
            }
        }

        if (input.getRequestQuery() != null && !input.getRequestQuery().isEmpty()) {
            for (Map.Entry<String, String> entry : input.getRequestQuery().entrySet()) {
                builder = builder.withQuery(entry.getKey(), entry.getValue());
            }
        }
        return builder;
    }

    public static boolean containsKeyIgnoreCase(Map<String, String> map, String key) {
        for (String existingKey : map.keySet()) {
            if (existingKey.equalsIgnoreCase(key)) {
                return true;
            }
        }
        return false;
    }

    public TosVectorsRequestHandler(Transport transport, TosRequestFactory factory) {
        this.vectorsHandler = new RequestHandler(transport);
        this.factory = factory;
    }

    public CreateVectorBucketOutput createVectorBucket(CreateVectorBucketInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateVectorBucketInput");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq("", "", "CreateVectorBucket", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> new CreateVectorBucketOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetVectorBucketOutput getVectorBucket(GetVectorBucketInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetVectorBucketInput");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "GetVectorBucket", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> PayloadConverter.parsePayload(res.getInputStream(), new TypeReference<GetVectorBucketOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteVectorBucketOutput deleteVectorBucket(DeleteVectorBucketInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteVectorBucketInput");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "DeleteVectorBucket", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> new DeleteVectorBucketOutput().setRequestInfo(res.RequestInfo()));
    }

    public ListVectorBucketsOutput listVectorBuckets(ListVectorBucketsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListVectorBucketsInput");

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);

        // 设置项目名称为请求头
        Map<String, String> headers = null;

        RequestBuilder builder = this.factory.initVectorReq("", "", "ListVectorBuckets", headers);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter
                .parsePayload(res.getInputStream(), new TypeReference<ListVectorBucketsOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public CreateIndexOutput createIndex(CreateIndexInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateIndexInput");
        ParamsChecker.ensureNotNull(input.getIndexName(), "indexName");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "CreateIndex", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> new CreateIndexOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetIndexOutput getIndex(GetIndexInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetIndexInput");
        ParamsChecker.ensureNotNull(input.getIndexName(), "indexName");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "GetIndex", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> PayloadConverter.parsePayload(res.getInputStream(), new TypeReference<GetIndexOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteIndexOutput deleteIndex(DeleteIndexInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteIndexInput");
        ParamsChecker.ensureNotNull(input.getIndexName(), "indexName");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "DeleteIndex", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> new DeleteIndexOutput().setRequestInfo(res.RequestInfo()));
    }

    public ListIndexesOutput listIndexes(ListIndexesInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListIndexesInput");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "ListIndexes", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> PayloadConverter.parsePayload(res.getInputStream(), new TypeReference<ListIndexesOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutVectorsOutput putVectors(PutVectorsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutVectorsInput");
        ParamsChecker.ensureNotNull(input.getIndexName(), "indexName");
        ParamsChecker.ensureNotNull(input.getVectors(), "vectors");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "PutVectors", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> new PutVectorsOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetVectorsOutput getVectors(GetVectorsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetVectorsInput");
        ParamsChecker.ensureNotNull(input.getIndexName(), "indexName");
        ParamsChecker.ensureNotNull(input.getKeys(), "keys");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "GetVectors", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> PayloadConverter.parsePayload(res.getInputStream(), new TypeReference<GetVectorsOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public ListVectorsOutput listVectors(ListVectorsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListVectorsInput");
        ParamsChecker.ensureNotNull(input.getIndexName(), "indexName");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "ListVectors", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> PayloadConverter.parsePayload(res.getInputStream(), new TypeReference<ListVectorsOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteVectorsOutput deleteVectors(DeleteVectorsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteVectorsInput");
        ParamsChecker.ensureNotNull(input.getIndexName(), "indexName");
        ParamsChecker.ensureNotNull(input.getKeys(), "keys");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "DeleteVectors", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> new DeleteVectorsOutput().setRequestInfo(res.RequestInfo()));
    }

    public PutVectorBucketPolicyOutput putVectorBucketPolicy(PutVectorBucketPolicyInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutVectorBucketPolicyInput");
        ParamsChecker.ensureNotNull(input.getPolicy(), "policy");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "PutVectorBucketPolicy", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> new PutVectorBucketPolicyOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetVectorBucketPolicyOutput getVectorBucketPolicy(GetVectorBucketPolicyInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetVectorBucketPolicyInput");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "GetVectorBucketPolicy", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> new GetVectorBucketPolicyOutput()
                        .setRequestInfo(res.RequestInfo())
                        .setPolicy(StringUtils.toString(res.getInputStream(), "bucket policy")));
    }

    public DeleteVectorBucketPolicyOutput deleteVectorBucketPolicy(DeleteVectorBucketPolicyInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteVectorBucketPolicyInput");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        ParamsChecker.isValidAccountId(input.getAccountId());

        // 构建请求体
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "DeleteVectorBucketPolicy", null);
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> new DeleteVectorBucketPolicyOutput().setRequestInfo(res.RequestInfo()));
    }

    public QueryVectorsOutput queryVectors(QueryVectorsInput input) throws TosException {
        // 1. 参数验证（必须）
        ParamsChecker.ensureNotNull(input, "QueryVectorsInput");
        ParamsChecker.ensureNotNull(input.getIndexName(), "indexName");
        ParamsChecker.ensureNotNull(input.getQueryVector(), "queryVector");
        ParamsChecker.isValidVectorBucketName(input.getVectorBucketName());
        // 2. 数据准备（PUT/POST需要）
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        // 3. 请求构建（RequestBuilder模式）
        RequestBuilder builder = this.factory.initVectorReq(input.getAccountId(), input.getVectorBucketName(),
                "QueryVectors", null);
        builder = this.handleGenericInput(builder, input);

        // 4. 请求执行
        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        // 5. 响应处理（lambda表达式）
        return vectorsHandler.doRequest(req, HttpStatus.OK,
                res -> PayloadConverter.parsePayload(res.getInputStream(), new TypeReference<QueryVectorsOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    private RequestBuilder handleGenericInput(RequestBuilder builder, QueryVectorsInput input) {
        // 处理通用输入参数
        if (input.getRequestHeaders() != null && !input.getRequestHeaders().isEmpty()) {
            for (String key : input.getRequestHeaders().keySet()) {
                builder = builder.withHeader(key, input.getRequestHeaders().get(key));
            }
        }
        if (input.getRequestQuery() != null && !input.getRequestQuery().isEmpty()) {
            for (String key : input.getRequestQuery().keySet()) {
                builder = builder.withQuery(key, input.getRequestQuery().get(key));
            }
        }
        return builder;
    }
}

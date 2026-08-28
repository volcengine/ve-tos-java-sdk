package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.AzRedundancyType;
import com.volcengine.tos.comm.common.BucketType;
import com.volcengine.tos.internal.model.AudioConvertJobRequest;
import com.volcengine.tos.internal.model.PutTemplateInput;
import com.volcengine.tos.internal.model.VideoConvertJobRequest;
import com.volcengine.tos.internal.util.*;
import com.volcengine.tos.model.GenericInput;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;
import com.volcengine.tos.model.bucket.PutAudioConvertTemplateInput;
import com.volcengine.tos.model.bucket.PutAudioConvertTemplateOutput;
import com.volcengine.tos.model.bucket.GetAudioConvertTemplateInput;
import com.volcengine.tos.model.bucket.GetAudioConvertTemplateOutput;
import com.volcengine.tos.model.bucket.DeleteAudioConvertTemplateInput;
import com.volcengine.tos.model.bucket.DeleteAudioConvertTemplateOutput;
import com.volcengine.tos.model.bucket.GetVideoConvertTemplateInput;
import com.volcengine.tos.model.bucket.GetVideoConvertTemplateOutput;
import com.volcengine.tos.model.bucket.DeleteVideoConvertTemplateInput;
import com.volcengine.tos.model.bucket.DeleteVideoConvertTemplateOutput;
import com.volcengine.tos.model.bucket.GetVideoConvertJobInput;
import com.volcengine.tos.model.bucket.GetVideoConvertJobOutput;
import com.volcengine.tos.model.bucket.GetAudioConvertJobInput;
import com.volcengine.tos.model.bucket.GetAudioConvertJobOutput;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TosBucketRequestHandler {
    private RequestHandler bucketHandler;
    private TosRequestFactory factory;

    public TosBucketRequestHandler(Transport transport, TosRequestFactory factory) {
        this.bucketHandler = new RequestHandler(transport);
        this.factory = factory;
    }

    private RequestBuilder handleGenericInput(RequestBuilder builder, GenericInput input) {
        if (StringUtils.isNotEmpty(input.getRequestHost())) {
            builder = builder.withHeader(TosHeader.HEADER_HOST, input.getRequestHost());
        }
        if (input.getRequestDate() != null) {
            builder = builder.withHeader(SigningUtils.v4Date, SigningUtils.iso8601Layout.format(input.getRequestDate().toInstant().atOffset(ZoneOffset.UTC)));
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

    public CreateBucketV2Output createBucket(CreateBucketV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateBucketV2Input");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withHeader(TosHeader.HEADER_ACL, input.getAcl() == null ? null : input.getAcl().toString())
                .withHeader(TosHeader.HEADER_GRANT_FULL_CONTROL, input.getGrantFullControl())
                .withHeader(TosHeader.HEADER_GRANT_READ, input.getGrantRead())
                .withHeader(TosHeader.HEADER_GRANT_READ_ACP, input.getGrantReadAcp())
                .withHeader(TosHeader.HEADER_GRANT_WRITE, input.getGrantWrite())
                .withHeader(TosHeader.HEADER_GRANT_WRITE_ACP, input.getGrantWriteAcp())
                .withHeader(TosHeader.HEADER_STORAGE_CLASS, input.getStorageClass() == null ? null : input.getStorageClass().toString())
                .withHeader(TosHeader.HEADER_AZ_REDUNDANCY, input.getAzRedundancy() == null ? null : input.getAzRedundancy().toString())
                .withHeader(TosHeader.HEADER_PROJECT_NAME, input.getProjectName())
                .withHeader(TosHeader.HEADER_TAGGING, input.getTagging())
                .withHeader(TosHeader.HEADER_BUCKET_TYPE, input.getBucketType() == null ? null : input.getBucketType().toString());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, null).setRetryableOnClientException(false);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new CreateBucketV2Output(res.RequestInfo(),
                res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_LOCATION)));
    }

    public HeadBucketV2Output headBucket(HeadBucketV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "HeadBucketInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.HEAD, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new HeadBucketV2Output(res.RequestInfo(),
                res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_BUCKET_REGION),
                TypeConverter.convertStorageClassType(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_STORAGE_CLASS)))
                .setProjectName(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_PROJECT_NAME))
                .setBucketType(BucketType.parse(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_BUCKET_TYPE)))
                .setAzRedundancy(AzRedundancyType.parse(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_AZ_REDUNDANCY)))
        );
    }

    public DeleteBucketOutput deleteBucket(DeleteBucketInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null).setRetryableOnClientException(false);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketOutput(res.RequestInfo()));
    }


    public ListBucketsV2Output listBuckets(ListBucketsV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListBucketsV2Input");
        RequestBuilder builder = this.factory.init("", "", null)
                .withHeader(TosHeader.HEADER_PROJECT_NAME, input.getProjectName())
                .withHeader(TosHeader.HEADER_BUCKET_TYPE, input.getBucketType() == null ? null : input.getBucketType().toString());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK,
                res -> PayloadConverter.parsePayload(res.getInputStream(),
                        new TypeReference<ListBucketsV2Output>() {
                        }).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketPolicyOutput putBucketPolicy(PutBucketPolicyInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketPolicyInput");
        ParamsChecker.ensureNotNull(input.getPolicy(), "policy");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("policy", "");
        builder = this.handleGenericInput(builder, input);
        byte[] policyStr = input.getPolicy().getBytes(StandardCharsets.UTF_8);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(policyStr)).setContentLength(policyStr.length);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new PutBucketPolicyOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketPolicyOutput getBucketPolicy(GetBucketPolicyInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketPolicyInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("policy", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new GetBucketPolicyOutput().setRequestInfo(res.RequestInfo())
                .setPolicy(StringUtils.toString(res.getInputStream(), "bucket policy")));
    }

    public DeleteBucketPolicyOutput deleteBucketPolicy(DeleteBucketPolicyInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketPolicyInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("policy", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res ->
                new DeleteBucketPolicyOutput().setRequestInfo(res.RequestInfo()));
    }

    public PutBucketCORSOutput putBucketCORS(PutBucketCORSInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketCORSInput");
        ParamsChecker.ensureNotNull(input.getRules(), "CORSRules");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("cors", "").withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(
                marshalResult.getData())).setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketCORSOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketCORSOutput getBucketCORS(GetBucketCORSInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketCORSInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("cors", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketCORSOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketCORSOutput deleteBucketCORS(DeleteBucketCORSInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketCORSInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("cors", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res ->
                new DeleteBucketCORSOutput().setRequestInfo(res.RequestInfo()));
    }

    public PutBucketStorageClassOutput putBucketStorageClass(PutBucketStorageClassInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketStorageClassInput");
        ParamsChecker.ensureNotNull(input.getStorageClass(), "StorageClass");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("storageClass", "")
                .withHeader(TosHeader.HEADER_STORAGE_CLASS, input.getStorageClass().toString());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketStorageClassOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketLocationOutput getBucketLocation(GetBucketLocationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketLocationInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("location", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketLocationOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketLifecycleOutput putBucketLifecycle(PutBucketLifecycleInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketLifecycleInput");
        ParamsChecker.ensureNotNull(input.getRules(), "LifecycleRules");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("lifecycle", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        if (input.isAllowSameActionOverlap()) {
            builder = builder.withHeader(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP, "true");
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketLifecycleOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketLifecycleOutput getBucketLifecycle(GetBucketLifecycleInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketLifecycleInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("lifecycle", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketLifecycleOutput>() {
                }).setRequestInfo(res.RequestInfo()).setAllowSameActionOverlap(Boolean.valueOf(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP))));
    }

    public DeleteBucketLifecycleOutput deleteBucketLifecycle(DeleteBucketLifecycleInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketLifecycleInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("lifecycle", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res ->
                new DeleteBucketLifecycleOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetObjectSetEndpointOutput getObjectSetEndpoint(GetObjectSetEndpointInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectSetEndpointInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsetendpoint", "")
                .withQuery("ObjectSetName", input.getObjectSetName());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> {
            List<ObjectSetEndpoint> endpoints = PayloadConverter.parsePayload(res.getInputStream(),
                    new TypeReference<List<ObjectSetEndpoint>>() {
                    });
            return new GetObjectSetEndpointOutput().setRequestInfo(res.RequestInfo()).setEndpoints(endpoints);
        });
    }

    public PutObjectSetLifecycleOutput putObjectSetLifecycle(PutObjectSetLifecycleInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectSetLifecycleInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ParamsChecker.ensureNotNull(input.getRules(), "LifecycleRules");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectset-lifecycle", "")
                .withQuery("ObjectSetName", input.getObjectSetName())
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        if (input.isAllowSameActionOverlap()) {
            builder = builder.withHeader(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP, "true");
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutObjectSetLifecycleOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetObjectSetLifecycleOutput getObjectSetLifecycle(GetObjectSetLifecycleInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectSetLifecycleInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectset-lifecycle", "")
                .withQuery("ObjectSetName", input.getObjectSetName());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetObjectSetLifecycleOutput>() {
                }).setRequestInfo(res.RequestInfo())
                .setAllowSameActionOverlap(Boolean.valueOf(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP))));
    }

    public DeleteObjectSetLifecycleOutput deleteObjectSetLifecycle(DeleteObjectSetLifecycleInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteObjectSetLifecycleInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectset-lifecycle", "")
                .withQuery("ObjectSetName", input.getObjectSetName());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteObjectSetLifecycleOutput().setRequestInfo(res.RequestInfo()));
    }

    public PutObjectSetLifecycleByTagOutput putObjectSetLifecycleByTag(PutObjectSetLifecycleByTagInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectSetLifecycleByTagInput");
        ParamsChecker.ensureNotNull(input.getObjectSetTagRules(), "ObjectSetTagRules");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectset-lifecycle-bytag", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        if (input.isAllowSameActionOverlap()) {
            builder = builder.withHeader(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP, "true");
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutObjectSetLifecycleByTagOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetObjectSetLifecycleByTagOutput getObjectSetLifecycleByTag(GetObjectSetLifecycleByTagInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectSetLifecycleByTagInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectset-lifecycle-bytag", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetObjectSetLifecycleByTagOutput>() {
                }).setRequestInfo(res.RequestInfo())
                .setAllowSameActionOverlap(Boolean.valueOf(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ALLOW_SAME_ACTION_OVERLAP))));
    }

    public DeleteObjectSetLifecycleByTagOutput deleteObjectSetLifecycleByTag(DeleteObjectSetLifecycleByTagInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteObjectSetLifecycleByTagInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectset-lifecycle-bytag", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteObjectSetLifecycleByTagOutput().setRequestInfo(res.RequestInfo()));
    }

    public PutBucketObjectSetConfigurationOutput putBucketObjectSetConfiguration(PutBucketObjectSetConfigurationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketObjectSetConfigurationInput");
        ParamsChecker.ensureNotNull(input.getPathLevel(), "PathLevel");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsetconfiguration", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketObjectSetConfigurationOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetBucketObjectSetConfigurationOutput getBucketObjectSetConfiguration(GetBucketObjectSetConfigurationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketObjectSetConfigurationInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsetconfiguration", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketObjectSetConfigurationOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutObjectSetOutput putObjectSet(PutObjectSetInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectSetInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectset", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutObjectSetOutput().setRequestInfo(res.RequestInfo()));
    }

    public DeleteObjectSetOutput deleteObjectSet(DeleteObjectSetInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteObjectSetInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectset", "")
                .withQuery("ObjectSetName", input.getObjectSetName());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteObjectSetOutput().setRequestInfo(res.RequestInfo()));
    }

    public ListObjectSetsOutput listObjectSets(ListObjectSetsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListObjectSetsInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsets", "");
        if (StringUtils.isNotEmpty(input.getPrefix())) {
            builder = builder.withQuery("prefix", input.getPrefix());
        }
        if (StringUtils.isNotEmpty(input.getTags())) {
            builder = builder.withQuery("tags", input.getTags());
        }
        if (input.getMaxKeys() != null) {
            builder = builder.withQuery("max-keys", input.getMaxKeys().toString());
        }
        if (StringUtils.isNotEmpty(input.getMarker())) {
            builder = builder.withQuery("marker", input.getMarker());
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<ListObjectSetsOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public GetObjectSetOutput getObjectSet(GetObjectSetInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectSetInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectset", "")
                .withQuery("ObjectSetName", input.getObjectSetName());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetObjectSetOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutObjectSetTaggingOutput putObjectSetTagging(PutObjectSetTaggingInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectSetTaggingInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsettagging", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new PutObjectSetTaggingOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetObjectSetTaggingOutput getObjectSetTagging(GetObjectSetTaggingInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectSetTaggingInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsettagging", "")
                .withQuery("ObjectSetName", input.getObjectSetName());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetObjectSetTaggingOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutObjectSetQuotaByTagOutput putObjectSetQuotaByTag(PutObjectSetQuotaByTagInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectSetQuotaByTagInput");
        ParamsChecker.ensureNotNull(input.getRules(), "Rules");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsetquotabytag", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutObjectSetQuotaByTagOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetObjectSetQuotaByTagOutput getObjectSetQuotaByTag(GetObjectSetQuotaByTagInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectSetQuotaByTagInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsetquotabytag", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetObjectSetQuotaByTagOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteObjectSetQuotaByTagOutput deleteObjectSetQuotaByTag(DeleteObjectSetQuotaByTagInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteObjectSetQuotaByTagInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsetquotabytag", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteObjectSetQuotaByTagOutput().setRequestInfo(res.RequestInfo()));
    }

    public PutObjectSetQuotaOutput putObjectSetQuota(PutObjectSetQuotaInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectSetQuotaInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ParamsChecker.ensureNotNull(input.getStorageQuota(), "StorageQuota");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsetquota", "")
                .withQuery("ObjectSetName", input.getObjectSetName())
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutObjectSetQuotaOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetObjectSetQuotaOutput getObjectSetQuota(GetObjectSetQuotaInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectSetQuotaInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsetquota", "")
                .withQuery("ObjectSetName", input.getObjectSetName());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetObjectSetQuotaOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public GetObjectSetStorageOutput getObjectSetStorage(GetObjectSetStorageInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectSetStorageInput");
        ParamsChecker.ensureNotNull(input.getObjectSetName(), "ObjectSetName");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("objectsetstorage", "")
                .withQuery("ObjectSetName", input.getObjectSetName());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetObjectSetStorageOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketMirrorBackOutput putBucketMirrorBack(PutBucketMirrorBackInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketMirrorBackInput");
        ParamsChecker.ensureNotNull(input.getRules(), "MirrorBackRules");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("mirror", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketMirrorBackOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketMirrorBackOutput getBucketMirrorBack(GetBucketMirrorBackInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketMirrorBackInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("mirror", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketMirrorBackOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketMirrorBackOutput deleteBucketMirrorBack(DeleteBucketMirrorBackInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketMirrorBackInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("mirror", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res ->
                new DeleteBucketMirrorBackOutput().setRequestInfo(res.RequestInfo()));
    }


    public PutBucketReplicationOutput putBucketReplication(PutBucketReplicationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketReplicationInput");
        ParamsChecker.ensureNotNull(input.getRole(), "ReplicationRole");
        ParamsChecker.ensureNotNull(input.getRules(), "ReplicationRule");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("replication", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketReplicationOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketReplicationOutput getBucketReplication(GetBucketReplicationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketReplicationInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("replication", "").withQuery("progress", "").withQuery("rule-id", input.getRuleID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketReplicationOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketReplicationOutput deleteBucketReplication(DeleteBucketReplicationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketReplicationInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("replication", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketReplicationOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public PutBucketVersioningOutput putBucketVersioning(PutBucketVersioningInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketVersioningInput");
        ParamsChecker.ensureNotNull(input.getStatus(), "VersioningStatusType");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("versioning", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketVersioningOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketVersioningOutput getBucketVersioning(GetBucketVersioningInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketVersioningInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("versioning", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketVersioningOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketWebsiteOutput putBucketWebsite(PutBucketWebsiteInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketWebsiteInput");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("website", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketWebsiteOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketWebsiteOutput getBucketWebsite(GetBucketWebsiteInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketWebsiteInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("website", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketWebsiteOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketWebsiteOutput deleteBucketWebsite(DeleteBucketWebsiteInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketWebsiteInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("website", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketWebsiteOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public PutBucketNotificationOutput putBucketNotification(PutBucketNotificationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketNotificationInput");
        ensureValidBucketName(input.getBucket());
//        ParamsChecker.ensureNotNull(input.getCloudFunctionConfigurations(), "CloudFunctionConfigurations");
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("notification", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketNotificationOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketNotificationOutput getBucketNotification(GetBucketNotificationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketNotificationInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("notification", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketNotificationOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketNotificationType2Output putBucketNotificationType2(PutBucketNotificationType2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketNotificationType2Output");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("notification_v2", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketNotificationType2Output()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketNotificationType2Output getBucketNotificationType2(GetBucketNotificationType2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketNotificationType2Output");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("notification_v2", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketNotificationType2Output>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketCustomDomainOutput putBucketCustomDomain(PutBucketCustomDomainInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketCustomDomainInput");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("customdomain", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketCustomDomainOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public ListBucketCustomDomainOutput listBucketCustomDomain(ListBucketCustomDomainInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListBucketCustomDomainInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("customdomain", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<ListBucketCustomDomainOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketCustomDomainOutput deleteBucketCustomDomain(DeleteBucketCustomDomainInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketCustomDomainInput");
        ParamsChecker.ensureNotNull(input.getDomain(), "Domain");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("customdomain", input.getDomain());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new DeleteBucketCustomDomainOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public PutBucketRealTimeLogOutput putBucketRealTimeLog(PutBucketRealTimeLogInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketRealTimeLogInput");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("realtimeLog", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketRealTimeLogOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketRealTimeLogOutput getBucketRealTimeLog(GetBucketRealTimeLogInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketRealTimeLogInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("realtimeLog", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketRealTimeLogOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketRealTimeLogOutput deleteBucketRealTimeLog(DeleteBucketRealTimeLogInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketRealTimeLogInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("realtimeLog", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketRealTimeLogOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public PutBucketACLOutput putBucketACL(PutBucketACLInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketACLInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("acl", "")
                .withHeader(TosHeader.HEADER_ACL, input.getAcl() == null ? null : input.getAcl().toString())
                .withHeader(TosHeader.HEADER_GRANT_FULL_CONTROL, input.getGrantFullControl())
                .withHeader(TosHeader.HEADER_GRANT_READ, input.getGrantRead())
                .withHeader(TosHeader.HEADER_GRANT_READ_ACP, input.getGrantReadAcp())
                .withHeader(TosHeader.HEADER_GRANT_WRITE, input.getGrantWrite())
                .withHeader(TosHeader.HEADER_GRANT_WRITE_ACP, input.getGrantWriteAcp());
        byte[] data = new byte[0];
        if (input.getOwner() != null && StringUtils.isNotEmpty(input.getOwner().getId())
                && input.getGrants() != null && input.getGrants().size() > 0) {
            TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
            data = marshalResult.getData();
            builder.withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(data)).setContentLength(data.length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketACLOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketACLOutput getBucketACL(GetBucketACLInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketACLInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("acl", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketACLOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketRenameOutput putBucketRename(PutBucketRenameInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketRenameInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("rename", "");
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        builder.withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketRenameOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketRenameOutput getBucketRename(GetBucketRenameInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketRenameInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("rename", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketRenameOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }


    public DeleteBucketRenameOutput deleteBucketRename(DeleteBucketRenameInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketRenameInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("rename", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketRenameOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public PutBucketEncryptionOutput putBucketEncryption(PutBucketEncryptionInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketEncryptionInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("encryption", "");
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        builder.withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketEncryptionOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketEncryptionOutput getBucketEncryption(GetBucketEncryptionInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketEncryptionInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("encryption", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketEncryptionOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketEncryptionOutput deleteBucketEncryption(DeleteBucketEncryptionInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketEncryptionInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("encryption", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketEncryptionOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public PutBucketTaggingOutput putBucketTagging(PutBucketTaggingInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketTaggingInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("tagging", "");
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        builder.withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketTaggingOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketTaggingOutput getBucketTagging(GetBucketTaggingInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketTaggingInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("tagging", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketTaggingOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketTaggingOutput deleteBucketTagging(DeleteBucketTaggingInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketTaggingInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("tagging", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketTaggingOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public PutBucketInventoryOutput putBucketInventory(PutBucketInventoryInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketInventoryInput");
        ensureValidBucketName(input.getBucket());
        if (StringUtils.isEmpty(input.getId())) {
            throw new TosClientException("tos: inventory id is empty", null);
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("inventory", "").withQuery("id", input.getId());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        builder.withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketInventoryOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketInventoryOutput getBucketInventory(GetBucketInventoryInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketInventoryInput");
        ensureValidBucketName(input.getBucket());
        if (StringUtils.isEmpty(input.getId())) {
            throw new TosClientException("tos: inventory id is empty", null);
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("inventory", "").withQuery("id", input.getId());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketInventoryOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public ListBucketInventoryOutput listBucketInventory(ListBucketInventoryInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketInventoryInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("inventory", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<ListBucketInventoryOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketInventoryOutput deleteBucketInventory(DeleteBucketInventoryInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketInventoryInput");
        ensureValidBucketName(input.getBucket());
        if (StringUtils.isEmpty(input.getId())) {
            throw new TosClientException("tos: inventory id is empty", null);
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("inventory", "").withQuery("id", input.getId());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketInventoryOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketInfoOutput getBucketInfo(GetBucketInfoInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketInfoInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("bucketInfo", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketInfoOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public boolean doesBucketExist(DoesBucketExistInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DoesBucketExistInput");
        ensureValidBucketName(input.getBucket());

        HeadBucketV2Input headBucketV2Input = new HeadBucketV2Input();
        headBucketV2Input.setBucket(input.getBucket());
        HeadBucketV2Output headBucketV2Output = null;
        try {
            headBucketV2Output = headBucket(headBucketV2Input);
            if (headBucketV2Output.getRequestInfo().getStatusCode() == HttpStatus.OK) {
                return true;
            }
        } catch (TosServerException e) {
            if (Objects.equals(e.getEc(), Consts.EcNotFoundErr)) {
                return false;
            }
            throw e;
        }
        return false;
    }

    public PutBucketAccessMonitorOutput putBucketAccessMonitor(PutBucketAccessMonitorInput input) {
        ParamsChecker.ensureNotNull(input, "PutBucketAccessMonitorInput");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("accessmonitor", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(
                marshalResult.getData())).setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketAccessMonitorOutput(res.RequestInfo()));
    }

    public GetBucketAccessMonitorOutput getBucketAccessMonitor(GetBucketAccessMonitorInput input) {
        ParamsChecker.ensureNotNull(input, "GetBucketAccessMonitorInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("accessmonitor", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketAccessMonitorOutput>() {
                }).setRequestInfo(res.RequestInfo()));

    }

    public PutBucketTrashOutput putBucketTrash(PutBucketTrashInput input) {
        ParamsChecker.ensureNotNull(input, "PutBucketTrashInput");
        ParamsChecker.ensureNotNull(input.getTrash(), "Trash");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("trash", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketTrashOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketTrashOutput getBucketTrash(GetBucketTrashInput input) {
        ParamsChecker.ensureNotNull(input, "GetBucketTrashInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("trash", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketTrashOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public TosRequestFactory getFactory() {
        return factory;
    }

    public TosBucketRequestHandler setFactory(TosRequestFactory factory) {
        this.factory = factory;
        return this;
    }

    public TosBucketRequestHandler setTransport(Transport transport) {
        if (this.bucketHandler == null) {
            this.bucketHandler = new RequestHandler(transport);
        } else {
            this.bucketHandler.setTransport(transport);
        }
        return this;
    }

    public Transport getTransport() {
        if (this.bucketHandler != null) {
            return this.bucketHandler.getTransport();
        }
        return null;
    }

    private void ensureValidBucketName(String bucket) {
        if (this.factory.isCustomDomain()) {
            // 使用自定义域名时不校验桶名
            return;
        }
        ParamsChecker.isValidBucketName(bucket);
    }

    public PutQosPolicyOutput putQosPolicy(PutQosPolicyInput input) {
        ParamsChecker.ensureNotNull(input, "PutQosPolicyInput");
        ParamsChecker.ensureNotNull(input.getPolicy(), "policy");

        RequestBuilder builder = this.factory.initControlReq(input.getAccountId(), "qospolicy", null);
        builder = this.handleGenericInput(builder, input);
        byte[] policyStr = input.getPolicy().getBytes(StandardCharsets.UTF_8);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(policyStr)).setContentLength(policyStr.length);

        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new PutQosPolicyOutput(res.RequestInfo()));
    }

    public DeleteQosPolicyOutput deleteQosPolicy(DeleteQosPolicyInput input) {
        ParamsChecker.ensureNotNull(input, "DeleteQosPolicyInput");
        RequestBuilder builder = this.factory.initControlReq(input.getAccountId(), "qospolicy", null);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);

        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteQosPolicyOutput(res.RequestInfo()));
    }

    public GetQosPolicyOutput getQosPolicy(GetQosPolicyInput input) {
        ParamsChecker.ensureNotNull(input, "GetQosPolicyInput");
        RequestBuilder builder = this.factory.initControlReq(input.getAccountId(), "qospolicy", null);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new GetQosPolicyOutput().setRequestInfo(res.RequestInfo())
                .setPolicy(StringUtils.toString(res.getInputStream(), "qos policy")));
    }

    public CreateAccessPointOutput createAccessPoint(CreateAccessPointInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateAccessPointInput");
        ParamsChecker.ensureNotNull(input.getAccountId(), "accountId");
        ParamsChecker.ensureNotNull(input.getAccessPointName(), "accessPointName");
        ParamsChecker.ensureNotNull(input.getBucket(), "Bucket");
        ParamsChecker.ensureNotNull(input.getNetworkOrigin(), "NetworkOrigin");

        ensureValidBucketName(input.getBucket());

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.initControlReq(input.getAccountId(), "accesspoint/" + input.getAccessPointName(), null)
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<CreateAccessPointOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public GetAccessPointOutput getAccessPoint(GetAccessPointInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetAccessPointInput");
        ParamsChecker.ensureNotNull(input.getAccountId(), "accountId");
        ParamsChecker.ensureNotNull(input.getAccessPointName(), "accessPointName");

        RequestBuilder builder = this.factory.initControlReq(input.getAccountId(), "accesspoint/" + input.getAccessPointName(), null);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetAccessPointOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public ListAccessPointsOutput listAccessPoints(ListAccessPointsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListAccessPointsInput");
        ParamsChecker.ensureNotNull(input.getAccountId(), "accountId");

        if (input.getBucket() != null) {
            ensureValidBucketName(input.getBucket());
        }

        RequestBuilder builder = this.factory.initControlReq(input.getAccountId(), "accesspoint", null)
                .withQuery("bucket", input.getBucket())
                .withQuery("maxResults", TosUtils.convertInteger(input.getMaxResults()))
                .withQuery("nextToken", input.getNextToken());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<ListAccessPointsOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteAccessPointOutput deleteAccessPoint(DeleteAccessPointInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteAccessPointInput");
        ParamsChecker.ensureNotNull(input.getAccountId(), "accountId");
        ParamsChecker.ensureNotNull(input.getAccessPointName(), "accessPointName");

        RequestBuilder builder = this.factory.initControlReq(input.getAccountId(), "accesspoint/" + input.getAccessPointName(), null);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);

        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT,
                res -> new DeleteAccessPointOutput().setRequestInfo(res.RequestInfo()));
    }

    public ListBindAcceleratorForAccessPointOutput listBindAcceleratorForAccessPoint(ListBindAcceleratorForAccessPointInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListBindAcceleratorForAccessPointInput");
        ParamsChecker.ensureNotNull(input.getAccountId(), "accountId");
        ParamsChecker.ensureNotNull(input.getAccessPointName(), "accessPointName");

        RequestBuilder builder = this.factory.initControlReq(input.getAccountId(),
                "accesspoint/" + input.getAccessPointName() + "/accelerator", null);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<ListBindAcceleratorForAccessPointOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public ListBindAccessPointForAcceleratorOutput listBindAccessPointForAccelerator(ListBindAccessPointForAcceleratorInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListBindAccessPointForAcceleratorInput");
        ParamsChecker.ensureNotNull(input.getAccountId(), "accountId");
        ParamsChecker.ensureNotNull(input.getAcceleratorId(), "acceleratorId");

        RequestBuilder builder = this.factory.initControlReq(input.getAccountId(),
                "accelerator/" + input.getAcceleratorId() + "/accesspoint", null);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<ListBindAccessPointForAcceleratorOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public PutAudioConvertTemplateOutput putAudioConvertTemplate(PutAudioConvertTemplateInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutAudioConvertTemplateInput");
        ParamsChecker.ensureNotNull(input.getName(), "name");
        ensureValidBucketName(input.getBucket());

        PutTemplateInput putTemplateInput = new PutTemplateInput();
        putTemplateInput.setName(input.getName());
        putTemplateInput.setAudioConvertConfig(input.getAudioConvertConfig());
        putTemplateInput.setTag("AudioConvert");

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(putTemplateInput);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("tag", "AudioConvert");
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.PUT,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> 
                new PutAudioConvertTemplateOutput().setRequestInfo(res.RequestInfo()).setId(StringUtils.toString(res.getInputStream(), "template id")));
    }

    public PutVideoConvertTemplateOutput putVideoConvertTemplate(PutVideoConvertTemplateInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutVideoConvertTemplateInput");
        ParamsChecker.ensureNotNull(input.getName(), "name");
        ensureValidBucketName(input.getBucket());

        PutTemplateInput putTemplateInput = new PutTemplateInput();
        putTemplateInput.setName(input.getName());
        putTemplateInput.setTranscodeConfig(input.getTranscodeConfig());
        putTemplateInput.setTag("Transcode");

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(putTemplateInput);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("tag", "Transcode");
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.PUT,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> 
                new PutVideoConvertTemplateOutput().setRequestInfo(res.RequestInfo()).setId(StringUtils.toString(res.getInputStream(), "template id")));
    }

    public PutConvertWorkflowOutput putConvertWorkflow(PutConvertWorkflowInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutConvertWorkflowInput");
        ParamsChecker.ensureNotNull(input.getRules(), "rules");
        ensureValidBucketName(input.getBucket());

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("workflow", "");
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.PUT,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> 
                new PutConvertWorkflowOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetConvertWorkflowOutput getConvertWorkflow(GetConvertWorkflowInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetConvertWorkflowInput");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("workflow", "");
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> 
                PayloadConverter.parsePayload(res.getInputStream(), new TypeReference<GetConvertWorkflowOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public DeleteConvertWorkflowOutput deleteConvertWorkflow(DeleteConvertWorkflowInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteConvertWorkflowInput");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("workflow", "");
        
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> 
                new DeleteConvertWorkflowOutput().setRequestInfo(res.RequestInfo()));
    }

    public GetVideoConvertTemplateOutput getVideoConvertTemplate(GetVideoConvertTemplateInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetVideoConvertTemplateInput");
        ParamsChecker.ensureNotNull(input.getId(), "id");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("tag", "Transcode")
                .withQuery("id", input.getId());
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetVideoConvertTemplateOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public GetVideoConvertJobOutput getVideoConvertJob(GetVideoConvertJobInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetVideoConvertJobInput");
        ParamsChecker.ensureNotNull(input.getJobId(), "jobId");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("job_type", "Transcode")
                .withQuery("job_id", input.getJobId())
                .withQuery("media_jobs", "");
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetVideoConvertJobOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public GetAudioConvertJobOutput getAudioConvertJob(GetAudioConvertJobInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetAudioConvertJobInput");
        ParamsChecker.ensureNotNull(input.getJobId(), "jobId");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("job_type", "AudioConvert")
                .withQuery("job_id", input.getJobId())
                .withQuery("media_jobs", "");
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetAudioConvertJobOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public CreateAsyncProcessTaskOutput createAsyncProcessTask(CreateAsyncProcessTaskInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateAsyncProcessTaskInput");
        ensureValidBucketName(input.getBucket());
        ParamsChecker.isValidKey(input.getKey());
        ParamsChecker.ensureNotNull(input.getAsyncProcess(), "asyncProcess");

        byte[] data = input.getAsyncProcess().getBytes(StandardCharsets.UTF_8);
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("x-tos-async-process", "")
                .withHeader(TosHeader.HEADER_CONTENT_TYPE, "application/json");
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST, new ByteArrayInputStream(data))
                .setContentLength(data.length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> {
            String body = StringUtils.toString(res.getInputStream(), "create async process task output");
            CreateAsyncProcessTaskOutput output;
            if (StringUtils.isNotEmpty(body) && body.trim().startsWith("{")) {
                output = PayloadConverter.parsePayload(body, new TypeReference<CreateAsyncProcessTaskOutput>() {
                });
            } else {
                output = new CreateAsyncProcessTaskOutput().setJobId(body);
            }
            return output.setRequestInfo(res.RequestInfo());
        });
    }

    public GetAnimationJobOutput getAnimationJob(GetAnimationJobInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetAnimationJobInput");
        ParamsChecker.ensureNotNull(input.getKey(), "key");
        ParamsChecker.ensureNotNull(input.getJobID(), "jobID");
        ensureValidBucketName(input.getBucket());

        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("job_type", "Animation")
                .withQuery("job_id", input.getJobID());
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetAnimationJobOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public GetAudioConvertTemplateOutput getAudioConvertTemplate(GetAudioConvertTemplateInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetAudioConvertTemplateInput");
        ParamsChecker.ensureNotNull(input.getId(), "id");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("tag", "AudioConvert")
                .withQuery("id", input.getId());
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetAudioConvertTemplateOutput>() {
                }).setRequestInfo(res.RequestInfo()));

    }

    public DeleteAudioConvertTemplateOutput deleteAudioConvertTemplate(DeleteAudioConvertTemplateInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteAudioConvertTemplateInput");
        ParamsChecker.ensureNotNull(input.getId(), "id");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("id", input.getId());
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> 
                new DeleteAudioConvertTemplateOutput().setRequestInfo(res.RequestInfo()));
    }

    public DeleteVideoConvertTemplateOutput deleteVideoConvertTemplate(DeleteVideoConvertTemplateInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteVideoConvertTemplateInput");
        ParamsChecker.ensureNotNull(input.getId(), "id");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("id", input.getId());
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> 
                new DeleteVideoConvertTemplateOutput().setRequestInfo(res.RequestInfo()));
    }

    public ListAudioConvertTemplatesOutput listAudioConvertTemplates(ListAudioConvertTemplatesInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListAudioConvertTemplatesInput");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("tag", "AudioConvert");
        
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> {
            ListAudioConvertTemplatesOutput output = new ListAudioConvertTemplatesOutput();
            List<AudioConvertTemplate> audioConvertTemplates = PayloadConverter.parsePayload(res.getInputStream(),
                    new TypeReference<List<AudioConvertTemplate>>() {
                    });
            if (audioConvertTemplates == null) {
                audioConvertTemplates = new ArrayList<>();
            }
            output.setAudioConvertTemplates(audioConvertTemplates);
            return output.setRequestInfo(res.RequestInfo());
        });
    }

    public ListVideoConvertTemplatesOutput listVideoConvertTemplates(ListVideoConvertTemplatesInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListVideoConvertTemplatesInput");
        ensureValidBucketName(input.getBucket());
        
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("tag", "Transcode");
        
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> {
            ListVideoConvertTemplatesOutput output = new ListVideoConvertTemplatesOutput();
            List<VideoConvertTemplate> videoConvertTemplates = PayloadConverter.parsePayload(res.getInputStream(),
                    new TypeReference<List<VideoConvertTemplate>>() {
                    });
            if (videoConvertTemplates == null) {
                videoConvertTemplates = new ArrayList<>();
            }
            output.setVideoConvertTemplates(videoConvertTemplates);
            return output.setRequestInfo(res.RequestInfo());
        });
    }

    public PutWatermarkTemplateOutput putWatermarkTemplate(PutWatermarkTemplateInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutWatermarkTemplateInput");
        ParamsChecker.ensureNotNull(input.getName(), "name");
        ParamsChecker.ensureNotNull(input.getWatermarkConfig(), "watermarkConfig");
        ensureValidBucketName(input.getBucket());

        PutTemplateInput putTemplateInput = new PutTemplateInput();
        putTemplateInput.setName(input.getName());
        putTemplateInput.setWatermarkConfig(input.getWatermarkConfig());
        putTemplateInput.setTag("Watermark");

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(putTemplateInput);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("tag", "Watermark")
                .withHeader(TosHeader.HEADER_CONTENT_TYPE, "application/json");
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.PUT,
                        new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return bucketHandler.doRequest(req, HttpStatus.OK, res ->
                new PutWatermarkTemplateOutput().setRequestInfo(res.RequestInfo())
                        .setId(StringUtils.toString(res.getInputStream(), "watermark template id")));
    }

    public GetWatermarkTemplateOutput getWatermarkTemplate(GetWatermarkTemplateInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetWatermarkTemplateInput");
        ParamsChecker.ensureNotNull(input.getId(), "id");
        ensureValidBucketName(input.getBucket());

        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("tag", "Watermark")
                .withQuery("id", input.getId());
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetWatermarkTemplateOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public ListWatermarkTemplatesOutput listWatermarkTemplates(ListWatermarkTemplatesInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListWatermarkTemplatesInput");
        ensureValidBucketName(input.getBucket());

        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("tag", "Watermark");
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> {
            ListWatermarkTemplatesOutput output = new ListWatermarkTemplatesOutput();
            List<WatermarkTemplate> watermarkTemplates = PayloadConverter.parsePayload(res.getInputStream(),
                    new TypeReference<List<WatermarkTemplate>>() {
                    });
            if (watermarkTemplates == null) {
                watermarkTemplates = new ArrayList<>();
            }
            output.setWatermarkTemplates(watermarkTemplates);
            return output.setRequestInfo(res.RequestInfo());
        });
    }

    public DeleteWatermarkTemplateOutput deleteWatermarkTemplate(DeleteWatermarkTemplateInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteWatermarkTemplateInput");
        ParamsChecker.ensureNotNull(input.getId(), "id");
        ensureValidBucketName(input.getBucket());

        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("process_template", "")
                .withQuery("id", input.getId());
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res ->
                new DeleteWatermarkTemplateOutput().setRequestInfo(res.RequestInfo()));
    }

    public FileCompressOutput fileCompress(FileCompressInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "FileCompressInput");
        ParamsChecker.ensureNotNull(input.getBucket(), "bucket");
        ensureValidBucketName(input.getBucket());
        ParamsChecker.ensureNotNull(input.getInput(), "Input");
        ParamsChecker.ensureNotNull(input.getFileCompressConfig(), "FileCompressConfig");
        ParamsChecker.ensureNotNull(input.getOutput(), "Output");

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("file_jobs", "")
                .withQuery("job_type", "FileCompress")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return bucketHandler.doRequest(req, HttpStatus.OK, res ->
                PayloadConverter.parsePayload(res.getInputStream(),
                        new TypeReference<FileCompressOutput>() {
                        }).setRequestInfo(res.RequestInfo()));
    }

    public FileUncompressOutput fileUncompress(FileUncompressInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "FileUncompressInput");
        ParamsChecker.ensureNotNull(input.getBucket(), "bucket");
        ensureValidBucketName(input.getBucket());
        ParamsChecker.ensureNotNull(input.getInput(), "Input");
        ParamsChecker.ensureNotNull(input.getFileUncompressConfig(), "FileUncompressConfig");
        ParamsChecker.ensureNotNull(input.getOutput(), "Output");

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("file_jobs", "")
                .withQuery("job_type", "FileUncompress")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return bucketHandler.doRequest(req, HttpStatus.OK, res ->
                PayloadConverter.parsePayload(res.getInputStream(),
                        new TypeReference<FileUncompressOutput>() {
                        }).setRequestInfo(res.RequestInfo()));
    }

    public PointCloudCompressOutput pointCloudCompress(PointCloudCompressInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PointCloudCompressInput");
        ParamsChecker.ensureNotNull(input.getBucket(), "bucket");
        ParamsChecker.ensureNotNull(input.getKey(), "key");
        ensureValidBucketName(input.getBucket());
        ParamsChecker.isValidKey(input.getKey());

        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("x-tos-process", "pointcloud/compress");
        if (StringUtils.isNotEmpty(input.getFormat())) {
            builder = builder.withQuery("format", input.getFormat());
        }
        if (StringUtils.isNotEmpty(input.getMethod())) {
            builder = builder.withQuery("method", input.getMethod());
        }
        if (StringUtils.isNotEmpty(input.getFields())) {
            builder = builder.withQuery("fields", input.getFields());
        }
        if (StringUtils.isNotEmpty(input.getLib())) {
            builder = builder.withQuery("lib", input.getLib());
        }
        if (input.getPointResolution() != null) {
            builder = builder.withQuery("point-resolution", String.valueOf(input.getPointResolution()));
        }
        if (input.getOctreeResolution() != null) {
            builder = builder.withQuery("octree-resolution", String.valueOf(input.getOctreeResolution()));
        }
        if (input.getDownSampling() != null) {
            builder = builder.withQuery("down-sampling", String.valueOf(input.getDownSampling()));
        }
        builder = this.handleGenericInput(builder, input);

        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        java.util.List<Integer> expectedCodes = new java.util.ArrayList<>(1);
        expectedCodes.add(HttpStatus.OK);
        TosResponse response = bucketHandler.doRequest(req, expectedCodes);
        PointCloudCompressOutput output = new PointCloudCompressOutput()
                .setRequestInfo(response.RequestInfo())
                .setContent(response.getInputStream());
        return output;
    }

    public CreateVideoConvertJobOutput createVideoConvertJob(CreateVideoConvertJobInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateVideoConvertJobInput");
        ParamsChecker.ensureNotNull(input.getInput(), "input");
        ParamsChecker.ensureNotNull(input.getOutput(), "output");
        ensureValidBucketName(input.getBucket());

        VideoConvertJobRequest jobRequest = new VideoConvertJobRequest();
        jobRequest.setInput(input.getInput());
        jobRequest.setTranscodeConfig(input.getTranscodeConfig());
        jobRequest.setOutput(input.getOutput());
        jobRequest.setCallback(input.getCallback());

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(jobRequest);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("job_type", "Transcode")
                .withQuery("media_jobs", "");
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<CreateVideoConvertJobOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

    public CreateAudioConvertJobOutput createAudioConvertJob(CreateAudioConvertJobInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateAudioConvertJobInput");
        ParamsChecker.ensureNotNull(input.getInput(), "input");
        ParamsChecker.ensureNotNull(input.getAudioConvertConfig(), "audioConvertConfig");
        ParamsChecker.ensureNotNull(input.getOutput(), "output");
        ensureValidBucketName(input.getBucket());

        // 构建请求体
        AudioConvertJobRequest jobRequest = new AudioConvertJobRequest();
        jobRequest.setTag("AudioConvert");
        jobRequest.setInput(input.getInput());
        jobRequest.setAudioConvertConfig(input.getAudioConvertConfig());
        jobRequest.setOutput(input.getOutput());

        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(jobRequest);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("job_type", "AudioConvert")
                .withQuery("media_jobs", "");
        builder = this.handleGenericInput(builder, input);
        
        TosRequest req = this.factory.build(builder, HttpMethod.POST,
                new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);

        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<CreateAudioConvertJobOutput>() {
                }).setRequestInfo(res.RequestInfo()));
    }

}

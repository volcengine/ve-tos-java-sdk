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

    public CreateVideoConvertJobOutput createVideoConvertJob(CreateVideoConvertJobInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateVideoConvertJobInput");
        ParamsChecker.ensureNotNull(input.getInput(), "input");
        ParamsChecker.ensureNotNull(input.getTranscodeConfig(), "transcodeConfig");
        ParamsChecker.ensureNotNull(input.getOutput(), "output");
        ensureValidBucketName(input.getBucket());

        // 构建请求体
        VideoConvertJobRequest jobRequest = new VideoConvertJobRequest();
        jobRequest.setTag("Transcode");
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

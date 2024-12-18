package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.AzRedundancyType;
import com.volcengine.tos.comm.common.BucketType;
import com.volcengine.tos.internal.util.*;
import com.volcengine.tos.model.GenericInput;
import com.volcengine.tos.model.bucket.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;

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
        return builder;
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
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(input.getPolicy()
                .getBytes(StandardCharsets.UTF_8))).setContentLength(input.getPolicy().length());
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
}

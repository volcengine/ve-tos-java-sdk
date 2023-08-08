package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.util.*;
import com.volcengine.tos.model.bucket.*;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class TosBucketRequestHandler {
    private RequestHandler bucketHandler;
    private TosRequestFactory factory;

    public TosBucketRequestHandler(Transport transport, TosRequestFactory factory) {
        this.bucketHandler = new RequestHandler(transport);
        this.factory = factory;
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
                .withHeader(TosHeader.HEADER_AZ_REDUNDANCY, input.getAzRedundancy() == null ? null: input.getAzRedundancy().toString());
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, null).setRetryableOnClientException(false);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new CreateBucketV2Output(res.RequestInfo(),
                        res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_LOCATION)));
    }

    public HeadBucketV2Output headBucket(HeadBucketV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "HeadBucketInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null);
        TosRequest req = this.factory.build(builder, HttpMethod.HEAD, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new HeadBucketV2Output(res.RequestInfo(),
                        res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_BUCKET_REGION),
                        TypeConverter.convertStorageClassType(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_STORAGE_CLASS)))
        );
    }

    public DeleteBucketOutput deleteBucket(DeleteBucketInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null).setRetryableOnClientException(false);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketOutput(res.RequestInfo()));
    }


    public ListBucketsV2Output listBuckets(ListBucketsV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListBucketsV2Input");
        RequestBuilder builder = this.factory.init("", "", null);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK,
                res -> PayloadConverter.parsePayload(res.getInputStream(),
                        new TypeReference<ListBucketsV2Output>(){}).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketPolicyOutput putBucketPolicy(PutBucketPolicyInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketPolicyInput");
        ParamsChecker.ensureNotNull(input.getPolicy(), "policy");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("policy", "");
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(input.getPolicy()
                .getBytes(StandardCharsets.UTF_8))).setContentLength(input.getPolicy().length());
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new PutBucketPolicyOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketPolicyOutput getBucketPolicy(GetBucketPolicyInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketPolicyInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("policy", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new GetBucketPolicyOutput().setRequestInfo(res.RequestInfo())
                .setPolicy(StringUtils.toString(res.getInputStream(), "bucket policy")));
    }

    public DeleteBucketPolicyOutput deleteBucketPolicy(DeleteBucketPolicyInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketPolicyInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("policy", "");
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
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(
                marshalResult.getData())).setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketCORSOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketCORSOutput getBucketCORS(GetBucketCORSInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketCORSInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("cors", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketCORSOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketCORSOutput deleteBucketCORS(DeleteBucketCORSInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketCORSInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("cors", "");
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
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketStorageClassOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketLocationOutput getBucketLocation(GetBucketLocationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketLocationInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("location", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketLocationOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketLifecycleOutput putBucketLifecycle(PutBucketLifecycleInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketLifecycleInput");
        ParamsChecker.ensureNotNull(input.getRules(), "LifecycleRules");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("lifecycle", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketLifecycleOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketLifecycleOutput getBucketLifecycle(GetBucketLifecycleInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketLifecycleInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("lifecycle", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketLifecycleOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketLifecycleOutput deleteBucketLifecycle(DeleteBucketLifecycleInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketLifecycleInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("lifecycle", "");
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
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketMirrorBackOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketMirrorBackOutput getBucketMirrorBack(GetBucketMirrorBackInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketMirrorBackInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("mirror", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketMirrorBackOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketMirrorBackOutput deleteBucketMirrorBack(DeleteBucketMirrorBackInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketMirrorBackInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("mirror", "");
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
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketReplicationOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketReplicationOutput deleteBucketReplication(DeleteBucketReplicationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketReplicationInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("replication", "");
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
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketVersioningOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketVersioningOutput getBucketVersioning(GetBucketVersioningInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketVersioningInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("versioning", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketVersioningOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketWebsiteOutput putBucketWebsite(PutBucketWebsiteInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketWebsiteInput");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("website", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketWebsiteOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketWebsiteOutput getBucketWebsite(GetBucketWebsiteInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketWebsiteInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("website", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketWebsiteOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketWebsiteOutput deleteBucketWebsite(DeleteBucketWebsiteInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketWebsiteInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("website", "");
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
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketNotificationOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketNotificationOutput getBucketNotification(GetBucketNotificationInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketNotificationInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("notification", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketNotificationOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketCustomDomainOutput putBucketCustomDomain(PutBucketCustomDomainInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketCustomDomainInput");
        ensureValidBucketName(input.getBucket());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("customdomain", "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketCustomDomainOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public ListBucketCustomDomainOutput listBucketCustomDomain(ListBucketCustomDomainInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListBucketCustomDomainInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("customdomain", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<ListBucketCustomDomainOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketCustomDomainOutput deleteBucketCustomDomain(DeleteBucketCustomDomainInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketCustomDomainInput");
        ParamsChecker.ensureNotNull(input.getDomain(), "Domain");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("customdomain", input.getDomain());
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
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketRealTimeLogOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketRealTimeLogOutput getBucketRealTimeLog(GetBucketRealTimeLogInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketRealTimeLogInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("realtimeLog", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketRealTimeLogOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketRealTimeLogOutput deleteBucketRealTimeLog(DeleteBucketRealTimeLogInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketRealTimeLogInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("realtimeLog", "");
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
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(data)).setContentLength(data.length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketACLOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketACLOutput getBucketACL(GetBucketACLInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketACLInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("acl", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketACLOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public PutBucketRenameOutput putBucketRename(PutBucketRenameInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutBucketRenameInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("rename", "");
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        builder.withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> new PutBucketRenameOutput()
                .setRequestInfo(res.RequestInfo()));
    }

    public GetBucketRenameOutput getBucketRename(GetBucketRenameInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetBucketRenameInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("rename", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return bucketHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                new TypeReference<GetBucketRenameOutput>(){}).setRequestInfo(res.RequestInfo()));
    }

    public DeleteBucketRenameOutput deleteBucketRename(DeleteBucketRenameInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteBucketRenameInput");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("rename", "");
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return bucketHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteBucketRenameOutput()
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

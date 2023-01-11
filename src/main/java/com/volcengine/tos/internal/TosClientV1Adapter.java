package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.UnexpectedStatusCodeException;
import com.volcengine.tos.comm.*;
import com.volcengine.tos.internal.util.*;
import com.volcengine.tos.model.acl.*;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TosClientV1Adapter {
    private final TosBucketRequestHandler bucketRequestHandler;
    private final TosObjectRequestHandler objectRequestHandler;
    private final TosFileRequestHandler fileRequestHandler;
    private final TosPreSignedRequestHandler preSignedRequestHandler;
    private static final ObjectMapper JSON = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final Logger log = LoggerFactory.getLogger(TosClientV1Adapter.class);

    public TosClientV1Adapter(TosBucketRequestHandler bucketRequestHandler,
                              TosObjectRequestHandler objectRequestHandler,
                              TosFileRequestHandler fileRequestHandler,
                              TosPreSignedRequestHandler preSignedRequestHandler) {
        ParamsChecker.ensureNotNull(bucketRequestHandler, "TosBucketRequestHandler");
        ParamsChecker.ensureNotNull(objectRequestHandler, "TosObjectRequestHandler");
        ParamsChecker.ensureNotNull(fileRequestHandler, "TosFileRequestHandler");
        ParamsChecker.ensureNotNull(preSignedRequestHandler, "TosPreSignedRequestHandler");
        ParamsChecker.ensureNotNull(objectRequestHandler.getTransport(), "Transport");
        ParamsChecker.ensureNotNull(objectRequestHandler.getFactory(), "TosFactory");
        this.bucketRequestHandler = bucketRequestHandler;
        this.objectRequestHandler = objectRequestHandler;
        this.fileRequestHandler = fileRequestHandler;
        this.preSignedRequestHandler = preSignedRequestHandler;
    }

    public CreateBucketOutput createBucket(CreateBucketInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateBucketInput");
        CreateBucketV2Input v2Input = CreateBucketV2Input.builder().bucket(input.getBucket())
                .acl(TypeConverter.convertACLType(input.getAcl()))
                .storageClass(TypeConverter.convertStorageClassType(input.getStorageClass()))
                .grantFullControl(input.getGrantFullControl())
                .grantRead(input.getGrantRead()).grantReadAcp(input.getGrantReadAcp())
                .grantWrite(input.getGrantWrite()).grantWriteAcp(input.getGrantWriteAcp()).build();
        CreateBucketV2Output v2Output = bucketRequestHandler.createBucket(v2Input);
        return new CreateBucketOutput(v2Output.getRequestInfo(), v2Output.getLocation());
    }

    public HeadBucketOutput headBucket(String bucket) throws TosException {
        HeadBucketV2Input v2Input = HeadBucketV2Input.builder().bucket(bucket).build();
        HeadBucketV2Output v2Output = bucketRequestHandler.headBucket(v2Input);
        String storageClass = v2Output.getStorageClass() == null ? null : v2Output.getStorageClass().toString();
        return new HeadBucketOutput(v2Output.getRequestInfo(), v2Output.getRegion(), storageClass);
    }

    public ListBucketsOutput listBuckets(ListBucketsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListBucketsInput");
        ListBucketsV2Output v2Output = bucketRequestHandler.listBuckets(new ListBucketsV2Input());
        ListBucketsOutput output = new ListBucketsOutput().setRequestInfo(v2Output.getRequestInfo());
        if (v2Output.getBuckets() != null) {
            ListedBucket[] buckets = new ListedBucket[v2Output.getBuckets().size()];
            for (int i = 0; i < v2Output.getBuckets().size(); i++) {
                buckets[i] = v2Output.getBuckets().get(i);
            }
            output.setBuckets(buckets);
        }
        if (v2Output.getOwner() != null) {
            Owner owner = v2Output.getOwner();
            output.setOwner(new ListedOwner().setId(owner.getId()).setDisplayName(owner.getDisplayName()));
        }
        return output;
    }

    public GetObjectOutput getObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        GetObjectV2Input v2Input = new GetObjectV2Input().setBucket(bucket).setKey(objectKey);
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getQuery() != null && builder.getQuery().containsKey("versionId")) {
                v2Input.setVersionID(builder.getQuery().get("versionId"));
            }
            if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                v2Input.setOptions(new ObjectMetaRequestOptions().setHeaders(builder.getHeaders()));
            }
        }
        GetObjectV2Output v2Output = objectRequestHandler.getObject(v2Input);
        GetObjectOutput output = new GetObjectOutput().setContent(v2Output.getContent());
        if (v2Output.getGetObjectBasicOutput() != null) {
            output.setRequestInfo(v2Output.getGetObjectBasicOutput().getRequestInfo())
                    .setObjectMeta(new ObjectMeta().fromGetObjectV2Output(v2Output.getGetObjectBasicOutput()))
                    .setContentRange(v2Output.getGetObjectBasicOutput().getContentRange());
        }
        return output;
    }

    public HeadObjectOutput headObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        HeadObjectV2Input v2Input = new HeadObjectV2Input().setBucket(bucket).setKey(objectKey);
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getQuery() != null && builder.getQuery().containsKey("versionId")) {
                v2Input.setVersionID(builder.getQuery().get("versionId"));
            }
            if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                v2Input.setOptions(new ObjectMetaRequestOptions().setHeaders(builder.getHeaders()));
            }
        }
        HeadObjectV2Output v2Output = objectRequestHandler.headObject(v2Input);
        return new HeadObjectOutput().setRequestInfo(v2Output.getRequestInfo()).setContentRange(v2Output.getContentRange())
                .setObjectMeta(new ObjectMeta().fromGetObjectV2Output(v2Output.getHeadObjectBasicOutput()));
    }

    public DeleteObjectOutput deleteObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        DeleteObjectInput v2Input = new DeleteObjectInput().setBucket(bucket).setKey(objectKey);
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getQuery() != null && builder.getQuery().containsKey("versionId")) {
                v2Input.setVersionID(builder.getQuery().get("versionId"));
            }
        }
        return objectRequestHandler.deleteObject(v2Input);
    }

    public DeleteMultiObjectsOutput deleteMultiObjects(String bucket, DeleteMultiObjectsInput input, RequestOptionsBuilder... builders) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteMultiObjectsInput");
        DeleteMultiObjectsV2Input v2Input = new DeleteMultiObjectsV2Input().setBucket(bucket).setQuiet(input.isQuiet());
        if (input.getObjectTobeDeleteds() != null) {
            v2Input.setObjects(new ArrayList<>(Arrays.asList(input.getObjectTobeDeleteds())));
        }
        DeleteMultiObjectsV2Output v2Output = objectRequestHandler.deleteMultiObjects(v2Input);
        DeleteMultiObjectsOutput output = new DeleteMultiObjectsOutput().setRequestInfo(v2Output.getRequestInfo());
        if (v2Output.getDeleteds() != null) {
            Deleted[] deleteds = new Deleted[v2Output.getDeleteds().size()];
            for (int i = 0; i < v2Output.getDeleteds().size(); i++) {
                deleteds[i] = v2Output.getDeleteds().get(i);
            }
            output.setDeleteds(deleteds);
        }
        if (v2Output.getErrors() != null) {
            DeleteError[] errors = new DeleteError[v2Output.getDeleteds().size()];
            for (int i = 0; i < v2Output.getDeleteds().size(); i++) {
                errors[i] = v2Output.getErrors().get(i);
            }
            output.setErrors(errors);
        }
        return output;
    }

    public PutObjectOutput putObject(String bucket, String objectKey, InputStream inputStream, RequestOptionsBuilder... builders) throws TosException {
        PutObjectInput input = new PutObjectInput().setContent(inputStream).setBucket(bucket).setKey(objectKey);
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                input.setOptions(new ObjectMetaRequestOptions().setHeaders(builder.getHeaders()));
            }
        }
        return objectRequestHandler.putObject(input);
    }

    public UploadFileOutput uploadFile(String bucket, UploadFileInput input, RequestOptionsBuilder... builders) throws TosException {
        ParamsChecker.ensureNotNull(input, "UploadFileInput");
        UploadFileV2Input v2Input = new UploadFileV2Input().setBucket(bucket).setKey(input.getObjectKey())
                .setCheckpointFile(input.getCheckpointFile()).setFilePath(input.getUploadFilePath())
                .setPartSize(input.getPartSize()).setTaskNum(input.getTaskNum()).setEnableCheckpoint(input.isEnableCheckpoint());
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                v2Input.setOptions(new ObjectMetaRequestOptions().setHeaders(builder.getHeaders()));
            }
        }
        UploadFileV2Output v2Output = fileRequestHandler.uploadFile(v2Input);
        CompleteMultipartUploadOutput completeMultipartUploadOutput = new CompleteMultipartUploadOutput()
                .setRequestInfo(v2Output.getRequestInfo()).setVersionID(v2Output.getVersionID()).setCrc64(v2Output.getHashCrc64ecma());
        return new UploadFileOutput().setCompleteMultipartUploadOutput(completeMultipartUploadOutput)
                .setUploadID(v2Output.getUploadID()).setBucket(v2Output.getBucket()).setObjectKey(v2Output.getKey());
    }

    public AppendObjectOutput appendObject(String bucket, String objectKey, InputStream content, long offset, RequestOptionsBuilder... builders) throws TosException {
        ParamsChecker.isValidBucketNameAndKey(bucket, objectKey);
        TosRequestFactory factory = objectRequestHandler.getFactory();
        RequestBuilder rb = factory.init(bucket, objectKey, null);
        for (RequestOptionsBuilder builder : builders) {
            builder.withOption(rb);
        }
        rb.withQuery("append", "").withQuery("offset", String.valueOf(offset));
        String contentType = rb.getHeaders().get(TosHeader.HEADER_CONTENT_TYPE);
        if (StringUtils.isEmpty(contentType)){
            if (rb.isAutoRecognizeContentType()){
                // set content type before upload
                contentType = MimeType.getInstance().getMimetype(objectKey);
                rb.getHeaders().put(TosHeader.HEADER_CONTENT_TYPE, contentType);
            }
        }
        TosRequest req = factory.build(rb, HttpMethod.POST, content).setEnableCrcCheck(false)
                // appendObject should not retry
                .setRetryableOnServerException(false).setRetryableOnClientException(false);
        AppendObjectOutput output = null;
        try(TosResponse res = roundTrip(req, HttpStatus.OK)) {
            String nextOffset = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_NEXT_APPEND_OFFSET);
            long appendOffset;
            try{
                appendOffset = Long.parseLong(nextOffset);
            } catch (NumberFormatException nfe){
                throw new TosClientException("server return unexpected Next-Append-Offset header: "+nextOffset, nfe);
            }
            output = new AppendObjectOutput().setRequestInfo(res.RequestInfo()).setNextAppendOffset(appendOffset)
                    .setEtag(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG))
                    .setCrc64(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
        }catch (IOException e) {
            log.debug("tos: close response body failed, {}", e.toString());
        }
        return output;
    }

    public SetObjectMetaOutput setObjectMeta(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        SetObjectMetaInput input = new SetObjectMetaInput().setBucket(bucket).setKey(objectKey);
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getQuery() != null && builder.getQuery().containsKey("versionId")) {
                input.setVersionID(builder.getQuery().get("versionId"));
            }
            if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                input.setOptions(new ObjectMetaRequestOptions().setHeaders(builder.getHeaders()));
            }
        }
        return objectRequestHandler.setObjectMeta(input);
    }

    public ListObjectsOutput listObjects(String bucket, ListObjectsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListObjectsInput");
        ListObjectsV2Input v2Input = new ListObjectsV2Input().setBucket(bucket).setPrefix(input.getPrefix())
                .setDelimiter(input.getDelimiter()).setEncodingType(input.getEncodingType())
                .setMarker(input.getMarker()).setMaxKeys(input.getMaxKeys()).setReverse(input.isReverse());
        ListObjectsV2Output v2Output = objectRequestHandler.listObjects(v2Input);
        ListObjectsOutput output = new ListObjectsOutput().setRequestInfo(v2Output.getRequestInfo())
                .setPrefix(v2Output.getPrefix()).setDelimiter(v2Output.getDelimiter())
                .setMarker(v2Output.getMarker()).setMaxKeys(v2Output.getMaxKeys())
                .setEncodingType(v2Output.getEncodingType()).setName(v2Output.getName())
                .setNextMarker(v2Output.getNextMarker()).setTruncated(v2Output.isTruncated());
        if (v2Output.getContents() != null) {
            ListedObject[] objects = new ListedObject[v2Output.getContents().size()];
            for (int i = 0; i < v2Output.getContents().size(); i++) {
                ListedObjectV2 objectV2 = v2Output.getContents().get(i);
                objects[i] = new ListedObject().setEtag(objectV2.getEtag()).setKey(objectV2.getKey())
                        .setOwner(objectV2.getOwner()).setSize(objectV2.getSize()).setType(objectV2.getType())
                        .setLastModified(DateConverter.dateToRFC1123String(objectV2.getLastModified()))
                        .setStorageClass(objectV2.getStorageClass());
            }
            output.setContents(objects);
        }
        if (v2Output.getCommonPrefixes() != null) {
            ListedCommonPrefix[] commonPrefixes = new ListedCommonPrefix[v2Output.getCommonPrefixes().size()];
            for (int i = 0; i < v2Output.getCommonPrefixes().size(); i++) {
                commonPrefixes[i] = v2Output.getCommonPrefixes().get(i);
            }
            output.setCommonPrefixes(commonPrefixes);
        }
        return output;
    }

    public ListObjectVersionsOutput listObjectVersions(String bucket, ListObjectVersionsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListObjectVersionsInput");
        ListObjectVersionsV2Input v2Input = new ListObjectVersionsV2Input().setBucket(bucket).setPrefix(input.getPrefix())
                .setDelimiter(input.getDelimiter()).setEncodingType(input.getEncodingType()).setMaxKeys(input.getMaxKeys())
                .setKeyMarker(input.getKeyMarker()).setVersionIDMarker(input.getVersionIDMarker());
        ListObjectVersionsV2Output v2Output = objectRequestHandler.listObjectVersions(v2Input);
        ListObjectVersionsOutput output = new ListObjectVersionsOutput().setRequestInfo(v2Output.getRequestInfo())
                .setPrefix(v2Output.getPrefix()).setDelimiter(v2Output.getDelimiter()).setMaxKeys(v2Output.getMaxKeys())
                .setKeyMarker(v2Output.getKeyMarker()).setNextKeyMarker(v2Output.getNextKeyMarker())
                .setEncodingType(v2Output.getEncodingType()).setName(v2Output.getName()).setTruncated(v2Output.isTruncated())
                .setVersionIDMarker(v2Output.getVersionIDMarker()).setNextVersionIDMarker(v2Output.getNextVersionIDMarker());
        if (v2Output.getDeleteMarkers() != null) {
            ListedDeleteMarkerEntry[] deleteMarkerEntries = new ListedDeleteMarkerEntry[v2Output.getDeleteMarkers().size()];
            for (int i = 0; i < v2Output.getDeleteMarkers().size(); i++) {
                deleteMarkerEntries[i] = v2Output.getDeleteMarkers().get(i);
            }
            output.setDeleteMarkers(deleteMarkerEntries);
        }
        if (v2Output.getCommonPrefixes() != null) {
            ListedCommonPrefix[] commonPrefixes = new ListedCommonPrefix[v2Output.getCommonPrefixes().size()];
            for (int i = 0; i < v2Output.getCommonPrefixes().size(); i++) {
                commonPrefixes[i] = v2Output.getCommonPrefixes().get(i);
            }
            output.setCommonPrefixes(commonPrefixes);
        }
        return output;
    }

    private CopyObjectOutput copyObject(CopyObjectV2Input v2Input, RequestOptionsBuilder... builders) {
        ParamsChecker.ensureNotNull(v2Input, "CopyObjectV2Input");
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                v2Input.setOptions(new ObjectMetaRequestOptions().setHeaders(builder.getHeaders()));
            }
        }
        CopyObjectV2Output v2Output = objectRequestHandler.copyObject(v2Input);
        return new CopyObjectOutput().setRequestInfo(v2Output.getRequestInfo()).setEtag(v2Output.getEtag())
                .setCrc64(v2Output.getHashCrc64ecma()).setVersionID(v2Output.getVersionID())
                .setLastModified(v2Output.getLastModified()).setSourceVersionID(v2Output.getSourceVersionID());
    }

    public CopyObjectOutput copyObject(String bucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        CopyObjectV2Input v2Input = new CopyObjectV2Input().setBucket(bucket).setKey(dstObjectKey)
                .setSrcKey(srcObjectKey).setSrcBucket(bucket);
        return copyObject(v2Input, builders);
    }

    public CopyObjectOutput copyObjectTo(String bucket, String dstBucket, String dstObjectKey, String srcObjectKey, RequestOptionsBuilder... builders) throws TosException {
        CopyObjectV2Input v2Input = new CopyObjectV2Input().setBucket(dstBucket).setKey(dstObjectKey)
                .setSrcKey(srcObjectKey).setSrcBucket(bucket);
        return copyObject(v2Input, builders);
    }

    public CopyObjectOutput copyObjectFrom(String bucket, String srcBucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        CopyObjectV2Input v2Input = new CopyObjectV2Input().setBucket(bucket).setKey(dstObjectKey)
                .setSrcKey(srcObjectKey).setSrcBucket(srcBucket);
        return copyObject(v2Input, builders);
    }

    public UploadPartCopyOutput uploadPartCopy(String bucket, UploadPartCopyInput input, RequestOptionsBuilder... builders) throws TosException {
        ParamsChecker.ensureNotNull(input, "UploadPartCopyInput");
        UploadPartCopyV2Input v2Input = new UploadPartCopyV2Input().setBucket(bucket).setKey(input.getDestinationKey())
                .setSourceBucket(input.getSourceBucket()).setSourceKey(input.getSourceKey())
                .setSourceVersionID(input.getSourceVersionID()).setPartNumber(input.getPartNumber()).setUploadID(input.getUploadID())
                .setCopySourceRange(input.getStartOffset(), input.getStartOffset()+input.getPartSize()-1);
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                v2Input.setOptions(new ObjectMetaRequestOptions().setHeaders(builder.getHeaders()));
            }
        }
        UploadPartCopyV2Output v2Output = objectRequestHandler.uploadPartCopy(v2Input);
        return new UploadPartCopyOutput().setPartNumber(v2Output.getPartNumber()).setEtag(v2Output.getEtag())
                .setSourceVersionID(v2Output.getCopySourceVersionID()).setRequestInfo(v2Output.getRequestInfo())
                .setLastModified(DateConverter.dateToRFC1123String(v2Output.getLastModified()));
    }

    public PutObjectAclOutput putObjectAcl(String bucket, PutObjectAclInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectAclInput");
        PutObjectACLInput aclInput = new PutObjectACLInput().setBucket(bucket).setKey(input.getKey()).setVersionID(input.getVersionId());
        if (input.getAclGrant() != null) {
            aclInput.setAcl(TypeConverter.convertACLType(input.getAclGrant().getAcl()));
            aclInput.setGrantFullControl(input.getAclGrant().getGrantFullControl());
            aclInput.setGrantRead(input.getAclGrant().getGrantRead());
            aclInput.setGrantReadAcp(input.getAclGrant().getGrantReadAcp());
            aclInput.setGrantWriteAcp(input.getAclGrant().getGrantWriteAcp());
        }
        if (input.getAclRules() != null) {
            ObjectAclRulesV2 aclRulesV2 = new ObjectAclRulesV2().setOwner(input.getAclRules().getOwner());
            if (input.getAclRules().getGrants() != null) {
                aclRulesV2.setGrants(getGrantV2List(input.getAclRules().getGrants()));
            }
            aclInput.setObjectAclRules(aclRulesV2);
        }
        PutObjectACLOutput aclOutput = objectRequestHandler.putObjectAcl(aclInput);
        return new PutObjectAclOutput().setRequestInfo(aclOutput.getRequestInfo());
    }

    private List<GrantV2> getGrantV2List(Grant[] input) {
        if (input == null) {
            return null;
        }
        List<GrantV2> grantV2s = new ArrayList<>(input.length);
        for (Grant grant : input) {
            GrantV2 grantV2 = new GrantV2().setPermission(TypeConverter.convertPermissionType(grant.getPermission()));
            if (grant.getGrantee() != null) {
                Grantee grantee = grant.getGrantee();
                grantV2.setGrantee(new GranteeV2().setCanned(TypeConverter.convertCannedType(grantee.getUri()))
                        .setId(grantee.getId()).setDisplayName(grantee.getDisplayName())
                        .setType(TypeConverter.convertGranteeType(grantee.getType())));
            }
            grantV2s.add(grantV2);
        }
        return grantV2s;
    }

    public GetObjectAclOutput getObjectAcl(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        GetObjectACLV2Input aclv2Input = new GetObjectACLV2Input().setBucket(bucket).setKey(objectKey);
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getQuery() != null && builder.getQuery().containsKey("versionId")) {
                aclv2Input.setVersionID(builder.getQuery().get("versionId"));
            }
        }
        GetObjectACLV2Output aclv2Output = objectRequestHandler.getObjectAcl(aclv2Input);
        GetObjectAclOutput output = new GetObjectAclOutput().setRequestInfo(aclv2Output.getRequestInfo())
                .setVersionId(aclv2Output.getVersionID()).setOwner(aclv2Output.getOwner());
        if (aclv2Output.getGrants() != null) {
            output.setGrants(getGrants(aclv2Output.getGrants()));
        }
        return output;
    }

    private Grant[] getGrants(List<GrantV2> grantV2s) {
        if (grantV2s == null) {
            return null;
        }
        Grant[] grants = new Grant[grantV2s.size()];
        for (int i = 0; i < grantV2s.size(); i++) {
            GrantV2 grantV2 = grantV2s.get(i);
            String permission = grantV2.getPermission() == null ? null : grantV2.getPermission().toString();
            Grant grant = new Grant().setPermission(permission);
            if (grantV2.getGrantee() != null) {
                GranteeV2 grantee = grantV2.getGrantee();
                String uri = grantee.getCanned() == null ? null : grantee.getCanned().toString();
                String type = grantee.getType() == null ? null : grantee.getType().toString();
                grant.setGrantee(new Grantee().setUri(uri).setType(type)
                        .setId(grantee.getId()).setDisplayName(grantee.getDisplayName()));
            }
            grants[i] = grant;
        }
        return grants;
    }

    public CreateMultipartUploadOutput createMultipartUpload(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        CreateMultipartUploadInput createMultipartUploadInput = new CreateMultipartUploadInput().setBucket(bucket).setKey(objectKey);
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                createMultipartUploadInput.setOptions(new ObjectMetaRequestOptions().setHeaders(builder.getHeaders()));
            }
        }
        return objectRequestHandler.createMultipartUpload(createMultipartUploadInput);
    }

    public UploadPartOutput uploadPart(String bucket, UploadPartInput input, RequestOptionsBuilder... builders) throws TosException {
        ParamsChecker.ensureNotNull(input, "UploadPartInput");
        UploadPartV2Input v2Input = new UploadPartV2Input().setBucket(bucket).setKey(input.getKey())
                .setPartNumber(input.getPartNumber()).setUploadID(input.getUploadID())
                .setContent(input.getContent()).setContentLength(input.getPartSize());
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            if (builder.getHeaders() != null && builder.getHeaders().size() > 0) {
                v2Input.setOptions(new ObjectMetaRequestOptions().setHeaders(builder.getHeaders()));
            }
        }
        UploadPartV2Output v2Output = objectRequestHandler.uploadPart(v2Input);
        return new UploadPartOutput().setRequestInfo(v2Output.getRequestInfo())
                .setEtag(v2Output.getEtag()).setPartNumber(v2Output.getPartNumber())
                .setSseCustomerAlgorithm(v2Output.getSsecAlgorithm()).setSseCustomerMD5(v2Output.getSsecKeyMD5());
    }

    public CompleteMultipartUploadOutput completeMultipartUpload(String bucket, CompleteMultipartUploadInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CompleteMultipartUploadInput");
        CompleteMultipartUploadV2Input v2Input = new CompleteMultipartUploadV2Input().setBucket(bucket)
                .setUploadID(input.getUploadID()).setKey(input.getKey()).setUploadedParts(getUploadedParts(input));
        CompleteMultipartUploadV2Output v2Output = objectRequestHandler.completeMultipartUpload(v2Input);
        return new CompleteMultipartUploadOutput().setRequestInfo(v2Output.getRequestInfo())
                .setVersionID(v2Output.getVersionID()).setCrc64(v2Output.getHashCrc64ecma());
    }

    private List<UploadedPartV2> getUploadedParts(CompleteMultipartUploadInput input) {
        if (input == null) {
            return null;
        }
        if (input.getMultiUploadedParts() != null) {
            List<UploadedPartV2> uploadedPartV2s = new ArrayList<>(input.getMultiUploadedParts().size());
            for (MultipartUploadedPart part : input.getUploadedParts()) {
                uploadedPartV2s.add(getUploadedPart(part));
            }
            return uploadedPartV2s;
        } else if (input.getUploadedParts() != null) {
            List<UploadedPartV2> uploadedPartV2s = new ArrayList<>(input.getMultiUploadedParts().size());
            for (MultipartUploadedPart part : input.getUploadedParts()) {
                uploadedPartV2s.add(getUploadedPart(part));
            }
            return uploadedPartV2s;
        }
        return null;
    }

    private UploadedPartV2 getUploadedPart(MultipartUploadedPart part) {
        if (part == null) {
            return null;
        }
        InnerUploadedPart innerUploadedPart = part.uploadedPart();
        if (innerUploadedPart != null) {
            return new UploadedPartV2().setEtag(innerUploadedPart.getEtag())
                    .setPartNumber(innerUploadedPart.getPartNumber());
        }
        return null;
    }

    public AbortMultipartUploadOutput abortMultipartUpload(String bucket, AbortMultipartUploadInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "AbortMultipartUploadInput");
        return objectRequestHandler.abortMultipartUpload(input.setBucket(bucket));
    }

    public ListUploadedPartsOutput listUploadedParts(String bucket, ListUploadedPartsInput input, RequestOptionsBuilder... builders) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListUploadedPartsInput");
        ListPartsInput listPartsInput = new ListPartsInput().setBucket(bucket).setKey(input.getKey()).setMaxParts(input.getMaxParts())
                .setUploadID(input.getUploadID()).setPartNumberMarker(input.getPartNumberMarker());
        ListPartsOutput listPartsOutput = objectRequestHandler.listParts(listPartsInput);
        String storageClass = listPartsOutput.getStorageClass() == null ? null : listPartsOutput.getStorageClass().toString();
        ListUploadedPartsOutput output = new ListUploadedPartsOutput().setRequestInfo(listPartsOutput.getRequestInfo())
                .setBucket(listPartsOutput.getBucket()).setKey(listPartsOutput.getKey()).setMaxParts(listPartsOutput.getMaxParts())
                .setPartNumberMarker(listPartsOutput.getPartNumberMarker()).setOwner(listPartsOutput.getOwner())
                .setNextPartNumberMarker(listPartsOutput.getNextPartNumberMarker()).setStorageClass(storageClass)
                .setTruncated(listPartsOutput.isTruncated()).setUploadID(listPartsOutput.getUploadID());
        if (listPartsOutput.getUploadedParts() != null) {
            UploadedPart[] uploadedParts = new UploadedPart[listPartsOutput.getUploadedParts().size()];
            for (int i = 0; i < listPartsOutput.getUploadedParts().size(); i++) {
                UploadedPartV2 partV2 = listPartsOutput.getUploadedParts().get(i);
                UploadedPart part = new UploadedPart().setPartNumber(partV2.getPartNumber())
                        .setEtag(partV2.getEtag()).setSize(partV2.getSize())
                        .setLastModified(DateConverter.dateToRFC1123String(partV2.getLastModified()));
                uploadedParts[i] = part;
            }
            output.setUploadedParts(uploadedParts);
        }
        return output;
    }

    public ListMultipartUploadsOutput listMultipartUploads(String bucket, ListMultipartUploadsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListMultipartUploadsInput");
        ListMultipartUploadsV2Input v2Input = new ListMultipartUploadsV2Input().setBucket(bucket)
                .setMaxUploads(input.getMaxUploads()).setDelimiter(input.getDelimiter())
                .setPrefix(input.getPrefix()).setKeyMarker(input.getKeyMarker()).setUploadIDMarker(input.getUploadIDMarker());
        ListMultipartUploadsV2Output v2Output = objectRequestHandler.listMultipartUploads(v2Input);
        ListMultipartUploadsOutput output = new ListMultipartUploadsOutput().setMaxUploads(v2Output.getMaxUploads())
                .setRequestInfo(v2Output.getRequestInfo()).setBucket(v2Output.getBucket()).setDelimiter(v2Output.getDelimiter())
                .setKeyMarker(v2Output.getKeyMarker()).setNextKeyMarker(v2Output.getNextKeyMarker()).setTruncated(v2Output.isTruncated())
                .setUploadIDMarker(v2Output.getUploadIDMarker()).setNextUploadIdMarker(v2Output.getNextUploadIdMarker());
        if (v2Output.getUploads() != null) {
            UploadInfo[] uploadInfos = new UploadInfo[v2Output.getUploads().size()];
            for (int i = 0; i < v2Output.getUploads().size(); i++) {
                ListedUpload listedUpload = v2Output.getUploads().get(i);
                String storageClass = listedUpload.getStorageClass() == null ? null : listedUpload.getStorageClass().toString();
                UploadInfo uploadInfo = new UploadInfo().setUploadID(listedUpload.getUploadID())
                        .setInitiated(DateConverter.dateToRFC1123String(listedUpload.getInitiated()))
                        .setKey(listedUpload.getKey()).setOwner(listedUpload.getOwner()).setStorageClass(storageClass);
                uploadInfos[i] = uploadInfo;
            }
            output.setUpload(uploadInfos);
        }
        return output;
    }

    public String preSignedURL(String httpMethod, String bucket, String objectKey, Duration ttl, RequestOptionsBuilder... builders) throws TosException {
        PreSignedURLInput input = new PreSignedURLInput().setBucket(bucket).setKey(objectKey).setHttpMethod(httpMethod);
        if (ttl != null) {
            input.setExpires(ttl.getSeconds());
        }
        if (builders != null && builders.length > 0) {
            RequestBuilder builder = new RequestBuilder();
            for (RequestOptionsBuilder b : builders) {
                b.withOption(builder);
            }
            input.setQuery(builder.getQuery());
            input.setHeader(builder.getHeaders());
        }
        PreSignedURLOutput output = preSignedRequestHandler.preSignedURL(input);
        return output.getSignedUrl();
    }

    TosResponse roundTrip(TosRequest request, int expectedCode) throws TosException {
        TosResponse res;
        try{
            res = objectRequestHandler.getTransport().roundTrip(request);
        } catch (IOException e){
            throw new TosClientException("request exception", e);
        } finally {
            if (request.getContent() != null) {
                try {
                    request.getContent().close();
                } catch (IOException e) {
                    log.debug("tos: close request body failed, {}", e.toString());
                }
            }
        }
        if (res.getStatusCode() == expectedCode) {
            return res;
        }
        try {
            if (res.getStatusCode() >= HttpStatus.BAD_REQUEST) {
                String s = StringUtils.toString(res.getInputStream());
                if (s.length() > 0) {
                    try{
                        ServerExceptionJson se = JSON.readValue(s, new TypeReference<ServerExceptionJson>(){});
                        throw new TosServerException(res.getStatusCode(), se.getCode(), se.getMessage(), se.getRequestID(), se.getHostID());
                    } catch (JsonProcessingException e) {
                        if (res.getStatusCode() == HttpStatus.BAD_REQUEST) {
                            throw new TosClientException("bad request, " + s, null);
                        }
                        throw new TosClientException("parse server exception failed", e);
                    }
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
        } catch (IOException e){
            throw new TosClientException("check exception error", e);
        }
        throw new UnexpectedStatusCodeException(res.getStatusCode(), expectedCode, res.getRequesID());
    }
}

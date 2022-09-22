package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.MimeType;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.model.CreateMultipartUploadOutputJson;
import com.volcengine.tos.internal.model.UploadPartCopyOutputJson;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.PayloadConverter;
import com.volcengine.tos.model.object.*;
import com.volcengine.tos.internal.util.StringUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TosObjectRequestHandlerImpl implements TosObjectRequestHandler {
    private RequestHandler objectHandler;
    private TosRequestFactory factory;
    private boolean clientAutoRecognizeContentType;

    public TosObjectRequestHandlerImpl(Transport transport, TosRequestFactory factory) {
        this.objectHandler = new RequestHandler(transport);
        this.factory = factory;
    }

    public TosObjectRequestHandlerImpl setClientAutoRecognizeContentType(boolean clientAutoRecognizeContentType) {
        this.clientAutoRecognizeContentType = clientAutoRecognizeContentType;
        return this;
    }

    @Override
    public GetObjectV2Output getObject(GetObjectV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "GetObjectV2Input");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(),
                input.getAllSettedHeaders()).withQuery("versionId", input.getVersionID());
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        TosResponse response = objectHandler.doRequest(req, getExpectedCodes(input.getAllSettedHeaders()));
        return buildGetObjectV2Output(response);
    }

    private static List<Integer> getExpectedCodes(Map<String, String> headers) {
        List<Integer> codes = new ArrayList<>(1);
        if (headers == null) {
            // default
            codes.add(HttpStatus.OK);
            return codes;
        }
        codes.add(HttpStatus.OK);
        if (headers.get(TosHeader.HEADER_RANGE) != null) {
            codes.add(HttpStatus.PARTIAL_CONTENT);
        }
        return codes;
    }

    private GetObjectV2Output buildGetObjectV2Output(TosResponse response) {
        GetObjectBasicOutput basicOutput = new GetObjectBasicOutput()
                .setRequestInfo(response.RequestInfo()).parseFromTosResponse(response);
        return new GetObjectV2Output(basicOutput, new TosObjectInputStream(response.getInputStream()));
    }

    @Override
    public HeadObjectV2Output headObject(HeadObjectV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "HeadObjectV2Input");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders());
        TosRequest req = this.factory.build(builder, HttpMethod.HEAD, null);
        return objectHandler.doRequest(req, getExpectedCodes(input.getAllSettedHeaders()),
                response -> new HeadObjectV2Output(new GetObjectBasicOutput()
                        .setRequestInfo(response.RequestInfo()).parseFromTosResponse(response)));
    }

    @Override
    public DeleteObjectOutput deleteObject(DeleteObjectInput input) throws TosException {
        ParamsChecker.isValidInput(input, "DeleteObjectInput");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("versionId", input.getVersionID());
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return objectHandler.doRequest(req, HttpStatus.NO_CONTENT,
                response -> new DeleteObjectOutput().setRequestInfo(response.RequestInfo())
                        .setDeleteMarker(Boolean.parseBoolean(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_DELETE_MARKER)))
                        .setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
        );
    }

    @Override
    public DeleteMultiObjectsV2Output deleteMultiObjects(DeleteMultiObjectsV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "DeleteMultiObjectsV2Input");
        ParamsChecker.isValidInput(input.getObjects(), "objects to be deleted are null");
        ParamsChecker.isValidBucketName(input.getBucket());
        for (ObjectTobeDeleted objectTobeDeleted : input.getObjects()) {
            ParamsChecker.isValidKey(objectTobeDeleted.getKey());
        }
        TosMarshalResult data = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withHeader(TosHeader.HEADER_CONTENT_MD5, data.getContentMD5())
                .withQuery("delete", "");
        TosRequest req = this.factory.build(builder, HttpMethod.POST, null).setData((data.getData()));
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> PayloadConverter.parsePayload(response.getInputStream(),
                        new TypeReference<DeleteMultiObjectsV2Output>(){}).requestInfo(response.RequestInfo())
        );
    }

    private PutObjectOutput putObject(PutObjectBasicInput input, InputStream content) {
        ParamsChecker.isValidInput(input, "PutObjectBasicInput");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders());
        addContentType(builder, input.getKey());
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, content);
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildPutObjectOutput);
    }

    private PutObjectOutput buildPutObjectOutput(TosResponse res) {
        return new PutObjectOutput().setRequestInfo(res.RequestInfo())
                .setEtag(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG))
                .setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setHashCrc64ecma(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64))
                .setSseCustomerAlgorithm(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerKeyMD5(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5))
                .setSseCustomerKey(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY));
    }

    @Override
    public PutObjectOutput putObject(PutObjectInput input) throws TosException {
        ParamsChecker.isValidInput(input, "PutObjectInput");
        return putObject(input.getPutObjectBasicInput(), input.getContent());
    }

    @Override
    public AppendObjectOutput appendObject(AppendObjectInput input) throws TosException {
        ParamsChecker.isValidInput(input, "AppendObjectInput");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        // append not support chunked, need to set contentLength
        if (input.getContentLength() < (128 << 10)) {
            throw new IllegalArgumentException("content length should not be less than 128KB, please set content length first");
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("append", "")
                .withQuery("offset", String.valueOf(input.getOffset()))
                .withContentLength(input.getContentLength());
        addContentType(builder, input.getKey());
        TosRequest req = this.factory.build(builder, HttpMethod.POST, input.getContent());
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildAppendObjectOutput);
    }

    private AppendObjectOutput buildAppendObjectOutput(TosResponse response) {
        String nextOffset = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_NEXT_APPEND_OFFSET);
        long appendOffset;
        try{
            appendOffset = Long.parseLong(nextOffset);
        } catch (NumberFormatException nfe){
            throw new TosClientException("server return unexpected Next-Append-Offset header: "+nextOffset, nfe);
        }
        return new AppendObjectOutput().setRequestInfo(response.RequestInfo())
                .setNextAppendOffset(appendOffset)
                .setHashCrc64ecma(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    @Override
    public SetObjectMetaOutput setObjectMeta(SetObjectMetaInput input) throws TosException {
        ParamsChecker.isValidInput(input, "SetObjectMetaInput");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(),
                input.getAllSettedHeaders()).withQuery("metadata", "");
        TosRequest req = this.factory.build(builder, HttpMethod.POST, null);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> new SetObjectMetaOutput().setRequestInfo(response.RequestInfo())
        );
    }

    @Override
    public ListObjectsV2Output listObjects(ListObjectsV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "ListObjectsV2Input");
        ParamsChecker.isValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("marker", input.getMarker())
                .withQuery("max-keys", String.valueOf(input.getMaxKeys()))
                .withQuery("reverse", String.valueOf(input.isReverse()))
                .withQuery("encoding-type", input.getEncodingType());
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> PayloadConverter.parsePayload(response.getInputStream(),
                        new TypeReference<ListObjectsV2Output>(){}).setRequestInfo(response.RequestInfo())
        );
    }

    @Override
    public ListObjectVersionsV2Output listObjectVersions(ListObjectVersionsV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "ListObjectVersionsV2Input");
        ParamsChecker.isValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("key-marker", input.getKeyMarker())
                .withQuery("max-keys", input.getMaxKeys() == 0 ? null : String.valueOf(input.getMaxKeys()))
                .withQuery("encoding-type", input.getEncodingType())
                .withQuery("versions", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> PayloadConverter.parsePayload(response.getInputStream(),
                        new TypeReference<ListObjectVersionsV2Output>(){}).setRequestInfo(response.RequestInfo())
        );
    }

    @Override
    public CopyObjectV2Output copyObject(CopyObjectV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "CopyObjectV2Input");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        ParamsChecker.isValidBucketNameAndKey(input.getSrcBucket(), input.getSrcKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders());
        TosRequest req = this.factory.buildWithCopy(builder, HttpMethod.PUT, input.getSrcBucket(), input.getSrcKey());
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildCopyObjectV2Output);
    }

    private CopyObjectV2Output buildCopyObjectV2Output(TosResponse response) {
        return PayloadConverter.parsePayload(response.getInputStream(), new TypeReference<CopyObjectV2Output>(){})
                .setRequestInfo(response.RequestInfo())
                .setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setSourceVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_COPY_SOURCE_VERSION_ID))
                .setHashCrc64ecma(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    @Override
    public UploadPartCopyV2Output uploadPartCopy(UploadPartCopyV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "UploadPartCopyV2Input");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        ParamsChecker.isValidBucketNameAndKey(input.getSourceBucket(), input.getSourceKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("partNumber", String.valueOf(input.getPartNumber()))
                .withQuery("uploadId", input.getUploadID())
                .withQuery("versionId", input.getSourceVersionID());
        TosRequest req = this.factory.buildWithCopy(builder, HttpMethod.PUT, input.getSourceBucket(), input.getSourceKey());
        return objectHandler.doRequest(req, HttpStatus.OK, response -> buildUploadPartCopyV2Output(input, response));
    }

    private UploadPartCopyV2Output buildUploadPartCopyV2Output(UploadPartCopyV2Input input, TosResponse response) {
        UploadPartCopyOutputJson out = PayloadConverter.parsePayload(response.getInputStream(),
                new TypeReference<UploadPartCopyOutputJson>(){});
        return new UploadPartCopyV2Output().requestInfo(response.RequestInfo())
                .copySourceVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_COPY_SOURCE_VERSION_ID))
                .partNumber(input.getPartNumber()).etag(out.getEtag()).lastModified(out.getLastModified());
    }

    @Override
    public PutObjectACLOutput putObjectAcl(PutObjectACLInput input) throws TosException {
        ParamsChecker.isValidInput(input, "PutObjectACLInput");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        byte[] content = null;
        if (input.getObjectAclRules() != null) {
            TosMarshalResult res = PayloadConverter.serializePayload(input.getObjectAclRules());
            content = res.getData();
        } else {
            // 防止NPE
            content = new byte[0];
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("acl", "")
                .withHeader(TosHeader.HEADER_ACL, input.getAcl() == null ? null : input.getAcl().toString())
                .withHeader(TosHeader.HEADER_GRANT_FULL_CONTROL, input.getGrantFullControl())
                .withHeader(TosHeader.HEADER_GRANT_READ, input.getGrantRead())
                .withHeader(TosHeader.HEADER_GRANT_READ_ACP, input.getGrantReadAcp())
                .withHeader(TosHeader.HEADER_GRANT_WRITE_ACP, input.getGrantWriteAcp());
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, null).setData(content);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> new PutObjectACLOutput().requestInfo(response.RequestInfo())
        );
    }

    @Override
    public GetObjectACLV2Output getObjectAcl(GetObjectACLV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "GetObjectACLV2Input");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("acl", "");
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildGetObjectACLV2Output);
    }

    private GetObjectACLV2Output buildGetObjectACLV2Output(TosResponse response) {
        return PayloadConverter.parsePayload(response.getInputStream(), new TypeReference<GetObjectACLV2Output>(){})
                .setRequestInfo(response.RequestInfo())
                .setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID));
    }

    @Override
    public CreateMultipartUploadOutput createMultipartUpload(CreateMultipartUploadInput input) throws TosException {
        ParamsChecker.isValidInput(input, "CreateMultipartUploadInput");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("uploads", "");
        addContentType(builder, input.getKey());
        TosRequest req = this.factory.build(builder, HttpMethod.POST, null);
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildCreateMultipartUploadOutput);
    }

    private CreateMultipartUploadOutput buildCreateMultipartUploadOutput(TosResponse response) {
        CreateMultipartUploadOutputJson upload = PayloadConverter.parsePayload(response.getInputStream(),
                new TypeReference<CreateMultipartUploadOutputJson>(){});
        return new CreateMultipartUploadOutput().setRequestInfo(response.RequestInfo())
                .setBucket(upload.getBucket()).setKey(upload.getKey()).setUploadID(upload.getUploadID())
                .setSseCustomerAlgorithm(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerMD5(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5))
                .setSseCustomerKey(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY));
    }

    private UploadPartV2Output buildUploadPartV2Output(TosResponse res, int partNumber) {
        return new UploadPartV2Output().setRequestInfo(res.RequestInfo())
                .setPartNumber(partNumber).setEtag(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG))
                .setSsecAlgorithm(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSsecKeyMD5(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5));
    }

    private UploadPartV2Output uploadPart(UploadPartBasicInput input, long contentLength, InputStream content) {
        ParamsChecker.isValidInput(input, "UploadPartBasicInput");
        ParamsChecker.isValidInput(input.getUploadID(), "uploadID");
        ParamsChecker.isValidPartNumber(input.getPartNumber());
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("uploadId", input.getUploadID())
                .withQuery("partNumber", String.valueOf(input.getPartNumber()))
                .withContentLength(contentLength);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, content);
        return objectHandler.doRequest(req, HttpStatus.OK, response -> buildUploadPartV2Output(response, input.getPartNumber()));
    }

    @Override
    public UploadPartV2Output uploadPart(UploadPartV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "UploadPartV2Input");
        return uploadPart(input.getUploadPartBasicInput(), input.getContentLength(), input.getContent());
    }

    @Override
    public CompleteMultipartUploadV2Output completeMultipartUpload(CompleteMultipartUploadV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "CompleteMultipartUploadV2Input");
        ParamsChecker.isValidInput(input.getUploadID(), "uploadID");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        TosMarshalResult data = PayloadConverter.serializePayload(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("uploadId", input.getUploadID());
        TosRequest req = this.factory.build(builder, HttpMethod.POST, null).setData(data.getData());
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildCompleteMultipartUploadOutput);
    }

    private CompleteMultipartUploadV2Output buildCompleteMultipartUploadOutput(TosResponse response) {
        CompleteMultipartUploadV2Output output = PayloadConverter.parsePayload(response.getInputStream(),
                new TypeReference<CompleteMultipartUploadV2Output>(){});
        return output.setRequestInfo(response.RequestInfo())
                .setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setHashCrc64ecma(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    @Override
    public AbortMultipartUploadOutput abortMultipartUpload(AbortMultipartUploadInput input) throws TosException {
        ParamsChecker.isValidInput(input, "AbortMultipartUploadInput");
        ParamsChecker.isValidInput(input.getUploadID(), "uploadID");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("uploadId", input.getUploadID());
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return objectHandler.doRequest(req, HttpStatus.NO_CONTENT,
                response -> new AbortMultipartUploadOutput().setRequestInfo(response.RequestInfo())
        );
    }

    @Override
    public ListPartsOutput listParts(ListPartsInput input) throws TosException {
        ParamsChecker.isValidInput(input, "ListPartsInput");
        ParamsChecker.isValidInput(input.getUploadID(), "uploadID");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        if (input.getMaxParts() < 0 || input.getPartNumberMarker() < 0) {
            throw new IllegalArgumentException("ListPartsInput maxParts or partNumberMarker is small than 0");
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("uploadId", input.getUploadID())
                .withQuery("max-parts", String.valueOf(input.getMaxParts()))
                .withQuery("part-number-marker", String.valueOf(input.getPartNumberMarker()));
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> PayloadConverter.parsePayload(response.getInputStream(),
                                new TypeReference<ListPartsOutput>(){}).setRequestInfo(response.RequestInfo())
        );
    }

    @Override
    public ListMultipartUploadsV2Output listMultipartUploads(ListMultipartUploadsV2Input input) throws TosException {
        ParamsChecker.isValidInput(input, "ListMultipartUploadsV2Input");
        ParamsChecker.isValidBucketName(input.getBucket());
        if (input.getMaxUploads() < 0) {
            throw new IllegalArgumentException("ListMultipartUploadsV2Input maxUploads is small than 0");
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("uploads", "")
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("key-marker", input.getKeyMarker())
                .withQuery("upload-id-marker", input.getUploadIDMarker())
                .withQuery("max-uploads", String.valueOf(input.getMaxUploads()));
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> PayloadConverter.parsePayload(response.getInputStream(),
                        new TypeReference<ListMultipartUploadsV2Output>(){}).setRequestInfo(response.RequestInfo())
        );
    }

    private void addContentType(RequestBuilder rb, String objectKey) throws TosClientException {
        String contentType = rb.getHeaders().get(TosHeader.HEADER_CONTENT_TYPE);
        if (StringUtils.isEmpty(contentType)){
            // request does not attach content-type and will auto recognize it default.
            // disable it by withAutoRecognizeContentType(false) in a request
            // or by clientAutoRecognizeContentType(false) while setting TosClientConfiguration
            if (this.clientAutoRecognizeContentType && rb.isAutoRecognizeContentType()){
                // set content type before upload
                contentType = MimeType.getInstance().getMimetype(objectKey);
                rb.withHeader(TosHeader.HEADER_CONTENT_TYPE, contentType);
            }
        }
    }
}

package com.volcengine.tos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.SignV4;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.MimeType;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.model.TosMarshalResult;
import com.volcengine.tos.model.acl.GetObjectAclOutput;
import com.volcengine.tos.model.acl.ObjectAclGrant;
import com.volcengine.tos.model.acl.PutObjectAclInput;
import com.volcengine.tos.model.acl.PutObjectAclOutput;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;
import com.volcengine.tos.session.Session;
import com.volcengine.tos.session.SessionTransport;
import com.volcengine.tos.transport.DefaultTransport;
import com.volcengine.tos.transport.Transport;
import com.volcengine.tos.transport.TransportConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Objects;

public class TOSClient implements TOS{
    /**
     * URL_MODE_DEFAULT url pattern is http(s)://{bucket}.domain/{object}
     */
    static final int URL_MODE_DEFAULT = 0;

    private static final String VERSION = "v0.2.2";
    static final String USER_AGENT = String.format("volc-tos-sdk-java/%s (%s/%s;%s)", VERSION, System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("java.version", "0"));

    private String scheme;
    private String host;
    private int urlMode;
    private String userAgent;
    private Credentials credentials;
    private Signer signer;
    private Transport transport;
    private Config config;

    private void schemeHost(String endpoint){
        if (endpoint.startsWith("https://")){
            this.scheme = "https";
            this.host = endpoint.substring("https://".length());
        } else if(endpoint.startsWith("http://")){
            this.scheme = "http";
            this.host = endpoint.substring("http://".length());
        } else {
            this.scheme = "http";
            this.host = endpoint;
        }
        this.urlMode = URL_MODE_DEFAULT;
    }

    public TOSClient(Session session) throws TosException {
        Objects.requireNonNull(session.getEndpoint(), "the endpoint is null");
        Objects.requireNonNull(session.getRegion(), "the region is null");
        Objects.requireNonNull(session.getCredentials(), "the credentials is null");

        // init TOSClient through session
        this.config = new Config().defaultConfig();
        this.config.setEndpoint(session.getEndpoint());
        this.schemeHost(session.getEndpoint());

        // if options have withTransportConfig or withReadWriteTimeout, it only works at the first init.
        Arrays.stream(session.getOptions()).forEach(option -> option.sessionOption(this));

        TransportConfig tc = SessionTransport.getConfigInstance();
        if (tc != null) {
            // transportConfig has been initialized.
            // use the same config.
            // and discard the config from sessionOption
            this.config.setTransportConfig(tc);
            // each TOSClient get the singleton HttpClient
            this.transport = SessionTransport.getInstance();
        } else{
            // first init
            this.transport = SessionTransport.getInstance(this.config.getTransportConfig());
        }

        Credentials cred = session.getCredentials();
        if (cred != null && this.signer == null) {
            this.signer = new SignV4(cred, session.getRegion());
        }

        if (userAgent == null || "".equals(userAgent)){
            userAgent = USER_AGENT;
        }
    }

    public TOSClient(String endpoint, ClientOptionsBuilder...options) throws TosException {
        this.config = new Config().defaultConfig();
        this.config.setEndpoint(endpoint);
        this.schemeHost(endpoint);

        Arrays.stream(options).forEach(option -> option.clientOption(this));

        if (this.transport == null) {
            this.transport = new DefaultTransport(this.config.getTransportConfig());
        }

        Credentials cred = this.credentials;
        if (cred != null && this.signer == null) {
            Objects.requireNonNull(this.config.getRegion(), "the region is null");
            this.signer = new SignV4(cred, this.config.getRegion());
        }

        if (userAgent == null || "".equals(userAgent)){
            userAgent = USER_AGENT;
        }
    }

    protected RequestBuilder newBuilder(String bucket, String object, RequestOptionsBuilder...builders){
        RequestBuilder rb = new RequestBuilder(this.signer, this.scheme, this.host,
                bucket, object, this.urlMode, new HashMap<>(), new HashMap<>());
        rb.withHeader(TosHeader.HEADER_USER_AGENT, this.userAgent);
        Arrays.stream(builders).forEach(builder -> builder.withOption(rb));
        return rb;
    }

    protected TosResponse roundTrip(TosRequest request, int expectedCode, int ...expectedCodes) throws TosException {
        TosResponse res;
        try{
            res = transport.roundTrip(request);
        } catch (IOException e){
            throw new TosClientException("request exception", e);
        }
        try {
            TosServerException.checkException(res, expectedCode, expectedCodes);
        } catch (IOException e){
            throw new TosClientException("check exception error", e);
        }
        return res;
    }

    @Override
    public CreateBucketOutput createBucket(CreateBucketInput input)  throws TosException{
        Objects.requireNonNull(input, "CreateBucketInput is null");
        Objects.requireNonNull(input.getBucket(), "bucket name is null");
        isValidBucketName(input.getBucket());

        TosRequest req = this.newBuilder(input.getBucket(), "")
                .withHeader(TosHeader.HEADER_ACL, input.getAcl())
                .withHeader(TosHeader.HEADER_GRANT_FULL_CONTROL, input.getGrantFullControl())
                .withHeader(TosHeader.HEADER_GRANT_READ, input.getGrantRead())
                .withHeader(TosHeader.HEADER_GRANT_READ_ACP, input.getGrantReadAcp())
                .withHeader(TosHeader.HEADER_GRANT_WRITE, input.getGrantWrite())
                .withHeader(TosHeader.HEADER_GRANT_WRITE_ACP, input.getGrantWriteAcp())
                .Build(HttpMethod.PUT, null);
        TosResponse res = this.roundTrip(req, HttpStatus.SC_OK);
        return new CreateBucketOutput(res.RequestInfo(), res.getHeaders().get(TosHeader.HEADER_LOCATION));
    }

    @Override
    public HeadBucketOutput headBucket(String bucket) throws TosException {
        isValidBucketName(bucket);
        TosRequest req = this.newBuilder(bucket, "").Build(HttpMethod.HEAD, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return new HeadBucketOutput(res.RequestInfo(), res.getHeaders().get(TosHeader.HEADER_BUCKET_REGION));
    }

    @Override
    public DeleteBucketOutput deleteBucket(String bucket) throws TosException {
        isValidBucketName(bucket);
        TosRequest req = this.newBuilder(bucket, "").Build(HttpMethod.DELETE, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_NO_CONTENT);
        return new DeleteBucketOutput(res.RequestInfo());
    }

    @Override
    public ListBucketsOutput listBuckets(ListBucketsInput input) throws TosException {
        TosRequest req = this.newBuilder("", "").Build(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListBucketsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    private int expectedCode(RequestBuilder rb) {
        Objects.requireNonNull(rb, "requestBuilder is null");
        return rb.getRange() != null? HttpStatus.SC_PARTIAL_CONTENT : HttpStatus.SC_OK;
    }

    @Override
    public GetObjectOutput getObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders);
        TosRequest req = rb.Build(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, expectedCode(rb));
        GetObjectOutput output = new GetObjectOutput().setRequestInfo(res.RequestInfo())
                .setContentRange(rb.getHeaders().get(TosHeader.HEADER_CONTENT_RANGE))
                .setContent(new TosObjectInputStream(res.getInputStream()));
        return output.setObjectMetaFromResponse(res);
    }

    @Override
    public HeadObjectOutput headObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders);
        TosRequest req = rb.Build(HttpMethod.HEAD, null);
        TosResponse res = roundTrip(req, expectedCode(rb));
        return new HeadObjectOutput().setRequestInfo(res.RequestInfo())
                .setContentRange(rb.getHeaders().get(TosHeader.HEADER_RANGE)).setObjectMeta(res);
    }

    @Override
    public DeleteObjectOutput deleteObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(objectKey);
        TosRequest req = newBuilder(bucket, objectKey, builders)
                .Build(HttpMethod.DELETE, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_NO_CONTENT);
        boolean deleteMarker = Boolean.parseBoolean(res.getHeaders().get(TosHeader.HEADER_DELETE_MARKER));
        return new DeleteObjectOutput().setRequestInfo(res.RequestInfo())
                .setDeleteMarker(deleteMarker)
                .setVersionID(res.getHeaders().get(TosHeader.HEADER_VERSIONID));
    }

    @Override
    public DeleteMultiObjectsOutput deleteMultiObjects(String bucket, DeleteMultiObjectsInput input, RequestOptionsBuilder... builders) throws TosException {
        TosMarshalResult inputRes = marshalInput(input);
        TosRequest req = newBuilder(bucket, "")
                .withHeader(TosHeader.HEADER_CONTENT_MD5, inputRes.getContentMD5())
                .withQuery("delete", "")
                .Build(HttpMethod.POST, null).setData((inputRes.getData()));
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return marshalOutput(res.getInputStream(), new TypeReference<DeleteMultiObjectsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    private void setContentType(RequestBuilder rb, String objectKey) throws TosClientException{
        String contentType = rb.getHeaders().get(TosHeader.HEADER_CONTENT_TYPE);
        if (StringUtils.isEmpty(contentType)){
            // request does not attach content-type
            // auto recognize default, check if user disable it by withAutoRecognizeContentType(false)
            if (rb.isAutoRecognizeContentType()){
                // set content type before upload
                contentType = MimeType.getInstance().getMimetype(objectKey);
                rb.getHeaders().put(TosHeader.HEADER_CONTENT_TYPE, contentType);
            }
        }
    }

    @Override
    public PutObjectOutput putObject(String bucket, String objectKey, InputStream inputStream, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders);
        setContentType(rb, objectKey);
        TosRequest req = rb.Build(HttpMethod.PUT, inputStream);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return new PutObjectOutput().setRequestInfo(res.RequestInfo())
                .setEtag(res.getHeaders().get(TosHeader.HEADER_ETAG))
                .setVersionID(res.getHeaders().get(TosHeader.HEADER_VERSIONID))
                .setSseCustomerAlgorithm(res.getHeaders().get(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerKeyMD5(res.getHeaders().get(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5));
    }

    @Override
    public AppendObjectOutput appendObject(String bucket, String objectKey, InputStream content, long offset, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders)
                .withQuery("append", "")
                .withQuery("offset", String.valueOf(offset));
        setContentType(rb, objectKey);
        TosRequest req = rb.Build(HttpMethod.POST, content);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        String nextOffset = res.getHeaders().get(TosHeader.HEADER_NEXT_APPEND_OFFSET);
        int appendOffset;
        try{
            appendOffset = Integer.parseInt(nextOffset);
        } catch (NumberFormatException nfe){
            throw new TosClientException("server return unexpected Next-Append-Offset header: "+nextOffset, nfe);
        }
        return new AppendObjectOutput()
                .setRequestInfo(res.RequestInfo())
                .setEtag(res.getHeaders().get(TosHeader.HEADER_ETAG))
                .setNextAppendOffset(appendOffset);
    }

    @Override
    public SetObjectMetaOutput setObjectMeta(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(objectKey);
        TosRequest req = newBuilder(bucket, objectKey, builders)
                .withQuery("metadata", "")
                .Build(HttpMethod.POST, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return new SetObjectMetaOutput().setRequestInfo(res.RequestInfo());
    }

    @Override
    public ListObjectsOutput listObjects(String bucket, ListObjectsInput input) throws TosException {
        TosRequest req = newBuilder(bucket, "")
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("marker", input.getMarker())
                .withQuery("max-keys", String.valueOf(input.getMaxKeys()))
                .withQuery("reverse", String.valueOf(input.isReverse()))
                .withQuery("encoding-type", input.getEncodingType())
                .Build(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListObjectsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    @Override
    public ListObjectVersionsOutput listObjectVersions(String bucket, ListObjectVersionsInput input) throws TosException {
        TosRequest req = newBuilder(bucket, "")
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("key-marker", input.getKeyMarker())
                .withQuery("max-keys", String.valueOf(input.getMaxKeys()))
                .withQuery("encoding-type", input.getEncodingType())
                .withQuery("versions", "")
                .Build(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListObjectVersionsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    @Override
    public CopyObjectOutput copyObject(String bucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(dstObjectKey, srcObjectKey);
        return copyObject(bucket, dstObjectKey, bucket, srcObjectKey, builders);
    }

    @Override
    public CopyObjectOutput copyObjectTo(String bucket, String dstBucket, String dstObjectKey, String srcObjectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidNames(dstBucket, dstObjectKey, srcObjectKey);
        return copyObject(dstBucket, dstObjectKey, bucket, srcObjectKey, builders);
    }

    @Override
    public CopyObjectOutput copyObjectFrom(String bucket, String srcBucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidNames(srcBucket, srcObjectKey, dstObjectKey);
        return copyObject(bucket, dstObjectKey, srcBucket, srcObjectKey, builders);
    }

    private static class InnerUploadPartCopyOutput{
        @JsonProperty("ETag")
        String etag;
        @JsonProperty("LastModified")
        String lastModified;

        private String getEtag() {
            return etag;
        }

        private String getLastModified() {
            return lastModified;
        }
    }
    @Override
    public UploadPartCopyOutput uploadPartCopy(String bucket, UploadPartCopyInput input, RequestOptionsBuilder... builders) throws TosException {
        isValidNames(input.getSourceBucket(), input.getDestinationKey());
        TosRequest req = newBuilder(bucket, input.getDestinationKey(), builders)
                .withQuery("partNumber", String.valueOf(input.getPartNumber()))
                .withQuery("uploadId", input.getUploadID())
                .withQuery("versionId", input.getSourceVersionID())
                .withHeader(TosHeader.HEADER_COPY_SOURCE_RANGE, copyRange(input.getStartOffset(), input.getPartSize()))
                .BuildWithCopySource(HttpMethod.PUT, input.getSourceBucket(), input.getSourceKey());
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        InnerUploadPartCopyOutput out = marshalOutput(res.getInputStream(), new TypeReference<InnerUploadPartCopyOutput>(){});
        return new UploadPartCopyOutput().setRequestInfo(res.RequestInfo())
                .setVersionID(res.getHeaders().get(TosHeader.HEADER_VERSIONID))
                .setSourceVersionID(res.getHeaders().get(TosHeader.HEADER_COPY_SOURCE_VERSION_ID))
                .setPartNumber(input.getPartNumber())
                .setEtag(out.getEtag())
                .setLastModified(out.getLastModified());
    }

    CopyObjectOutput copyObject(String dstBucket, String dstObject, String srcBucket, String srcObject, RequestOptionsBuilder...builders) throws TosException {
        TosRequest req = this.newBuilder(dstBucket, dstObject, builders)
                .BuildWithCopySource(HttpMethod.PUT, srcBucket, srcObject);
        TosResponse res = this.roundTrip(req, HttpStatus.SC_OK);
        return marshalOutput(res.getInputStream(), new TypeReference<CopyObjectOutput>(){})
                .setRequestInfo(res.RequestInfo())
                .setVersionID(res.getHeaders().get(TosHeader.HEADER_VERSIONID))
                .setSourceVersionID(res.getHeaders().get(TosHeader.HEADER_COPY_SOURCE_VERSION_ID));
    }

    @Override
    public PutObjectAclOutput putObjectAcl(String bucket, PutObjectAclInput input) throws TosException {
        isValidKey(input.getKey());
        InputStream content = null;
        try {
            if (input.getAclRules() != null) {
                byte[] data = new ObjectMapper().writeValueAsBytes(input.getAclRules());
                content = new ByteArrayInputStream(data);
            }
        } catch (JsonProcessingException jpe) {
            throw new TosClientException("json parse exception", jpe);
        }
        RequestBuilder builder = newBuilder(bucket, input.getKey())
                .withQuery("acl", "");
        ObjectAclGrant grant = input.getAclGrant();
        if (grant != null) {
            builder = builder.withHeader(TosHeader.HEADER_ACL, grant.getAcl())
                    .withHeader(TosHeader.HEADER_GRANT_FULL_CONTROL, grant.getGrantFullControl())
                    .withHeader(TosHeader.HEADER_GRANT_READ, grant.getGrantRead())
                    .withHeader(TosHeader.HEADER_GRANT_READ_ACP, grant.getGrantReadAcp())
                    .withHeader(TosHeader.HEADER_GRANT_WRITE, grant.getGrantWrite())
                    .withHeader(TosHeader.HEADER_GRANT_WRITE_ACP, grant.getGrantWriteAcp());
        }
        TosRequest req = builder.Build(HttpMethod.PUT, content);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return new PutObjectAclOutput().setRequestInfo(res.RequestInfo());
    }

    @Override
    public GetObjectAclOutput getObjectAcl(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(objectKey);
        TosRequest req = newBuilder(bucket, objectKey)
                .withQuery("acl", "")
                .Build(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return marshalOutput(res.getInputStream(), new TypeReference<GetObjectAclOutput>(){})
                .setRequestInfo(res.RequestInfo()).setVersionId(res.getHeaders().get(TosHeader.HEADER_VERSIONID));
    }

    @Override
    public CreateMultipartUploadOutput createMultipartUpload(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders)
                .withQuery("uploads", "");
        setContentType(rb, objectKey);
        TosRequest req = rb.Build(HttpMethod.POST, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        multipartUpload upload = marshalOutput(res.getInputStream(),
                new TypeReference<multipartUpload>(){});
        return new CreateMultipartUploadOutput().setRequestInfo(res.RequestInfo())
                .setBucket(upload.getBucket())
                .setKey(upload.getKey())
                .setUploadID(upload.getUploadID())
                .setSseCustomerAlgorithm(res.getHeaders().get(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerMD5(res.getHeaders().get(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5));
    }

    InputStream limitPartSizeReader(InputStream input, int limitedPartSize) throws TosClientException{
        byte[] content = new byte[limitedPartSize];
        InputStream limitedStream;
        try{
            int realPartSize = input.read(content);
            if (realPartSize != limitedPartSize) {
                throw new TosClientException("read limit input exception, need read " + limitedPartSize
                        + " bytes, but read " + realPartSize + " bytes in fact.", null);
            }
            limitedStream = new ByteArrayInputStream(content);
        } catch (IOException e){
            throw new TosClientException("read inputStream exception", e);
        }
        return limitedStream;
    }

    private int resolveStreamSize(String method, Long size) throws TosClientException {
        int intSize = size.intValue();
        if (intSize != size) {
            throw new TosClientException(method + " has overhead size" + size, null);
        }
        return intSize;
    }

    @Override
    public UploadPartOutput uploadPart(String bucket, UploadPartInput input, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(input.getKey());
        int limitedPartSize = resolveStreamSize("UploadPart", input.getPartSize());
        InputStream content = limitPartSizeReader(input.getContent(), limitedPartSize);
        TosRequest req = newBuilder(bucket, input.getKey(), builders)
                .withQuery("uploadId", input.getUploadID())
                .withQuery("partNumber", String.valueOf(input.getPartNumber()))
                .Build(HttpMethod.PUT, content);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return new UploadPartOutput().setRequestInfo(res.RequestInfo())
                .setPartNumber(input.getPartNumber())
                .setEtag(res.getHeaders().get(TosHeader.HEADER_ETAG))
                .setSseCustomerAlgorithm(res.getHeaders().get(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerMD5(res.getHeaders().get(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5));
    }

    @Override
    public CompleteMultipartUploadOutput completeMultipartUpload(String bucket, CompleteMultipartUploadInput input) throws TosException {
        int partsNum = input.getUploadedPartsLength();
        CompleteMultipartUploadInput.InnerCompleteMultipartUploadInput multipart = new
                CompleteMultipartUploadInput.InnerCompleteMultipartUploadInput(partsNum);
        for (int i = 0; i < partsNum; i++) {
            multipart.setPartsByIdx(input.getUploadedParts()[i].uploadedPart(), i);
        }
        Arrays.sort(multipart.getParts());
        byte[] data;
        try{
            data = new ObjectMapper().writeValueAsBytes(multipart);
        } catch (JsonProcessingException jpe) {
            throw new TosClientException("json parse exception", jpe);
        }
        TosRequest req = newBuilder(bucket, input.getKey())
                .withQuery("uploadId", input.getUploadID())
                .Build(HttpMethod.POST, null).setData(data);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return new CompleteMultipartUploadOutput().setRequestInfo(res.RequestInfo())
                .setVersionID(res.getHeaders().get(TosHeader.HEADER_VERSIONID));
    }

    @Override
    public AbortMultipartUploadOutput abortMultipartUpload(String bucket, AbortMultipartUploadInput input) throws TosException {
        isValidKey(input.getKey());
        TosRequest req = newBuilder(bucket, input.getKey())
                .withQuery("uploadId", input.getUploadID())
                .Build(HttpMethod.DELETE, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_NO_CONTENT);
        return new AbortMultipartUploadOutput().setRequestInfo(res.RequestInfo());
    }

    @Override
    public ListUploadedPartsOutput listUploadedParts(String bucket, ListUploadedPartsInput input, RequestOptionsBuilder... builders) throws TosException {
        isValidKey(input.getKey());
        TosRequest req = newBuilder(bucket, input.getKey(), builders)
                .withQuery("uploadId", input.getUploadID())
                .Build(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListUploadedPartsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    @Override
    public ListMultipartUploadsOutput listMultipartUploads(String bucket, ListMultipartUploadsInput input) throws TosException {
        TosRequest req = newBuilder(bucket, "")
                .withQuery("uploads", "")
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("key-marker", input.getKeyMarker())
                .withQuery("upload-id-marker", input.getUploadIDMarker())
                .withQuery("max-uploads", String.valueOf(input.getMaxUploads()))
                .Build(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.SC_OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListMultipartUploadsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    @Override
    public String preSignedURL(String httpMethod, String bucket, String objectKey, Duration ttl, RequestOptionsBuilder...builders) throws TosException{
        return newBuilder(bucket, objectKey, builders).preSignedURL(httpMethod, ttl);
    }

    private TosMarshalResult marshalInput(Object input) throws TosException{
        byte[] data;
        String dataMD5;
        try{
            data = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(input);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(data);
            dataMD5 = Base64.getEncoder().encodeToString(bytes);
        } catch (IOException | NoSuchAlgorithmException e){
            throw new TosClientException("Marshal Input Exception", e);
        }
        return new TosMarshalResult(dataMD5, data);
    }
    
    private <T> T marshalOutput(InputStream reader,TypeReference<T> valueTypeRef) throws TosException{
        try{
            return new ObjectMapper().readValue(reader, valueTypeRef);
        } catch (IOException e){
            throw new TosClientException("Marshal Output Exception", e);
        }
    }

    static String copyRange(long startOffset, long partSize) {
        String cr = "";
        if (startOffset != 0) {
            if (partSize != 0) {
                cr = String.format("bytes=%d-%d", startOffset, startOffset+partSize-1);
            } else {
                cr = String.format("bytes=%d-", startOffset);
            }
        } else if (partSize != 0){
            cr = String.format("bytes=0-%d", partSize-1);
        }
        return cr;
    }

    static void isValidBucketName(String name) {
        if (StringUtils.isEmpty(name) || name.length() < 3 || name.length() > 63) {
            throw new IllegalArgumentException("tos: bucket name length must between [3, 64)");
        }
        char[] cn = name.toCharArray();
        for (char c : cn) {
            if (!(('a' <= c && c <= 'z') || ('0' <= c && c <= '9') || c == '-')){
                throw new IllegalArgumentException("tos: bucket name can consist only of lowercase letters, numbers, and '-' ");
            }
        }
        if (cn[0] == '-' || cn[name.length()-1] == '-'){
            throw new IllegalArgumentException("tos: bucket name must begin and end with a letter or number");
        }
    }

    static void isValidNames(String bucket, String key, String ...keys){
        isValidBucketName(bucket);
        isValidKey(key, keys);
    }

    static void isValidKey(String key, String ...keys){
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("tos: object name is empty");
        }
        for (String k: keys) {
            if (k == null || k.length() == 0) {
                throw new IllegalArgumentException("tos: object name is empty");
            }
        }
    }

    public String getScheme() {
        return scheme;
    }

    public TOSClient setScheme(String scheme) {
        this.scheme = scheme;
        return this;
    }

    public String getHost() {
        return host;
    }

    public TOSClient setHost(String host) {
        this.host = host;
        return this;
    }

    public int getUrlMode() {
        return urlMode;
    }

    public TOSClient setUrlMode(int urlMode) {
        this.urlMode = urlMode;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public TOSClient setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public Credentials getCredentials() {
        return credentials;
    }

    public TOSClient setCredentials(Credentials credentials) {
        this.credentials = credentials;
        return this;
    }

    public Signer getSigner() {
        return signer;
    }

    public TOSClient setSigner(Signer signer) {
        this.signer = signer;
        return this;
    }

    public Transport getTransport() {
        return transport;
    }

    public TOSClient setTransport(Transport transport) {
        this.transport = transport;
        return this;
    }

    public Config getConfig() {
        return config;
    }

    public TOSClient setConfig(Config config) {
        this.config = config;
        return this;
    }

    @Override
    public String toString() {
        return "TOSClient{" +
                "scheme='" + scheme + '\'' +
                ", host='" + host + '\'' +
                ", urlMode=" + urlMode +
                ", userAgent='" + userAgent + '\'' +
                ", credentials=" + credentials +
                ", signer=" + signer +
                ", transport=" + transport +
                ", config=" + config +
                '}';
    }
}

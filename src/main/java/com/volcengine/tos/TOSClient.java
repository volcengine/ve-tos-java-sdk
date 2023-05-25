package com.volcengine.tos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.SignV4;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.comm.*;
import com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream;
import com.volcengine.tos.internal.*;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.PayloadConverter;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.acl.GetObjectAclOutput;
import com.volcengine.tos.model.acl.ObjectAclGrant;
import com.volcengine.tos.model.acl.PutObjectAclInput;
import com.volcengine.tos.model.acl.PutObjectAclOutput;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;
import com.volcengine.tos.session.Session;
import com.volcengine.tos.session.SessionOptionsBuilder;
import com.volcengine.tos.session.SessionTransport;
import com.volcengine.tos.transport.DefaultTransport;
import com.volcengine.tos.transport.TransportConfig;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Deprecated
public class TOSClient implements TOS{
    /**
     * URL_MODE_DEFAULT url pattern is http(s)://{bucket}.domain/{object}
     */
    static final int URL_MODE_DEFAULT = 0;

    private static final String VERSION = "v2.6.0";
    private static final String SDK_NAME = "ve-tos-java-sdk";
    private static final String USER_AGENT = String.format("%s/%s (%s/%s;%s)", SDK_NAME, VERSION,
            System.getProperty("os.name"), System.getProperty("os.arch"), System.getProperty("java.version", "0"));

    private String scheme;
    private String host;
    private int urlMode;
    private String userAgent;
    private Credentials credentials;
    private Signer signer;
    private Transport transport;
    private Config config;

    List<String> getSchemeAndHost(String endpoint) {
        return ParamsChecker.parseFromEndpoint(endpoint);
    }

    private void schemeHost(String endpoint){
        List<String> schemeHost = getSchemeAndHost(endpoint);
        this.scheme = schemeHost.get(0);
        this.host = schemeHost.get(1);
        this.urlMode = URL_MODE_DEFAULT;
    }

    public TOSClient(Session session){
        Objects.requireNonNull(session.getEndpoint(), "the endpoint is null");
        Objects.requireNonNull(session.getRegion(), "the region is null");
        Objects.requireNonNull(session.getCredentials(), "the credentials is null");

        // init TOSClient through session
        this.config = new Config().defaultConfig();
        this.config.setEndpoint(session.getEndpoint());
        this.schemeHost(session.getEndpoint());

        // if options have withTransportConfig or withReadWriteTimeout, it only works at the first init.
        for (SessionOptionsBuilder option : session.getOptions()) {
            option.sessionOption(this);
        }

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

    public TOSClient(String endpoint, ClientOptionsBuilder...options) {
        this.config = new Config().defaultConfig();
        this.config.setEndpoint(endpoint);

        for (ClientOptionsBuilder option : options) {
            option.clientOption(this);
        }
        this.schemeHost(endpoint);

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
        for (RequestOptionsBuilder builder : builders) {
            builder.withOption(rb);
        }
        return rb;
    }

    TosResponse roundTrip(TosRequest request, int expectedCode) throws TosException {
        TosResponse res;
        try{
            res = transport.roundTrip(request);
        } catch (IOException e){
            throw new TosClientException("request exception", e);
        }
        if (res.getStatusCode() == expectedCode) {
            return res;
        }
        if (res.getStatusCode() >= HttpStatus.BAD_REQUEST) {
            String s = StringUtils.toString(res.getInputStream(), "response body");
            if (s.length() > 0) {
                try{
                    ServerExceptionJson se = TosUtils.getJsonMapper().readValue(s, new TypeReference<ServerExceptionJson>(){});
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
        throw new UnexpectedStatusCodeException(res.getStatusCode(), expectedCode, res.getRequesID());
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
                .withHeader(TosHeader.HEADER_STORAGE_CLASS, input.getStorageClass())
                .buildRequest(HttpMethod.PUT, null);
        TosResponse res = this.roundTrip(req, HttpStatus.OK);
        return new CreateBucketOutput(res.RequestInfo(), res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_LOCATION));
    }

    @Override
    public HeadBucketOutput headBucket(String bucket) throws TosException {
        isValidBucketName(bucket);
        TosRequest req = this.newBuilder(bucket, "").buildRequest(HttpMethod.HEAD, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return new HeadBucketOutput(res.RequestInfo(),
                res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_BUCKET_REGION),
                res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_STORAGE_CLASS));
    }

    @Override
    public DeleteBucketOutput deleteBucket(String bucket) throws TosException {
        isValidBucketName(bucket);
        TosRequest req = this.newBuilder(bucket, "").buildRequest(HttpMethod.DELETE, null);
        TosResponse res = roundTrip(req, HttpStatus.NO_CONTENT);
        return new DeleteBucketOutput(res.RequestInfo());
    }

    @Override
    public ListBucketsOutput listBuckets(ListBucketsInput input) throws TosException {
        TosRequest req = this.newBuilder("", "").buildRequest(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListBucketsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    @Override
    public PutBucketPolicyOutput putBucketPolicy(String bucket, String policy) throws TosException {
        isValidBucketName(bucket);
        TosRequest req = this.newBuilder(bucket, "").withQuery("policy", "").
                buildRequest(HttpMethod.PUT, new ByteArrayInputStream(policy.getBytes(StandardCharsets.UTF_8)));
        TosResponse res = roundTrip(req, HttpStatus.NO_CONTENT);
        return new PutBucketPolicyOutput().setRequestInfo(res.RequestInfo());
    }

    @Override
    public GetBucketPolicyOutput getBucketPolicy(String bucket) throws TosException {
        isValidBucketName(bucket);
        TosRequest req = this.newBuilder(bucket, "").withQuery("policy", "").
                buildRequest(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return new GetBucketPolicyOutput().setRequestInfo(res.RequestInfo())
                .setPolicy(StringUtils.toString(res.getInputStream(), "bucket policy"));
    }

    @Override
    public DeleteBucketPolicyOutput deleteBucketPolicy(String bucket) throws TosException {
        isValidBucketName(bucket);
        TosRequest req = this.newBuilder(bucket, "").withQuery("policy", "").buildRequest(HttpMethod.DELETE, null);
        TosResponse res = roundTrip(req, HttpStatus.NO_CONTENT);
        return new DeleteBucketPolicyOutput().setRequestInfo(res.RequestInfo());
    }

    private int expectedCode(RequestBuilder rb) {
        Objects.requireNonNull(rb, "requestBuilder is null");
        return rb.getHeaders().get(TosHeader.HEADER_RANGE) != null? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;
    }

    @Override
    public GetObjectOutput getObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders);
        TosRequest req = rb.buildRequest(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, expectedCode(rb));
        GetObjectOutput output = new GetObjectOutput().setRequestInfo(res.RequestInfo())
                .setContentRange(rb.getHeaders().get(TosHeader.HEADER_CONTENT_RANGE))
                .setContent(new TosObjectInputStream(res.getInputStream()));
        return output.setObjectMetaFromResponse(res);
    }

    @Override
    public HeadObjectOutput headObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders);
        TosRequest req = rb.buildRequest(HttpMethod.HEAD, null);
        TosResponse res = roundTrip(req, expectedCode(rb));
        return new HeadObjectOutput().setRequestInfo(res.RequestInfo())
                .setContentRange(rb.getHeaders().get(TosHeader.HEADER_CONTENT_RANGE)).setObjectMeta(res);
    }

    @Override
    public DeleteObjectOutput deleteObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidKey(objectKey);
        TosRequest req = newBuilder(bucket, objectKey, builders)
                .buildRequest(HttpMethod.DELETE, null);
        TosResponse res = roundTrip(req, HttpStatus.NO_CONTENT);
        boolean deleteMarker = Boolean.parseBoolean(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_DELETE_MARKER));
        return new DeleteObjectOutput().setRequestInfo(res.RequestInfo())
                .setDeleteMarker(deleteMarker)
                .setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID));
    }

    @Override
    public DeleteMultiObjectsOutput deleteMultiObjects(String bucket, DeleteMultiObjectsInput input, RequestOptionsBuilder... builders) throws TosException {
        Objects.requireNonNull(input, "DeleteMultiObjectsInput is null");
        isValidBucketName(bucket);
        TosMarshalResult inputRes = PayloadConverter.serializePayloadAndComputeMD5(input);
        TosRequest req = newBuilder(bucket, "", builders)
                .withHeader(TosHeader.HEADER_CONTENT_MD5, inputRes.getContentMD5())
                .withQuery("delete", "")
                .buildRequest(HttpMethod.POST, null).setData(inputRes.getData());
        TosResponse res = roundTrip(req, HttpStatus.OK);
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
        isValidBucketName(bucket);
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders);
        setContentType(rb, objectKey);
        TosRequest req = rb.buildRequest(HttpMethod.PUT, inputStream);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return new PutObjectOutput().setRequestInfo(res.RequestInfo())
                .setEtag(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG))
                .setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setCrc64(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64))
                .setSseCustomerAlgorithm(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerKeyMD5(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5))
                .setSseCustomerKey(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY));
    }

    @Override
    public UploadFileOutput uploadFile(String bucket, UploadFileInput input, RequestOptionsBuilder... builders) throws TosException {
        validateInput(bucket, input);
        if (input.isEnableCheckpoint()) {
            validateCheckpointPath(bucket, input);
        }
        UploadFileInfo fileInfo = getUploadFileInfo(input.getUploadFilePath());
        return uploadPartConcurrent(input, getCheckpoint(bucket, input, fileInfo, builders), builders);
    }

    private void validateInput(String bucket, UploadFileInput input) {
        Objects.requireNonNull(input, "UploadFileInput is null");
        Objects.requireNonNull(input.getUploadFilePath(), "UploadFilePath is null");

        isValidBucketName(bucket);
        isValidKey(input.getObjectKey());

        if (input.getPartSize() < Consts.MIN_PART_SIZE || input.getPartSize() > Consts.MAX_PART_SIZE) {
            throw new IllegalArgumentException("The input part size is invalid, please set it range from 5MB to 5GB");
        }
        if (input.getTaskNum() > 1000) {
            input.setTaskNum(1000);
        }
        if (input.getTaskNum() < 1) {
            input.setTaskNum(1);
        }
        File file = new File(input.getUploadFilePath());
        if (!file.exists()) {
            throw new IllegalArgumentException("The file to upload is not found in the specific path: " + input.getUploadFilePath());
        }
        if (file.isDirectory()) {
            // 不支持文件夹上传
            throw new IllegalArgumentException("Does not support directory, please specific your file path");
        }
    }

    private void validateCheckpointPath(String bucket, UploadFileInput input) {
        String checkpointFileSuffix = bucket + "." + input.getObjectKey().replace("/", "_") + ".upload";
        if (StringUtils.isEmpty(input.getCheckpointFile())) {
            input.setCheckpointFile(input.getUploadFilePath() + "." + checkpointFileSuffix);
        } else {
            File ufcf = new File(input.getCheckpointFile());
            if (!ufcf.exists()) {
                throw new IllegalArgumentException("The checkpoint file is not found in the specific path: " + input.getUploadFilePath());
            }
            if (ufcf.isDirectory()) {
                input.setCheckpointFile(input.getCheckpointFile() + "/" + checkpointFileSuffix);
            }
        }
    }

    private UploadFileInfo getUploadFileInfo(String uploadFilePath){
        File file = new File(uploadFilePath);
        return new UploadFileInfo().setFilePath(uploadFilePath).setFileSize(file.length()).setLastModified(file.lastModified());
    }

    private UploadFileCheckpoint getCheckpoint(String bucket, UploadFileInput input, UploadFileInfo fileInfo,
                                               RequestOptionsBuilder... builders) throws TosException{
        UploadFileCheckpoint checkpoint = null;
        if (input.isEnableCheckpoint()) {
            try{
                checkpoint = loadCheckpointFromFile(input.getCheckpointFile());
            } catch (IOException | ClassNotFoundException e){
                deleteCheckpointFile(input.getCheckpointFile());
            }
        }
        boolean valid = false;
        if (checkpoint != null) {
            valid = checkpoint.isValid(fileInfo.getFileSize(), fileInfo.getLastModified(),
                    bucket, input.getObjectKey(), input.getUploadFilePath());
            if (!valid) {
                deleteCheckpointFile(input.getCheckpointFile());
            }
        }
        if (checkpoint == null || !valid) {
            checkpoint = initCheckpoint(bucket, input, fileInfo, builders);
            if (input.isEnableCheckpoint()) {
                try{
                    checkpoint.writeToFile(input.getCheckpointFile(), TosUtils.getJsonMapper());
                } catch (IOException e) {
                    throw new TosClientException("record to checkpoint file failed", e);
                }
            }
        }
        return checkpoint;
    }

    private boolean deleteCheckpointFile(String checkpointFilePath) {
        File file = new File(checkpointFilePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    private UploadFileCheckpoint loadCheckpointFromFile(String checkpointFilePath) throws IOException, ClassNotFoundException{
        Objects.requireNonNull(checkpointFilePath, "checkpointFilePath is null");
        File f = new File(checkpointFilePath);
        try(FileInputStream checkpointFile = new FileInputStream(f))
        {
            byte[] data = new byte[(int)f.length()];
            checkpointFile.read(data);
            return TosUtils.getJsonMapper().readValue(data, new TypeReference<UploadFileCheckpoint>(){});
        }
    }

    private UploadFileCheckpoint initCheckpoint(String bucket, UploadFileInput input, UploadFileInfo info, RequestOptionsBuilder... builders) throws TosException{
        UploadFileCheckpoint checkpoint = new UploadFileCheckpoint()
                .setBucket(bucket).setKey(input.getObjectKey()).setFileInfo(info)
                .setPartInfoList(getPartsFromFile(info.getFileSize(), input.getPartSize()));
        CreateMultipartUploadOutput output = this.createMultipartUpload(bucket, input.getObjectKey(), builders);
        checkpoint.setUploadID(output.getUploadID());
        return checkpoint;
    }

    private List<UploadFilePartInfo> getPartsFromFile(long uploadFileSize, long partSize) {
        long partNum = uploadFileSize / partSize;
        long lastPartSize = uploadFileSize % partSize;
        if (lastPartSize != 0) {
            partNum++;
        }
        if (partNum > 10000) {
            throw new IllegalArgumentException("The split file parts number is larger than 10000, please increase your part size");
        }
        List<UploadFilePartInfo> partInfoList = new ArrayList<>((int) partNum);
        for(int i = 0; i < partNum; i++) {
            if (i < partNum-1) {
                partInfoList.add(new UploadFilePartInfo().setPartSize(partSize).setPartNum(i+1).setOffset(i * partSize));
            } else {
                partInfoList.add(new UploadFilePartInfo().setPartSize(lastPartSize).setPartNum(i+1).setOffset(i * partSize));
            }
        }
        return partInfoList;
    }

    private UploadFileOutput uploadPartConcurrent(UploadFileInput input, UploadFileCheckpoint checkpoint, RequestOptionsBuilder... builders) throws TosException {
        ExecutorService executor = Executors.newFixedThreadPool(input.getTaskNum());
        List<Future<MultipartUploadedPart>> futures = new ArrayList<>(checkpoint.getPartInfoList().size());
        List<MultipartUploadedPart> uploadPartOutputs = new ArrayList<>(checkpoint.getPartInfoList().size());
        TosUtils.getLogger().debug("Upload file split to {} parts.", checkpoint.getPartInfoList().size());
        for (int i = 0; i < checkpoint.getPartInfoList().size(); i++) {
            UploadFilePartInfo partInfo = checkpoint.getPartInfoList().get(i);
            if (!partInfo.isCompleted()) {
                final int finalI = i;
                Future<MultipartUploadedPart> future = executor.submit(() -> {
                    long start = System.nanoTime();
                    InputStream in = getContentFromFile(checkpoint.getFileInfo().getFilePath(),
                            partInfo.getOffset(), partInfo.getPartSize());
                    // 区分错误进行 catch
                    // 4xx
                    // 超时处理
                    UploadPartOutput output = this.uploadPart(checkpoint.getBucket(),
                            new UploadPartInput().setKey(checkpoint.getKey())
                                    .setUploadID(checkpoint.getUploadID())
                                    .setPartNumber(partInfo.getPartNum())
                                    .setPartSize(partInfo.getPartSize())
                                    .setContent(in), builders);
                    partInfo.setPart(output);
                    partInfo.setCompleted(true);
                    if (input.isEnableCheckpoint()) {
                        checkpoint.writeToFile(input.getCheckpointFile(), TosUtils.getJsonMapper());
                    }
                    long end = System.nanoTime();
                    TosUtils.getLogger().debug("Upload No.{} part cost {} milliseconds, part size is {}", (finalI +1), (end-start) / 1000000, partInfo.getPartSize());
                    return output;
                });
                futures.add(future);
            } else {
                uploadPartOutputs.add(checkpoint.getPartInfoList().get(i).getPart());
            }
        }

        for (Future<MultipartUploadedPart> future : futures) {
            try {
                uploadPartOutputs.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                if (!input.isEnableCheckpoint()) {
                    this.abortMultipartUpload(checkpoint.getBucket(), new AbortMultipartUploadInput(checkpoint.getKey(), checkpoint.getUploadID()));
                }
                throw new TosClientException("Thread executor failed", e);
            }
        }

        executor.shutdown();
        try{
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            if (!input.isEnableCheckpoint()) {
                this.abortMultipartUpload(checkpoint.getBucket(), new AbortMultipartUploadInput(checkpoint.getKey(), checkpoint.getUploadID()));
            }
            throw new TosClientException("await upload executor terminated failed", e);
        }

        CompleteMultipartUploadOutput output = this.completeMultipartUpload(checkpoint.getBucket(), new CompleteMultipartUploadInput()
                .setUploadID(checkpoint.getUploadID()).setKey(checkpoint.getKey()).setMultiUploadedParts(uploadPartOutputs));
        if (input.isEnableCheckpoint()) {
            deleteCheckpointFile(input.getCheckpointFile());
        }
        return new UploadFileOutput().setUploadID(checkpoint.getUploadID()).setBucket(checkpoint.getBucket())
                .setObjectKey(checkpoint.getKey()).setCompleteMultipartUploadOutput(output);
    }

    private InputStream getContentFromFile(String filePath, long offset, long size) throws IOException, TosClientException {
        FileInputStream in = new FileInputStream(filePath);
        if (in.skip(offset) != offset) {
            throw new IllegalArgumentException("The offset is invalid");
        }
        return new TosRepeatableBoundedFileInputStream(in, size);
    }

    @Override
    public AppendObjectOutput appendObject(String bucket, String objectKey, InputStream content, long offset, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders)
                .withQuery("append", "")
                .withQuery("offset", String.valueOf(offset));
        setContentType(rb, objectKey);
        TosRequest req = rb.buildRequest(HttpMethod.POST, content);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        String nextOffset = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_NEXT_APPEND_OFFSET);
        int appendOffset;
        try{
            appendOffset = Integer.parseInt(nextOffset);
        } catch (NumberFormatException nfe){
            throw new TosClientException("server return unexpected Next-Append-Offset header: "+nextOffset, nfe);
        }
        return new AppendObjectOutput()
                .setRequestInfo(res.RequestInfo())
                .setEtag(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG))
                .setNextAppendOffset(appendOffset)
                .setCrc64(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    @Override
    public SetObjectMetaOutput setObjectMeta(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidKey(objectKey);
        TosRequest req = newBuilder(bucket, objectKey, builders)
                .withQuery("metadata", "")
                .buildRequest(HttpMethod.POST, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return new SetObjectMetaOutput().setRequestInfo(res.RequestInfo());
    }

    @Override
    public ListObjectsOutput listObjects(String bucket, ListObjectsInput input) throws TosException {
        Objects.requireNonNull(input, "ListObjectsInput is null");
        isValidBucketName(bucket);
        TosRequest req = newBuilder(bucket, "")
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("marker", input.getMarker())
                .withQuery("max-keys", String.valueOf(input.getMaxKeys()))
                .withQuery("reverse", String.valueOf(input.isReverse()))
                .withQuery("encoding-type", input.getEncodingType())
                .buildRequest(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListObjectsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    @Override
    public ListObjectVersionsOutput listObjectVersions(String bucket, ListObjectVersionsInput input) throws TosException {
        Objects.requireNonNull(input, "ListObjectVersionsInput is null");
        isValidBucketName(bucket);
        TosRequest req = newBuilder(bucket, "")
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("key-marker", input.getKeyMarker())
                .withQuery("max-keys", input.getMaxKeys() == 0 ? null : String.valueOf(input.getMaxKeys()))
                .withQuery("encoding-type", input.getEncodingType())
                .withQuery("versions", "")
                .buildRequest(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListObjectVersionsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    @Override
    public CopyObjectOutput copyObject(String bucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidKeySet(dstObjectKey, srcObjectKey);
        return copyObject(bucket, dstObjectKey, bucket, srcObjectKey, builders);
    }

    @Override
    public CopyObjectOutput copyObjectTo(String bucket, String dstBucket, String dstObjectKey, String srcObjectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidNames(dstBucket, dstObjectKey, srcObjectKey);
        return copyObject(dstBucket, dstObjectKey, bucket, srcObjectKey, builders);
    }

    @Override
    public CopyObjectOutput copyObjectFrom(String bucket, String srcBucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidNames(srcBucket, srcObjectKey, dstObjectKey);
        return copyObject(bucket, dstObjectKey, srcBucket, srcObjectKey, builders);
    }

    @Deprecated
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
        Objects.requireNonNull(input, "UploadPartCopyInput is null");
        isValidBucketName(bucket);
        isValidNames(input.getSourceBucket(), input.getDestinationKey());
        TosRequest req = newBuilder(bucket, input.getDestinationKey(), builders)
                .withQuery("partNumber", String.valueOf(input.getPartNumber()))
                .withQuery("uploadId", input.getUploadID())
                .withQuery("versionId", input.getSourceVersionID())
                .withHeader(TosHeader.HEADER_COPY_SOURCE_RANGE, copyRange(input.getStartOffset(), input.getPartSize()))
                .buildRequestWithCopySource(HttpMethod.PUT, input.getSourceBucket(), input.getSourceKey());
        TosResponse res = roundTrip(req, HttpStatus.OK);
        InnerUploadPartCopyOutput out = marshalOutput(res.getInputStream(), new TypeReference<InnerUploadPartCopyOutput>(){});
        return new UploadPartCopyOutput().setRequestInfo(res.RequestInfo())
                .setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setSourceVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_COPY_SOURCE_VERSION_ID))
                .setPartNumber(input.getPartNumber())
                .setEtag(out.getEtag())
                .setLastModified(out.getLastModified())
                .setCrc64(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    private CopyObjectOutput copyObject(String dstBucket, String dstObject, String srcBucket, String srcObject, RequestOptionsBuilder...builders) throws TosException {
        TosRequest req = this.newBuilder(dstBucket, dstObject, builders)
                .buildRequestWithCopySource(HttpMethod.PUT, srcBucket, srcObject);
        TosResponse res = this.roundTrip(req, HttpStatus.OK);
        return marshalOutput(res.getInputStream(), new TypeReference<CopyObjectOutput>(){})
                .setRequestInfo(res.RequestInfo())
                .setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setSourceVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_COPY_SOURCE_VERSION_ID))
                .setCrc64(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    @Override
    public PutObjectAclOutput putObjectAcl(String bucket, PutObjectAclInput input) throws TosException {
        Objects.requireNonNull(input, "PutObjectAclInput is null");
        isValidBucketName(bucket);
        isValidKey(input.getKey());
        byte[] content = null;
        try {
            if (input.getAclRules() != null) {
                content = TosUtils.getJsonMapper().writeValueAsBytes(input.getAclRules());
            } else {
                // 防止NPE
                content = new byte[0];
            }
        } catch (JsonProcessingException jpe) {
            throw new TosClientException("tos: json parse exception", jpe);
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
        TosRequest req = builder.buildRequest(HttpMethod.PUT, null).setData(content);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return new PutObjectAclOutput().setRequestInfo(res.RequestInfo());
    }

    @Override
    public GetObjectAclOutput getObjectAcl(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidKey(objectKey);
        TosRequest req = newBuilder(bucket, objectKey)
                .withQuery("acl", "")
                .buildRequest(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return marshalOutput(res.getInputStream(), new TypeReference<GetObjectAclOutput>(){})
                .setRequestInfo(res.RequestInfo()).setVersionId(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID));
    }

    @Override
    public CreateMultipartUploadOutput createMultipartUpload(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        isValidBucketName(bucket);
        isValidKey(objectKey);
        RequestBuilder rb = newBuilder(bucket, objectKey, builders)
                .withQuery("uploads", "");
        setContentType(rb, objectKey);
        TosRequest req = rb.buildRequest(HttpMethod.POST, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        multipartUpload upload = marshalOutput(res.getInputStream(),
                new TypeReference<multipartUpload>(){});
        return new CreateMultipartUploadOutput().setRequestInfo(res.RequestInfo())
                .setBucket(upload.getBucket())
                .setKey(upload.getKey())
                .setUploadID(upload.getUploadID())
                .setSseCustomerAlgorithm(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerMD5(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5))
                .setSseCustomerKey(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY));
    }

    @Override
    public UploadPartOutput uploadPart(String bucket, UploadPartInput input, RequestOptionsBuilder... builders) throws TosException {
        Objects.requireNonNull(input, "UploadPartInput is null");
        isValidBucketName(bucket);
        isValidKey(input.getKey());
        TosRequest req = newBuilder(bucket, input.getKey(), builders)
                .withQuery("uploadId", input.getUploadID())
                .withQuery("partNumber", String.valueOf(input.getPartNumber()))
                .withContentLength(input.getPartSize())
                .buildRequest(HttpMethod.PUT, input.getContent());
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return new UploadPartOutput().setRequestInfo(res.RequestInfo())
                .setPartNumber(input.getPartNumber())
                .setEtag(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG))
                .setSseCustomerAlgorithm(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerMD5(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5));
    }

    @Override
    public CompleteMultipartUploadOutput completeMultipartUpload(String bucket, CompleteMultipartUploadInput input) throws TosException {
        Objects.requireNonNull(input, "CompleteMultipartUploadInput is null");
        Objects.requireNonNull(input.getUploadID(), "upload id is null");
        isValidBucketName(bucket);
        byte[] data = input.getUploadedPartData(TosUtils.getJsonMapper());
        TosRequest req = newBuilder(bucket, input.getKey())
                .withQuery("uploadId", input.getUploadID())
                .buildRequest(HttpMethod.POST, null).setData(data);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        CompleteMultipartUploadOutput output = PayloadConverter.parsePayload(
                res.getInputStream(), new TypeReference<CompleteMultipartUploadOutput>(){});
        return output.setRequestInfo(res.RequestInfo())
                .setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setCrc64(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    @Override
    public AbortMultipartUploadOutput abortMultipartUpload(String bucket, AbortMultipartUploadInput input) throws TosException {
        Objects.requireNonNull(input, "AbortMultipartUploadInput is null");
        Objects.requireNonNull(input.getUploadID(), "upload id is null");
        isValidBucketName(bucket);
        isValidKey(input.getKey());
        TosRequest req = newBuilder(bucket, input.getKey())
                .withQuery("uploadId", input.getUploadID())
                .buildRequest(HttpMethod.DELETE, null);
        TosResponse res = roundTrip(req, HttpStatus.NO_CONTENT);
        return new AbortMultipartUploadOutput().setRequestInfo(res.RequestInfo());
    }

    @Override
    public ListUploadedPartsOutput listUploadedParts(String bucket, ListUploadedPartsInput input, RequestOptionsBuilder... builders) throws TosException {
        Objects.requireNonNull(input, "ListUploadedPartsInput is null");
        isValidBucketName(bucket);
        isValidKey(input.getKey());
        if (input.getMaxParts() < 0 || input.getPartNumberMarker() < 0) {
            throw new IllegalArgumentException("tos: ListUploadedPartsInput maxParts or partNumberMarker is small than 0");
        }
        TosRequest req = newBuilder(bucket, input.getKey(), builders)
                .withQuery("uploadId", input.getUploadID())
                .withQuery("max-parts", String.valueOf(input.getMaxParts()))
                .withQuery("part-number-marker", String.valueOf(input.getPartNumberMarker()))
                .buildRequest(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListUploadedPartsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    @Override
    public ListMultipartUploadsOutput listMultipartUploads(String bucket, ListMultipartUploadsInput input) throws TosException {
        Objects.requireNonNull(input, "ListMultipartUploadsInput is null");
        isValidBucketName(bucket);
        TosRequest req = newBuilder(bucket, "")
                .withQuery("uploads", "")
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("key-marker", input.getKeyMarker())
                .withQuery("upload-id-marker", input.getUploadIDMarker())
                .withQuery("max-uploads", String.valueOf(input.getMaxUploads()))
                .buildRequest(HttpMethod.GET, null);
        TosResponse res = roundTrip(req, HttpStatus.OK);
        return marshalOutput(res.getInputStream(), new TypeReference<ListMultipartUploadsOutput>(){})
                .setRequestInfo(res.RequestInfo());
    }

    @Override
    public String preSignedURL(String httpMethod, String bucket, String objectKey, Duration ttl, RequestOptionsBuilder...builders) throws TosException{
        isValidBucketName(bucket);
        isValidKey(objectKey);
        return newBuilder(bucket, objectKey, builders).preSignedURL(httpMethod, ttl);
    }

//    private TosMarshalResult marshalInput(Object input) throws TosException{
//        byte[] data;
//        String dataMD5;
//        try{
//            data = JSON.setSerializationInclusion(JsonInclude.Include.NON_NULL).writeValueAsBytes(input);
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            byte[] bytes = md.digest(data);
//            dataMD5 = Base64.getEncoder().encodeToString(bytes);
//        } catch (IOException | NoSuchAlgorithmException e){
//            throw new TosClientException("Marshal Input Exception", e);
//        }
//        return new TosMarshalResult(dataMD5, data);
//    }
    
    private <T> T marshalOutput(InputStream reader, TypeReference<T> valueTypeRef) throws TosException{
        try{
            return TosUtils.getJsonMapper().readValue(reader, valueTypeRef);
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
        isValidKey(key);
        isValidKeySet(keys);
    }

    private static void isValidKey(String key){
        if (StringUtils.isEmpty(key)) {
            throw new IllegalArgumentException("tos: object name is empty");
        }
    }

    private static void isValidKeySet(String ...keys) {
        for (String k: keys) {
            isValidKey(k);
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

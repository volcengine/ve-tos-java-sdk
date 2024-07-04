package com.volcengine.tos.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.MimeType;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.comm.common.BucketType;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.model.*;
import com.volcengine.tos.internal.util.*;
import com.volcengine.tos.internal.util.aborthook.DefaultAbortTosObjectInputStreamHook;
import com.volcengine.tos.internal.util.ratelimit.RateLimitedInputStream;
import com.volcengine.tos.model.GenericInput;
import com.volcengine.tos.model.bucket.HeadBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Output;
import com.volcengine.tos.model.object.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TosObjectRequestHandler {
    private TosBucketRequestHandler bucketRequestHandler;
    private RequestHandler objectHandler;
    private TosRequestFactory factory;
    private boolean clientAutoRecognizeContentType;
    private boolean enableCrcCheck;
    private boolean disableEncodingMeta;
    private final BucketCacheLock[] bucketCacheLocks;

    private static class BucketCache {
        BucketType bucketType;
        long lastUpdateTimeNanos;
        double timeout;
    }

    private static class BucketCacheLock {
        Map<String, BucketCache> bucketTypes;
        ReadWriteLock lock;
    }

    public TosObjectRequestHandler(Transport transport, TosRequestFactory factory) {
        this(transport, factory, null);
    }

    public TosObjectRequestHandler(Transport transport, TosRequestFactory factory, TosBucketRequestHandler bucketRequestHandler) {
        this.objectHandler = new RequestHandler(transport);
        this.factory = factory;
        this.bucketRequestHandler = bucketRequestHandler;
        this.bucketCacheLocks = new BucketCacheLock[16];
        for (int i = 0; i < this.bucketCacheLocks.length; i++) {
            BucketCacheLock bucketCacheLock = new BucketCacheLock();
            bucketCacheLock.bucketTypes = new HashMap<>();
            bucketCacheLock.lock = new ReentrantReadWriteLock();
            this.bucketCacheLocks[i] = bucketCacheLock;
        }
    }

    public TosObjectRequestHandler setTransport(Transport transport) {
        if (this.objectHandler == null) {
            this.objectHandler = new RequestHandler(transport);
        } else {
            this.objectHandler.setTransport(transport);
        }
        return this;
    }

    public Transport getTransport() {
        if (this.objectHandler != null) {
            return this.objectHandler.getTransport();
        }
        return null;
    }

    public TosRequestFactory getFactory() {
        return factory;
    }

    public TosObjectRequestHandler setFactory(TosRequestFactory factory) {
        this.factory = factory;
        return this;
    }

    public boolean isClientAutoRecognizeContentType() {
        return clientAutoRecognizeContentType;
    }

    public boolean isEnableCrcCheck() {
        return enableCrcCheck;
    }

    public TosObjectRequestHandler setClientAutoRecognizeContentType(boolean clientAutoRecognizeContentType) {
        this.clientAutoRecognizeContentType = clientAutoRecognizeContentType;
        return this;
    }

    public TosObjectRequestHandler setEnableCrcCheck(boolean enableCrcCheck) {
        this.enableCrcCheck = enableCrcCheck;
        return this;
    }

    public TosObjectRequestHandler setDisableEncodingMeta(boolean disableEncodingMeta) {
        this.disableEncodingMeta = disableEncodingMeta;
        return this;
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

    private BucketType getBucketType(String bucket) {
        if (this.bucketRequestHandler == null) {
            return null;
        }
        BucketCacheLock bcl = this.bucketCacheLocks[Math.abs(bucket.hashCode()) % this.bucketCacheLocks.length];

        bcl.lock.readLock().lock();
        BucketCache bc = bcl.bucketTypes.get(bucket);
        bcl.lock.readLock().unlock();

        if (bc != null && (System.nanoTime() - bc.lastUpdateTimeNanos < bc.timeout)) {
            return bc.bucketType;
        }

        bcl.lock.writeLock().lock();
        try {
            bc = bcl.bucketTypes.get(bucket);
            if (bc != null && (System.nanoTime() - bc.lastUpdateTimeNanos < bc.timeout)) {
                return bc.bucketType;
            }
            HeadBucketV2Output output = this.bucketRequestHandler.headBucket(new HeadBucketV2Input().setBucket(bucket));
            bc = new BucketCache();
            bc.bucketType = output.getBucketType();
            bc.lastUpdateTimeNanos = System.nanoTime();
            bc.timeout = 15 * 60 * 1e9;
            bcl.bucketTypes.put(bucket, bc);
            return bc.bucketType;
        } catch (TosServerException ex) {
            if (bc != null) {
                bcl.bucketTypes.remove(bucket);
            }
            TosUtils.getLogger().warn("try to get bucket type failed", ex);
            throw ex;
        } finally {
            bcl.lock.writeLock().unlock();
        }
    }

    public GetFileStatusOutput getFileStatus(GetFileStatusInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetFileStatusInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        BucketType bucketType = this.getBucketType(input.getBucket());
        if (bucketType != null && bucketType.getType().equals(BucketType.BUCKET_TYPE_HNS.getType())) {
            HeadObjectV2Input hinput = new HeadObjectV2Input().setBucket(input.getBucket())
                    .setKey(input.getKey());
            hinput.setRequestDate(input.getRequestDate());
            hinput.setRequestHost(input.getRequestHost());
            HeadObjectV2Output output = this.headObject(hinput);
            GetFileStatusOutput goutput = new GetFileStatusOutput();
            goutput.setRequestInfo(output.getRequestInfo());
            goutput.setKey(input.getKey());
            goutput.setLastModified(output.getLastModified());
            goutput.setCrc64(output.getHashCrc64ecma());
            if (output.getRequestInfo().getHeader() != null) {
                goutput.setCrc32(output.getRequestInfo().getHeader().get(TosHeader.HEADER_CRC32.toLowerCase()));
            }
            goutput.setSize(output.getContentLength());
            return goutput;
        }

        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("stat", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildGetFileStatusOutput);
    }

    private GetFileStatusOutput buildGetFileStatusOutput(TosResponse response) {
        return PayloadConverter.parsePayload(response.getInputStream(), new TypeReference<GetFileStatusOutput>() {
                })
                .setRequestInfo(response.RequestInfo());
    }

    public GetObjectV2Output getObject(GetObjectV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectV2Input");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(),
                        input.getAllSettedHeaders()).withQuery("versionId", input.getVersionID())
                .withQuery(TosHeader.QUERY_RESPONSE_CACHE_CONTROL, input.getResponseCacheControl())
                .withQuery(TosHeader.QUERY_RESPONSE_CONTENT_DISPOSITION, input.getResponseContentDisposition())
                .withQuery(TosHeader.QUERY_RESPONSE_CONTENT_ENCODING, input.getResponseContentEncoding())
                .withQuery(TosHeader.QUERY_RESPONSE_CONTENT_TYPE, input.getResponseContentType())
                .withQuery(TosHeader.QUERY_RESPONSE_CONTENT_LANGUAGE, input.getResponseContentLanguage())
                .withQuery(TosHeader.QUERY_RESPONSE_EXPIRES, DateConverter.dateToRFC1123String(input.getResponseExpires()))
                .withQuery(TosHeader.QUERY_DATA_PROCESS, input.getProcess())
                .withQuery(TosHeader.QUERY_SAVE_BUCKET, input.getSaveBucket())
                .withQuery(TosHeader.QUERY_SAVE_OBJECT, input.getSaveObject());

        if (input.getDocPage() > 0) {
            builder = builder.withQuery(TosHeader.QUERY_DOC_PAGE, Integer.toString(input.getDocPage()));
        }
        if (input.getSrcType() != null) {
            builder = builder.withQuery(TosHeader.QUERY_DOC_SRC_TYPE, input.getSrcType().toString());
        }
        if (input.getDstType() != null) {
            builder = builder.withQuery(TosHeader.QUERY_DOC_DST_TYPE, input.getDstType().toString());
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        try {
            TosResponse response = objectHandler.doRequest(req, getExpectedCodes(input.getAllSettedHeaders()));
            return buildGetObjectV2Output(response, input.getRateLimiter(), input.getDataTransferListener());
        } catch (TosException ex) {
            throw ex.setRequestUrl(req.toURL().toString());
        }
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

    private GetObjectV2Output buildGetObjectV2Output(TosResponse response, RateLimiter rateLimiter,
                                                     DataTransferListener dataTransferListener) {
        GetObjectBasicOutput basicOutput = new GetObjectBasicOutput()
                .setRequestInfo(response.RequestInfo()).parseFromTosResponse(response);
        InputStream content = response.getInputStream();
        if (rateLimiter != null) {
            content = new RateLimitedInputStream(content, rateLimiter);
        }
        if (dataTransferListener != null) {
            content = new SimpleDataTransferListenInputStream(content, dataTransferListener, response.getContentLength());
        }
        if (this.enableCrcCheck && response.getStatusCode() != HttpStatus.PARTIAL_CONTENT) {
            // 开启 crc 校验且非 Range 下载
            String serverCrc64ecma = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64);
            if (StringUtils.isNotEmpty(serverCrc64ecma)) {
                content = new CheckCrc64AutoInputStream(content, new CRC64Checksum(), serverCrc64ecma);
            }
        }

        return new GetObjectV2Output(basicOutput, new TosObjectInputStream(content))
                .setHook(new DefaultAbortTosObjectInputStreamHook(content, response.getSource()));
    }

    public HeadObjectV2Output headObject(HeadObjectV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "HeadObjectV2Input");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("versionId", input.getVersionID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.HEAD, null);
        return objectHandler.doRequest(req, getExpectedCodes(input.getAllSettedHeaders()),
                response -> {
                    HeadObjectV2Output output = new HeadObjectV2Output(new GetObjectBasicOutput()
                            .setRequestInfo(response.RequestInfo()).parseFromTosResponse(response));
                    String symlinkTargetSize = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SYMLINK_TARGET_SIZE);
                    return StringUtils.isNotEmpty(symlinkTargetSize) ? output.setSymlinkTargetSize(Long.parseLong(symlinkTargetSize)) : output;
                });
    }

    public DeleteObjectOutput deleteObject(DeleteObjectInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteObjectInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        if (!input.isRecursive()) {
            RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("versionId", input.getVersionID());
            builder = this.handleGenericInput(builder, input);
            TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
            return objectHandler.doRequest(req, HttpStatus.NO_CONTENT,
                    response -> new DeleteObjectOutput().setRequestInfo(response.RequestInfo())
                            .setDeleteMarker(Boolean.parseBoolean(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_DELETE_MARKER)))
                            .setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
            );
        }

        BucketType bucketType = this.getBucketType(input.getBucket());
        boolean hns = bucketType != null && bucketType.getType().equals(BucketType.BUCKET_TYPE_HNS.getType());
        if (hns && this.isRecursiveByServer(input)) {
            RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                    .withQuery("versionId", input.getVersionID())
                    .withQuery("recursive", "true");
            builder = this.handleGenericInput(builder, input);
            TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
            return objectHandler.doRequest(req, HttpStatus.NO_CONTENT,
                    response -> new DeleteObjectOutput().setRequestInfo(response.RequestInfo())
            );
        }

        return new RecursiveDeleter(input, hns, this).deleteRecursive();
    }

    private boolean isRecursiveByServer(DeleteObjectInput input) {
        try {
            Field f = input.getClass().getDeclaredField("recursiveByServer");
            f.setAccessible(true);
            return f.getBoolean(input);
        } catch (Exception e) {
            return false;
        }
    }

    public DeleteMultiObjectsV2Output deleteMultiObjects(DeleteMultiObjectsV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteMultiObjectsV2Input");
        ParamsChecker.ensureNotNull(input.getObjects(), "objects to be deleted");
        ensureValidBucketName(input.getBucket());
        for (ObjectTobeDeleted objectTobeDeleted : input.getObjects()) {
            ensureValidKey(objectTobeDeleted.getKey());
        }
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5()).withQuery("delete", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.POST, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return objectHandler.doRequest(req, HttpStatus.OK, response -> PayloadConverter.parsePayload(response.getInputStream(),
                new TypeReference<DeleteMultiObjectsV2Output>() {
                }).requestInfo(response.RequestInfo())
        );
    }

    private PutObjectOutput putObject(PutObjectBasicInput input, InputStream content) {
        ParamsChecker.ensureNotNull(input, "PutObjectBasicInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        content = ensureNotNullContent(content);
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withContentLength(input.getContentLength())
                .withHeader(TosHeader.HEADER_CALLBACK, input.getCallback())
                .withHeader(TosHeader.HEADER_CALLBACK_VAR, input.getCallbackVar())
                .withHeader(TosHeader.HEADER_X_IF_MATCH, input.getIfMatch())
                .withHeader(TosHeader.HEADER_TAGGING, input.getTagging());
        if (input.isForbidOverwrite()) {
            builder = builder.withHeader(TosHeader.HEADER_FORBID_OVERWRITE, "true");
        }
        addContentType(builder, input.getKey());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, content)
                .setEnableCrcCheck(this.enableCrcCheck)
                .setRateLimiter(input.getRateLimiter())
                .setDataTransferListener(input.getDataTransferListener())
                .setReadLimit(input.getReadLimit());
        setRetryStrategy(req, content);
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildPutObjectOutput);
    }

    private InputStream ensureNotNullContent(InputStream content) {
        if (content == null) {
            content = new ByteArrayInputStream("".getBytes());
        }
        return content;
    }

    private static void setRetryStrategy(TosRequest request, InputStream stream) {
        boolean canRetry = stream.markSupported() || stream instanceof FileInputStream;
        request.setRetryableOnServerException(canRetry);
        request.setRetryableOnClientException(canRetry);
    }

    private PutObjectOutput buildPutObjectOutput(TosResponse res) {
        String callbackResult = StringUtils.toString(res.getInputStream(), "callbackResult");
        return new PutObjectOutput().setRequestInfo(res.RequestInfo())
                .setEtag(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG))
                .setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setHashCrc64ecma(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64))
                .setSseCustomerAlgorithm(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerKeyMD5(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5))
                .setSseCustomerKey(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY))
                .setCallbackResult(callbackResult);
    }

    public PutObjectOutput putObject(PutObjectInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectInput");
        if (input.getPutObjectBasicInput() != null) {
            input.getPutObjectBasicInput().setRequestHost(input.getRequestHost());
            input.getPutObjectBasicInput().setRequestDate(input.getRequestDate());
        }
        return putObject(input.getPutObjectBasicInput(), input.getContent());
    }

    public AppendObjectOutput appendObject(AppendObjectInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "AppendObjectInput");
        if (this.enableCrcCheck && input.getOffset() > 0 && StringUtils.isEmpty(input.getPreHashCrc64ecma())) {
            throw new TosClientException("tos: client enable crc64 check but preHashCrc64ecma is not set", null);
        }
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());

        BucketType bucketType = this.getBucketType(input.getBucket());
        if (bucketType != null && bucketType.getType().equals(BucketType.BUCKET_TYPE_HNS.getType())) {
            ModifyObjectInput minput = new ModifyObjectInput().setBucket(input.getBucket())
                    .setKey(input.getKey()).setOffset(input.getOffset()).setContent(input.getContent())
                    .setContentLength(input.getContentLength()).setDataTransferListener(input.getDataTransferListener())
                    .setRateLimiter(input.getRateLimiter());
            minput.setRequestDate(input.getRequestDate());
            minput.setRequestHost(input.getRequestHost());
            if (input.getOptions() != null) {
                long trafficLimit = input.getOptions().getTrafficLimit();
                if (trafficLimit > 0) {
                    minput.setTrafficLimit(trafficLimit);
                }
            }
            ModifyObjectOutput output = this.modifyObject(minput, input.getPreHashCrc64ecma(), this.enableCrcCheck);
            return new AppendObjectOutput().setRequestInfo(output.getRequestInfo())
                    .setNextAppendOffset(output.getNextModifyOffset()).setHashCrc64ecma(output.getHashCrc64ecma());
        }

        // append not support chunked, need to set contentLength
        if (input.getContentLength() <= 0) {
            throw new TosClientException("content length should be set in appendObject method.", null);
        }

        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("append", "")
                .withQuery("offset", String.valueOf(input.getOffset()))
                .withContentLength(input.getContentLength())
                .withHeader(TosHeader.HEADER_X_IF_MATCH, input.getIfMatch());

        addContentType(builder, input.getKey());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.POST, input.getContent())
                // appendObject should not retry
                .setRetryableOnServerException(false).setRetryableOnClientException(false)
                .setEnableCrcCheck(this.enableCrcCheck)
                .setCrc64InitValue(CRC64Utils.unsignedLongStringToLong(input.getPreHashCrc64ecma()))
                .setRateLimiter(input.getRateLimiter())
                .setDataTransferListener(input.getDataTransferListener());
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildAppendObjectOutput);
    }

    private AppendObjectOutput buildAppendObjectOutput(TosResponse response) {
        String nextOffset = response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_NEXT_APPEND_OFFSET);
        long appendOffset;
        try {
            appendOffset = Long.parseLong(nextOffset);
        } catch (NumberFormatException nfe) {
            throw new TosClientException("tos: server return unexpected Next-Append-Offset header: " + nextOffset, nfe);
        }
        return new AppendObjectOutput().setRequestInfo(response.RequestInfo())
                .setNextAppendOffset(appendOffset)
                .setHashCrc64ecma(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    public SetObjectMetaOutput setObjectMeta(SetObjectMetaInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "SetObjectMetaInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(),
                input.getAllSettedHeaders()).withQuery("metadata", "").withQuery("versionId", input.getVersionID());
        addContentType(builder, input.getKey());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.POST, null);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> new SetObjectMetaOutput().setRequestInfo(response.RequestInfo())
        );
    }

    public ListObjectsV2Output listObjects(ListObjectsV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListObjectsV2Input");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("marker", input.getMarker())
                .withQuery("max-keys", TosUtils.convertInteger(input.getMaxKeys()))
                .withQuery("reverse", String.valueOf(input.isReverse()))
                .withQuery("encoding-type", input.getEncodingType());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK, response -> PayloadConverter.parsePayload(response.getInputStream(),
                new TypeReference<ListObjectsV2Output>() {
                }).setRequestInfo(response.RequestInfo()));
    }

    public ListObjectsType2Output listObjectsType2(ListObjectsType2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListObjectsType2Input");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("list-type", "2")
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("start-after", input.getStartAfter())
                .withQuery("continuation-token", input.getContinuationToken())
                .withQuery("max-keys", TosUtils.convertInteger(input.getMaxKeys()))
                .withQuery("encoding-type", input.getEncodingType())
                .withQuery("fetch-owner", "true");
        if (input.isFetchMeta()) {
            builder = builder.withQuery("fetch-meta", "true");
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        ListObjectsType2Output output = objectHandler.doRequest(req, HttpStatus.OK, response -> PayloadConverter.parsePayload(response.getInputStream(),
                new TypeReference<ListObjectsType2Output>() {
                }).setRequestInfo(response.RequestInfo()));
        if (output.getContents() != null && output.getContents().size() > 0 && this.disableEncodingMeta) {
            for (ListedObjectV2 obj : output.getContents()) {
                try {
                    Field f = obj.getClass().getDeclaredField("disableEncodingMeta");
                    f.setAccessible(true);
                    f.set(obj, true);
                } catch (Exception e) {
                }
            }
        }

        return output;
    }

    public ListObjectsType2Output listObjectsType2UntilFinished(ListObjectsType2Input input) {
        ParamsChecker.ensureNotNull(input, "ListObjectsType2Input");
        if (input.isListOnlyOnce()) {
            return listObjectsType2(input);
        }

        int mk = input.getMaxKeys() > 0 ? input.getMaxKeys() : 1000;

        int totalRecords = 0;
        List<ListedCommonPrefix> commonPrefixes = null;
        List<ListedObjectV2> contents = null;

        ListObjectsType2Output tmp = null;
        String continuationToken = input.getContinuationToken();
        boolean listFinished = false;
        while (!listFinished) {
            tmp = listObjectsType2(input.setContinuationToken(continuationToken));

            if (tmp.getCommonPrefixes() != null) {
                if (commonPrefixes == null) {
                    commonPrefixes = tmp.getCommonPrefixes();
                } else {
                    commonPrefixes.addAll(tmp.getCommonPrefixes());
                }
            }
            if (tmp.getContents() != null) {
                if (contents == null) {
                    contents = tmp.getContents();
                } else {
                    contents.addAll(tmp.getContents());
                }
            }

            totalRecords += tmp.getKeyCount();
            continuationToken = tmp.getNextContinuationToken();
            listFinished = isListFinished(tmp.isTruncated(), mk, totalRecords);
        }
        return tmp.setCommonPrefixes(commonPrefixes).setContents(contents).setKeyCount(totalRecords);
    }

    private boolean isListFinished(boolean isTruncated, int mk, int totalRecords) {
        boolean returnMaxKeysRecord = mk == totalRecords;
        // if returnMaxKeysRecord, or if not returnMaxKeysRecord but not truncated
        // it means that the list request is finished.
        return returnMaxKeysRecord || !isTruncated;
    }

    public ListObjectVersionsV2Output listObjectVersions(ListObjectVersionsV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListObjectVersionsV2Input");
        ensureValidBucketName(input.getBucket());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("key-marker", input.getKeyMarker())
                .withQuery("max-keys", TosUtils.convertInteger(input.getMaxKeys()))
                .withQuery("encoding-type", input.getEncodingType())
                .withQuery("version-id-marker", input.getVersionIDMarker())
                .withQuery("versions", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> PayloadConverter.parsePayload(response.getInputStream(),
                        new TypeReference<ListObjectVersionsV2Output>() {
                        }).setRequestInfo(response.RequestInfo())
        );
    }

    public CopyObjectV2Output copyObject(CopyObjectV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CopyObjectV2Input");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        ensureValidBucketName(input.getSrcBucket());
        ensureValidKey(input.getSrcKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("versionId", input.getSrcVersionID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.buildWithCopy(builder, HttpMethod.PUT, input.getSrcBucket(), input.getSrcKey());
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildCopyObjectV2Output);
    }

    private CopyObjectV2Output buildCopyObjectV2Output(TosResponse response) {
        // 一把解 CopyObjectV2Output 和 ServerExceptionJson
        String rspMsg = StringUtils.toString(response.getInputStream(), "copy result");
        try {
            CopyObjectV2Output output = PayloadConverter.parsePayload(rspMsg,
                    new TypeReference<CopyObjectV2Output>() {
                    });
            return output.setRequestInfo(response.RequestInfo())
                    .setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                    .setSourceVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_COPY_SOURCE_VERSION_ID))
                    .setHashCrc64ecma(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
        } catch (TosClientException e) {
            ServerExceptionJson errMsg = PayloadConverter.parsePayload(rspMsg,
                    new TypeReference<ServerExceptionJson>() {
                    });
            throw new TosServerException(response.getStatusCode(), errMsg.getCode(), errMsg.getMessage(),
                    errMsg.getRequestID(), errMsg.getHostID()).setEc(errMsg.getEc()).setKey(errMsg.getKey());
        }
    }

    public UploadPartCopyV2Output uploadPartCopy(UploadPartCopyV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "UploadPartCopyV2Input");
        ParamsChecker.ensureNotNull(input.getUploadID(), "UploadID");
        ParamsChecker.isValidPartNumber(input.getPartNumber());
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        ensureValidBucketName(input.getSourceBucket());
        ensureValidKey(input.getSourceKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("partNumber", TosUtils.convertInteger(input.getPartNumber()))
                .withQuery("uploadId", input.getUploadID())
                .withQuery("versionId", input.getSourceVersionID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.buildWithCopy(builder, HttpMethod.PUT, input.getSourceBucket(), input.getSourceKey());
        return objectHandler.doRequest(req, HttpStatus.OK, response -> buildUploadPartCopyV2Output(input, response));
    }

    private UploadPartCopyV2Output buildUploadPartCopyV2Output(UploadPartCopyV2Input input, TosResponse response) {
        String rspMsg = StringUtils.toString(response.getInputStream(), "copy result");
        try {
            UploadPartCopyOutputJson out = PayloadConverter.parsePayload(rspMsg,
                    new TypeReference<UploadPartCopyOutputJson>() {
                    });
            return new UploadPartCopyV2Output().requestInfo(response.RequestInfo())
                    .copySourceVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_COPY_SOURCE_VERSION_ID))
                    .partNumber(input.getPartNumber()).etag(out.getEtag()).lastModified(out.getLastModified());
        } catch (TosClientException e) {
            ServerExceptionJson errMsg = PayloadConverter.parsePayload(rspMsg,
                    new TypeReference<ServerExceptionJson>() {
                    });
            throw new TosServerException(response.getStatusCode(), errMsg.getCode(), errMsg.getMessage(),
                    errMsg.getRequestID(), errMsg.getHostID()).setEc(errMsg.getEc()).setKey(errMsg.getKey());
        }
    }

    public PutObjectACLOutput putObjectAcl(PutObjectACLInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectACLInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("acl", "").withQuery("versionId", input.getVersionID())
                .withHeader(TosHeader.HEADER_ACL, input.getAcl() == null ? null : input.getAcl().toString())
                .withHeader(TosHeader.HEADER_GRANT_FULL_CONTROL, input.getGrantFullControl())
                .withHeader(TosHeader.HEADER_GRANT_READ, input.getGrantRead())
                .withHeader(TosHeader.HEADER_GRANT_READ_ACP, input.getGrantReadAcp())
                .withHeader(TosHeader.HEADER_GRANT_WRITE_ACP, input.getGrantWriteAcp());
        byte[] content = new byte[0];
        if (input.getObjectAclRules() != null) {
            TosMarshalResult res = PayloadConverter.serializePayloadAndComputeMD5(input.getObjectAclRules());
            content = res.getData();
            builder.withHeader(TosHeader.HEADER_CONTENT_MD5, res.getContentMD5());
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(content)).setContentLength(content.length);
        return objectHandler.doRequest(req, HttpStatus.OK, response -> new PutObjectACLOutput().requestInfo(response.RequestInfo()));
    }

    public GetObjectACLV2Output getObjectAcl(GetObjectACLV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectACLV2Input");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("acl", "").withQuery("versionId", input.getVersionID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildGetObjectACLV2Output);
    }

    private GetObjectACLV2Output buildGetObjectACLV2Output(TosResponse response) {
        return PayloadConverter.parsePayload(response.getInputStream(), new TypeReference<GetObjectACLV2Output>() {
                })
                .setRequestInfo(response.RequestInfo())
                .setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID));
    }

    public PutObjectTaggingOutput putObjectTagging(PutObjectTaggingInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutObjectTaggingInput");
        ParamsChecker.ensureNotNull(input.getTagSet(), "TagSet");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("tagging", "").withQuery("versionId", input.getVersionID())
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return objectHandler.doRequest(req, HttpStatus.OK, response -> new PutObjectTaggingOutput()
                .setRequestInfo(response.RequestInfo()).setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID)));
    }

    public GetObjectTaggingOutput getObjectTagging(GetObjectTaggingInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetObjectTaggingInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("tagging", "").withQuery("versionId", input.getVersionID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK, res -> PayloadConverter.parsePayload(res.getInputStream(),
                        new TypeReference<GetObjectTaggingOutput>() {
                        }).setRequestInfo(res.RequestInfo())
                .setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID)));
    }

    public DeleteObjectTaggingOutput deleteObjectTagging(DeleteObjectTaggingInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "DeleteObjectTaggingInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("tagging", "").withQuery("versionId", input.getVersionID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null);
        return objectHandler.doRequest(req, HttpStatus.NO_CONTENT, res -> new DeleteObjectTaggingOutput()
                .setRequestInfo(res.RequestInfo()).setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID)));
    }

    public FetchObjectOutput fetchObject(FetchObjectInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "FetchObjectInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        ParamsChecker.ensureNotNull(input.getUrl(), "URL");
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("fetch", "").withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.POST, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return objectHandler.doRequest(req, HttpStatus.OK, response -> PayloadConverter.parsePayload(response.getInputStream(),
                        new TypeReference<FetchObjectOutput>() {
                        }).setRequestInfo(response.RequestInfo())
                .setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setSsecAlgorithm(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSsecKeyMD5(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5))
        );
    }

    public PutFetchTaskOutput putFetchTask(PutFetchTaskInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutFetchTaskInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        ParamsChecker.ensureNotNull(input.getUrl(), "URL");
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), "", input.getAllSettedHeaders())
                .withQuery("fetchTask", "").withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.POST, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return objectHandler.doRequest(req, HttpStatus.OK, response -> PayloadConverter.parsePayload(response.getInputStream(),
                new TypeReference<PutFetchTaskOutput>() {
                }).setRequestInfo(response.RequestInfo()));
    }

    public GetFetchTaskOutput getFetchTask(GetFetchTaskInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetFetchTaskInput");
        ensureValidBucketName(input.getBucket());
        if (StringUtils.isEmpty(input.getTaskId())) {
            throw new TosClientException("empty task id", null);
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null).withQuery("fetchTask", "").withQuery("taskId", input.getTaskId());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        GetFetchTaskOutput output = objectHandler.doRequest(req, HttpStatus.OK, response -> PayloadConverter.parsePayload(response.getInputStream(),
                new TypeReference<GetFetchTaskOutput>() {
                }).setRequestInfo(response.RequestInfo()));
        if (output.getTask() != null && output.getTask().getMeta() != null && output.getTask().getMeta().size() > 0 && disableEncodingMeta) {
            try {
                Field f = output.getTask().getClass().getDeclaredField("disableEncodingMeta");
                f.setAccessible(true);
                f.set(output.getTask(), true);
            } catch (Exception e) {
            }
        }
        return output;
    }

    public CreateMultipartUploadOutput createMultipartUpload(CreateMultipartUploadInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CreateMultipartUploadInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("uploads", "").withHeader(TosHeader.HEADER_TAGGING, input.getTagging());
        addContentType(builder, input.getKey());
        if (input.isForbidOverwrite()) {
            builder = builder.withHeader(TosHeader.HEADER_FORBID_OVERWRITE, "true");
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.POST, null).setRetryableOnClientException(false);
        return objectHandler.doRequest(req, HttpStatus.OK, this::buildCreateMultipartUploadOutput);
    }

    private CreateMultipartUploadOutput buildCreateMultipartUploadOutput(TosResponse response) {
        CreateMultipartUploadOutputJson upload = PayloadConverter.parsePayload(response.getInputStream(),
                new TypeReference<CreateMultipartUploadOutputJson>() {
                });
        return new CreateMultipartUploadOutput().setRequestInfo(response.RequestInfo())
                .setBucket(upload.getBucket()).setKey(upload.getKey()).setUploadID(upload.getUploadID())
                .setEncodingType(upload.getEncodingType())
                .setSseCustomerAlgorithm(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSseCustomerMD5(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5))
                .setSseCustomerKey(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY));
    }

    private UploadPartV2Output buildUploadPartV2Output(TosResponse res, int partNumber) {
        return new UploadPartV2Output().setRequestInfo(res.RequestInfo())
                .setPartNumber(partNumber).setEtag(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG))
                .setSsecAlgorithm(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_ALGORITHM))
                .setSsecKeyMD5(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SSE_CUSTOMER_KEY_MD5))
                .setHashCrc64ecma(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    private UploadPartV2Output uploadPart(UploadPartBasicInput input, long contentLength, InputStream content) {
        ParamsChecker.ensureNotNull(input, "UploadPartBasicInput");
        ParamsChecker.ensureNotNull(input.getUploadID(), "uploadID");
        ParamsChecker.ensureNotNull(content, "InputStream");
        ParamsChecker.isValidPartNumber(input.getPartNumber());
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), input.getAllSettedHeaders())
                .withQuery("uploadId", input.getUploadID())
                .withQuery("partNumber", TosUtils.convertInteger(input.getPartNumber()))
                .withContentLength(contentLength);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, content)
                .setEnableCrcCheck(this.enableCrcCheck).setRateLimiter(input.getRateLimiter())
                .setDataTransferListener(input.getDataTransferListener())
                .setReadLimit(input.getReadLimit());
        setRetryStrategy(req, content);
        return objectHandler.doRequest(req, HttpStatus.OK, response -> buildUploadPartV2Output(response, input.getPartNumber()));
    }

    public UploadPartV2Output uploadPart(UploadPartV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "UploadPartV2Input");
        if (input.getUploadPartBasicInput() != null) {
            input.getUploadPartBasicInput().setRequestDate(input.getRequestDate());
            input.getUploadPartBasicInput().setRequestHost(input.getRequestHost());
        }
        return uploadPart(input.getUploadPartBasicInput(), input.getContentLength(), input.getContent());
    }

    public CompleteMultipartUploadV2Output completeMultipartUpload(CompleteMultipartUploadV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "CompleteMultipartUploadV2Input");
        ParamsChecker.ensureNotNull(input.getUploadID(), "uploadID");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("uploadId", input.getUploadID())
                .withHeader(TosHeader.HEADER_CALLBACK, input.getCallback())
                .withHeader(TosHeader.HEADER_CALLBACK_VAR, input.getCallbackVar());
        if (input.isForbidOverwrite()) {
            builder = builder.withHeader(TosHeader.HEADER_FORBID_OVERWRITE, "true");
        }
        String contentMd5 = null;
        byte[] data = new byte[0];
        if (!input.isCompleteAll()) {
            ensureUploadedPartsNotNull(input);
            TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
            contentMd5 = marshalResult.getContentMD5();
            data = marshalResult.getData();
        } else {
            ensureUploadedPartsNull(input);
            builder.withHeader(TosHeader.HEADER_COMPLETE_ALL, Consts.USE_COMPLETE_ALL);
        }
        builder.withHeader(TosHeader.HEADER_CONTENT_MD5, contentMd5);
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.POST, new ByteArrayInputStream(data))
                .setContentLength(data.length).setRetryableOnClientException(false);
        List<Integer> unexpectedCodes = new ArrayList<>();
        unexpectedCodes.add(HttpStatus.NON_AUTHORITATIVE_INFO);
        return objectHandler.doRequest(req, HttpStatus.OK, unexpectedCodes, response -> {
            boolean hasCallbackResult = StringUtils.isNotEmpty(input.getCallback());
            return buildCompleteMultipartUploadOutput(response, hasCallbackResult);
        });
    }

    private void ensureUploadedPartsNull(CompleteMultipartUploadV2Input input) {
        if (input != null && input.getUploadedParts() != null && input.getUploadedParts().size() != 0) {
            throw new TosClientException("tos: you should not set uploadedParts while using completeAll.", null);
        }
    }

    private void ensureUploadedPartsNotNull(CompleteMultipartUploadV2Input input) {
        if (input == null || input.getUploadedParts() == null || input.getUploadedParts().size() == 0) {
            throw new TosClientException("tos: you must specify at least one part.", null);
        }
    }

    private CompleteMultipartUploadV2Output buildCompleteMultipartUploadOutput(TosResponse response, boolean hasCallbackResult) {
        String respBody = StringUtils.toString(response.getInputStream(), "response body");
        CompleteMultipartUploadV2Output output = new CompleteMultipartUploadV2Output();
        if (hasCallbackResult) {
            // if response body return callback result, then set etag and location from header.
            output.setCallbackResult(respBody);
            output.setEtag(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_ETAG));
            output.setLocation(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_LOCATION));
        } else {
            output = PayloadConverter.parsePayload(respBody, new TypeReference<CompleteMultipartUploadV2Output>() {
            });
        }
        return output.setRequestInfo(response.RequestInfo())
                .setVersionID(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                .setHashCrc64ecma(response.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
    }

    public AbortMultipartUploadOutput abortMultipartUpload(AbortMultipartUploadInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "AbortMultipartUploadInput");
        ParamsChecker.ensureNotNull(input.getUploadID(), "uploadID");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("uploadId", input.getUploadID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.DELETE, null).setRetryableOnClientException(false);
        return objectHandler.doRequest(req, HttpStatus.NO_CONTENT,
                response -> new AbortMultipartUploadOutput().setRequestInfo(response.RequestInfo())
        );
    }

    public ListPartsOutput listParts(ListPartsInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListPartsInput");
        ParamsChecker.ensureNotNull(input.getUploadID(), "uploadID");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        if (input.getMaxParts() < 0 || input.getPartNumberMarker() < 0) {
            throw new TosClientException("ListPartsInput maxParts or partNumberMarker is small than 0", null);
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("uploadId", input.getUploadID())
                .withQuery("max-parts", TosUtils.convertInteger(input.getMaxParts()))
                .withQuery("part-number-marker", TosUtils.convertInteger(input.getPartNumberMarker()))
                .withQuery("encoding-type", input.getEncodingType());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> PayloadConverter.parsePayload(response.getInputStream(),
                        new TypeReference<ListPartsOutput>() {
                        }).setRequestInfo(response.RequestInfo())
        );
    }

    public ListMultipartUploadsV2Output listMultipartUploads(ListMultipartUploadsV2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListMultipartUploadsV2Input");
        ensureValidBucketName(input.getBucket());
        if (input.getMaxUploads() < 0) {
            throw new TosClientException("ListMultipartUploadsV2Input maxUploads is small than 0", null);
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), "", null)
                .withQuery("uploads", "")
                .withQuery("prefix", input.getPrefix())
                .withQuery("delimiter", input.getDelimiter())
                .withQuery("key-marker", input.getKeyMarker())
                .withQuery("upload-id-marker", input.getUploadIDMarker())
                .withQuery("max-uploads", TosUtils.convertInteger(input.getMaxUploads()))
                .withQuery("encoding-type", input.getEncodingType());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);
        return objectHandler.doRequest(req, HttpStatus.OK,
                response -> PayloadConverter.parsePayload(response.getInputStream(),
                        new TypeReference<ListMultipartUploadsV2Output>() {
                        }).setRequestInfo(response.RequestInfo())
        );
    }

    public RenameObjectOutput renameObject(RenameObjectInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "RenameObjectInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        ensureValidKey(input.getNewKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("name", input.getNewKey()).withQuery("rename", "");
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, null);
        return objectHandler.doRequest(req, HttpStatus.NO_CONTENT, response -> new RenameObjectOutput()
                .setRequestInfo(response.RequestInfo()));
    }

    public RestoreObjectOutput restoreObject(RestoreObjectInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "RestoreObjectInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        TosMarshalResult marshalResult = PayloadConverter.serializePayloadAndComputeMD5(input);
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withHeader(TosHeader.HEADER_CONTENT_MD5, marshalResult.getContentMD5())
                .withQuery("restore", "")
                .withQuery("versionId", input.getVersionID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.POST, new ByteArrayInputStream(marshalResult.getData()))
                .setContentLength(marshalResult.getData().length);
        return objectHandler.doRequest(req, restoreObjectExceptedCodes(), response -> new RestoreObjectOutput()
                .setRequestInfo(response.RequestInfo()));
    }

    public PutSymlinkOutput putSymlink(PutSymlinkInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PutSymlinkInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        if (StringUtils.isEmpty(input.getSymlinkTargetKey())) {
            throw new TosClientException("empty symlink target key", null);
        }
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("symlink", "")
                .withHeader(TosHeader.HEADER_SYMLINK_BUCKET, input.getSymlinkTargetBucket())
                .withHeader(TosHeader.HEADER_ACL, input.getAcl() == null ? null : input.getAcl().toString())
                .withHeader(TosHeader.HEADER_STORAGE_CLASS, input.getStorageClass() == null ? null : input.getStorageClass().toString());

        try {
            builder = builder.withHeader(TosHeader.HEADER_SYMLINK_TARGET, URLEncoder.encode(input.getSymlinkTargetKey(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new TosClientException("encoding symlink target key failed", e);
        }

        if (input.isForbidOverwrite()) {
            builder = builder.withHeader(TosHeader.HEADER_FORBID_OVERWRITE, "true");
        }
        if (input.getMeta() != null) {
            for (Map.Entry<String, String> entry : input.getMeta().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    builder = builder.withHeader(TosHeader.HEADER_META_PREFIX + key, value);
                }
            }
        }
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.PUT, null).setContentLength(0);
        return objectHandler.doRequest(req, HttpStatus.OK, res -> new PutSymlinkOutput()
                .setRequestInfo(res.RequestInfo()).setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID)));
    }

    public GetSymlinkOutput getSymlink(GetSymlinkInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "GetSymlinkInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("symlink", "").withQuery("versionId", input.getVersionID());
        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.GET, null);

        return objectHandler.doRequest(req, HttpStatus.OK, res -> {
            String symlinkTargetKey = null;
            try {
                symlinkTargetKey = URLDecoder.decode(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SYMLINK_TARGET), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new TosClientException("decode symlink target key failed", e);
            }
            return new GetSymlinkOutput()
                    .setRequestInfo(res.RequestInfo())
                    .setVersionID(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_VERSIONID))
                    .setSymlinkTargetKey(symlinkTargetKey)
                    .setSymlinkTargetBucket(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_SYMLINK_BUCKET))
                    .setLastModified(DateConverter.rfc1123StringToDate(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_LAST_MODIFIED)));
        });
    }

    public ModifyObjectOutput modifyObject(ModifyObjectInput input, String preHashCrc64ecma, boolean enableCrcCheck) {
        ParamsChecker.ensureNotNull(input, "ModifyObjectInput");
        ensureValidBucketName(input.getBucket());
        ensureValidKey(input.getKey());
        InputStream content = ensureNotNullContent(input.getContent());
        RequestBuilder builder = this.factory.init(input.getBucket(), input.getKey(), null)
                .withQuery("modify", "")
                .withQuery("offset", String.valueOf(input.getOffset()))
                .withContentLength(input.getContentLength());

        if (input.getTrafficLimit() > 0) {
            builder = builder.withHeader(TosHeader.HEADER_TRAFFIC_LIMIT, String.valueOf(input.getTrafficLimit()));
        }

        builder = this.handleGenericInput(builder, input);
        TosRequest req = this.factory.build(builder, HttpMethod.POST, content)
                // modifyobject should not retry
                .setRetryableOnServerException(false).setRetryableOnClientException(false)
                .setRateLimiter(input.getRateLimiter())
                .setEnableCrcCheck(enableCrcCheck)
                .setCrc64InitValue(CRC64Utils.unsignedLongStringToLong(preHashCrc64ecma))
                .setDataTransferListener(input.getDataTransferListener());


        return objectHandler.doRequest(req, HttpStatus.OK, res -> {
            String nextModifyOffset = res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_NEXT_MODIFY_OFFSET);
            try {
                return new ModifyObjectOutput().setRequestInfo(res.RequestInfo())
                        .setNextModifyOffset(Long.parseLong(nextModifyOffset))
                        .setHashCrc64ecma(res.getHeaderWithKeyIgnoreCase(TosHeader.HEADER_CRC64));
            } catch (NumberFormatException nfe) {
                throw new TosClientException("tos: server return unexpected Next-Modify-Offset header: " + nextModifyOffset, nfe);
            }
        });
    }

    private List<Integer> restoreObjectExceptedCodes() {
        List<Integer> expectedCodes = new ArrayList<>();
        expectedCodes.add(HttpStatus.OK);
        expectedCodes.add(HttpStatus.ACCEPTED);
        return expectedCodes;
    }

    private void addContentType(RequestBuilder rb, String objectKey) throws TosClientException {
        String contentType = rb.getHeaders().get(TosHeader.HEADER_CONTENT_TYPE);
        if (StringUtils.isEmpty(contentType)) {
            // request does not attach content-type and will auto recognize it default.
            // disable it by withAutoRecognizeContentType(false) in a request
            // or by clientAutoRecognizeContentType(false) while setting TosClientConfiguration
            if (this.clientAutoRecognizeContentType && rb.isAutoRecognizeContentType()) {
                // set content type before upload
                contentType = MimeType.getInstance().getMimetype(objectKey);
                rb.withHeader(TosHeader.HEADER_CONTENT_TYPE, contentType);
            }
        }
    }

    private void ensureValidBucketName(String bucket) {
        if (this.factory.isCustomDomain()) {
            // 使用自定义域名时不校验桶名
            return;
        }
        ParamsChecker.isValidBucketName(bucket);
    }

    private void ensureValidKey(String key) {
        ParamsChecker.isValidKey(key);
    }
}

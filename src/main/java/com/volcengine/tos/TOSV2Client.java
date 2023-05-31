package com.volcengine.tos;

import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.auth.SignV4;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.internal.*;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.acl.GetObjectAclOutput;
import com.volcengine.tos.model.acl.PutObjectAclInput;
import com.volcengine.tos.model.acl.PutObjectAclOutput;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;
import com.volcengine.tos.internal.Transport;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.transport.TransportConfig;

import java.io.*;
import java.time.Duration;

public class TOSV2Client implements TOSV2 {
    private TOSClientConfiguration config;
    private TosBucketRequestHandler bucketRequestHandler;
    private TosObjectRequestHandler objectRequestHandler;
    private TosFileRequestHandler fileRequestHandler;
    private TosPreSignedRequestHandler preSignedRequestHandler;
    private TosClientV1Adapter clientV1Adapter;

    private Transport transport;
    private Signer signer;
    private TosRequestFactory factory;

    protected TOSV2Client(TOSClientConfiguration conf) {
        validateAndInitConfig(conf);
        initRequestHandler();
        initV1ClientAdapter();
    }

    private void validateAndInitConfig(TOSClientConfiguration conf) {
        ParamsChecker.ensureNotNull(conf, "TOSClientConfiguration");
        ParamsChecker.ensureNotNull(conf.getRegion(), "region");
        this.config = conf;
        validateEndpoint();
    }

    private void validateEndpoint() {
        if (StringUtils.isEmpty(this.config.getEndpoint())) {
            if (TosUtils.getSupportedRegion().containsKey(this.config.getRegion())) {
                this.config.setEndpoint(TosUtils.getSupportedRegion().get(this.config.getRegion()).get(0));
            } else {
                throw new TosClientException("endpoint is null and region is invalid", null);
            }
        }
    }

    private void initRequestHandler() {
        if (this.factory == null) {
            if (this.transport == null) {
                setIsHttpByEndpoint(this.config.getEndpoint());
                this.transport = new RequestTransport(this.config.getTransportConfig());
            }
            if (this.signer == null && this.config.getCredentials() != null) {
                // 允许 signer 为空，匿名访问
                this.signer = new SignV4(this.config.getCredentials(), this.config.getRegion());
            }
            this.factory = new TosRequestFactory(this.signer, this.config.getEndpoint()).setIsCustomDomain(config.isCustomDomain());
        }
        this.bucketRequestHandler = new TosBucketRequestHandler(this.transport, this.factory);
        this.objectRequestHandler = new TosObjectRequestHandler(this.transport, this.factory)
                .setClientAutoRecognizeContentType(this.config.isClientAutoRecognizeContentType())
                .setEnableCrcCheck(this.config.isEnableCrc());
        this.fileRequestHandler = new TosFileRequestHandler(objectRequestHandler, this.transport, this.factory)
                .setEnableCrcCheck(this.config.isEnableCrc());
        this.preSignedRequestHandler = new TosPreSignedRequestHandler(this.factory, this.signer);
    }

    private void setIsHttpByEndpoint(String endpoint) {
        if (this.config == null || this.config.getTransportConfig() == null || StringUtils.isEmpty(endpoint)) {
            return;
        }
        boolean isHttp = endpoint.startsWith(Consts.SCHEME_HTTP);
        config.getTransportConfig().setHttp(isHttp);
    }

    private void initV1ClientAdapter() {
        this.clientV1Adapter = new TosClientV1Adapter(bucketRequestHandler, objectRequestHandler,
                fileRequestHandler, preSignedRequestHandler);
    }

    @Override
    public void changeCredentials(Credentials credentials) {
        this.config.setCredentials(credentials);
        if (credentials == null) {
            // 匿名访问
            this.signer = null;
        } else {
            this.signer = new SignV4(credentials, this.config.getRegion());
        }
        this.factory.setSigner(this.signer);
        cascadeUpdateRequestFactory();
    }

    private void cascadeUpdateRequestFactory() {
        this.bucketRequestHandler.setFactory(this.factory);
        this.objectRequestHandler.setFactory(this.factory);
        this.fileRequestHandler.setObjectHandler(this.objectRequestHandler);
        this.fileRequestHandler.setFactory(this.factory);
        this.preSignedRequestHandler.setSigner(this.signer);
        this.preSignedRequestHandler.setFactory(this.factory);
    }

    private void cascadeUpdateTransport() {
        this.bucketRequestHandler.setTransport(this.transport);
        this.objectRequestHandler.setTransport(this.transport);
        this.fileRequestHandler.setObjectHandler(this.objectRequestHandler);
        this.fileRequestHandler.setTransport(this.transport);
    }

    protected TOSClientConfiguration getConfig() {
        return config;
    }

    protected TosRequestFactory getFactory() {
        return factory;
    }

    @Override
    public void changeRegionAndEndpoint(String region, String endpoint) {
        this.config.setRegion(region);
        this.config.setEndpoint(endpoint);
        validateEndpoint();
        this.factory.setEndpoint(endpoint);
        cascadeUpdateRequestFactory();
    }

    @Override
    public void changeTransportConfig(TransportConfig config) {
        this.config.setTransportConfig(config);
        this.transport.switchConfig(this.config.getTransportConfig());
        cascadeUpdateTransport();
    }

    @Override
    public CreateBucketV2Output createBucket(String bucket) throws TosException {
        return createBucket(CreateBucketV2Input.builder().bucket(bucket).build());
    }

    @Override
    public CreateBucketV2Output createBucket(CreateBucketV2Input input) throws TosException {
        return bucketRequestHandler.createBucket(input);
    }

    @Override
    public HeadBucketV2Output headBucket(HeadBucketV2Input input) throws TosException {
        return bucketRequestHandler.headBucket(input);
    }

    @Override
    public DeleteBucketOutput deleteBucket(String bucket) throws TosException {
        return deleteBucket(DeleteBucketInput.builder().bucket(bucket).build());
    }

    @Override
    public DeleteBucketOutput deleteBucket(DeleteBucketInput input) throws TosException {
        return bucketRequestHandler.deleteBucket(input);
    }

    @Override
    public ListBucketsV2Output listBuckets(ListBucketsV2Input input) throws TosException {
        return bucketRequestHandler.listBuckets(input);
    }

    @Override
    public PutBucketPolicyOutput putBucketPolicy(String bucket, String policy) throws TosException {
        return putBucketPolicy(PutBucketPolicyInput.builder().bucket(bucket).policy(policy).build());
    }

    @Override
    public PutBucketPolicyOutput putBucketPolicy(PutBucketPolicyInput input) throws TosException {
        return bucketRequestHandler.putBucketPolicy(input);
    }

    @Override
    public GetBucketPolicyOutput getBucketPolicy(String bucket) throws TosException {
        return getBucketPolicy(GetBucketPolicyInput.builder().bucket(bucket).build());
    }

    @Override
    public GetBucketPolicyOutput getBucketPolicy(GetBucketPolicyInput input) throws TosException {
        return bucketRequestHandler.getBucketPolicy(input);
    }

    @Override
    public DeleteBucketPolicyOutput deleteBucketPolicy(String bucket) throws TosException {
        return deleteBucketPolicy(DeleteBucketPolicyInput.builder().bucket(bucket).build());
    }

    @Override
    public DeleteBucketPolicyOutput deleteBucketPolicy(DeleteBucketPolicyInput input) throws TosException {
        return bucketRequestHandler.deleteBucketPolicy(input);
    }

    @Override
    public PutBucketCORSOutput putBucketCORS(PutBucketCORSInput input) throws TosException {
        return bucketRequestHandler.putBucketCORS(input);
    }

    @Override
    public GetBucketCORSOutput getBucketCORS(GetBucketCORSInput input) throws TosException {
        return bucketRequestHandler.getBucketCORS(input);
    }

    @Override
    public DeleteBucketCORSOutput deleteBucketCORS(DeleteBucketCORSInput input) throws TosException {
        return bucketRequestHandler.deleteBucketCORS(input);
    }

    @Override
    public PutBucketStorageClassOutput putBucketStorageClass(PutBucketStorageClassInput input) throws TosException {
        return bucketRequestHandler.putBucketStorageClass(input);
    }

    @Override
    public GetBucketLocationOutput getBucketLocation(GetBucketLocationInput input) throws TosException {
        return bucketRequestHandler.getBucketLocation(input);
    }

    @Override
    public PutBucketLifecycleOutput putBucketLifecycle(PutBucketLifecycleInput input) throws TosException {
        return bucketRequestHandler.putBucketLifecycle(input);
    }

    @Override
    public GetBucketLifecycleOutput getBucketLifecycle(GetBucketLifecycleInput input) throws TosException {
        return bucketRequestHandler.getBucketLifecycle(input);
    }

    @Override
    public DeleteBucketLifecycleOutput deleteBucketLifecycle(DeleteBucketLifecycleInput input) throws TosException {
        return bucketRequestHandler.deleteBucketLifecycle(input);
    }

    @Override
    public PutBucketMirrorBackOutput putBucketMirrorBack(PutBucketMirrorBackInput input) throws TosException {
        return bucketRequestHandler.putBucketMirrorBack(input);
    }

    @Override
    public GetBucketMirrorBackOutput getBucketMirrorBack(GetBucketMirrorBackInput input) throws TosException {
        return bucketRequestHandler.getBucketMirrorBack(input);
    }

    @Override
    public DeleteBucketMirrorBackOutput deleteBucketMirrorBack(DeleteBucketMirrorBackInput input) throws TosException {
        return bucketRequestHandler.deleteBucketMirrorBack(input);
    }

    @Override
    public PutBucketReplicationOutput putBucketReplication(PutBucketReplicationInput input) throws TosException {
        return bucketRequestHandler.putBucketReplication(input);
    }

    @Override
    public GetBucketReplicationOutput getBucketReplication(GetBucketReplicationInput input) throws TosException {
        return bucketRequestHandler.getBucketReplication(input);
    }

    @Override
    public DeleteBucketReplicationOutput deleteBucketReplication(DeleteBucketReplicationInput input) throws TosException {
        return bucketRequestHandler.deleteBucketReplication(input);
    }

    @Override
    public PutBucketVersioningOutput putBucketVersioning(PutBucketVersioningInput input) throws TosException {
        return bucketRequestHandler.putBucketVersioning(input);
    }

    @Override
    public GetBucketVersioningOutput getBucketVersioning(GetBucketVersioningInput input) throws TosException {
        return bucketRequestHandler.getBucketVersioning(input);
    }

    @Override
    public PutBucketWebsiteOutput putBucketWebsite(PutBucketWebsiteInput input) throws TosException {
        return bucketRequestHandler.putBucketWebsite(input);
    }

    @Override
    public GetBucketWebsiteOutput getBucketWebsite(GetBucketWebsiteInput input) throws TosException {
        return bucketRequestHandler.getBucketWebsite(input);
    }

    @Override
    public DeleteBucketWebsiteOutput deleteBucketWebsite(DeleteBucketWebsiteInput input) throws TosException {
        return bucketRequestHandler.deleteBucketWebsite(input);
    }

    @Override
    public PutBucketNotificationOutput putBucketNotification(PutBucketNotificationInput input) throws TosException {
        return bucketRequestHandler.putBucketNotification(input);
    }

    @Override
    public GetBucketNotificationOutput getBucketNotification(GetBucketNotificationInput input) throws TosException {
        return bucketRequestHandler.getBucketNotification(input);
    }

    @Override
    public PutBucketCustomDomainOutput putBucketCustomDomain(PutBucketCustomDomainInput input) throws TosException {
        return bucketRequestHandler.putBucketCustomDomain(input);
    }

    @Override
    public ListBucketCustomDomainOutput listBucketCustomDomain(ListBucketCustomDomainInput input) throws TosException {
        return bucketRequestHandler.listBucketCustomDomain(input);
    }

    @Override
    public DeleteBucketCustomDomainOutput deleteBucketCustomDomain(DeleteBucketCustomDomainInput input) throws TosException {
        return bucketRequestHandler.deleteBucketCustomDomain(input);
    }

    @Override
    public PutBucketRealTimeLogOutput putBucketRealTimeLog(PutBucketRealTimeLogInput input) throws TosException {
        return bucketRequestHandler.putBucketRealTimeLog(input);
    }

    @Override
    public GetBucketRealTimeLogOutput getBucketRealTimeLog(GetBucketRealTimeLogInput input) throws TosException {
        return bucketRequestHandler.getBucketRealTimeLog(input);
    }

    @Override
    public DeleteBucketRealTimeLogOutput deleteBucketRealTimeLog(DeleteBucketRealTimeLogInput input) throws TosException {
        return bucketRequestHandler.deleteBucketRealTimeLog(input);
    }

    @Override
    public PutBucketACLOutput putBucketACL(PutBucketACLInput input) throws TosException {
        return bucketRequestHandler.putBucketACL(input);
    }

    @Override
    public GetBucketACLOutput getBucketACL(GetBucketACLInput input) throws TosException {
        return bucketRequestHandler.getBucketACL(input);
    }

    @Override
    public PutBucketRenameOutput putBucketRename(PutBucketRenameInput input) throws TosException {
        return bucketRequestHandler.putBucketRename(input);
    }

    @Override
    public GetBucketRenameOutput getBucketRename(GetBucketRenameInput input) throws TosException {
        return bucketRequestHandler.getBucketRename(input);
    }

    @Override
    public DeleteBucketRenameOutput deleteBucketRename(DeleteBucketRenameInput input) throws TosException {
        return bucketRequestHandler.deleteBucketRename(input);
    }

    @Override
    public GetObjectV2Output getObject(GetObjectV2Input input) throws TosException {
        return objectRequestHandler.getObject(input);
    }

    @Override
    public GetObjectToFileOutput getObjectToFile(GetObjectToFileInput input) throws TosException {
        return fileRequestHandler.getObjectToFile(input);
    }

    @Override
    public UploadFileV2Output uploadFile(UploadFileV2Input input) throws TosException {
        return fileRequestHandler.uploadFile(input);
    }

    @Override
    public DownloadFileOutput downloadFile(DownloadFileInput input) throws TosException {
        return fileRequestHandler.downloadFile(input);
    }

    @Override
    public ResumableCopyObjectOutput resumableCopyObject(ResumableCopyObjectInput input) throws TosException {
        return fileRequestHandler.resumableCopyObject(input);
    }

    @Override
    public HeadObjectV2Output headObject(HeadObjectV2Input input) throws TosException {
        return objectRequestHandler.headObject(input);
    }

    @Override
    public DeleteObjectOutput deleteObject(DeleteObjectInput input) throws TosException {
        return objectRequestHandler.deleteObject(input);
    }

    @Override
    public DeleteMultiObjectsV2Output deleteMultiObjects(DeleteMultiObjectsV2Input input) throws TosException {
        return objectRequestHandler.deleteMultiObjects(input);
    }

    @Override
    public PutObjectOutput putObject(PutObjectInput input) throws TosException {
        return objectRequestHandler.putObject(input);
    }

    @Override
    public PutObjectFromFileOutput putObjectFromFile(PutObjectFromFileInput input) throws TosException {
        return fileRequestHandler.putObjectFromFile(input);
    }

    @Override
    public AppendObjectOutput appendObject(AppendObjectInput input) throws TosException {
        return objectRequestHandler.appendObject(input);
    }

    @Override
    public SetObjectMetaOutput setObjectMeta(SetObjectMetaInput input) throws TosException {
        return objectRequestHandler.setObjectMeta(input);
    }

    @Override
    public ListObjectsV2Output listObjects(ListObjectsV2Input input) throws TosException {
        return objectRequestHandler.listObjects(input);
    }

    @Override
    public ListObjectsType2Output listObjectsType2(ListObjectsType2Input input) throws TosException {
        ParamsChecker.ensureNotNull(input, "ListObjectsType2Input");
        if (input.isListOnlyOnce()) {
            return objectRequestHandler.listObjectsType2(input);
        }
        return objectRequestHandler.listObjectsType2UntilFinished(input);
    }

    @Override
    public ListObjectVersionsV2Output listObjectVersions(ListObjectVersionsV2Input input) throws TosException {
        return objectRequestHandler.listObjectVersions(input);
    }

    @Override
    public CopyObjectV2Output copyObject(CopyObjectV2Input input) throws TosException {
        return objectRequestHandler.copyObject(input);
    }

    @Override
    public UploadPartCopyV2Output uploadPartCopy(UploadPartCopyV2Input input) throws TosException {
        return objectRequestHandler.uploadPartCopy(input);
    }

    @Override
    public PutObjectACLOutput putObjectAcl(PutObjectACLInput input) throws TosException {
        return objectRequestHandler.putObjectAcl(input);
    }

    @Override
    public GetObjectACLV2Output getObjectAcl(GetObjectACLV2Input input) throws TosException {
        return objectRequestHandler.getObjectAcl(input);
    }

    @Override
    public PutObjectTaggingOutput putObjectTagging(PutObjectTaggingInput input) throws TosException {
        return objectRequestHandler.putObjectTagging(input);
    }

    @Override
    public GetObjectTaggingOutput getObjectTagging(GetObjectTaggingInput input) throws TosException {
        return objectRequestHandler.getObjectTagging(input);
    }

    @Override
    public DeleteObjectTaggingOutput deleteObjectTagging(DeleteObjectTaggingInput input) throws TosException {
        return objectRequestHandler.deleteObjectTagging(input);
    }

    @Override
    public FetchObjectOutput fetchObject(FetchObjectInput input) throws TosException {
        return objectRequestHandler.fetchObject(input);
    }

    @Override
    public PutFetchTaskOutput putFetchTask(PutFetchTaskInput input) throws TosException {
        return objectRequestHandler.putFetchTask(input);
    }

    @Override
    public CreateMultipartUploadOutput createMultipartUpload(CreateMultipartUploadInput input) throws TosException {
        return objectRequestHandler.createMultipartUpload(input);
    }

    @Override
    public UploadPartV2Output uploadPart(UploadPartV2Input input) throws TosException {
        return objectRequestHandler.uploadPart(input);
    }

    @Override
    public UploadPartFromFileOutput uploadPartFromFile(UploadPartFromFileInput input) throws TosException {
        return fileRequestHandler.uploadPartFromFile(input);
    }

    @Override
    public CompleteMultipartUploadV2Output completeMultipartUpload(CompleteMultipartUploadV2Input input) throws TosException {
        return objectRequestHandler.completeMultipartUpload(input);
    }

    @Override
    public AbortMultipartUploadOutput abortMultipartUpload(AbortMultipartUploadInput input) throws TosException {
        return objectRequestHandler.abortMultipartUpload(input);
    }

    @Override
    public ListPartsOutput listParts(ListPartsInput input) throws TosException {
        return objectRequestHandler.listParts(input);
    }

    @Override
    public ListMultipartUploadsV2Output listMultipartUploads(ListMultipartUploadsV2Input input) throws TosException {
        return objectRequestHandler.listMultipartUploads(input);
    }

    @Override
    public RenameObjectOutput renameObject(RenameObjectInput input) throws TosException {
        return objectRequestHandler.renameObject(input);
    }

    @Override
    public RestoreObjectOutput restoreObject(RestoreObjectInput input) throws TosException {
        return objectRequestHandler.restoreObject(input);
    }

    @Override
    public PreSignedURLOutput preSignedURL(PreSignedURLInput input) throws TosException {
        return preSignedRequestHandler.preSignedURL(input);
    }

    @Override
    public PreSignedPostSignatureOutput preSignedPostSignature(PreSignedPostSignatureInput input) throws TosException {
        return preSignedRequestHandler.preSignedPostSignature(input);
    }

    @Deprecated
    @Override
    public PreSingedPolicyURLOutput preSingedPolicyURL(PreSingedPolicyURLInput input) throws TosException {
        ParamsChecker.ensureNotNull(input, "PreSingedPolicyURLInput");
        PreSignedPolicyURLOutput out = preSignedRequestHandler.preSignedPolicyURL(new PreSignedPolicyURLInput()
                .setBucket(input.getBucket()).setExpires(input.getExpires()).setConditions(input.getConditions())
                .setAlternativeEndpoint(input.getAlternativeEndpoint()).setCustomDomain(input.isCustomDomain()));
        return new PreSingedPolicyURLOutput().setPreSignedPolicyURLGenerator(out.getPreSignedPolicyURLGenerator())
                .setSignatureQuery(out.getSignatureQuery()).setHost(out.getHost()).setScheme(out.getScheme());
    }

    @Override
    public PreSignedPolicyURLOutput preSignedPolicyURL(PreSignedPolicyURLInput input) throws TosException {
        return preSignedRequestHandler.preSignedPolicyURL(input);
    }

    @Override
    public CreateBucketOutput createBucket(CreateBucketInput input) throws TosException {
        return clientV1Adapter.createBucket(input);
    }

    @Override
    public HeadBucketOutput headBucket(String bucket) throws TosException {
        return clientV1Adapter.headBucket(bucket);
    }

    @Override
    public ListBucketsOutput listBuckets(ListBucketsInput input) throws TosException {
        return clientV1Adapter.listBuckets(input);
    }

    @Override
    public GetObjectOutput getObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.getObject(bucket, objectKey, builders);
    }

    @Override
    public HeadObjectOutput headObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.headObject(bucket, objectKey, builders);
    }

    @Override
    public DeleteObjectOutput deleteObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.deleteObject(bucket, objectKey, builders);
    }

    @Override
    public DeleteMultiObjectsOutput deleteMultiObjects(String bucket, DeleteMultiObjectsInput input, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.deleteMultiObjects(bucket, input, builders);
    }

    @Override
    public PutObjectOutput putObject(String bucket, String objectKey, InputStream inputStream, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.putObject(bucket, objectKey, inputStream, builders);
    }

    @Override
    public UploadFileOutput uploadFile(String bucket, UploadFileInput input, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.uploadFile(bucket, input, builders);
    }

    @Override
    public AppendObjectOutput appendObject(String bucket, String objectKey, InputStream content, long offset, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.appendObject(bucket, objectKey, content, offset, builders);
    }

    @Override
    public SetObjectMetaOutput setObjectMeta(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.setObjectMeta(bucket, objectKey, builders);
    }

    @Override
    public ListObjectsOutput listObjects(String bucket, ListObjectsInput input) throws TosException {
        return clientV1Adapter.listObjects(bucket, input);
    }

    @Override
    public ListObjectVersionsOutput listObjectVersions(String bucket, ListObjectVersionsInput input) throws TosException {
        return clientV1Adapter.listObjectVersions(bucket, input);
    }

    @Override
    public CopyObjectOutput copyObject(String bucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.copyObject(bucket, srcObjectKey, dstObjectKey, builders);
    }

    @Override
    public CopyObjectOutput copyObjectTo(String bucket, String dstBucket, String dstObjectKey, String srcObjectKey, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.copyObjectTo(bucket, dstBucket, dstObjectKey, srcObjectKey, builders);
    }

    @Override
    public CopyObjectOutput copyObjectFrom(String bucket, String srcBucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.copyObjectFrom(bucket, srcBucket, srcObjectKey, dstObjectKey, builders);
    }

    @Override
    public UploadPartCopyOutput uploadPartCopy(String bucket, UploadPartCopyInput input, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.uploadPartCopy(bucket, input, builders);
    }

    @Override
    public PutObjectAclOutput putObjectAcl(String bucket, PutObjectAclInput input) throws TosException {
        return clientV1Adapter.putObjectAcl(bucket, input);
    }

    @Override
    public GetObjectAclOutput getObjectAcl(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.getObjectAcl(bucket, objectKey, builders);
    }

    @Override
    public CreateMultipartUploadOutput createMultipartUpload(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.createMultipartUpload(bucket, objectKey, builders);
    }

    @Override
    public UploadPartOutput uploadPart(String bucket, UploadPartInput input, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.uploadPart(bucket, input, builders);
    }

    @Override
    public CompleteMultipartUploadOutput completeMultipartUpload(String bucket, CompleteMultipartUploadInput input) throws TosException {
        return clientV1Adapter.completeMultipartUpload(bucket, input);
    }

    @Override
    public AbortMultipartUploadOutput abortMultipartUpload(String bucket, AbortMultipartUploadInput input) throws TosException {
        return clientV1Adapter.abortMultipartUpload(bucket, input);
    }

    @Override
    public ListUploadedPartsOutput listUploadedParts(String bucket, ListUploadedPartsInput input, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.listUploadedParts(bucket, input, builders);
    }

    @Override
    public ListMultipartUploadsOutput listMultipartUploads(String bucket, ListMultipartUploadsInput input) throws TosException {
        return clientV1Adapter.listMultipartUploads(bucket, input);
    }

    @Override
    public String preSignedURL(String httpMethod, String bucket, String objectKey, Duration ttl, RequestOptionsBuilder... builders) throws TosException {
        return clientV1Adapter.preSignedURL(httpMethod, bucket, objectKey, ttl, builders);
    }
}

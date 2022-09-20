package com.volcengine.tos;

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
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.time.Duration;

public class TOSV2Client implements TOSV2 {
    private static final Logger LOG = LoggerFactory.getLogger(TOSV2Client.class);

    private TOSClient client;
    private TOSClientConfiguration config;
    private TosBucketRequestHandler bucketRequestHandler;
    private TosObjectRequestHandler objectRequestHandler;
    private TosFileRequestHandler fileRequestHandler;

    private Transport transport;
    private Signer signer;
    private TosRequestFactory factory;

    protected TOSV2Client(TOSClientConfiguration conf) {
        validateAndInitConfig(conf);
        initRequestHandler();
        initV1Client();
    }

    private void validateAndInitConfig(TOSClientConfiguration conf) {
        ParamsChecker.isValidInput(conf, "TOSClientConfiguration");
        ParamsChecker.isValidInput(conf.getRegion(), "region");
        ParamsChecker.isValidInput(conf.getCredentials(), "credentials");
        this.config = conf;
        if (StringUtils.isEmpty(this.config.getEndpoint())) {
            if (TosUtils.getSupportedRegion().containsKey(this.config.getRegion())) {
                this.config.setEndpoint(TosUtils.getSupportedRegion().get(this.config.getRegion()).get(0));
            } else {
                throw new IllegalArgumentException("endpoint is null and region is invalid");
            }
        }
    }

    private void initRequestHandler() {
        if (this.factory == null) {
            if (this.transport == null) {
                this.transport = new RequestTransport(this.config.getTransportConfig());
            }
            if (this.signer == null) {
                this.signer = new SignV4(this.config.getCredentials(), this.config.getRegion());
            }
            this.factory = new TosRequestFactoryImpl(this.signer, this.config.getEndpoint());
        }
        this.bucketRequestHandler = new TosBucketRequestHandlerImpl(this.transport, this.factory);
        this.objectRequestHandler = new TosObjectRequestHandlerImpl(this.transport, this.factory)
                .setClientAutoRecognizeContentType(this.config.isClientAutoRecognizeContentType());
        this.fileRequestHandler = new TosFileRequestHandlerImpl(objectRequestHandler, this.transport, this.factory);
    }

    private void initV1Client() {
        this.client = new TOSClient(this.config.getEndpoint(),
                ClientOptions.withRegion(this.config.getRegion()),
                ClientOptions.withCredentials(this.config.getCredentials()),
                ClientOptions.withTransport(this.transport),
                ClientOptions.withSigner(this.signer));
    }

    @Override
    public CreateBucketV2Output createBucket(String bucket) throws TosException {
        return this.createBucket(CreateBucketV2Input.builder().bucket(bucket).build());
    }

    @Override
    public CreateBucketV2Output createBucket(CreateBucketV2Input input) throws TosException {
        return this.bucketRequestHandler.createBucket(input);
    }

    @Override
    public HeadBucketV2Output headBucket(HeadBucketV2Input input) throws TosException {
        return this.bucketRequestHandler.headBucket(input);
    }

    @Override
    public DeleteBucketOutput deleteBucket(String bucket) throws TosException {
        return this.deleteBucket(DeleteBucketInput.builder().bucket(bucket).build());
    }

    @Override
    public DeleteBucketOutput deleteBucket(DeleteBucketInput input) throws TosException {
        return this.bucketRequestHandler.deleteBucket(input);
    }

    @Override
    public ListBucketsV2Output listBuckets(ListBucketsV2Input input) throws TosException {
        return this.bucketRequestHandler.listBuckets(input);
    }

    @Override
    public PutBucketPolicyOutput putBucketPolicy(String bucket, String policy) throws TosException {
        return this.putBucketPolicy(PutBucketPolicyInput.builder().bucket(bucket).policy(policy).build());
    }

    @Override
    public PutBucketPolicyOutput putBucketPolicy(PutBucketPolicyInput input) throws TosException {
        return this.bucketRequestHandler.putBucketPolicy(input);
    }

    @Override
    public GetBucketPolicyOutput getBucketPolicy(String bucket) throws TosException {
        return this.getBucketPolicy(GetBucketPolicyInput.builder().bucket(bucket).build());
    }

    @Override
    public GetBucketPolicyOutput getBucketPolicy(GetBucketPolicyInput input) throws TosException {
        return this.bucketRequestHandler.getBucketPolicy(input);
    }

    @Override
    public DeleteBucketPolicyOutput deleteBucketPolicy(String bucket) throws TosException {
        return this.deleteBucketPolicy(DeleteBucketPolicyInput.builder().bucket(bucket).build());
    }

    @Override
    public DeleteBucketPolicyOutput deleteBucketPolicy(DeleteBucketPolicyInput input) throws TosException {
        return this.bucketRequestHandler.deleteBucketPolicy(input);
    }

    @Override
    public GetObjectV2Output getObject(GetObjectV2Input input) throws TosException {
        return objectRequestHandler.getObject(input);
    }

    @Override
    public GetObjectToFileOutput getObjectToFile(GetObjectToFileInput input) throws TosException {
        return fileRequestHandler.getObjectToFile(input);
    }

//    @Override
//    public UploadFileOutput uploadFile(String bucket, UploadFileInput input) throws TosException {
//        return null;
//    }

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
    public PreSignedURLOutput preSignedURL(PreSignedURLInput input) throws TosException {
        ParamsChecker.isValidInput(input, "PreSignedURLInput");
        ParamsChecker.isValidBucketNameAndKey(input.getBucket(), input.getKey());
        RequestBuilder builder = this.factory.init(input.getBucket(), "", input.getHeader());
        if (input.getQuery() != null) {
            input.getQuery().forEach(builder::withQuery);
        }
        // todo where is header?
        return new PreSignedURLOutput(builder.preSignedURL(input.getHttpMethod().toString(),
                Duration.ofSeconds(input.getExpires())), null);
    }

    @Override
    public CreateBucketOutput createBucket(CreateBucketInput input) throws TosException {
        return this.client.createBucket(input);
    }

    @Override
    public HeadBucketOutput headBucket(String bucket) throws TosException {
        return this.client.headBucket(bucket);
    }

    @Override
    public ListBucketsOutput listBuckets(ListBucketsInput input) throws TosException {
        return this.client.listBuckets(input);
    }

    @Override
    public GetObjectOutput getObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return this.client.getObject(bucket, objectKey, builders);
    }

    @Override
    public HeadObjectOutput headObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return this.client.headObject(bucket, objectKey, builders);
    }

    @Override
    public DeleteObjectOutput deleteObject(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return this.client.deleteObject(bucket, objectKey, builders);
    }

    @Override
    public DeleteMultiObjectsOutput deleteMultiObjects(String bucket, DeleteMultiObjectsInput input, RequestOptionsBuilder... builders) throws TosException {
        return this.client.deleteMultiObjects(bucket, input, builders);
    }

    @Override
    public PutObjectOutput putObject(String bucket, String objectKey, InputStream inputStream, RequestOptionsBuilder... builders) throws TosException {
        return this.client.putObject(bucket, objectKey, inputStream, builders);
    }

    @Override
    public UploadFileOutput uploadFile(String bucket, UploadFileInput input, RequestOptionsBuilder... builders) throws TosException {
        return this.client.uploadFile(bucket, input, builders);
    }

    @Override
    public AppendObjectOutput appendObject(String bucket, String objectKey, InputStream content, long offset, RequestOptionsBuilder... builders) throws TosException {
        return this.client.appendObject(bucket, objectKey, content, offset, builders);
    }

    @Override
    public SetObjectMetaOutput setObjectMeta(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return this.client.setObjectMeta(bucket, objectKey, builders);
    }

    @Override
    public ListObjectsOutput listObjects(String bucket, ListObjectsInput input) throws TosException {
        return this.client.listObjects(bucket, input);
    }

    @Override
    public ListObjectVersionsOutput listObjectVersions(String bucket, ListObjectVersionsInput input) throws TosException {
        return this.client.listObjectVersions(bucket, input);
    }

    @Override
    public CopyObjectOutput copyObject(String bucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        return this.client.copyObject(bucket, srcObjectKey, dstObjectKey, builders);
    }

    @Override
    public CopyObjectOutput copyObjectTo(String bucket, String dstBucket, String dstObjectKey, String srcObjectKey, RequestOptionsBuilder... builders) throws TosException {
        return this.client.copyObjectTo(bucket, dstBucket, dstObjectKey, srcObjectKey, builders);
    }

    @Override
    public CopyObjectOutput copyObjectFrom(String bucket, String srcBucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder... builders) throws TosException {
        return this.client.copyObjectFrom(bucket, srcBucket, srcObjectKey, dstObjectKey, builders);
    }

    @Override
    public UploadPartCopyOutput uploadPartCopy(String bucket, UploadPartCopyInput input, RequestOptionsBuilder... builders) throws TosException {
        return this.client.uploadPartCopy(bucket, input, builders);
    }

    @Override
    public PutObjectAclOutput putObjectAcl(String bucket, PutObjectAclInput input) throws TosException {
        return this.client.putObjectAcl(bucket, input);
    }

    @Override
    public GetObjectAclOutput getObjectAcl(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return this.client.getObjectAcl(bucket, objectKey, builders);
    }

    @Override
    public CreateMultipartUploadOutput createMultipartUpload(String bucket, String objectKey, RequestOptionsBuilder... builders) throws TosException {
        return this.client.createMultipartUpload(bucket, objectKey, builders);
    }

    @Override
    public UploadPartOutput uploadPart(String bucket, UploadPartInput input, RequestOptionsBuilder... builders) throws TosException {
        return this.client.uploadPart(bucket, input, builders);
    }

    @Override
    public CompleteMultipartUploadOutput completeMultipartUpload(String bucket, CompleteMultipartUploadInput input) throws TosException {
        return this.client.completeMultipartUpload(bucket, input);
    }

    @Override
    public AbortMultipartUploadOutput abortMultipartUpload(String bucket, AbortMultipartUploadInput input) throws TosException {
        return this.client.abortMultipartUpload(bucket, input);
    }

    @Override
    public ListUploadedPartsOutput listUploadedParts(String bucket, ListUploadedPartsInput input, RequestOptionsBuilder... builders) throws TosException {
        return this.client.listUploadedParts(bucket, input, builders);
    }

    @Override
    public ListMultipartUploadsOutput listMultipartUploads(String bucket, ListMultipartUploadsInput input) throws TosException {
        return this.client.listMultipartUploads(bucket, input);
    }

    @Override
    public String preSignedURL(String httpMethod, String bucket, String objectKey, Duration ttl, RequestOptionsBuilder... builders) throws TosException {
        return this.client.preSignedURL(httpMethod, bucket, objectKey, ttl, builders);
    }
}

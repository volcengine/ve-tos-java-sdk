package com.volcengine.tos;

import java.io.Closeable;

import com.volcengine.tos.auth.Credentials;
import com.volcengine.tos.model.bucket.CreateBucketV2Input;
import com.volcengine.tos.model.bucket.CreateBucketV2Output;
import com.volcengine.tos.model.bucket.DeleteBucketCORSInput;
import com.volcengine.tos.model.bucket.DeleteBucketCORSOutput;
import com.volcengine.tos.model.bucket.DeleteBucketCustomDomainInput;
import com.volcengine.tos.model.bucket.DeleteBucketCustomDomainOutput;
import com.volcengine.tos.model.bucket.DeleteBucketEncryptionInput;
import com.volcengine.tos.model.bucket.DeleteBucketEncryptionOutput;
import com.volcengine.tos.model.bucket.DeleteBucketInput;
import com.volcengine.tos.model.bucket.DeleteBucketInventoryInput;
import com.volcengine.tos.model.bucket.DeleteBucketInventoryOutput;
import com.volcengine.tos.model.bucket.DeleteBucketLifecycleInput;
import com.volcengine.tos.model.bucket.DeleteBucketLifecycleOutput;
import com.volcengine.tos.model.bucket.DeleteBucketMirrorBackInput;
import com.volcengine.tos.model.bucket.DeleteBucketMirrorBackOutput;
import com.volcengine.tos.model.bucket.DeleteBucketOutput;
import com.volcengine.tos.model.bucket.DeleteBucketPolicyInput;
import com.volcengine.tos.model.bucket.DeleteBucketPolicyOutput;
import com.volcengine.tos.model.bucket.DeleteBucketRealTimeLogInput;
import com.volcengine.tos.model.bucket.DeleteBucketRealTimeLogOutput;
import com.volcengine.tos.model.bucket.DeleteBucketRenameInput;
import com.volcengine.tos.model.bucket.DeleteBucketRenameOutput;
import com.volcengine.tos.model.bucket.DeleteBucketReplicationInput;
import com.volcengine.tos.model.bucket.DeleteBucketReplicationOutput;
import com.volcengine.tos.model.bucket.DeleteBucketTaggingInput;
import com.volcengine.tos.model.bucket.DeleteBucketTaggingOutput;
import com.volcengine.tos.model.bucket.DeleteBucketWebsiteInput;
import com.volcengine.tos.model.bucket.DeleteBucketWebsiteOutput;
import com.volcengine.tos.model.bucket.GetBucketACLInput;
import com.volcengine.tos.model.bucket.GetBucketACLOutput;
import com.volcengine.tos.model.bucket.GetBucketCORSInput;
import com.volcengine.tos.model.bucket.GetBucketCORSOutput;
import com.volcengine.tos.model.bucket.GetBucketEncryptionInput;
import com.volcengine.tos.model.bucket.GetBucketEncryptionOutput;
import com.volcengine.tos.model.bucket.GetBucketInventoryInput;
import com.volcengine.tos.model.bucket.GetBucketInventoryOutput;
import com.volcengine.tos.model.bucket.GetBucketLifecycleInput;
import com.volcengine.tos.model.bucket.GetBucketLifecycleOutput;
import com.volcengine.tos.model.bucket.GetBucketLocationInput;
import com.volcengine.tos.model.bucket.GetBucketLocationOutput;
import com.volcengine.tos.model.bucket.GetBucketMirrorBackInput;
import com.volcengine.tos.model.bucket.GetBucketMirrorBackOutput;
import com.volcengine.tos.model.bucket.GetBucketNotificationInput;
import com.volcengine.tos.model.bucket.GetBucketNotificationOutput;
import com.volcengine.tos.model.bucket.GetBucketNotificationType2Input;
import com.volcengine.tos.model.bucket.GetBucketNotificationType2Output;
import com.volcengine.tos.model.bucket.GetBucketPolicyInput;
import com.volcengine.tos.model.bucket.GetBucketPolicyOutput;
import com.volcengine.tos.model.bucket.GetBucketRealTimeLogInput;
import com.volcengine.tos.model.bucket.GetBucketRealTimeLogOutput;
import com.volcengine.tos.model.bucket.GetBucketRenameInput;
import com.volcengine.tos.model.bucket.GetBucketRenameOutput;
import com.volcengine.tos.model.bucket.GetBucketReplicationInput;
import com.volcengine.tos.model.bucket.GetBucketReplicationOutput;
import com.volcengine.tos.model.bucket.GetBucketTaggingInput;
import com.volcengine.tos.model.bucket.GetBucketTaggingOutput;
import com.volcengine.tos.model.bucket.GetBucketVersioningInput;
import com.volcengine.tos.model.bucket.GetBucketVersioningOutput;
import com.volcengine.tos.model.bucket.GetBucketWebsiteInput;
import com.volcengine.tos.model.bucket.GetBucketWebsiteOutput;
import com.volcengine.tos.model.bucket.HeadBucketOutput;
import com.volcengine.tos.model.bucket.HeadBucketV2Input;
import com.volcengine.tos.model.bucket.HeadBucketV2Output;
import com.volcengine.tos.model.bucket.ListBucketCustomDomainInput;
import com.volcengine.tos.model.bucket.ListBucketCustomDomainOutput;
import com.volcengine.tos.model.bucket.ListBucketInventoryInput;
import com.volcengine.tos.model.bucket.ListBucketInventoryOutput;
import com.volcengine.tos.model.bucket.ListBucketsV2Input;
import com.volcengine.tos.model.bucket.ListBucketsV2Output;
import com.volcengine.tos.model.bucket.PutBucketACLInput;
import com.volcengine.tos.model.bucket.PutBucketACLOutput;
import com.volcengine.tos.model.bucket.PutBucketCORSInput;
import com.volcengine.tos.model.bucket.PutBucketCORSOutput;
import com.volcengine.tos.model.bucket.PutBucketCustomDomainInput;
import com.volcengine.tos.model.bucket.PutBucketCustomDomainOutput;
import com.volcengine.tos.model.bucket.PutBucketEncryptionInput;
import com.volcengine.tos.model.bucket.PutBucketEncryptionOutput;
import com.volcengine.tos.model.bucket.PutBucketInventoryInput;
import com.volcengine.tos.model.bucket.PutBucketInventoryOutput;
import com.volcengine.tos.model.bucket.PutBucketLifecycleInput;
import com.volcengine.tos.model.bucket.PutBucketLifecycleOutput;
import com.volcengine.tos.model.bucket.PutBucketMirrorBackInput;
import com.volcengine.tos.model.bucket.PutBucketMirrorBackOutput;
import com.volcengine.tos.model.bucket.PutBucketNotificationInput;
import com.volcengine.tos.model.bucket.PutBucketNotificationOutput;
import com.volcengine.tos.model.bucket.PutBucketNotificationType2Input;
import com.volcengine.tos.model.bucket.PutBucketNotificationType2Output;
import com.volcengine.tos.model.bucket.PutBucketPolicyInput;
import com.volcengine.tos.model.bucket.PutBucketPolicyOutput;
import com.volcengine.tos.model.bucket.PutBucketRealTimeLogInput;
import com.volcengine.tos.model.bucket.PutBucketRealTimeLogOutput;
import com.volcengine.tos.model.bucket.PutBucketRenameInput;
import com.volcengine.tos.model.bucket.PutBucketRenameOutput;
import com.volcengine.tos.model.bucket.PutBucketReplicationInput;
import com.volcengine.tos.model.bucket.PutBucketReplicationOutput;
import com.volcengine.tos.model.bucket.PutBucketStorageClassInput;
import com.volcengine.tos.model.bucket.PutBucketStorageClassOutput;
import com.volcengine.tos.model.bucket.PutBucketTaggingInput;
import com.volcengine.tos.model.bucket.PutBucketTaggingOutput;
import com.volcengine.tos.model.bucket.PutBucketVersioningInput;
import com.volcengine.tos.model.bucket.PutBucketVersioningOutput;
import com.volcengine.tos.model.bucket.PutBucketWebsiteInput;
import com.volcengine.tos.model.bucket.PutBucketWebsiteOutput;
import com.volcengine.tos.model.object.AbortMultipartUploadInput;
import com.volcengine.tos.model.object.AbortMultipartUploadOutput;
import com.volcengine.tos.model.object.AppendObjectInput;
import com.volcengine.tos.model.object.AppendObjectOutput;
import com.volcengine.tos.model.object.CompleteMultipartUploadV2Input;
import com.volcengine.tos.model.object.CompleteMultipartUploadV2Output;
import com.volcengine.tos.model.object.CopyObjectV2Input;
import com.volcengine.tos.model.object.CopyObjectV2Output;
import com.volcengine.tos.model.object.CreateMultipartUploadInput;
import com.volcengine.tos.model.object.CreateMultipartUploadOutput;
import com.volcengine.tos.model.object.DeleteMultiObjectsV2Input;
import com.volcengine.tos.model.object.DeleteMultiObjectsV2Output;
import com.volcengine.tos.model.object.DeleteObjectInput;
import com.volcengine.tos.model.object.DeleteObjectOutput;
import com.volcengine.tos.model.object.DeleteObjectTaggingInput;
import com.volcengine.tos.model.object.DeleteObjectTaggingOutput;
import com.volcengine.tos.model.object.DownloadFileInput;
import com.volcengine.tos.model.object.DownloadFileOutput;
import com.volcengine.tos.model.object.FetchObjectInput;
import com.volcengine.tos.model.object.FetchObjectOutput;
import com.volcengine.tos.model.object.GetFetchTaskInput;
import com.volcengine.tos.model.object.GetFetchTaskOutput;
import com.volcengine.tos.model.object.GetFileStatusInput;
import com.volcengine.tos.model.object.GetFileStatusOutput;
import com.volcengine.tos.model.object.GetObjectACLV2Input;
import com.volcengine.tos.model.object.GetObjectACLV2Output;
import com.volcengine.tos.model.object.GetObjectTaggingInput;
import com.volcengine.tos.model.object.GetObjectTaggingOutput;
import com.volcengine.tos.model.object.GetObjectToFileInput;
import com.volcengine.tos.model.object.GetObjectToFileOutput;
import com.volcengine.tos.model.object.GetObjectV2Input;
import com.volcengine.tos.model.object.GetObjectV2Output;
import com.volcengine.tos.model.object.GetSymlinkInput;
import com.volcengine.tos.model.object.GetSymlinkOutput;
import com.volcengine.tos.model.object.HeadObjectV2Input;
import com.volcengine.tos.model.object.HeadObjectV2Output;
import com.volcengine.tos.model.object.ListMultipartUploadsV2Input;
import com.volcengine.tos.model.object.ListMultipartUploadsV2Output;
import com.volcengine.tos.model.object.ListObjectVersionsV2Input;
import com.volcengine.tos.model.object.ListObjectVersionsV2Output;
import com.volcengine.tos.model.object.ListObjectsType2Input;
import com.volcengine.tos.model.object.ListObjectsType2Output;
import com.volcengine.tos.model.object.ListObjectsV2Input;
import com.volcengine.tos.model.object.ListObjectsV2Output;
import com.volcengine.tos.model.object.ListPartsInput;
import com.volcengine.tos.model.object.ListPartsOutput;
import com.volcengine.tos.model.object.PreSignedPolicyURLInput;
import com.volcengine.tos.model.object.PreSignedPolicyURLOutput;
import com.volcengine.tos.model.object.PreSignedPostSignatureInput;
import com.volcengine.tos.model.object.PreSignedPostSignatureOutput;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;
import com.volcengine.tos.model.object.PreSingedPolicyURLInput;
import com.volcengine.tos.model.object.PreSingedPolicyURLOutput;
import com.volcengine.tos.model.object.PutFetchTaskInput;
import com.volcengine.tos.model.object.PutFetchTaskOutput;
import com.volcengine.tos.model.object.PutObjectACLInput;
import com.volcengine.tos.model.object.PutObjectACLOutput;
import com.volcengine.tos.model.object.PutObjectFromFileInput;
import com.volcengine.tos.model.object.PutObjectFromFileOutput;
import com.volcengine.tos.model.object.PutObjectInput;
import com.volcengine.tos.model.object.PutObjectOutput;
import com.volcengine.tos.model.object.PutObjectTaggingInput;
import com.volcengine.tos.model.object.PutObjectTaggingOutput;
import com.volcengine.tos.model.object.PutSymlinkInput;
import com.volcengine.tos.model.object.PutSymlinkOutput;
import com.volcengine.tos.model.object.RenameObjectInput;
import com.volcengine.tos.model.object.RenameObjectOutput;
import com.volcengine.tos.model.object.RestoreObjectInput;
import com.volcengine.tos.model.object.RestoreObjectOutput;
import com.volcengine.tos.model.object.ResumableCopyObjectInput;
import com.volcengine.tos.model.object.ResumableCopyObjectOutput;
import com.volcengine.tos.model.object.SetObjectMetaInput;
import com.volcengine.tos.model.object.SetObjectMetaOutput;
import com.volcengine.tos.model.object.SetObjectTimeInput;
import com.volcengine.tos.model.object.SetObjectTimeOutput;
import com.volcengine.tos.model.object.UploadFileV2Input;
import com.volcengine.tos.model.object.UploadFileV2Output;
import com.volcengine.tos.model.object.UploadPartCopyV2Input;
import com.volcengine.tos.model.object.UploadPartCopyV2Output;
import com.volcengine.tos.model.object.UploadPartFromFileInput;
import com.volcengine.tos.model.object.UploadPartFromFileOutput;
import com.volcengine.tos.model.object.UploadPartV2Input;
import com.volcengine.tos.model.object.UploadPartV2Output;
import com.volcengine.tos.transport.TransportConfig;

public interface TOSV2 extends TOS, Closeable {

    /**
     * create a new bucket
     *
     * @param bucket require input's bucketName not null
     * @return {@link CreateBucketV2Output}
     * @throws TosException
     */
    CreateBucketV2Output createBucket(String bucket) throws TosException;

    /**
     * create a new bucket
     *
     * @param input require input's bucketName not null
     * @return {@link CreateBucketV2Output}
     * @throws TosException
     */
    CreateBucketV2Output createBucket(CreateBucketV2Input input) throws TosException;

    /**
     * get bucket's meta data
     *
     * @param bucket bucket name
     * @return {@link HeadBucketV2Output}
     * @throws TosException
     */
    /**
     * get bucket's meta data
     *
     * @param input bucket name
     * @return {@link HeadBucketOutput}
     * @throws TosException
     */
    HeadBucketV2Output headBucket(HeadBucketV2Input input) throws TosException;

    /**
     * delete an existed bucket
     *
     * @param input bucket name
     * @return {@link DeleteBucketOutput}
     * @throws TosException
     */
    DeleteBucketOutput deleteBucket(DeleteBucketInput input) throws TosException;

    /**
     * list existed bucket owned by you.
     *
     * @param input no params required
     * @return {@link ListBucketsV2Output}
     * @throws TosException
     */
    ListBucketsV2Output listBuckets(ListBucketsV2Input input) throws TosException;

    /**
     * set bucket policy
     *
     * @param bucket bucket name
     * @param policy bucket policy
     * @return PutBucketPolicyOutput
     * @throws TosException
     */
    @Override
    PutBucketPolicyOutput putBucketPolicy(String bucket, String policy) throws TosException;

    /**
     * set bucket policy
     *
     * @param input set bucket name and bucket policy
     * @return PutBucketPolicyOutput
     * @throws TosException
     */
    PutBucketPolicyOutput putBucketPolicy(PutBucketPolicyInput input) throws TosException;

    /**
     * get policy of a bucket
     *
     * @param bucket bucket name
     * @return GetBucketPolicyOutput
     * @throws TosException
     */
    @Override
    GetBucketPolicyOutput getBucketPolicy(String bucket) throws TosException;

    /**
     * get policy of a bucket
     *
     * @param input set bucket name
     * @return GetBucketPolicyOutput
     * @throws TosException
     */
    GetBucketPolicyOutput getBucketPolicy(GetBucketPolicyInput input) throws TosException;

    /**
     * delete the policy of a bucket
     *
     * @param bucket bucket name
     * @return DeleteBucketPolicyOutput
     * @throws TosException
     */
    @Override
    DeleteBucketPolicyOutput deleteBucketPolicy(String bucket) throws TosException;

    /**
     * delete the policy of a bucket
     *
     * @param input set bucket name
     * @return DeleteBucketPolicyOutput
     * @throws TosException
     */
    DeleteBucketPolicyOutput deleteBucketPolicy(DeleteBucketPolicyInput input) throws TosException;

    /**
     * set the CORS of a bucket
     *
     * @param input set bucket name and the bucket CORS rules
     * @return PutBucketCORSOutput
     * @throws TosException
     */
    PutBucketCORSOutput putBucketCORS(PutBucketCORSInput input) throws TosException;

    /**
     * get the CORS of a bucket
     *
     * @param input set bucket name
     * @return GetBucketCORSOutput
     * @throws TosException
     */
    GetBucketCORSOutput getBucketCORS(GetBucketCORSInput input) throws TosException;

    /**
     * delete the CORS of a bucket
     *
     * @param input set bucket name
     * @return DeleteBucketCORSOutput
     * @throws TosException
     */
    DeleteBucketCORSOutput deleteBucketCORS(DeleteBucketCORSInput input) throws TosException;

    /**
     * set storageClass of a bucket
     *
     * @param input set bucket name and the specific storageClassType
     * @return PutBucketStorageClassOutput
     * @throws TosException
     */
    PutBucketStorageClassOutput putBucketStorageClass(PutBucketStorageClassInput input) throws TosException;

    /**
     * get the location of a bucket
     *
     * @param input set bucket name
     * @return GetBucketLocationOutput
     * @throws TosException
     */
    GetBucketLocationOutput getBucketLocation(GetBucketLocationInput input) throws TosException;

    /**
     * set the lifecycle rules of a bucket
     *
     * @param input set bucket name and the bucket lifecycle rules
     * @return PutBucketLifecycleOutput
     * @throws TosException
     */
    PutBucketLifecycleOutput putBucketLifecycle(PutBucketLifecycleInput input) throws TosException;

    /**
     * get the lifecycle rules of a bucket
     *
     * @param input set bucket name
     * @return GetBucketLifecycleOutput
     * @throws TosException
     */
    GetBucketLifecycleOutput getBucketLifecycle(GetBucketLifecycleInput input) throws TosException;

    /**
     * delete the lifecycle rules of a bucket
     *
     * @param input set bucket name
     * @return DeleteBucketLifecycleOutput
     * @throws TosException
     */
    DeleteBucketLifecycleOutput deleteBucketLifecycle(DeleteBucketLifecycleInput input) throws TosException;

    /**
     * set the mirrorBack rules of a bucket
     *
     * @param input set bucket name and the bucket mirrorBack rules
     * @return PutBucketMirrorBackOutput
     * @throws TosException
     */
    PutBucketMirrorBackOutput putBucketMirrorBack(PutBucketMirrorBackInput input) throws TosException;

    /**
     * get the mirrorBack rules of a bucket
     *
     * @param input set bucket name
     * @return GetBucketMirrorBackOutput
     * @throws TosException
     */
    GetBucketMirrorBackOutput getBucketMirrorBack(GetBucketMirrorBackInput input) throws TosException;

    /**
     * delete the mirrorBack rules of a bucket
     *
     * @param input set bucket name
     * @return DeleteBucketMirrorBackOutput
     * @throws TosException
     */
    DeleteBucketMirrorBackOutput deleteBucketMirrorBack(DeleteBucketMirrorBackInput input) throws TosException;

    /**
     * set the replication rules of a bucket
     *
     * @param input set bucket name and the bucket replication rules
     * @return PutBucketReplicationOutput
     * @throws TosException
     */
    PutBucketReplicationOutput putBucketReplication(PutBucketReplicationInput input) throws TosException;

    /**
     * get the replication rules of a bucket
     *
     * @param input set bucket name
     * @return GetBucketReplicationOutput
     * @throws TosException
     */
    GetBucketReplicationOutput getBucketReplication(GetBucketReplicationInput input) throws TosException;

    /**
     * delete the replication rules of a bucket
     *
     * @param input set bucket name
     * @return DeleteBucketReplicationOutput
     * @throws TosException
     */
    DeleteBucketReplicationOutput deleteBucketReplication(DeleteBucketReplicationInput input) throws TosException;

    /**
     * set the multi-version status of a bucket
     *
     * @param input set bucket name and the bucket multi-version status
     * @return PutBucketVersioningOutput
     * @throws TosException
     */
    PutBucketVersioningOutput putBucketVersioning(PutBucketVersioningInput input) throws TosException;

    /**
     * get the multi-version status of a bucket
     *
     * @param input set bucket name
     * @return GetBucketVersioningOutput
     * @throws TosException
     */
    GetBucketVersioningOutput getBucketVersioning(GetBucketVersioningInput input) throws TosException;

    /**
     * set the website config of a bucket
     *
     * @param input set bucket name and the bucket website config rules
     * @return PutBucketWebsiteOutput
     * @throws TosException
     */
    PutBucketWebsiteOutput putBucketWebsite(PutBucketWebsiteInput input) throws TosException;

    /**
     * get the website config of a bucket
     *
     * @param input set bucket name
     * @return GetBucketWebsiteOutput
     * @throws TosException
     */
    GetBucketWebsiteOutput getBucketWebsite(GetBucketWebsiteInput input) throws TosException;

    /**
     * delete the website config of a bucket
     *
     * @param input set bucket name
     * @return DeleteBucketWebsiteOutput
     * @throws TosException
     */
    DeleteBucketWebsiteOutput deleteBucketWebsite(DeleteBucketWebsiteInput input) throws TosException;

    /**
     * set the event notification rules of a bucket
     *
     * @param input set bucket name and the bucket event notification rules
     * @return PutBucketNotificationOutput
     * @throws TosException
     */
    PutBucketNotificationOutput putBucketNotification(PutBucketNotificationInput input) throws TosException;

    /**
     * get the event notification rules of a bucket
     *
     * @param input set bucket name
     * @return GetBucketNotificationOutput
     * @throws TosException
     */
    GetBucketNotificationOutput getBucketNotification(GetBucketNotificationInput input) throws TosException;

    /**
     * set the event notification v2 rules of a bucket
     *
     * @param input set bucket name and the bucket event notification rules
     * @return PutBucketNotificationType2Output
     * @throws TosException
     */
    PutBucketNotificationType2Output putBucketNotificationType2(PutBucketNotificationType2Input input) throws TosException;

    /**
     * get the event notification rules of a bucket
     *
     * @param input set bucket name
     * @return GetBucketNotificationType2Output
     * @throws TosException
     */
    GetBucketNotificationType2Output getBucketNotificationType2(GetBucketNotificationType2Input input) throws TosException;

    /**
     * add a custom domain config of a bucket
     *
     * @param input set bucket name and the custom domain rule
     * @return PutBucketCustomDomainOutput
     * @throws TosException
     */
    PutBucketCustomDomainOutput putBucketCustomDomain(PutBucketCustomDomainInput input) throws TosException;

    /**
     * list all the custom domains of a bucket
     *
     * @param input set bucket name
     * @return ListBucketCustomDomainOutput
     * @throws TosException
     */
    ListBucketCustomDomainOutput listBucketCustomDomain(ListBucketCustomDomainInput input) throws TosException;

    /**
     * delete a specific custom domain of a bucket
     *
     * @param input set bucket name
     * @return DeleteBucketCustomDomainOutput
     * @throws TosException
     */
    DeleteBucketCustomDomainOutput deleteBucketCustomDomain(DeleteBucketCustomDomainInput input) throws TosException;

    /**
     * set the realtime log config of a bucket
     *
     * @param input set bucket name and the realtime log config
     * @return PutBucketRealTimeLogOutput
     * @throws TosException
     */
    PutBucketRealTimeLogOutput putBucketRealTimeLog(PutBucketRealTimeLogInput input) throws TosException;

    /**
     * get the realtime log config of a bucket
     *
     * @param input set bucket name
     * @return GetBucketRealTimeLogOutput
     * @throws TosException
     */
    GetBucketRealTimeLogOutput getBucketRealTimeLog(GetBucketRealTimeLogInput input) throws TosException;

    /**
     * delete the realtime log config of a bucket
     *
     * @param input set bucket name
     * @return DeleteBucketRealTimeLogOutput
     * @throws TosException
     */
    DeleteBucketRealTimeLogOutput deleteBucketRealTimeLog(DeleteBucketRealTimeLogInput input) throws TosException;

    /**
     * set the acl rules of a bucket
     *
     * @param input set bucket name and the bucket lifecycle rules
     * @return PutBucketACLOutput
     * @throws TosException
     */
    PutBucketACLOutput putBucketACL(PutBucketACLInput input) throws TosException;

    /**
     * get the acl rules of a bucket
     *
     * @param input set bucket name
     * @return GetBucketACLOutput
     * @throws TosException
     */
    GetBucketACLOutput getBucketACL(GetBucketACLInput input) throws TosException;

    /**
     * set rename enabled of a bucket
     *
     * @param input set bucket name and the rename-switch
     * @return PutBucketRenameOutput
     * @throws TosException
     */
    PutBucketRenameOutput putBucketRename(PutBucketRenameInput input) throws TosException;

    /**
     * get the rename-enable status of a bucket
     *
     * @param input set bucket name
     * @return GetBucketRenameOutput
     * @throws TosException
     */
    GetBucketRenameOutput getBucketRename(GetBucketRenameInput input) throws TosException;

    /**
     * delete the rename-enable config of a bucket
     *
     * @param input set bucket name
     * @return DeleteBucketRenameOutput
     * @throws TosException
     */
    DeleteBucketRenameOutput deleteBucketRename(DeleteBucketRenameInput input) throws TosException;

    PutBucketEncryptionOutput putBucketEncryption(PutBucketEncryptionInput input) throws TosException;

    GetBucketEncryptionOutput getBucketEncryption(GetBucketEncryptionInput input) throws TosException;

    DeleteBucketEncryptionOutput deleteBucketEncryption(DeleteBucketEncryptionInput input) throws TosException;

    PutBucketTaggingOutput putBucketTagging(PutBucketTaggingInput input) throws TosException;

    GetBucketTaggingOutput getBucketTagging(GetBucketTaggingInput input) throws TosException;

    DeleteBucketTaggingOutput deleteBucketTagging(DeleteBucketTaggingInput input) throws TosException;

    PutBucketInventoryOutput putBucketInventory(PutBucketInventoryInput input) throws TosException;

    GetBucketInventoryOutput getBucketInventory(GetBucketInventoryInput input) throws TosException;

    ListBucketInventoryOutput listBucketInventory(ListBucketInventoryInput input) throws TosException;

    DeleteBucketInventoryOutput deleteBucketInventory(DeleteBucketInventoryInput input) throws TosException;

    /**
     * get data and metadata of an object
     *
     * @param input set get object options
     * @return {@link GetObjectV2Output}
     * @throws TosException
     */
    GetObjectV2Output getObject(GetObjectV2Input input) throws TosException;

    /**
     * get data and metadata of an object and store into a local file
     *
     * @param input set get object options
     * @return {@link GetObjectToFileOutput}
     * @throws TosException
     */
    GetObjectToFileOutput getObjectToFile(GetObjectToFileInput input) throws TosException;

    /**
     * If the key exists, return the metadata of the key, otherwise return the
     * first object information under the directory with the key.
     *
     * @param input get file status options
     * @return {@link GetFileStatusOutput}
     * @throws TosException
     */
    GetFileStatusOutput getFileStatus(GetFileStatusInput input) throws TosException;

    UploadFileV2Output uploadFile(UploadFileV2Input input) throws TosException;

    DownloadFileOutput downloadFile(DownloadFileInput input) throws TosException;

    ResumableCopyObjectOutput resumableCopyObject(ResumableCopyObjectInput input) throws TosException;

    /**
     * get metadata of an object with its data stream
     *
     * @param input set head object options
     * @return {@link HeadObjectV2Output}
     * @throws TosException
     */
    HeadObjectV2Output headObject(HeadObjectV2Input input) throws TosException;

    /**
     * delete an object
     *
     * @param input set delete object options
     * @return {@link DeleteObjectOutput}
     * @throws TosException
     */
    DeleteObjectOutput deleteObject(DeleteObjectInput input) throws TosException;

    /**
     * delete a list of objects
     *
     * @param input set delete multi objects options
     * @return {@link DeleteMultiObjectsV2Output}
     * @throws TosException
     */
    DeleteMultiObjectsV2Output deleteMultiObjects(DeleteMultiObjectsV2Input input) throws TosException;

    /**
     * upload an object to TOS server
     *
     * @param input set put object option
     * @return {@link PutObjectOutput}
     * @throws TosException
     */
    PutObjectOutput putObject(PutObjectInput input) throws TosException;

    /**
     * upload an object to TOS server from file
     *
     * @param input set put object option
     * @return {@link PutObjectFromFileOutput}
     * @throws TosException
     */
    PutObjectFromFileOutput putObjectFromFile(PutObjectFromFileInput input) throws TosException;

    /**
     * append content at the tail of an appendable object
     *
     * @param input set append object option
     * @return {@link AppendObjectOutput}
     * @throws TosException
     */
    AppendObjectOutput appendObject(AppendObjectInput input) throws TosException;

    /**
     * set some metadata of the object
     *
     * @param input set setObjectMeta option
     * @return {@link SetObjectMetaOutput}
     * @throws TosException
     */
    SetObjectMetaOutput setObjectMeta(SetObjectMetaInput input) throws TosException;

    /**
     * set object last modify time
     *
     * @param input setObjectTime option
     * @return {@link SetObjectTimeOutput}
     * @throws TosException
     */
    SetObjectTimeOutput setObjectTime(SetObjectTimeInput input) throws TosException;

    /**
     * list objects of a bucket deprecated since v2.4.0, use listObjectsType2
     * instead
     *
     * @param input list object options
     * @return {@link ListObjectsV2Output}
     * @throws TosException
     */
    @Deprecated
    ListObjectsV2Output listObjects(ListObjectsV2Input input) throws TosException;

    /**
     * list objects of a bucket
     *
     * @param input list object options
     * @return {@link ListObjectsType2Output}
     * @throws TosException
     */
    ListObjectsType2Output listObjectsType2(ListObjectsType2Input input) throws TosException;

    /**
     * list multi-version objects of a bucket
     *
     * @param input query params
     * @return {@link ListObjectVersionsV2Output}
     * @throws TosException
     */
    ListObjectVersionsV2Output listObjectVersions(ListObjectVersionsV2Input input) throws TosException;

    /**
     * copy an object in the same bucket
     *
     * @param input set copy object option
     * @return {@link CopyObjectV2Output}
     * @throws TosException
     */
    CopyObjectV2Output copyObject(CopyObjectV2Input input) throws TosException;

    /**
     * copy a part of object as a part of a multipart upload operation
     *
     * @param input
     * @return {@link UploadPartCopyV2Output}
     * @throws TosException
     */
    UploadPartCopyV2Output uploadPartCopy(UploadPartCopyV2Input input) throws TosException;

    /**
     * set object's acl grants or rules
     *
     * @param input set acl option.
     * @return {@link PutObjectACLOutput}
     * @throws TosException
     */
    PutObjectACLOutput putObjectAcl(PutObjectACLInput input) throws TosException;

    /**
     * get object's acl grants or rules
     *
     * @param input set bucket, key and versionID.
     * @return {@link GetObjectACLV2Output}
     * @throws TosException
     */
    GetObjectACLV2Output getObjectAcl(GetObjectACLV2Input input) throws TosException;

    /**
     * set object's tags in the object meta info
     *
     * @param input set object tag set.
     * @return {@link PutObjectTaggingOutput}
     * @throws TosException
     */
    PutObjectTaggingOutput putObjectTagging(PutObjectTaggingInput input) throws TosException;

    /**
     * get object's tags in the object meta info
     *
     * @param input set bucket, key and versionID.
     * @return {@link GetObjectTaggingOutput}
     * @throws TosException
     */
    GetObjectTaggingOutput getObjectTagging(GetObjectTaggingInput input) throws TosException;

    /**
     * delete object's tags in the object meta info
     *
     * @param input set bucket, key and versionID.
     * @return {@link DeleteObjectTaggingOutput}
     * @throws TosException
     */
    DeleteObjectTaggingOutput deleteObjectTagging(DeleteObjectTaggingInput input) throws TosException;

    /**
     * fetch an object in the specific url synchronously
     *
     * @param input set fetch options.
     * @return {@link FetchObjectOutput}
     * @throws TosException
     */
    FetchObjectOutput fetchObject(FetchObjectInput input) throws TosException;

    /**
     * fetch an object in the specific url asynchronously
     *
     * @param input set fetch options.
     * @return {@link PutFetchTaskOutput}
     * @throws TosException
     */
    PutFetchTaskOutput putFetchTask(PutFetchTaskInput input) throws TosException;

    GetFetchTaskOutput getFetchTask(GetFetchTaskInput input) throws TosException;

    /**
     * create a multipart upload operation
     *
     * @param input set the createMultipartUpload option
     * @return {@link CreateMultipartUploadOutput}
     * @throws TosException
     */
    CreateMultipartUploadOutput createMultipartUpload(CreateMultipartUploadInput input) throws TosException;

    /**
     * upload a part for a multipart upload operation
     *
     * @param input set uploadpart option
     * @return {@link UploadPartV2Output}
     * @throws TosException
     */
    UploadPartV2Output uploadPart(UploadPartV2Input input) throws TosException;

    /**
     * upload a part from file for a multipart upload operation
     *
     * @param input set uploadpart option
     * @return {@link UploadPartFromFileOutput}
     * @throws TosException
     */
    UploadPartFromFileOutput uploadPartFromFile(UploadPartFromFileInput input) throws TosException;

    /**
     * complete a multipart upload operation
     *
     * @param input set completeMultipartUpload option
     * @return {@link CompleteMultipartUploadV2Output}
     * @throws TosException
     */
    CompleteMultipartUploadV2Output completeMultipartUpload(CompleteMultipartUploadV2Input input) throws TosException;

    /**
     * abort a multipart upload operation
     *
     * @param input set abortMultipartUpload option
     * @return {@link AbortMultipartUploadOutput}
     * @throws TosException
     */
    AbortMultipartUploadOutput abortMultipartUpload(AbortMultipartUploadInput input) throws TosException;

    /**
     * list parts that have been uploaded.
     *
     * @param input set listParts option
     * @return {@link ListPartsOutput}
     * @throws TosException
     */
    ListPartsOutput listParts(ListPartsInput input) throws TosException;

    /**
     * list multiparts uploading
     *
     * @param input ListMultipartUploadsInput
     * @return {@link ListMultipartUploadsV2Output}
     * @throws TosException
     */
    ListMultipartUploadsV2Output listMultipartUploads(ListMultipartUploadsV2Input input) throws TosException;

    /**
     * rename an object in the bucket
     *
     * @param input set the source key to be renamed and the renamed key.
     * @return {@link RenameObjectOutput}
     * @throws TosException
     */
    RenameObjectOutput renameObject(RenameObjectInput input) throws TosException;

    /**
     * restore an object in the bucket
     *
     * @param input set restoreObject options.
     * @return {@link RestoreObjectOutput}
     * @throws TosException
     */
    RestoreObjectOutput restoreObject(RestoreObjectInput input) throws TosException;

    PutSymlinkOutput putSymlink(PutSymlinkInput input) throws TosException;

    GetSymlinkOutput getSymlink(GetSymlinkInput input) throws TosException;

//    ModifyObjectOutput modifyObject(ModifyObjectInput input) throws TosException;
    /**
     * create a pre-signed URL
     *
     * @param input PreSignedURLInput
     * @return {@link PreSignedURLOutput}
     * @throws TosException
     */
    PreSignedURLOutput preSignedURL(PreSignedURLInput input) throws TosException;

    /**
     * create a pre-signed for postObject request
     *
     * @param input PreSignedPostSignatureInput
     * @return {@link PreSignedPostSignatureOutput}
     * @throws TosException
     */
    PreSignedPostSignatureOutput preSignedPostSignature(PreSignedPostSignatureInput input) throws TosException;

    /**
     * create a pre-signed for batch object request
     *
     * @param input PreSingedPolicyURLInput
     * @return {@link PreSingedPolicyURLOutput}
     * @throws TosException
     */
    PreSingedPolicyURLOutput preSingedPolicyURL(PreSingedPolicyURLInput input) throws TosException;

    /**
     * create a pre-signed for batch object request
     *
     * @param input PreSignedPolicyURLInput
     * @return {@link PreSignedPolicyURLOutput}
     * @throws TosException
     */
    PreSignedPolicyURLOutput preSignedPolicyURL(PreSignedPolicyURLInput input) throws TosException;

    @Deprecated
    void changeCredentials(Credentials credentials);

    @Deprecated
    void changeRegionAndEndpoint(String region, String endpoint);

    void changeTransportConfig(TransportConfig config);

    boolean refreshEndpointRegion(String endpoint, String region);

    boolean refreshCredentials(String accessKey, String secretKey, String securityToken);
}

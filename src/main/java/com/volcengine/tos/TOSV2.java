package com.volcengine.tos;

import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;

public interface TOSV2 extends TOS {
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
     * @param input
     *              require input's bucketName not null
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
     * @param bucket bucket name
     * @param policy bucket policy
     * @return PutBucketPolicyOutput
     * @throws TosException
     */
    @Override
    PutBucketPolicyOutput putBucketPolicy(String bucket, String policy) throws TosException;

    /**
     * set bucket policy
     * @param input set bucket name and bucket policy
     * @return PutBucketPolicyOutput
     * @throws TosException
     */
    PutBucketPolicyOutput putBucketPolicy(PutBucketPolicyInput input) throws TosException;

    /**
     * get policy of a bucket
     * @param bucket bucket name
     * @return GetBucketPolicyOutput
     * @throws TosException
     */
    @Override
    GetBucketPolicyOutput getBucketPolicy(String bucket) throws TosException;

    /**
     * get policy of a bucket
     * @param input set bucket name
     * @return GetBucketPolicyOutput
     * @throws TosException
     */
    GetBucketPolicyOutput getBucketPolicy(GetBucketPolicyInput input) throws TosException;

    /**
     * delete the policy of a bucket
     * @param bucket bucket name
     * @return DeleteBucketPolicyOutput
     * @throws TosException
     */
    @Override
    DeleteBucketPolicyOutput deleteBucketPolicy(String bucket) throws TosException;

    /**
     * delete the policy of a bucket
     * @param input set bucket name
     * @return DeleteBucketPolicyOutput
     * @throws TosException
     */
    DeleteBucketPolicyOutput deleteBucketPolicy(DeleteBucketPolicyInput input) throws TosException;


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

    UploadFileV2Output uploadFile(UploadFileV2Input input) throws TosException;

    DownloadFileOutput downloadFile(DownloadFileInput input) throws TosException;

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
     * list objects of a bucket
     *
     * @param input list object options
     * @return {@link ListObjectsV2Output}
     * @throws TosException
     */
    ListObjectsV2Output listObjects(ListObjectsV2Input input) throws TosException;

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
     * @param input set acl option.
     * @return {@link GetObjectACLV2Output}
     * @throws TosException
     */
    GetObjectACLV2Output getObjectAcl(GetObjectACLV2Input input) throws TosException;

    /**
     * create a multipart upload operation
     *
     * @param input set the createMultipartUpload option
     *
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
     * create a pre-signed URL
     * @param input PreSignedURLInput
     * @return {@link PreSignedURLOutput}
     * @throws TosException
     */
    PreSignedURLOutput preSignedURL(PreSignedURLInput input) throws TosException;
}

package com.volcengine.tos;

import com.volcengine.tos.model.acl.GetObjectAclOutput;
import com.volcengine.tos.model.acl.PutObjectAclInput;
import com.volcengine.tos.model.acl.PutObjectAclOutput;
import com.volcengine.tos.model.bucket.*;
import com.volcengine.tos.model.object.*;

import java.io.InputStream;
import java.time.Duration;

/**
 * @author volcengine tos
 */
public interface TOS {
    /**
     * create a new bucket
     *
     * @param input
     *              require input's bucketName not null
     * @return {@link CreateBucketOutput}
     * @throws TosException
     */
    CreateBucketOutput createBucket(CreateBucketInput input) throws TosException;

    /**
     * get bucket's meta data
     *
     * @param bucket bucket name
     * @return {@link HeadBucketOutput}
     * @throws TosException
     */
    HeadBucketOutput headBucket(String bucket) throws TosException;

    /**
     * delete an existed bucket
     *
     * @param bucket bucket name
     * @return {@link DeleteBucketOutput}
     * @throws TosException
     */
    DeleteBucketOutput deleteBucket(String bucket) throws TosException;

    /**
     * list existed bucket owned by you.
     *
     * @param input no params required
     * @return {@link ListBucketsOutput}
     * @throws TosException
     */
    ListBucketsOutput listBuckets(ListBucketsInput input) throws TosException;

    /**
     * get data and metadata of an object
     *
     * @param bucket the bucket to operate
     * @param objectKey the name of object
     * @param builders  optional. setting withXXX properties.
     *                  withVersionID: which version of this object.
     *                  withRange: the range of content.
     *                  withIfModifiedSince: return if the object modified after the given date,
     *                  otherwise return status code 304.
     *                  withIfUnmodifiedSince, withIfMatch, withIfNoneMatch set If-Unmodified-Since, If-Match and If-None-Match.
     * @return {@link GetObjectOutput}
     * @throws TosException
     */
    GetObjectOutput getObject(String bucket, String objectKey, RequestOptionsBuilder...builders) throws TosException;

    /**
     * get metadata of an object with its data stream
     *
     * @param bucket the bucket to operate
     * @param objectKey the name of object
     * @param builders  optional. setting withXXX properties.
     *                  withVersionID: which version of this object.
     *                  withRange: the range of content.
     *                  withIfModifiedSince: return if the object modified after the given date,
     *                  otherwise return status code 304.
     *                  withIfUnmodifiedSince, withIfMatch, withIfNoneMatch set If-Unmodified-Since, If-Match and If-None-Match.
     * @return {@link HeadObjectOutput}
     * @throws TosException
     */
    HeadObjectOutput headObject(String bucket, String objectKey, RequestOptionsBuilder...builders) throws TosException;

    /**
     * delete an object
     *
     * @param bucket the bucket to operate
     * @param objectKey the name of object
     * @param builders  optional. setting withXXX properties.
     *                  withVersionID: which version of this object will be deleted
     * @return {@link DeleteObjectOutput}
     * @throws TosException
     */
    DeleteObjectOutput deleteObject(String bucket, String objectKey, RequestOptionsBuilder...builders) throws TosException;

    /**
     * delete a list of objects
     *
     * @param bucket the bucket to operate
     * @param input    the objects to be deleted
     * @param builders optional. setting withXXX properties.
     * @return {@link DeleteMultiObjectsOutput}
     * @throws TosException
     */
    DeleteMultiObjectsOutput deleteMultiObjects(String bucket, DeleteMultiObjectsInput input, RequestOptionsBuilder...builders) throws TosException;

    /**
     * upload an object to TOS server
     *
     * @param bucket the bucket to operate
     * @param objectKey   the name of object
     * @param inputStream the content of object
     * @param builders    optional. setting withXXX properties.
     *                    withContentType: set Content-Type.
     *                    withContentDisposition: set Content-Disposition.
     *                    withContentLanguage: set Content-Language.
     *                    withContentEncoding: set Content-Encoding.
     *                    withCacheControl: set Cache-Control.
     *                    withExpires: set Expires.
     *                    withMeta: set meta header(s).
     *                    withContentSHA256: set Content-Sha256.
     *                    withContentMD5: set Content-MD5.
     *                    withExpires: set Expires.
     *                    withServerSideEncryptionCustomer: set server side encryption options.
     *                    withACL, withACLGrantFullControl, withACLGrantRead, withACLGrantReadAcp,
     *                    withACLGrantWrite, withACLGrantWriteAcp set object acl.
     * @return {@link PutObjectOutput}
     * @throws TosException
     */
    PutObjectOutput putObject(String bucket, String objectKey, InputStream inputStream, RequestOptionsBuilder...builders) throws TosException;

    /**
     * append content at the tail of an appendable object
     *
     * @param bucket the bucket to operate
     * @param objectKey the name of object
     * @param content   the content of object
     * @param offset    append position, equals to the current object-size
     * @param builders  optional. setting withXXX properties.
     *                  withContentType: set Content-Type.
     *                  withContentDisposition: set Content-Disposition.
     *                  withContentLanguage: set Content-Language.
     *                  withContentEncoding: set Content-Encoding.
     *                  withCacheControl: set Cache-Control.
     *                  withExpires: set Expires.
     *                  withMeta: set meta header(s).
     *                  withExpires: set Expires.
     *                  withACL, withACLGrantFullControl, withACLGrantRead, withACLGrantReadAcp,
     *                  withACLGrantWrite, withACLGrantWriteAcp set object acl.
     *                  above options only take effect when offset parameter is 0.
     *
     *                  withContentSHA256: set Content-Sha256.
     *                  withContentMD5: set Content-MD5.
     * @return {@link AppendObjectOutput}
     * @throws TosException
     */
    AppendObjectOutput appendObject(String bucket, String objectKey, InputStream content, long offset, RequestOptionsBuilder...builders) throws TosException;

    /**
     * set some metadata of the object
     *
     * @param bucket the bucket to operate
     * @param objectKey the name of object
     * @param builders  optional. setting withXXX properties.
     *                  withContentType set Content-Type.
     *                  withContentDisposition set Content-Disposition.
     *                  withContentLanguage set Content-Languag.
     *                  withContentEncoding set Content-Encoding.
     *                  withCacheControl set Cache-Control.
     *                  withExpires set Expires.
     *                  withMeta set meta header(s).
     *                  withVersionID which version of this object will be set
     * @return {@link SetObjectMetaOutput}
     * @throws TosException
     */
    SetObjectMetaOutput setObjectMeta(String bucket, String objectKey, RequestOptionsBuilder...builders) throws TosException;

    /**
     * list objects of a bucket
     *
     * @param bucket the bucket to operate
     * @param input query params
     * @return {@link ListObjectsOutput}
     * @throws TosException
     */
    ListObjectsOutput listObjects(String bucket, ListObjectsInput input) throws TosException;

    /**
     * list multi-version objects of a bucket
     *
     * @param bucket the bucket to operate
     * @param input query params
     * @return {@link ListObjectVersionsOutput}
     * @throws TosException
     */
    ListObjectVersionsOutput listObjectVersions(String bucket, ListObjectVersionsInput input) throws TosException;

    /**
     * copy an object in the same bucket
     *
     * @param bucket the bucket to operate
     * @param srcObjectKey the source object name
     * @param dstObjectKey the destination object name
     * NOTICE: srcObjectKey and dstObjectKey belongs to the same bucket.
     * @param builders     optional. setting withXXX properties.
     *                     withVersionID the version id of source object.
     *                     withMetadataDirective copy source object metadata or replace with new object metadata.
     *
     *                     withACL withACLGrantFullControl withACLGrantRead withACLGrantReadAcp
     *                     withACLGrantWrite withACLGrantWriteAcp set object acl.
     *
     *                     withCopySourceIfMatch withCopySourceIfNoneMatch withCopySourceIfModifiedSince
     *                     withCopySourceIfUnmodifiedSince set copy conditions.
     *
     *                     if copyObject called with withMetadataDirective(TosHeaders.METADATA_DIRECTIVE_REPLACE),
     *                     these properties can be used:
     *                     withContentType set Content-Type.
     *                     withContentDisposition set Content-Disposition.
     *                     withContentLanguage set Content-Language.
     *                     withContentEncoding set Content-Encoding.
     *                     withCacheControl set Cache-Control.
     *                     withExpires set Expires.
     *                     withMeta set meta header(s),
     * @return {@link CopyObjectOutput}
     * @throws TosException
     */
    CopyObjectOutput copyObject(String bucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder...builders) throws TosException;

    /**
     * copy an object from bucket A to bucket B, the method is called by bucket A.
     *
     * @param bucket the bucket to operate
     * @param dstBucket    the destination bucket
     * @param dstObjectKey the destination object name
     * @param srcObjectKey the source object name
     * @param builders     optional. setting withXXX properties.
     *                     withVersionID the version id of source object.
     *                     withMetadataDirective copy source object metadata or replace with new object metadata.
     *
     *                     withACL withACLGrantFullControl withACLGrantRead withACLGrantReadAcp
     *                     withACLGrantWrite withACLGrantWriteAcp set object acl.
     *
     *                     withCopySourceIfMatch withCopySourceIfNoneMatch withCopySourceIfModifiedSince
     *                     withCopySourceIfUnmodifiedSince set copy conditions.
     *
     *                     if copyObjectTo called with withMetadataDirective(TosHeaders.METADATA_DIRECTIVE_REPLACE),
     *                     these properties can be used:
     *                     withContentType set Content-Type.
     *                     withContentDisposition set Content-Disposition.
     *                     withContentLanguage set Content-Language.
     *                     withContentEncoding set Content-Encoding.
     *                     withCacheControl set Cache-Control.
     *                     withExpires set Expires.
     *                     withMeta set meta header(s).
     * @return {@link CopyObjectOutput}
     * @throws TosException
     */
    CopyObjectOutput copyObjectTo(String bucket, String dstBucket, String dstObjectKey, String srcObjectKey, RequestOptionsBuilder...builders) throws TosException;

    /**
     * copy an object from bucket A to bucket B, the method is called by bucket B.
     *
     * @param bucket the bucket to operate
     * @param srcBucket    the source bucket
     * @param srcObjectKey the source object name
     * @param dstObjectKey the destination object name
     * @param builders     optional. setting withXXX properties.
     *                     withVersionID the version id of source object.
     *                     withMetadataDirective copy source object metadata or replace with new object metadata.
     *
     *                     withACL withACLGrantFullControl withACLGrantRead withACLGrantReadAcp
     *                     withACLGrantWrite withACLGrantWriteAcp set object acl.
     *
     *                     withCopySourceIfMatch withCopySourceIfNoneMatch withCopySourceIfModifiedSince
     *                     withCopySourceIfUnmodifiedSince set copy conditions.
     *
     *                     if copyObjectFrom called with withMetadataDirective(TosHeaders.METADATA_DIRECTIVE_REPLACE),
     *                     these properties can be used:
     *                     withContentType set Content-Type.
     *                     withContentDisposition set Content-Disposition.
     *                     withContentLanguage set Content-Language.
     *                     withContentEncoding set Content-Encoding.
     *                     withCacheControl set Cache-Control.
     *                     withExpires set Expires.
     *                     withMeta set meta header(s).
     * @return {@link CopyObjectOutput}
     * @throws TosException
     */
    CopyObjectOutput copyObjectFrom(String bucket, String srcBucket, String srcObjectKey, String dstObjectKey, RequestOptionsBuilder...builders) throws TosException;

    /**
     * copy a part of object as a part of a multipart upload operation
     *
     * @param bucket the bucket to operate
     * @param input uploadID, destinationKey, sourceBucket, sourceKey, partNumber, partSize and startOffset are inclusive,
     *              other parameters are optional.
     * @param builders optional. setting withXXX properties.
     *                 withCopySourceIfMatch, withCopySourceIfNoneMatch, withCopySourceIfModifiedSince and
     *                 withCopySourceIfUnmodifiedSince set copy conditions
     * @return {@link UploadPartCopyOutput}
     * @throws TosException
     */
    UploadPartCopyOutput uploadPartCopy(String bucket, UploadPartCopyInput input, RequestOptionsBuilder...builders) throws TosException;

    /**
     * set object's acl grants or rules
     *
     * @param bucket the bucket to operate
     * @param input aclGrant, aclRules can not set both.
     * @return {@link PutObjectAclOutput}
     * @throws TosException
     */
    PutObjectAclOutput putObjectAcl(String bucket, PutObjectAclInput input) throws TosException;

    /**
     * get object's acl grants or rules
     *
     * @param bucket the bucket to operate
     * @param objectKey the name of object
     * @param builders  optional. setting withXXX properties.
     *                  withVersionID the version of the object
     * @return {@link GetObjectAclOutput}
     * @throws TosException
     */
    GetObjectAclOutput getObjectAcl(String bucket, String objectKey, RequestOptionsBuilder...builders) throws TosException;

    /**
     * create a multipart upload operation
     *
     * @param bucket the bucket to operate
     * @param objectKey the name of object
     * @param builders  optional. setting withXXX properties.
     *                  withContentType set Content-Type.
     *                  withContentDisposition set Content-Disposition.
     *                  withContentLanguage set Content-Language.
     *                  withContentEncoding set Content-Encoding.
     *                  withCacheControl set Cache-Control.
     *                  withExpires set Expires.
     *                  withMeta set meta header(s).
     *                  withContentSHA256 set Content-Sha256.
     *                  withContentMD5 set Content-MD5.
     *                  withExpires set Expires.
     *                  withServerSideEncryptionCustomer set server side encryption options.
     *                  withACL, WithACLGrantFullControl, withACLGrantRead, withACLGrantReadAcp,
     *                  withACLGrantWrite, withACLGrantWriteAcp set object acl
     *
     * @return {@link CreateMultipartUploadOutput}
     * @throws TosException
     */
    CreateMultipartUploadOutput createMultipartUpload(String bucket, String objectKey, RequestOptionsBuilder...builders) throws TosException;

    /**
     * upload a part for a multipart upload operation
     *
     * @param bucket the bucket to operate
     * @param input    some params are required, eg, key, uploadID, partSize, partNumber and partNumber
     * @param builders optional. setting withXXX properties.
     * @return {@link UploadPartOutput}
     * @throws TosException
     */
    UploadPartOutput uploadPart(String bucket, UploadPartInput input, RequestOptionsBuilder...builders) throws TosException;

    /**
     * complete a multipart upload operation
     *
     * @param bucket the bucket to operate
     * @param input key: the object name,
     *              uploadID: the uploadID got from CreateMultipartUpload,
     *              uploadedParts: upload part output got from uploadPart or uploadPartCopy
     * @return {@link CompleteMultipartUploadOutput}
     * @throws TosException
     */
    CompleteMultipartUploadOutput completeMultipartUpload(String bucket, CompleteMultipartUploadInput input) throws TosException;

    /**
     * abort a multipart upload operation
     *
     * @param bucket the bucket to operate
     * @param input
     * @return {@link AbortMultipartUploadOutput}
     * @throws TosException
     */
    AbortMultipartUploadOutput abortMultipartUpload(String bucket, AbortMultipartUploadInput input) throws TosException;

    /**
     * list parts that have been uploaded.
     *
     * @param bucket the bucket to operate
     * @param input    key, uploadID and other parameters
     * @param builders optional. setting withXXX properties.
     * @return {@link ListUploadedPartsOutput}
     * @throws TosException
     */
    ListUploadedPartsOutput listUploadedParts(String bucket, ListUploadedPartsInput input, RequestOptionsBuilder...builders) throws TosException;

    /**
     * list multipart uploads
     *
     * @param bucket the bucket to operate
     * @param input ListMultipartUploadsInput
     * @return {@link ListMultipartUploadsOutput}
     * @throws TosException
     */
    ListMultipartUploadsOutput listMultipartUploads(String bucket, ListMultipartUploadsInput input) throws TosException;

    /**
     * create a pre-signed URL
     * @param httpMethod the http method in the URL, such as GET, POST, PUT, HEAD
     * @param bucket the bucket to operate
     * @param objectKey the object name
     * @param ttl the time-to-live of signed URL
     * @param builders withVersionID the version id of the object
     * @return
     * @throws TosException
     */
    String preSignedURL(String httpMethod, String bucket, String objectKey, Duration ttl, RequestOptionsBuilder...builders) throws TosException;
}

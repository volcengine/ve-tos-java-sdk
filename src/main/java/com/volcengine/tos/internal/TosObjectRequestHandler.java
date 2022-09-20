package com.volcengine.tos.internal;

import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.*;

public interface TosObjectRequestHandler {
    GetObjectV2Output getObject(GetObjectV2Input input) throws TosException;

    HeadObjectV2Output headObject(HeadObjectV2Input input) throws TosException;

    DeleteObjectOutput deleteObject(DeleteObjectInput input) throws TosException;

    DeleteMultiObjectsV2Output deleteMultiObjects(DeleteMultiObjectsV2Input input) throws TosException;

    PutObjectOutput putObject(PutObjectInput input) throws TosException;

    AppendObjectOutput appendObject(AppendObjectInput input) throws TosException;

    SetObjectMetaOutput setObjectMeta(SetObjectMetaInput input) throws TosException;

    ListObjectsV2Output listObjects(ListObjectsV2Input input) throws TosException;

    ListObjectVersionsV2Output listObjectVersions(ListObjectVersionsV2Input input) throws TosException;

    CopyObjectV2Output copyObject(CopyObjectV2Input input) throws TosException;

    UploadPartCopyV2Output uploadPartCopy(UploadPartCopyV2Input input) throws TosException;

    PutObjectACLOutput putObjectAcl(PutObjectACLInput input) throws TosException;

    GetObjectACLV2Output getObjectAcl(GetObjectACLV2Input input) throws TosException;

    CreateMultipartUploadOutput createMultipartUpload(CreateMultipartUploadInput input) throws TosException;

    UploadPartV2Output uploadPart(UploadPartV2Input input) throws TosException;

    CompleteMultipartUploadV2Output completeMultipartUpload(CompleteMultipartUploadV2Input input) throws TosException;

    AbortMultipartUploadOutput abortMultipartUpload(AbortMultipartUploadInput input) throws TosException;

    ListPartsOutput listParts(ListPartsInput input) throws TosException;

    ListMultipartUploadsV2Output listMultipartUploads(ListMultipartUploadsV2Input input) throws TosException;
}

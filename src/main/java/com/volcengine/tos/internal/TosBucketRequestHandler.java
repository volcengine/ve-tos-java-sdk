package com.volcengine.tos.internal;

import com.volcengine.tos.TosException;
import com.volcengine.tos.model.bucket.*;

public interface TosBucketRequestHandler {
    CreateBucketV2Output createBucket(CreateBucketV2Input input) throws TosException;

    HeadBucketV2Output headBucket(HeadBucketV2Input input) throws TosException;

    DeleteBucketOutput deleteBucket(DeleteBucketInput input) throws TosException;

    ListBucketsV2Output listBuckets(ListBucketsV2Input input) throws TosException;

    PutBucketPolicyOutput putBucketPolicy(PutBucketPolicyInput input) throws TosException;

    GetBucketPolicyOutput getBucketPolicy(GetBucketPolicyInput input) throws TosException;

    DeleteBucketPolicyOutput deleteBucketPolicy(DeleteBucketPolicyInput input) throws TosException;
}

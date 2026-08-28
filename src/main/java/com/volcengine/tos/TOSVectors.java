package com.volcengine.tos;
import com.volcengine.tos.model.vectors.QueryVectorsInput;
import com.volcengine.tos.model.vectors.QueryVectorsOutput;

import com.volcengine.tos.model.vectors.CreateIndexInput;
import com.volcengine.tos.model.vectors.CreateIndexOutput;
import com.volcengine.tos.model.vectors.CreateVectorBucketInput;
import com.volcengine.tos.model.vectors.CreateVectorBucketOutput;
import com.volcengine.tos.model.vectors.DeleteIndexInput;
import com.volcengine.tos.model.vectors.DeleteIndexOutput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketInput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketOutput;
import com.volcengine.tos.model.vectors.DeleteVectorsInput;
import com.volcengine.tos.model.vectors.DeleteVectorsOutput;
import com.volcengine.tos.model.vectors.GetIndexInput;
import com.volcengine.tos.model.vectors.GetIndexOutput;
import com.volcengine.tos.model.vectors.GetVectorBucketInput;
import com.volcengine.tos.model.vectors.GetVectorBucketOutput;
import com.volcengine.tos.model.vectors.GetVectorsInput;
import com.volcengine.tos.model.vectors.GetVectorsOutput;
import com.volcengine.tos.model.vectors.ListIndexesInput;
import com.volcengine.tos.model.vectors.ListIndexesOutput;
import com.volcengine.tos.model.vectors.ListVectorsInput;
import com.volcengine.tos.model.vectors.ListVectorsOutput;
import com.volcengine.tos.model.vectors.ListVectorBucketsInput;
import com.volcengine.tos.model.vectors.ListVectorBucketsOutput;
import com.volcengine.tos.model.vectors.PutVectorsInput;
import com.volcengine.tos.model.vectors.PutVectorsOutput;
import com.volcengine.tos.model.vectors.PutVectorBucketPolicyInput;
import com.volcengine.tos.model.vectors.PutVectorBucketPolicyOutput;
import com.volcengine.tos.model.vectors.GetVectorBucketPolicyInput;
import com.volcengine.tos.model.vectors.GetVectorBucketPolicyOutput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketPolicyInput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketPolicyOutput;

import java.io.Closeable;

public interface TOSVectors extends Closeable {
    CreateVectorBucketOutput createVectorBucket(CreateVectorBucketInput input) throws TosException;
    GetVectorBucketOutput getVectorBucket(GetVectorBucketInput input) throws TosException;
    DeleteVectorBucketOutput deleteVectorBucket(DeleteVectorBucketInput input) throws TosException;
    ListVectorBucketsOutput listVectorBuckets(ListVectorBucketsInput input) throws TosException;
    CreateIndexOutput createIndex(CreateIndexInput input) throws TosException;
    GetIndexOutput getIndex(GetIndexInput input) throws TosException;
    DeleteIndexOutput deleteIndex(DeleteIndexInput input) throws TosException;
    ListIndexesOutput listIndexes(ListIndexesInput input) throws TosException;
    PutVectorsOutput putVectors(PutVectorsInput input) throws TosException;
    DeleteVectorsOutput deleteVectors(DeleteVectorsInput input) throws TosException;
    GetVectorsOutput getVectors(GetVectorsInput input) throws TosException;
    ListVectorsOutput listVectors(ListVectorsInput input) throws TosException;
    PutVectorBucketPolicyOutput putVectorBucketPolicy(PutVectorBucketPolicyInput input) throws TosException;
    GetVectorBucketPolicyOutput getVectorBucketPolicy(GetVectorBucketPolicyInput input) throws TosException;
    DeleteVectorBucketPolicyOutput deleteVectorBucketPolicy(DeleteVectorBucketPolicyInput input) throws TosException;
    QueryVectorsOutput queryVectors(QueryVectorsInput input) throws TosException;
}

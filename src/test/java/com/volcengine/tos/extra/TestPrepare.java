package com.volcengine.tos.extra;

import com.volcengine.tos.*;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.model.bucket.CreateBucketInput;
import com.volcengine.tos.model.bucket.HeadBucketOutput;
import com.volcengine.tos.model.object.*;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestPrepare {
    private static TOSV2 client;

    @Test
    void createAndClearBucketTest() throws TosException, InterruptedException {
        TOSClientConfiguration conf = TOSClientConfiguration.builder().region(Consts.region).endpoint(Consts.endpoint)
                .credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).build();
        client = new TOSV2ClientBuilder().build(conf);
        try{
            HeadBucketOutput head = client.headBucket(Consts.bucket);
        } catch (TosException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                try{
                    client.createBucket(new CreateBucketInput(Consts.bucket));
                    Thread.sleep(10000);
                } catch (TosException te) {
                    if (e.getStatusCode() == HttpStatus.CONFLICT) {
                        // ignore
                    }
                    te.printStackTrace();
                    Assert.fail();
                }
            } else {
                e.printStackTrace();
                Assert.fail();
            }
        }
        try{
            HeadBucketOutput head = client.headBucket(Consts.bucketCopy);
        } catch (TosException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                try{
                    client.createBucket(new CreateBucketInput(Consts.bucketCopy));
                    Thread.sleep(10000);
                } catch (TosException te) {
                    if (e.getStatusCode() == HttpStatus.CONFLICT) {
                        // ignore
                    }
                    te.printStackTrace();
                    Assert.fail();
                }
            } else {
                e.printStackTrace();
                Assert.fail();
            }
        }

        // clear bucket
        clearBucket(Consts.bucket);
        clearBucket(Consts.bucketMultiVersionDisabled);
        clearBucket(Consts.bucketCopy);
    }

    private void clearBucket(String bucket) {
        try{
            boolean isTruncated = true;
            String keyMarker = null;
            String versionIdMarker = null;
            while(isTruncated) {
                ListObjectVersionsV2Input input = new ListObjectVersionsV2Input().setBucket(bucket)
                        .setKeyMarker(keyMarker).setVersionIDMarker(versionIdMarker);
                ListObjectVersionsV2Output output = client.listObjectVersions(input);
                if (output == null) {
                    break;
                }
                if (output.getVersions() != null) {
                    for (ListedObjectVersion version : output.getVersions()) {
                        client.deleteObject(new DeleteObjectInput().setBucket(bucket)
                                .setKey(version.getKey()).setVersionID(version.getVersionID()));
                    }
                }
                if (output.getDeleteMarkers() != null) {
                    for (ListedDeleteMarkerEntry delete : output.getDeleteMarkers()) {
                        client.deleteObject(new DeleteObjectInput().setBucket(bucket)
                                .setKey(delete.getKey()).setVersionID(delete.getVersionID()));
                    }
                }
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }

            isTruncated = true;
            String marker = null;
            while(isTruncated) {
                ListObjectsV2Input input = new ListObjectsV2Input().setBucket(bucket)
                        .setMarker(marker);
                ListObjectsV2Output output = client.listObjects(input);
                if (output == null) {
                    break;
                }
                if (output.getContents() != null) {
                    for (ListedObjectV2 object : output.getContents()) {
                        client.deleteObject(new DeleteObjectInput().setBucket(bucket).setKey(object.getKey()));
                    }
                }
                isTruncated = output.isTruncated();
                marker = output.getNextMarker();
            }

            isTruncated = true;
            keyMarker = null;
            String uploadIdMarker = null;
            while(isTruncated) {
                ListMultipartUploadsV2Input input = new ListMultipartUploadsV2Input().setBucket(bucket)
                        .setKeyMarker(keyMarker).setUploadIDMarker(uploadIdMarker);
                ListMultipartUploadsV2Output output = client.listMultipartUploads(input);
                if (output == null) {
                    break;
                }
                if (output.getUploads() != null) {
                    for (ListedUpload upload : output.getUploads()) {
                        client.abortMultipartUpload(new AbortMultipartUploadInput().setBucket(bucket)
                                .setKey(upload.getKey()).setUploadID(upload.getUploadID()));
                    }
                }
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                uploadIdMarker = output.getNextUploadIdMarker();
            }
        } catch (TosException e) {
            Consts.LOG.error("clear bucket failed");
            e.printStackTrace();
            Assert.fail();
        }
    }

}

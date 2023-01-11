package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.*;

import java.util.ArrayList;
import java.util.List;

public class UploadPartCopyExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String srcBucketName = "your src bucket name";
        String srcObjectKey = "your src object key";
        String bucketName = "your bucket name";
        String objectKey = "your object key";

        long partSize = 10 * 1024 * 1024;

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            // 从源桶中获取待拷贝对象的大小
            HeadObjectV2Output headInfo = tos.headObject(new HeadObjectV2Input().setBucket(srcBucketName).setKey(srcObjectKey));
            long contentLength = headInfo.getHeadObjectBasicOutput().getContentLength();

            // 初始化分片拷贝任务
            CreateMultipartUploadInput create = new CreateMultipartUploadInput().setBucket(bucketName).setKey(objectKey);
            CreateMultipartUploadOutput created = tos.createMultipartUpload(create);
            String uploadID = created.getUploadID();

            // 进行分片拷贝
            List<UploadedPartV2> partList = new ArrayList<>();
            // 从位置 0 开始拷贝
            long rangeStart = 0, rangeEnd = 0;
            int partNumber = 1;
            while (rangeEnd < contentLength) {
                // 最长不能超过源对象长度
                rangeEnd = Math.min(rangeStart + partSize - 1, contentLength);
                UploadPartCopyV2Input input = new UploadPartCopyV2Input().setBucket(bucketName).setKey(objectKey)
                        .setUploadID(uploadID).setSourceBucket(srcBucketName).setSourceKey(srcObjectKey)
                        .setPartNumber(partNumber).setCopySourceRange(rangeStart, rangeEnd);
                UploadPartCopyV2Output output = tos.uploadPartCopy(input);
                System.out.println("uploadPartCopy succeed, part's etag is " + output.getEtag());
                System.out.println("uploadPartCopy succeed, part's partNumber is " + output.getPartNumber());
                partList.add(new UploadedPartV2().setEtag(output.getEtag()).setPartNumber(output.getPartNumber()));
                rangeStart = rangeEnd + 1;
                partNumber++;
            }

            // 合并拷贝的分片
            CompleteMultipartUploadV2Input complete = new CompleteMultipartUploadV2Input().setBucket(bucketName)
                    .setKey(objectKey).setUploadID(uploadID).setUploadedParts(partList);
            CompleteMultipartUploadV2Output output = tos.completeMultipartUpload(complete);
            System.out.println("copyObject succeed, object's etag is " + output.getEtag());
            System.out.println("copyObject succeed, object's crc64 is " + output.getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("copyObject failed");
            e.printStackTrace();
        }
    }
}

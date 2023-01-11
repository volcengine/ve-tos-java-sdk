package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

import java.util.ArrayList;
import java.util.List;

public class UploadPartCopyExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        // 源桶名
        String srcBucketName = "src-bucket-example";
        // 源对象名，需保证对象存在，否则报404
        String srcObjectKey = "src_example_dir/example_object.txt";
        // 目的桶名
        String bucketName = "bucket-example";
        // 目的对象名，如果目的对象存在，默认会将其覆盖
        String objectKey = "dst_example_dir/example_object.txt";

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
//                // 如果需要设置目的对象的 ACL/storageClass 等元数据，可参考以下代码
//                ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
//                // 指定 ACL 为 private
//                options.setAclType(ACLType.ACL_PRIVATE);
//                // 指定 storageClass 为标准存储
//                options.setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD);
//                input.setOptions(options);
//                // 指定限定条件，源对象的 ETag 与指定的 ETag 匹配时才拷贝，
//                // 指定的 ETag 可通过 headObject 获取，此处的 "XXX" 仅为示例，请使用正确的 ETag
//                input.setCopySourceIfMatch("XXX");
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
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("copyObject failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("copyObject failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("copyObject failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

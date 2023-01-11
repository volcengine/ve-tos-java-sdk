package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

public class RenameDirExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        // 源桶名
        String srcBucketName = "src-bucket-example";
        // 源目录
        String srcDir = "src_example_dir/";
        // 目的桶名
        String bucketName = "bucket-example";
        // 目的目录
        String dstDir = "dst_example_dir/";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            boolean isTruncated = true;
            String continuationToken = null;
            while (isTruncated) {
                ListObjectsType2Input input = new ListObjectsType2Input().setBucket(bucketName)
                        .setContinuationToken(continuationToken).setPrefix(srcDir);
                ListObjectsType2Output output = tos.listObjectsType2(input);
                System.out.printf("listObjectsType2 succeed, is truncated? %b, next continuation token is %s.\n",
                        output.isTruncated(), output.getNextContinuationToken());
                if (output.getContents() != null) {
                    for (int i = 0; i < output.getContents().size(); i++) {
                        ListedObjectV2 object = output.getContents().get(i);
                        // 重命名为 dstDir 目录下的文件
                        String dstKey = dstDir + object.getKey().substring(srcDir.length());
                        CopyObjectV2Input copy = new CopyObjectV2Input().setBucket(bucketName).setKey(dstKey)
                                .setSrcBucket(srcBucketName).setSrcKey(object.getKey());
//            // 如果需要设置目的对象的 ACL/storageClass 等元数据，或指定拷贝时元数据的继承/重写方式，可参考以下代码
//            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
//            // 指定 ACL 为 private
//            options.setAclType(ACLType.ACL_PRIVATE);
//            // 指定 storageClass 为标准存储
//            options.setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD);
//            input.setOptions(options);
//            // 指定限定条件，源对象的 ETag 与指定的 ETag 匹配时才拷贝，
//            // 指定的 ETag 可通过 headObject 获取，此处的 "XXX" 仅为示例，请使用正确的 ETag
//            input.setCopySourceIfMatch("XXX");
//            // 默认情况下目的对象的元数据，除了 ACL 之外，全部继承自源对象的元数据
//            // 可通过以下设置，指定拷贝时重写对象元数据，不继承源对象的元数据。
//            input.setMetadataDirective(MetadataDirectiveType.METADATA_DIRECTIVE_REPLACE);
                        CopyObjectV2Output copyOutput = tos.copyObject(copy);
                        System.out.println("copyObject succeed, object's etag is " + copyOutput.getEtag());
                        System.out.println("copyObject succeed, object's crc64 is " + copyOutput.getHashCrc64ecma());

                        // 删除源数据
                        tos.deleteObject(new DeleteObjectInput().setBucket(srcBucketName).setKey(object.getKey()));
                        System.out.println("rename object in " + srcDir + ", from " + object.getKey() + " to " + dstKey);
                    }
                }
                isTruncated = output.isTruncated();
                continuationToken = output.getNextContinuationToken();
            }

        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("rename Object failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("rename Object failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("rename Object failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

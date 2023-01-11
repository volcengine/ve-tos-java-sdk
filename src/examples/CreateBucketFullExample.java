package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.bucket.CreateBucketV2Input;
import com.volcengine.tos.model.bucket.CreateBucketV2Output;

public class CreateBucketFullExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        /*
         * 桶名字符长度为3~63个字符
         * 桶名字符集包括：小写字母a-z、数字0-9和连字符'-'
         * 桶名不能以连字符'-'作为开头或结尾
         */
        String bucketName = "your bucket name";
        // 设置桶的访问权限
        ACLType aclType = ACLType.ACL_PRIVATE;
        // 设置桶的存储类型
        StorageClassType storageClassType = StorageClassType.STORAGE_CLASS_STANDARD;

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            CreateBucketV2Input input = new CreateBucketV2Input().setBucket(bucketName)
                    .setAcl(aclType).setStorageClass(storageClassType);
            CreateBucketV2Output output = tos.createBucket(input);
            System.out.println("bucket created: " + output.getLocation());
        } catch (TosException e) {
            System.out.println("createBucket failed");
            e.printStackTrace();
        }
    }
}

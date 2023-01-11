package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.model.object.PutObjectACLInput;
import com.volcengine.tos.model.object.PutObjectACLOutput;

public class PutObjectAclExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";


        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            // 授予 objectKey 对象公共读权限
            PutObjectACLInput input = new PutObjectACLInput().setAcl(ACLType.ACL_PUBLIC_READ)
                    .setBucket(bucketName).setKey(objectKey);
            PutObjectACLOutput output = tos.putObjectAcl(input);
            System.out.println("putObjectAcl succeed, request info is " + output);
        } catch (TosException e) {
            System.out.println("putObjectAcl failed");
            e.printStackTrace();
        }
    }
}

package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.GetObjectACLV2Input;
import com.volcengine.tos.model.object.GetObjectACLV2Output;

public class GetObjectAclExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";


        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetObjectACLV2Input input = new GetObjectACLV2Input().setBucket(bucketName).setKey(objectKey);
            GetObjectACLV2Output output = tos.getObjectAcl(input);
            System.out.println("getObjectAcl succeed, full acl info is " + output);
        } catch (TosException e) {
            System.out.println("getObjectAcl failed");
            e.printStackTrace();
        }
    }
}

package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.DeleteObjectInput;
import com.volcengine.tos.model.object.DeleteObjectOutput;


public class DeleteObjectBasicExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            DeleteObjectInput input = new DeleteObjectInput().setBucket(bucketName).setKey(objectKey);
            DeleteObjectOutput output = tos.deleteObject(input);
            System.out.println("deleteObject succeed, " + output);
        } catch (TosException e) {
            System.out.println("deleteObject failed");
            e.printStackTrace();
        }
    }
}

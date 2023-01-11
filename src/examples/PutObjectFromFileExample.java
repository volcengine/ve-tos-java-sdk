package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.PutObjectBasicInput;
import com.volcengine.tos.model.object.PutObjectFromFileInput;
import com.volcengine.tos.model.object.PutObjectFromFileOutput;

public class PutObjectFromFileExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";
        String filePath = "your file's path to putObject";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try {
            PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucketName).setKey(objectKey);
            PutObjectFromFileInput putObjectInput = new PutObjectFromFileInput().setPutObjectBasicInput(basicInput).setFilePath(filePath);
            PutObjectFromFileOutput output = tos.putObjectFromFile(putObjectInput);
            System.out.println("putObject succeed, object's etag is " + output.getPutObjectOutput().getEtag());
            System.out.println("putObject succeed, object's crc64 is " + output.getPutObjectOutput().getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("putObjectFromFile failed");
            e.printStackTrace();
        }
    }
}

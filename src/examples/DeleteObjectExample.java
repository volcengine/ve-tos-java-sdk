package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.DeleteObjectInput;
import com.volcengine.tos.model.object.DeleteObjectOutput;

public class DeleteObjectExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";
        // 如果桶开启了多版本功能，可以指定版本号删除对象
        String versionID = "the specific version id";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            DeleteObjectInput input = new DeleteObjectInput().setBucket(bucketName).setKey(objectKey);
            // 如果桶开启了多版本功能，可以指定版本号删除对象
            input.setVersionID(versionID);
            DeleteObjectOutput output = tos.deleteObject(input);
            System.out.println("deleteObject succeed, request info is " + output.getRequestInfo());
        } catch (TosException e) {
            System.out.println("deleteObject failed");
            e.printStackTrace();
        }
    }
}

package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.GetObjectToFileInput;
import com.volcengine.tos.model.object.GetObjectToFileOutput;
import com.volcengine.tos.model.object.GetObjectV2Input;

public class GetObjectToFileExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        // 需要确保下载的数据已存在
        String objectKey = "your object key";
        String filePath = "your file path";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetObjectV2Input input = new GetObjectV2Input().setBucket(bucketName).setKey(objectKey);
            // 以下代码展示如何将数据下载到本地文件
            GetObjectToFileInput fileInput = new GetObjectToFileInput(input, filePath);
            GetObjectToFileOutput fileOutput = tos.getObjectToFile(fileInput);
            System.out.println("getObjectToFile succeed, object's metadata is " + fileOutput.getGetObjectBasicOutput());
        } catch (TosException e) {
            if (e.getStatusCode() == 404) {
                // 下载不存在的对象会返回404
                System.out.println("the object you want to download is not found");
            } else {
                System.out.println("getObjectToFile failed");
            }
            e.printStackTrace();
        }
    }
}

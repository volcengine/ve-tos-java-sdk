package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.GetObjectToFileInput;
import com.volcengine.tos.model.object.GetObjectToFileOutput;
import com.volcengine.tos.model.object.GetObjectV2Input;

public class GetObjectToFileExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 对象名
        String objectKey = "example_dir/example_object.txt";
        // 对象数据保存的本地文件路径，需保证不存在，否则会覆盖原有文件
        String filePath = "example_dir/example_file.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetObjectV2Input input = new GetObjectV2Input().setBucket(bucketName).setKey(objectKey);
            // 以下代码展示如何将数据下载到本地文件
            GetObjectToFileInput fileInput = new GetObjectToFileInput(input, filePath);
            GetObjectToFileOutput fileOutput = tos.getObjectToFile(fileInput);
            System.out.println("getObjectToFile succeed, object's metadata is " + fileOutput.getGetObjectBasicOutput());
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("getObject failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("getObject failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("getObject failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

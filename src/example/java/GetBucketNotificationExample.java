package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.bucket.CloudFunctionConfiguration;
import com.volcengine.tos.model.bucket.GetBucketNotificationInput;
import com.volcengine.tos.model.bucket.GetBucketNotificationOutput;

public class GetBucketNotificationExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetBucketNotificationInput input = new GetBucketNotificationInput().setBucket(bucketName);
            GetBucketNotificationOutput output = tos.getBucketNotification(input);
            System.out.println("getBucketNotification succeed");
            if (output.getCloudFunctionConfiguration() != null) {
                System.out.println("this bucket has " + output.getCloudFunctionConfiguration().size() + " notification rules");
                for (int i = 0; i < output.getCloudFunctionConfiguration().size(); i++){
                    CloudFunctionConfiguration conf = output.getCloudFunctionConfiguration().get(i);
                    // 事件通知名称
                    System.out.println("Cloud Function Configuration ID:" + conf.getId());
                    // 订阅的事件
                    if (conf.getEvents() != null && conf.getEvents().size() > 0) {
                        for(int j = 0; j < conf.getEvents().size(); j++) {
                            System.out.println("Cloud Function Configuration Events " + j + " is " + conf.getEvents().get(j));
                        }
                    }
                    // 设置匹配对象的前缀信息和后缀信息
                    System.out.println("Cloud Function Configuration Filter " + conf.getFilter());
                    // 设置订阅事件的函数服务
                    System.out.println("Cloud Function Configuration CloudFunction " + conf.getCloudFunction());
                }
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("getBucketNotification failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("getBucketNotification failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("getBucketNotification failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

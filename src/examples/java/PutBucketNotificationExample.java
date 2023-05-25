package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.bucket.*;

import java.util.ArrayList;
import java.util.List;

public class PutBucketNotificationExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        // 事件通知名称
        String id = "event notification id";
        // 设置订阅事件的函数服务
        String cloudFunction = "your cloud function";

        String event = "tos:ObjectCreated:Put";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            // 需要订阅的事件，以下仅为示例，含义是上传对象事件
            List<String> events = new ArrayList<>();
            events.add(event);
            FilterRule rule = new FilterRule().setName("prefix").setValue("test-");
            List<FilterRule> rules = new ArrayList<>();
            rules.add(rule);
            Filter filter = new Filter().setKey(new FilterKey().setRules(rules));
            CloudFunctionConfiguration configuration = new CloudFunctionConfiguration()
                    .setId(id).setCloudFunction(cloudFunction)
                    .setEvents(events)
                    // 设置匹配对象的前缀信息和后缀信息
                    .setFilter(filter);
            List<CloudFunctionConfiguration> configurations = new ArrayList<>();
            configurations.add(configuration);
            PutBucketNotificationInput input = new PutBucketNotificationInput().setBucket(bucketName)
                    .setCloudFunctionConfigurations(configurations);
            PutBucketNotificationOutput output = tos.putBucketNotification(input);
            System.out.println("putBucketNotification succeed");
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putBucketNotification failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putBucketNotification failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putBucketNotification failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

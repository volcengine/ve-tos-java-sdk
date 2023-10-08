package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.bucket.GetBucketReplicationInput;
import com.volcengine.tos.model.bucket.GetBucketReplicationOutput;
import com.volcengine.tos.model.bucket.ReplicationRule;

public class GetBucketReplicationAllExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";
        String bucketName = "bucket-example";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetBucketReplicationInput input = new GetBucketReplicationInput().setBucket(bucketName);
            GetBucketReplicationOutput output = tos.getBucketReplication(input);
            if (output.getRules() != null && output.getRules().size() > 0) {
                for (int i = 0; i < output.getRules().size(); i++){
                    ReplicationRule rule = output.getRules().get(i);
                    // 规则 ID
                    System.out.println("rule id:" + rule.getId());
                    // 规则状态
                    System.out.println("rule status:" + rule.getStatus());
                    for (String prefix : rule.getPrefixSet()) {
                        System.out.println("rule prefix:" + prefix);
                    }
                    // 目标桶
                    System.out.println("rule dst bucket:" + rule.getDestination().getBucket());
                    // 目标地域
                    System.out.println("rule dst location:" + rule.getDestination().getLocation());
                    // 存储类型
                    System.out.println("rule dst storage class:" + rule.getDestination().getStorageClass());
                    // 存储类型规则
                    System.out.println("rule dst storage class inherit directive:" +
                            rule.getDestination().getStorageClassInheritDirectiveType());
                }
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("getBucketLocation failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("getBucketLocation failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("getBucketLocation failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

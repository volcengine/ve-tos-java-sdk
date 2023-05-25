package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.bucket.*;

public class GetBucketWebsiteExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetBucketWebsiteInput input = new GetBucketWebsiteInput().setBucket(bucketName);
            GetBucketWebsiteOutput output = tos.getBucketWebsite(input);
            System.out.println("getBucketWebsite succeed");
            if (output.getRedirectAllRequestsTo() != null) {
                System.out.println("getBucketWebsite RedirectAllRequestsTo HostName:" + output.getRedirectAllRequestsTo().getHostname());
                System.out.println("getBucketWebsite RedirectAllRequestsTo Protocol:" + output.getRedirectAllRequestsTo().getProtocol());
            }
            if (output.getIndexDocument() != null) {
                System.out.println("getBucketWebsite IndexDocument ForbiddenSubDir:" + output.getIndexDocument().isForbiddenSubDir());
                System.out.println("getBucketWebsite IndexDocument Suffix:" + output.getIndexDocument().getSuffix());
            }
            if (output.getErrorDocument() != null) {
                System.out.println("getBucketWebsite ErrorDocument Key:" + output.getErrorDocument().getKey());
            }
            if (output.getRoutingRules() != null && output.getRoutingRules().size() > 0) {
                for(int i = 0; i < output.getRoutingRules().size(); i++) {
                    RoutingRule rule = output.getRoutingRules().get(i);
                    System.out.println("No." + i + " rule");
                    if (rule.getCondition() != null) {
                        System.out.println("getBucketWebsite RoutingRules Rule Condition HttpErrorCodeReturnedEquals:" + rule.getCondition().getHttpErrorCodeReturnedEquals());
                        System.out.println("getBucketWebsite RoutingRules Rule Condition KeyPrefixEquals:" + rule.getCondition().getKeyPrefixEquals());
                    }
                    if (rule.getRedirect() != null) {
                        System.out.println("getBucketWebsite RoutingRules Rule Redirect HostName:" + rule.getRedirect().getHostname());
                        System.out.println("getBucketWebsite RoutingRules Rule Redirect Protocol:" + rule.getRedirect().getProtocol());
                        System.out.println("getBucketWebsite RoutingRules Rule Redirect ReplaceKeyWith:" + rule.getRedirect().getReplaceKeyWith());
                        System.out.println("getBucketWebsite RoutingRules Rule Redirect ReplaceKeyPrefixWith:" + rule.getRedirect().getReplaceKeyPrefixWith());
                        System.out.println("getBucketWebsite RoutingRules Rule Redirect HttpRedirectCode:" + rule.getRedirect().getHttpRedirectCode());
                    }
                }
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("getBucketWebsite failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("getBucketWebsite failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("getBucketWebsite failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.common.ProtocolType;
import com.volcengine.tos.model.bucket.*;

import java.util.ArrayList;
import java.util.List;

public class PutBucketWebsiteExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            // 场景1 重定向所有的请求到另一个站点
            RedirectAllRequestsTo redirectAllRequestsTo = new RedirectAllRequestsTo().setHostname("www.example.com").setProtocol("http");
            PutBucketWebsiteInput input = new PutBucketWebsiteInput().setBucket(bucketName)
                    .setRedirectAllRequestsTo(redirectAllRequestsTo);
            PutBucketWebsiteOutput output = tos.putBucketWebsite(input);
            System.out.println("putBucketWebsite succeed");

            // 场景2 根据规则重定向请求
            // RoutingRule 设置前缀匹配，获取重定向结果
            RoutingRule rule = new RoutingRule()
                    // 重定向规则的条件配置
                    .setCondition(new RoutingRuleCondition()
                            // 指定重定向规则的对象键前缀匹配条件
                            .setKeyPrefixEquals("prefix")
                            // 指定重定向规则的错误码匹配条件
                            .setHttpErrorCodeReturnedEquals(404))
                    // 重定向规则的具体重定向目标配置
                    .setRedirect(new RoutingRuleRedirect()
                            // 指定重定向规则的目标协议
                            .setProtocol(ProtocolType.PROTOCOL_HTTP)
                            // 指定重定向的目的地址
                            .setHostname("www.redirect.com"));
            List<RoutingRule> rules = new ArrayList<>();
            rules.add(rule);
            IndexDocument indexDocument = new IndexDocument().setSuffix("index.html").setForbiddenSubDir(true);
            ErrorDocument errorDocument = new ErrorDocument().setKey("error.html");
            input = new PutBucketWebsiteInput().setBucket(bucketName)
                    .setRoutingRules(rules)
                    .setIndexDocument(indexDocument)
                    .setErrorDocument(errorDocument);
            output = tos.putBucketWebsite(input);
            System.out.println("putBucketWebsite succeed");
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putBucketWebsite failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putBucketWebsite failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putBucketWebsite failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.common.StatusType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.bucket.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PutBucketLifecycleExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            List<LifecycleRule> rules = new ArrayList<>();
            // rule1，对象创建 70 天后过期
            LifecycleRule rule1 = new LifecycleRule()
                    .setId("rule1")
                    .setPrefix("test1/")
                    .setStatus(StatusType.STATUS_ENABLED)
                    .setExpiration(new Expiration().setDays(70));
            rules.add(rule1);
            // rule2，对象于指定时间后过期
            Date expirationDate = Date.from(LocalDateTime.of(2023, 12, 31, 0, 0, 0, 0).toInstant(ZoneOffset.UTC));
            LifecycleRule rule2 = new LifecycleRule()
                    .setId("rule2")
                    .setPrefix("test2/")
                    .setStatus(StatusType.STATUS_ENABLED)
                    .setExpiration(new Expiration().setDate(expirationDate));
            rules.add(rule2);
            // rule3，使用分片上传接口，已上传但未合并的分片的过期规则
            AbortInCompleteMultipartUpload abortRule = new AbortInCompleteMultipartUpload().setDaysAfterInitiation(10);
            LifecycleRule rule3 = new LifecycleRule()
                    .setId("rule3")
                    .setPrefix("test3/")
                    .setStatus(StatusType.STATUS_ENABLED)
                    .setAbortInCompleteMultipartUpload(abortRule);
            rules.add(rule3);
            // rule4，设置对象的转化规则。指定对象在 30 天后转化为低频存储
            Transition transition = new Transition().setDays(30).setStorageClass(StorageClassType.STORAGE_CLASS_IA);
            List<Transition> transitions = new ArrayList<>();
            transitions.add(transition);
            LifecycleRule rule4 = new LifecycleRule()
                    .setId("rule4")
                    .setPrefix("test4/")
                    .setStatus(StatusType.STATUS_ENABLED)
                    .setTransitions(transitions);
            rules.add(rule4);
            // rule5，设置匹配标签，与 rule4 不同在于，除了匹配前缀外需对象被设置了对应标签才能匹配此规则
            Tag tag = new Tag().setKey("key1").setValue("value1");
            List<Tag> tags = new ArrayList<>();
            tags.add(tag);
            LifecycleRule rule5 = new LifecycleRule()
                    .setId("rule5")
                    .setPrefix("test5/")
                    .setStatus(StatusType.STATUS_ENABLED)
                    .setTransitions(transitions)
                    .setTags(tags);
            rules.add(rule5);
            // rule6，针对开启多版本对象的桶
            // 设置最新版本在70天后过期
            // 历史版本在 30 天后转化为低频存储
            // 历史版本在 40 天后过期
            NoncurrentVersionTransition noncurrentVersionTransition = new NoncurrentVersionTransition().setNoncurrentDays(30)
                    .setStorageClass(StorageClassType.STORAGE_CLASS_IA);
            List<NoncurrentVersionTransition> noncurrentVersionTransitions = new ArrayList<>();
            noncurrentVersionTransitions.add(noncurrentVersionTransition);
            NoncurrentVersionExpiration noncurrentVersionExpiration = new NoncurrentVersionExpiration().setNoncurrentDays(40);
            LifecycleRule rule6 = new LifecycleRule()
                    .setId("rule6")
                    .setPrefix("test6/")
                    .setStatus(StatusType.STATUS_ENABLED)
                    .setExpiration(new Expiration().setDays(70))
                    .setNoncurrentVersionExpiration(noncurrentVersionExpiration)
                    .setNoncurrentVersionTransitions(noncurrentVersionTransitions);
            rules.add(rule6);

            PutBucketLifecycleInput input = new PutBucketLifecycleInput().setBucket(bucketName).setRules(rules);
            PutBucketLifecycleOutput output = tos.putBucketLifecycle(input);
            System.out.println("putBucketLifecycle succeed");
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putBucketLifecycle failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putBucketLifecycle failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putBucketLifecycle failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

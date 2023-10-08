package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.common.RedirectType;
import com.volcengine.tos.model.bucket.*;

import java.util.ArrayList;
import java.util.List;

public class PutBucketMirrorBackExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            // 设置回源条件，比如在返回 404 后回源
            Condition condition = new Condition().setHttpCode(404);
            // 指定镜像回源携带的header
            List<String> passList = new ArrayList<>();
            passList.add("pass_header1");
            passList.add("pass_header2");
            List<String> removeList = new ArrayList<>();
            removeList.add("remove_header1");
            removeList.add("remove_header2");
            MirrorHeader mirrorHeader = new MirrorHeader().setPassAll(true)
                    .setPass(passList).setRemove(removeList);

            // 配置公共可访问的源端配置
            List<String> primary = new ArrayList<>();
            primary.add("http://www.volcengine.com/obj/tostest/1");
            List<String> follower = new ArrayList<>();
            follower.add("http://www.volcengine.com/obj/tostest/2");
            SourceEndpoint sourceEndpoint = new SourceEndpoint().setPrimary(primary).setFollower(follower);
            PublicSource publicSource = new PublicSource().setSourceEndpoint(sourceEndpoint);
            // 设置重定向后执行的动作
            Redirect redirect = new Redirect()
                    .setRedirectType(RedirectType.REDIRECT_MIRROR)
                    // 表明重定向后是否去配置的源站拉取信息
                    .setFetchSourceOnRedirect(true)
                    // 执行跳转或者镜像回源规则时，是否携带请求参数
                    .setPassQuery(true)
                    // 镜像回源获取的结果为 3xx 时，是否继续跳转到指定的 Location 获取数据
                    .setFollowRedirect(true)
                    .setMirrorHeader(mirrorHeader)
                    .setPublicSource(publicSource);
            MirrorBackRule rule = new MirrorBackRule().setId("1").setCondition(condition).setRedirect(redirect);
            List<MirrorBackRule> rules = new ArrayList<>();
            rules.add(rule);
            PutBucketMirrorBackInput input = new PutBucketMirrorBackInput().setBucket(bucketName).setRules(rules);
            PutBucketMirrorBackOutput output = tos.putBucketMirrorBack(input);
            System.out.println("putBucketMirrorBack succeed");
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putBucketMirrorBack failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putBucketMirrorBack failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putBucketMirrorBack failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.GetObjectBasicOutput;
import com.volcengine.tos.model.object.HeadObjectV2Input;
import com.volcengine.tos.model.object.HeadObjectV2Output;

import java.util.Map;

public class HeadObjectExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        String objectKey = "example_dir/example_object.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            HeadObjectV2Input input = new HeadObjectV2Input().setBucket(bucketName).setKey(objectKey);
            HeadObjectV2Output output = tos.headObject(input);
            GetObjectBasicOutput meta = output.getHeadObjectBasicOutput();
            System.out.println("headObject succeed.");
            System.out.println("object's contentType is " + meta.getContentType());
            System.out.println("object's etag is " + meta.getEtag());
            System.out.println("object's storageClass is " + meta.getStorageClass());
            System.out.println("object's contentDisposition is " + meta.getContentDisposition());
            System.out.println("object's lastModified is " + meta.getLastModified());
            System.out.println("object's cacheControl is " + meta.getCacheControl());
            System.out.println("object's contentLength is " + meta.getContentLength());
            System.out.println("object's expires is " + meta.getExpires());
            System.out.println("object's contentEncoding is " + meta.getContentEncoding());
            System.out.println("object's contentLanguage is " + meta.getContentLanguage());
            // 如果桶开启了多版本，getVersionID 会有值，默认为对象的最新版本
            System.out.println("object's version id is " + meta.getVersionID());
            if (meta.getCustomMetadata() != null && meta.getCustomMetadata().size() > 0) {
                System.out.println("object has custom meta data.");
                for (Map.Entry<String, String> custom : meta.getCustomMetadata().entrySet()) {
                    System.out.println("object custom meta data key is " + custom.getKey() + ", value is " + custom.getValue());
                }
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("headObject failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("headObject failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("headObject failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

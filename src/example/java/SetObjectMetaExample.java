package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.ObjectMetaRequestOptions;
import com.volcengine.tos.model.object.SetObjectMetaInput;
import com.volcengine.tos.model.object.SetObjectMetaOutput;

import java.util.HashMap;
import java.util.Map;

public class SetObjectMetaExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 对象名，需保证对象已存在
        String objectKey = "example_dir/example_object.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            // 以下所有设置的参数值均为可选参数，仅供参考，请根据业务实际需要进行设置
            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();

            // 设置 HTTP Header。
            // 指定上传的内容类型。内容类型决定浏览器将以什么形式读取文件。
            // SDK 会默认根据 objectKey 的后缀扩展名识别设置，如果没有扩展名则设置默认值 binary/octet-stream。
            options.setContentType("text/plain");
            // 设置内容被下载时的名称。
            options.setContentDisposition("attachment; filename=\"DownloadFileName\"");
            // 设置内容被下载时网页的缓存行为。
            options.setCacheControl("no-cache");

            // 设置自定义 Header。
            Map<String, String> customMeta = new HashMap<>();
            customMeta.put("userCustomKey", "userCustomValue");
            options.setCustomMetadata(customMeta);

            SetObjectMetaInput input = new SetObjectMetaInput().setBucket(bucketName).setKey(objectKey).setOptions(options);
            SetObjectMetaOutput output = tos.setObjectMeta(input);
            System.out.println("setObjectMeta succeed");
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("setObjectMeta failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("setObjectMeta failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("setObjectMeta failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.AbortMultipartUploadInput;

public class AbortMultipartUploadExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 指定的需要取消的分片上传任务的uploadID，
        // 需保证该 uploadId 已通过初始化分片上传接口 createMultipartUpload 调用返回。
        // 否则，对于不存在的 uploadId 会返回 404 not found。
        String uploadId = "the specific uploadId";
        // 与 uploadId 对应的对象 key，模拟 example_dir 下的 example_object.txt 文件
        String objectKey = "example_dir/example_object.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            AbortMultipartUploadInput input = new AbortMultipartUploadInput().setBucket(bucketName)
                    .setKey(objectKey).setUploadID(uploadId);
            tos.abortMultipartUpload(input);
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("abortMultipartUpload failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("abortMultipartUpload failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("abortMultipartUpload failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

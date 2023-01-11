package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

public class AbortMultipartUploadWithPrefixExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        String prefix = "example_dir/";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            boolean isTruncated = true;
            String keyMarker = null;
            String uploadIdMarker = null;
            while (isTruncated) {
                ListMultipartUploadsV2Input input = new ListMultipartUploadsV2Input().setBucket(bucketName)
                        .setKeyMarker(keyMarker).setUploadIDMarker(uploadIdMarker).setPrefix(prefix);
                ListMultipartUploadsV2Output output = tos.listMultipartUploads(input);
                System.out.printf("listMultipartUploads succeed, is truncated? %b, next uploadIDMarker is %s, " +
                        "next keyMarker is %s.\n", output.isTruncated(), output.getNextUploadIdMarker(), output.getNextKeyMarker());
                if (output.getUploads() != null) {
                    for (int i = 0; i < output.getUploads().size(); i++) {
                        ListedUpload upload = output.getUploads().get(i);
                        AbortMultipartUploadInput abort = new AbortMultipartUploadInput().setBucket(bucketName)
                                .setKey(upload.getKey()).setUploadID(upload.getUploadID());
                        tos.abortMultipartUpload(abort);
                        System.out.println("Aborted upload is " + upload);
                    }
                }
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                uploadIdMarker = output.getNextUploadIdMarker();
            }
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

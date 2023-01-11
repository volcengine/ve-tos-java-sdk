package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

public class ListMultipartUploadsByPageExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        int maxKeys = 10;

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
        try{
            ListMultipartUploadsV2Input input = new ListMultipartUploadsV2Input().setBucket(bucketName)
                    .setMaxUploads(maxKeys);
            ListMultipartUploadsV2Output output = tos.listMultipartUploads(input);
            System.out.printf("listMultipartUploads succeed, is truncated? %b, next uploadIDMarker is %s, " +
                    "next keyMarker is %s.\n", output.isTruncated(), output.getNextUploadIdMarker(), output.getNextKeyMarker());
            if (output.getUploads() != null) {
                for (int i = 0; i < output.getUploads().size(); i++) {
                    ListedUpload upload = output.getUploads().get(i);
                    System.out.printf("Listed upload, key is %s, uploadId is %s, storageClass is %s\n", 
                            upload.getKey(), upload.getUploadID(), upload.getStorageClass().getStorageClass());
                }
            }
            if (output.getCommonPrefixes() != null) {
                for (int i = 0; i < output.getCommonPrefixes().size(); i++) {
                    ListedCommonPrefix commonPrefix = output.getCommonPrefixes().get(i);
                    System.out.println("Listed commonPrefix is " + commonPrefix.getPrefix());
                }
            }
            if (output.isTruncated()) {
                // isTruncated 为 true 说明接下来还有数据，可以继续列举
                // 下一页的列举需要设置上一次列举返回的 NextKeyMarker 和 NextUploadIDMarker，必须同时设置
                input.setKeyMarker(output.getNextKeyMarker()).setUploadIDMarker(output.getNextUploadIdMarker());
                output = tos.listMultipartUploads(input);
                System.out.printf("listMultipartUploads succeed, is truncated? %b, next uploadIDMarker is %s, " +
                        "next keyMarker is %s.\n", output.isTruncated(), output.getNextUploadIdMarker(), output.getNextKeyMarker());
                if (output.getUploads() != null) {
                    for (int i = 0; i < output.getUploads().size(); i++) {
                        ListedUpload upload = output.getUploads().get(i);
                        System.out.printf("Listed upload, key is %s, uploadId is %s, storageClass is %s\n", 
                            upload.getKey(), upload.getUploadID(), upload.getStorageClass().getStorageClass());
                    }
                }
                if (output.getCommonPrefixes() != null) {
                    for (int i = 0; i < output.getCommonPrefixes().size(); i++) {
                        ListedCommonPrefix commonPrefix = output.getCommonPrefixes().get(i);
                        System.out.println("Listed commonPrefix is " + commonPrefix.getPrefix());
                    }
                }
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("listMultipartUploads failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("listMultipartUploads failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("listMultipartUploads failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

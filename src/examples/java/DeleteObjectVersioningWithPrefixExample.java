package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

public class DeleteObjectVersioningWithPrefixExample {
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
            String versionIdMarker = null;
            while (isTruncated) {
                ListObjectVersionsV2Input input = new ListObjectVersionsV2Input().setBucket(bucketName)
                        .setPrefix(prefix).setKeyMarker(keyMarker).setVersionIDMarker(versionIdMarker);
                ListObjectVersionsV2Output output = tos.listObjectVersions(input);
                if (output.getVersions() != null){
                    for (int i = 0; i < output.getVersions().size(); i++) {
                        ListedObjectVersion version = output.getVersions().get(i);
                        DeleteObjectInput deleteInput = new DeleteObjectInput().setBucket(bucketName)
                                .setKey(version.getKey()).setVersionID(version.getVersionID());
                        tos.deleteObject(deleteInput);
                        System.out.println("deleteObject succeed, deleted key is " + version);
                    }
                }
                if (output.getDeleteMarkers() != null){
                    for (int i = 0; i < output.getDeleteMarkers().size(); i++) {
                        ListedDeleteMarkerEntry deleteMarker = output.getDeleteMarkers().get(i);
                        DeleteObjectInput deleteInput = new DeleteObjectInput().setBucket(bucketName)
                                .setKey(deleteMarker.getKey()).setVersionID(deleteMarker.getVersionID());
                        tos.deleteObject(deleteInput);
                        System.out.println("deleteObject succeed, deleted key is " + deleteMarker);
                    }
                }
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }

        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("deleteObject failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("deleteObject failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("deleteObject failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

package example.java;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.object.GetObjectV2Input;
import com.volcengine.tos.model.object.GetObjectV2Output;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class VideoSnapshotSaveAsExample {
    static class SaveAsResult {
        String bucket;
        String fileSize;
        String object;
        String status;

        @Override
        public String toString() {
            return "SaveAsResult{" +
                    "bucket='" + bucket + '\'' +
                    ", fileSize='" + fileSize + '\'' +
                    ", object='" + object + '\'' +
                    ", status='" + status + '\'' +
                    '}';
        }
    }

    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 需要确保下载的数据已存在
        String objectKey = "video.mp4";
        String style = "video/snapshot,t_300"; //视频截帧，截取第 300ms 的视频帧
        String saveBucket = "bucket-example";
        String saveObject = "temp.jpg";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try {
            GetObjectV2Input input = new GetObjectV2Input().setBucket(bucketName).setKey(objectKey).setProcess(style)
                    .setSaveBucket(Base64.getUrlEncoder().encodeToString(saveBucket.getBytes(StandardCharsets.UTF_8)))
                    .setSaveObject(Base64.getUrlEncoder().encodeToString(saveObject.getBytes(StandardCharsets.UTF_8)));
            try (ByteArrayOutputStream stream = new ByteArrayOutputStream();
                 GetObjectV2Output output = tos.getObject(input)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = output.getContent().read(buffer)) != -1) {
                    stream.write(buffer, 0, length);
                }
                String respBody = stream.toString("UTF-8");
                System.out.println("Image inspect success, inspect info is " + respBody);
                SaveAsResult result = TosUtils.getJsonMapper().readValue(respBody, new TypeReference<SaveAsResult>() {
                });
                System.out.println(result.toString());
            } catch (JacksonException e) {
                System.out.println("parse response data failed");
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("read response data failed");
                e.printStackTrace();
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("Image saveAs failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("Image saveAs failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("Image saveAs failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

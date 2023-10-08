package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.util.ratelimit.DefaultRateLimiter;
import com.volcengine.tos.model.object.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class PutObjectWithProgressAndRateLimiterExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 对象名，模拟 example_dir 下的 example_object.txt 文件
        String objectKey = "example_dir/example_object.txt";
        // 本地文件路径，请保证文件存在，暂不支持文件夹功能
        String filePath = "example_dir/example_file.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try(FileInputStream inputStream = new FileInputStream(filePath)){
            PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucketName).setKey(objectKey);

            // 以下代码展示如何处理进度条
            // 在 ObjectMetaRequestOptions 中设置文件大小，可在进度条中显示 total 总长度，否则 DataTransferStatus.getTotalBytes 值为 -1。
            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions().setContentLength(new File(filePath).length());
            basicInput.setOptions(options);
            // 自定义实现 DataTransferListener，实现进度条功能
            DataTransferListener listener = getDataTransferListener();
            basicInput.setDataTransferListener(listener);

            // 以下代码展示如何设置客户端限速
            // 配置上传对象最大限速为 20MB/s，平均限速为 5MB/s。
            RateLimiter limiter = new DefaultRateLimiter(20 * 1024 * 1024, 5 * 1024 * 1024);
            basicInput.setRateLimiter(limiter);

            PutObjectInput putObjectInput = new PutObjectInput().setPutObjectBasicInput(basicInput).setContent(inputStream);
            PutObjectOutput output = tos.putObject(putObjectInput);
            System.out.println("putObject succeed, object's etag is " + output.getEtag());
            System.out.println("putObject succeed, object's crc64 is " + output.getHashCrc64ecma());
        } catch (IOException e) {
            System.out.println("putObject read file failed");
            e.printStackTrace();
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putObject failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putObject failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putObject failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }

    private static DataTransferListener getDataTransferListener() {
        return new DataTransferListener() {
            // 自定义实现 DataTransferListener 的 dataTransferStatusChange 接口
            @Override
            public void dataTransferStatusChange(DataTransferStatus dataTransferStatus) {
                if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_STARTED) {
                    System.out.println("putObject started.");
                } else if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_RW) {
                    System.out.printf("putObject, send %d bytes once, has sent %d bytes, total %d bytes.\n",
                            dataTransferStatus.getRwOnceBytes(), dataTransferStatus.getConsumedBytes(),
                            dataTransferStatus.getTotalBytes());
                } else if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_FAILED) {
                    System.out.printf("putObject failed, has sent %d bytes, total %d bytes.\n",
                            dataTransferStatus.getConsumedBytes(), dataTransferStatus.getTotalBytes());
                } else if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_SUCCEED) {
                    System.out.printf("putObject succeed, has sent %d bytes, total %d bytes.\n",
                            dataTransferStatus.getConsumedBytes(), dataTransferStatus.getTotalBytes());
                }
            }
        };
    }
}

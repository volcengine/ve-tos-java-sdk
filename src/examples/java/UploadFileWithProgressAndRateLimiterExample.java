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
import com.volcengine.tos.model.object.CreateMultipartUploadInput;
import com.volcengine.tos.model.object.ObjectMetaRequestOptions;
import com.volcengine.tos.model.object.UploadFileV2Input;
import com.volcengine.tos.model.object.UploadFileV2Output;

import java.io.File;

public class UploadFileWithProgressAndRateLimiterExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 对象名，模拟 example_dir 下的 example_object.txt 文件
        String objectKey = "example_dir/example_object.txt";
        // uploadFilePath 设置待上传的本地文件路径，建议使用绝对路径，确保文件存在，不支持上传文件夹
        String uploadFilePath = "example_dir/example_file.txt";
        // taskNum 设置并发上传的并发数，范围为 1-1000，默认为 1
        int taskNum = 5;
        // partSize 设置文件分片大小，范围为 5MB-5GB，默认为 20MB
        long partSize = 20 * 1024 * 1024;
        // enableCheckpoint 设置是否开启断点续传功能，开启则会在本地记录上传进度
        boolean enableCheckpoint = true;
        // checkpointFilePath 设置断点续传记录文件存放位置，若不设置则默认在 uploadFilePath 路径下生成
        // 其格式为 {uploadFilePath}.{bucket+objectKey 的 Base64Md5 值}.upload
        String checkpointFilePath = "the checkpoint file path";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            CreateMultipartUploadInput create = new CreateMultipartUploadInput().setBucket(bucketName).setKey(objectKey);
            UploadFileV2Input input = new UploadFileV2Input().setFilePath(uploadFilePath).setEnableCheckpoint(enableCheckpoint)
                    .setCheckpointFile(checkpointFilePath).setPartSize(partSize).setTaskNum(taskNum);

            // 以下代码展示如何处理进度条
            // 在 ObjectMetaRequestOptions 中设置文件大小，可在进度条中显示 total 总长度，否则 DataTransferStatus.getTotalBytes 值为 -1。
            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions().setContentLength(new File(uploadFilePath).length());
            create.setOptions(options);
            input.setCreateMultipartUploadInput(create);
            // 自定义实现 DataTransferListener，实现进度条功能
            DataTransferListener listener = getDataTransferListener();
            input.setDataTransferListener(listener);

            // 以下代码展示如何设置客户端限速
            // 配置上传对象最大限速为 20MB/s，平均限速为 5MB/s。
            RateLimiter limiter = new DefaultRateLimiter(20 * 1024 * 1024, 5 * 1024 * 1024);
            input.setRateLimiter(limiter);

            UploadFileV2Output output = tos.uploadFile(input);
            System.out.println("uploadFile succeed, object's etag is " + output.getEtag());
            System.out.println("uploadFile succeed, object's crc64 is " + output.getHashCrc64ecma());
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("uploadFile failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("uploadFile failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("uploadFile failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }

    private static DataTransferListener getDataTransferListener() {
        return new DataTransferListener() {
            // 自定义实现 DataTransferListener 的 dataTransferStatusChange 接口
            @Override
            public void dataTransferStatusChange(DataTransferStatus dataTransferStatus) {
                if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_STARTED) {
                    System.out.println("uploadFile started.");
                } else if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_RW) {
                    System.out.printf("uploadFile, send %d bytes once, has sent %d bytes, total %d bytes.\n",
                            dataTransferStatus.getRwOnceBytes(), dataTransferStatus.getConsumedBytes(),
                            dataTransferStatus.getTotalBytes());
                } else if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_FAILED) {
                    System.out.printf("uploadFile failed, has sent %d bytes, total %d bytes.\n",
                            dataTransferStatus.getConsumedBytes(), dataTransferStatus.getTotalBytes());
                } else if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_SUCCEED) {
                    System.out.printf("uploadFile succeed, has sent %d bytes, total %d bytes.\n",
                            dataTransferStatus.getConsumedBytes(), dataTransferStatus.getTotalBytes());
                }
            }
        };
    }
}

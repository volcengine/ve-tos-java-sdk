package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.DownloadFileInput;
import com.volcengine.tos.model.object.DownloadFileOutput;
import com.volcengine.tos.model.object.HeadObjectV2Input;

public class DownloadFileExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 对象名，如果对象名以 "/"(linux 或 macOS 系统) 或 "\"(Windows 系统)结尾，将在本地生成对应空文件夹
        String objectKey = "example_dir/example_object.txt";

        // downloadFilePath 设置待下载的文件路径，建议使用绝对路径，确保路径下不存在文件，否则会将其覆盖。
        String downloadFilePath = "example_dir/example_file.txt";
        // taskNum 设置并发上传的并发数，范围为 1-1000
        int taskNum = 5;
        // partSize 设置文件分片大小，范围为 5MB - 5GB，默认为 20MB
        long partSize = 10 * 1024 * 1024;
        // enableCheckpoint 设置是否开启断点续传功能，开启则会在本地记录上传进度
        boolean enableCheckpoint = true;
        // checkpointFilePath 设置断点续传记录文件存放位置，若不设置则默认在 downloadFilePath 路径下生成
        // 其格式为 {downloadFilePath}.{bucket+objectKey+versionID 的 Base64Md5 值}.download
        String checkpointFilePath = "the checkpoint file path";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            HeadObjectV2Input head = new HeadObjectV2Input().setBucket(bucketName).setKey(objectKey);
            DownloadFileInput input = new DownloadFileInput().setHeadObjectV2Input(head)
                    .setFilePath(downloadFilePath).setEnableCheckpoint(enableCheckpoint)
                    .setCheckpointFile(checkpointFilePath).setPartSize(partSize).setTaskNum(taskNum);
            DownloadFileOutput output = tos.downloadFile(input);
            System.out.println("downloadFile succeed, object's etag is " + output.getOutput().getHeadObjectBasicOutput().getEtag());
            System.out.println("downloadFile succeed, object's crc64 is " + output.getOutput().getHeadObjectBasicOutput().getHashCrc64ecma());
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("downloadFile failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("downloadFile failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("downloadFile failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

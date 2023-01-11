package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.DownloadEventType;
import com.volcengine.tos.model.object.*;

public class DownloadFileWithCancelExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";

        // downloadFilePath 设置待下载的文件路径，建议使用绝对路径，确保路径下不存在文件，否则会将其覆盖。
        // 如果 objectKey 以 "/"(linux 或 macOS 系统) 或 "\"(Windows 系统)结尾，将在本地生成对应空文件夹
        String downloadFilePath = "the path of file to download";
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
            // 以下代码通过 DownloadEventListener 监听下载事件。
            // 如果出现 DownloadEventDownloadPartFailed 事件，即有下载失败的分片时就终止下载任务。
            // 以下代码仅作为示例，用户可根据业务需要进行使用。
            boolean isAbort = true;
            DownloadEventListener listener = new DownloadEventListener() {
                @Override
                public void eventChange(DownloadEvent downloadEvent) {
                    if (downloadEvent.getDownloadEventType() == DownloadEventType.DownloadEventDownloadPartFailed) {
                        System.out.println("event change, uploadPart failed");
                        if (input.getCancelHook() != null) {
                            // 调用 cancel 时，如果 isAbort 为 true，会终止断点续传，删除本地 checkpoint 文件，
                            // 并清理本地已下载的临时文件。
                            // 如果 isAbort 为 false，只会暂停当前下载任务，再次调用 downloadFile 可从断点处继续下载。
                            input.getCancelHook().cancel(isAbort);
                        }
                    }
                }
            };
            input.setCancelHook(true).setDownloadEventListener(listener);
            DownloadFileOutput output = tos.downloadFile(input);
            System.out.println("downloadFile succeed, object's etag is " + output.getOutput().getHeadObjectBasicOutput().getEtag());
            System.out.println("downloadFile succeed, object's crc64 is " + output.getOutput().getHeadObjectBasicOutput().getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("downloadFile failed");
            e.printStackTrace();
        }
    }
}

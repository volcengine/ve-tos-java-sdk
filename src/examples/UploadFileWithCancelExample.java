package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.UploadEventType;
import com.volcengine.tos.model.object.*;

public class UploadFileWithCancelExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";

        // uploadFilePath 设置待上传的文件路径，建议使用绝对路径，确保文件存在，不支持上传文件夹
        String uploadFilePath = "the path of file to upload";
        // taskNum 设置并发上传的并发数，范围为 1-1000
        int taskNum = 5;
        // partSize 设置文件分片大小，范围为 5MB - 5GB，默认为 20MB
        long partSize = 10 * 1024 * 1024;
        // enableCheckpoint 设置是否开启断点续传功能，开启则会在本地记录上传进度
        boolean enableCheckpoint = true;
        // checkpointFilePath 设置断点续传记录文件存放位置，若不设置则默认在 uploadFilePath 路径下生成
        // 其格式为 {uploadFilePath}.{bucket+objectKey 的 Base64Md5 值}.upload
        String checkpointFilePath = "the checkpoint file path";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            CreateMultipartUploadInput create = new CreateMultipartUploadInput().setBucket(bucketName).setKey(objectKey);
            UploadFileV2Input input = new UploadFileV2Input().setCreateMultipartUploadInput(create)
                    .setFilePath(uploadFilePath).setEnableCheckpoint(enableCheckpoint)
                    .setCheckpointFile(checkpointFilePath).setPartSize(partSize).setTaskNum(taskNum);
            // 以下代码通过 UploadEventListener 监听上传事件。
            // 如果出现 UploadEventUploadPartFailed 事件，即有上传失败的分片时就终止上传任务。
            // 以下代码仅作为示例，用户可根据业务需要进行使用。
            boolean isAbort = true;
            UploadEventListener listener = new UploadEventListener() {
                @Override
                public void eventChange(UploadEvent uploadEvent) {
                    if (uploadEvent.getUploadEventType() == UploadEventType.UploadEventUploadPartFailed) {
                        System.out.println("event change, uploadPart failed");
                        if (input.getCancelHook() != null) {
                            // 调用 cancel 时，如果 isAbort 为 true，会终止断点续传，删除本地 checkpoint 文件，
                            // 并调用 abortMultipartUpload 取消分片上传。
                            // 如果 isAbort 为 false，只会暂停当前上传任务，再次调用 uploadFile 可从断点处续传。
                            input.getCancelHook().cancel(isAbort);
                        }
                    }
                }
            };
            input.setCancelHook(true).setUploadEventListener(listener);
            UploadFileV2Output output = tos.uploadFile(input);
            System.out.println("uploadFile succeed, object's etag is " + output.getEtag());
            System.out.println("uploadFile succeed, object's crc64 is " + output.getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("uploadFile failed");
            e.printStackTrace();
        }
    }
}

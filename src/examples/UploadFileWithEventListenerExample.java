package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.UploadEventType;
import com.volcengine.tos.model.object.*;

public class UploadFileWithEventListenerExample {
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
        // 其格式为 {uploadFilePath}.{bucket+objectKey 的 md5 值}.upload
        String checkpointFilePath = "the checkpoint file path";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            CreateMultipartUploadInput create = new CreateMultipartUploadInput().setBucket(bucketName).setKey(objectKey);
            UploadEventListener listener = new UploadEventListener() {
                @Override
                public void eventChange(UploadEvent uploadEvent) {
                    if (uploadEvent.getUploadEventType() == UploadEventType.UploadEventCreateMultipartUploadSucceed) {
                        System.out.println("event change, createMultipartUpload succeed");
                    }
                    if (uploadEvent.getUploadEventType() == UploadEventType.UploadEventCreateMultipartUploadFailed) {
                        System.out.println("event change, createMultipartUpload failed");
                    }
                    if (uploadEvent.getUploadEventType() == UploadEventType.UploadEventUploadPartSucceed) {
                        System.out.println("event change, uploadPart succeed");
                    }
                    if (uploadEvent.getUploadEventType() == UploadEventType.UploadEventUploadPartFailed) {
                        System.out.println("event change, uploadPart failed");
                    }
                    if (uploadEvent.getUploadEventType() == UploadEventType.UploadEventUploadPartAborted) {
                        System.out.println("event change, uploadPart aborted");
                    }
                    if (uploadEvent.getUploadEventType() == UploadEventType.UploadEventCompleteMultipartUploadSucceed) {
                        System.out.println("event change, completeMultipartUpload succeed");
                    }
                    if (uploadEvent.getUploadEventType() == UploadEventType.UploadEventCompleteMultipartUploadFailed) {
                        System.out.println("event change, completeMultipartUpload failed");
                    }
                }
            };
            UploadFileV2Input input = new UploadFileV2Input().setCreateMultipartUploadInput(create)
                    .setFilePath(uploadFilePath).setEnableCheckpoint(enableCheckpoint)
                    .setCheckpointFile(checkpointFilePath).setPartSize(partSize).setTaskNum(taskNum)
                    .setUploadEventListener(listener);
            UploadFileV2Output output = tos.uploadFile(input);
            System.out.println("uploadFile succeed, object's etag is " + output.getEtag());
            System.out.println("uploadFile succeed, object's crc64 is " + output.getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("uploadFile failed");
            e.printStackTrace();
        }
    }
}

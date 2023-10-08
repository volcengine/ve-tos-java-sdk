package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UploadFileWithDirectoryExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // uploadFileDirectory 设置待上传的本地文件夹，建议使用绝对路径
        String uploadFileDirectory = "example_dir";

        // 以 100MB 作为阈值，大于等于此阈值使用 uploadFile 接口上传，小于则使用 putObjectFromFile 上传
        // 此值仅供参考，请根据客户端资源情况进行调整
        long threshold = 100 * 1024 * 1024;

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        List<String> fileList = getFileListFromPath(uploadFileDirectory);
        if (fileList == null || fileList.size() == 0) {
            System.out.println("uploadFile done, there are no files under " + uploadFileDirectory);
            return;
        }

        for (String file : fileList) {
            // 循环遍历文件夹下面的文件，使用文件名作为对象名进行上传。
            if (new File(file).length() >= threshold) {
                uploadLargeFile(tos, bucketName, file);
            } else {
                uploadSmallFile(tos, bucketName, file);
            }
        }
    }

    private static void uploadSmallFile(TOSV2 tos, String bucketName, String file) {
        try {
            PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucketName).setKey(file);
            PutObjectFromFileInput putObjectInput = new PutObjectFromFileInput().setPutObjectBasicInput(basicInput).setFilePath(file);
            PutObjectFromFileOutput output = tos.putObjectFromFile(putObjectInput);
            System.out.println("putObject succeed, object's etag is " + output.getPutObjectOutput().getEtag());
            System.out.println("putObject succeed, object's crc64 is " + output.getPutObjectOutput().getHashCrc64ecma());
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

    private static void uploadLargeFile(TOSV2 tos, String bucketName, String file) {
        // taskNum 设置并发上传的并发数，范围为 1-1000，默认为 1
        int taskNum = 5;
        // partSize 设置文件分片大小，范围为 5MB-5GB，默认为 20MB
        long partSize = 20 * 1024 * 1024;
        // enableCheckpoint 设置是否开启断点续传功能，开启则会在本地记录上传进度
        boolean enableCheckpoint = true;
        // checkpointFilePath 设置断点续传记录文件存放位置，若不设置则默认在 uploadFileDirectory 路径下生成
        // 其格式为 {uploadFileDirectory}.{bucket+objectKey 的 Base64Md5 值}.upload
        String checkpointFilePath = "the checkpoint file path";
        try{
            CreateMultipartUploadInput create = new CreateMultipartUploadInput().setBucket(bucketName).setKey(file);
            UploadFileV2Input input = new UploadFileV2Input().setCreateMultipartUploadInput(create)
                    .setFilePath(file).setEnableCheckpoint(enableCheckpoint)
                    .setCheckpointFile(checkpointFilePath).setPartSize(partSize).setTaskNum(taskNum);
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

    private static List<String> getFileListFromPath(String filePath) {
        List<String> fileList = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return fileList;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return fileList;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                fileList.addAll(getFileListFromPath(f.getPath()));
            } else {
                fileList.add(f.getPath());
            }
        }
        return fileList;
    }
}

package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MultipartUploadWithFileFullExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 对象名，模拟 example_dir 下的 example_object.txt 文件
        String objectKey = "example_dir/example_object.txt";
        // 本地文件路径，请保证文件存在，暂不支持文件夹功能。
        String filePath = "example_dir/example_file.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        String uploadId = null;
        // 1. 初始化分片上传
        try{
            CreateMultipartUploadInput create = new CreateMultipartUploadInput().setBucket(bucketName).setKey(objectKey);
//            // 如果需要设置对象的元数据，需要在初始化分片上传的时候设置
//            // 以下所有设置参数均为可选，参数值仅供参考，请根据业务实际需要进行设置。
//            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
//            // 设置对象访问权限，此处为私有权限
//            options.setAclType(ACLType.ACL_PRIVATE);
//            // 设置对象存储类型
//            options.setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD);
//            // SDK 默认会根据 objectKey 后缀识别 Content-Type，也可以自定义设置
//            options.setContentType("text/plain");
//            // 自定义对象的元数据，对于自定义的元数据，SDK 会自动对 key 添加
//            // "X-Tos-Meta-" 的前缀，因此用户无需自行添加。
//            Map<String, String> custom = new HashMap<>();
//            custom.put("name", "volc_user");
//            // 在 TOS 服务端存储的元数据为："X-Tos-Meta-name: volc_user"
//            options.setCustomMetadata(custom);
//            create.setOptions(options);

            CreateMultipartUploadOutput createOutput = tos.createMultipartUpload(create);
            System.out.println("createMultipartUpload succeed, uploadId is " + createOutput.getUploadID());

            // 从 createMultipartUpload 结果中获取 uploadId，用于后续的分片上传和合并分片。
            uploadId = createOutput.getUploadID();

            // 2. 上传分片。
            // 假设分片大小统一为 5MB。
            long partSize = 5 * 1024 * 1024;

            // 已上传分片列表，每次上传分片后记录。
            List<UploadedPartV2> uploadedParts = new ArrayList<>();

            // 以下代码展示如何使用 uploadPartFromFile 接口上传分片，按照每 5MB 大小从头到尾读取文件的一部分进行上传。

            // fileSize 为文件总大小
            long fileSize = new File(filePath).length();
            // offset 为读取文件的位置。如果 offset < fileSize，说明已读到文件末尾，不再读取上传（此时文件已上传完成）
            long offset = 0;
            for (int i = 1; offset < fileSize; ++i) {
                // 注意 partNumber 从 1 开始计数。
                int partNumber = i;
                if (fileSize-offset < partSize) {
                    // 如果 skip 过后剩余的数据长度小于 partSize，即剩余数据长度没有达到 partSize
                    // 说明已到达最后一个分片，需要修改分片大小
                    partSize = fileSize-offset;
                }
                UploadPartBasicInput basicInput = new UploadPartBasicInput().setBucket(bucketName)
                        .setKey(objectKey).setUploadID(uploadId).setPartNumber(partNumber);
                UploadPartFromFileInput input = new UploadPartFromFileInput().setUploadPartBasicInput(basicInput)
                        .setFilePath(filePath).setPartSize(partSize).setOffset(offset);
                UploadPartFromFileOutput output = tos.uploadPartFromFile(input);
                // 存储已上传分片信息，必须设置 partNumber 和 etag
                uploadedParts.add(new UploadedPartV2().setPartNumber(partNumber).setEtag(output.getUploadPartV2Output().getEtag()));
                System.out.printf("uploadPart succeed, partNumber is %d, etag is %s, crc64 value is %s\n",
                        output.getUploadPartV2Output().getPartNumber(), output.getUploadPartV2Output().getEtag(),
                        output.getUploadPartV2Output().getHashCrc64ecma());

                // 每上传一次分片，需要更新 offset 的值
                offset += partSize;
            }

            // 3. 合并已上传的分片
            // 需要设置桶名、对象名、uploadId和已上传的分片列表
            CompleteMultipartUploadV2Input complete = new CompleteMultipartUploadV2Input().setBucket(bucketName)
                    .setKey(objectKey).setUploadID(uploadId).setUploadedParts(uploadedParts);
            CompleteMultipartUploadV2Output completedOutput = tos.completeMultipartUpload(complete);
            System.out.printf("completeMultipartUpload succeed, etag is %s, crc64 value is %s, location is %s.\n",
                    completedOutput.getEtag(), completedOutput.getHashCrc64ecma(), completedOutput.getLocation());
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("multipart upload failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("multipart upload failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("multipart upload failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

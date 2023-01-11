package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.object.*;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultipartUploadFullExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        String uploadID = null;
        // 1. 初始化分片上传
        try{
            CreateMultipartUploadInput create = new CreateMultipartUploadInput().setBucket(bucketName).setKey(objectKey);
            // 如果需要设置对象的元数据，需要在初始化分片上传的时候设置
            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
            // 设置对象访问权限，此处为私有权限
            options.setAclType(ACLType.ACL_PRIVATE);
            // 设置对象访问授权列表
            options.setGrantFullControl("id=123,id=456");
            options.setGrantRead("id=567");
            options.setGrantReadAcp("id=678");
            options.setGrantWrite("id=567");
            options.setGrantWriteAcp("id=678");
            // 设置对象存储类型
            options.setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD);
            // SDK 默认会根据 objectKey 后缀识别 Content-Type，也可以自定义设置
            options.setContentType("text/plain");
            // 设置对象内容语言
            options.setContentLanguage("en-US");
            // 设置对象内容编码方式
            options.setContentEncoding("gzip");
            // 设置对象被下载时的名称
            options.setContentDisposition("attachment;filename=123.txt");
            // 设置对象的网页缓存行为
            options.setCacheControl("no-cache, no-store, must-revalidate");
            // 设置对象的服务端加密方式，当前只支持 AES-256
            options.setServerSideEncryption("AES-256");
            // 自定义对象的元数据，对于自定义的元数据，SDK 会自动对 key 添加
            // "X-Tos-Meta-" 的前缀，因此用户无需自行添加。
            Map<String, String> custom = new HashMap<>();
            custom.put("name", "volc_user");
            // 在 TOS 服务端存储的元数据为："X-Tos-Meta-name: volc_user"
            options.setCustomMetadata(custom);
            create.setOptions(options);

            CreateMultipartUploadOutput createOutput = tos.createMultipartUpload(create);
            System.out.println("createMultipartUpload succeed, uploadID is " + createOutput.getUploadID());

            uploadID = createOutput.getUploadID();
        } catch (TosException e) {
            System.out.println("createMultipartUpload failed");
            e.printStackTrace();
        }

        // 2. 上传分片，有多种方式可以上传，以下示例分别展示。
        // 假设分片大小统一为 5MB，共上传 5 个分片。
        long partSize = 5 * 1024 * 1024;
        // 已上传分片列表
        List<UploadedPartV2> uploadedParts = new ArrayList<>(5);
        // 第 1-3 个分片通过读取本地文件到 FileInputStream 上传。
        String filePath = "your data file path";
        for (int i = 1; i <= 3; ++i) {
            try{
                InputStream content = new FileInputStream(filePath);
                // 每次只上传文件的一部分，需要跳过前面已上传的部分。
                long skip = (i-1) * partSize;
                content.skip(skip);
                UploadPartBasicInput basicInput = new UploadPartBasicInput().setBucket(bucketName)
                        .setKey(objectKey).setUploadID(uploadID).setPartNumber(i);
                UploadPartV2Input input = new UploadPartV2Input().setUploadPartBasicInput(basicInput)
                        .setContentLength(partSize).setContent(content);
                UploadPartV2Output output = tos.uploadPart(input);
                // 存储已上传分片信息，必须设置 partNumber 和 etag
                uploadedParts.add(new UploadedPartV2().setPartNumber(i).setEtag(output.getEtag()));
                System.out.printf("uploadPart succeed, partNumber is %d, etag is %s, crc64 value is %s\n",
                        output.getPartNumber(), output.getEtag(), output.getHashCrc64ecma());
            }catch (IOException | TosException e) {
                System.out.println("uploadPart failed");
                e.printStackTrace();
            }
        }

        // 第 4 个分片通过 uploadPartFromFile 上传。
        try{
            UploadPartBasicInput basicInput = new UploadPartBasicInput().setBucket(bucketName)
                    .setKey(objectKey).setUploadID(uploadID).setPartNumber(4);
            // 每次只上传文件的一部分，需要跳过前面已上传的部分。
            // 之前已上传 3 个分片，此处跳过前 3 个分片数据，从 offset 的位置开始读取文件数据。
            long offset = 3 * partSize;
            UploadPartFromFileInput input = new UploadPartFromFileInput().setUploadPartBasicInput(basicInput)
                    .setFilePath(filePath).setPartSize(partSize).setOffset(offset);
            UploadPartFromFileOutput output = tos.uploadPartFromFile(input);
            // 存储已上传分片信息，必须设置 partNumber 和 etag
            uploadedParts.add(new UploadedPartV2().setPartNumber(4).setEtag(output.getUploadPartV2Output().getEtag()));
            System.out.printf("uploadPart succeed, partNumber is %d, etag is %s, crc64 value is %s\n",
                    output.getUploadPartV2Output().getPartNumber(), output.getUploadPartV2Output().getEtag(),
                    output.getUploadPartV2Output().getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("uploadPart failed");
            e.printStackTrace();
        }

        // 第 5 个分片通过在内存中新建 byte 数组上传。
        try{
            UploadPartBasicInput basicInput = new UploadPartBasicInput().setBucket(bucketName)
                    .setKey(objectKey).setUploadID(uploadID).setPartNumber(5);
            // byte 数组数据，对于小 size 的数据可以使用。
            // 大 size 的数据用 byte 数组内存开销较大，而且 int 长度最长只能支持到约 4GB，不建议使用。
            byte[] data = new byte[(int)partSize];
            InputStream content = new ByteArrayInputStream(data);
            UploadPartV2Input input = new UploadPartV2Input().setUploadPartBasicInput(basicInput)
                    .setContentLength(data.length).setContent(content);
            UploadPartV2Output output = tos.uploadPart(input);
            // 存储已上传分片信息，必须设置 partNumber 和 etag
            uploadedParts.add(new UploadedPartV2().setPartNumber(5).setEtag(output.getEtag()));
            System.out.printf("uploadPart succeed, partNumber is %d, etag is %s, crc64 value is %s\n",
                    output.getPartNumber(), output.getEtag(), output.getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("uploadPart failed");
            e.printStackTrace();
        }

        // 合并已上传的分片
        try{
            CompleteMultipartUploadV2Input complete = new CompleteMultipartUploadV2Input().setBucket(bucketName)
                    .setKey(objectKey).setUploadID(uploadID).setUploadedParts(uploadedParts);
            CompleteMultipartUploadV2Output completedOutput = tos.completeMultipartUpload(complete);
            System.out.printf("completeMultipartUpload succeed, etag is %s, crc64 value is %s, location is %s.\n",
                    completedOutput.getEtag(), completedOutput.getHashCrc64ecma(), completedOutput.getLocation());
        }catch (TosException e) {
            System.out.println("completeMultipartUpload failed");
            e.printStackTrace();
        }

    }
}

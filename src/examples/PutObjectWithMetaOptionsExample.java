package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.object.ObjectMetaRequestOptions;
import com.volcengine.tos.model.object.PutObjectBasicInput;
import com.volcengine.tos.model.object.PutObjectInput;
import com.volcengine.tos.model.object.PutObjectOutput;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

public class PutObjectWithMetaOptionsExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
            ByteArrayInputStream stream = new ByteArrayInputStream(data.getBytes());
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
            // 设置对象MD5，用于服务端校验数据是否与客户端传输的一致
            options.setContentMD5("yjtlyPoGKxvDj+QOPocqjg==");
            // 设置对象的服务端加密方式，当前只支持 AES-256
            options.setServerSideEncryption("AES-256");
            // 自定义对象的元数据，对于自定义的元数据，SDK 会自动对 key 添加
            // "X-Tos-Meta-" 的前缀，因此用户无需自行添加。
            Map<String, String> custom = new HashMap<>();
            custom.put("name", "volc_user");
            // 在 TOS 服务端存储的元数据为："X-Tos-Meta-name: volc_user"
            options.setCustomMetadata(custom);

            PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucketName).setKey(objectKey).setOptions(options);
            PutObjectInput putObjectInput = new PutObjectInput().setPutObjectBasicInput(basicInput).setContent(stream);
            PutObjectOutput output = tos.putObject(putObjectInput);
            System.out.println("putObject succeed, object's etag is " + output.getEtag());
            System.out.println("putObject succeed, object's crc64 is " + output.getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("putObject failed");
            e.printStackTrace();
        }
    }
}

package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.StorageClassType;
import com.volcengine.tos.model.object.AppendObjectInput;
import com.volcengine.tos.model.object.AppendObjectOutput;
import com.volcengine.tos.model.object.ObjectMetaRequestOptions;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AppendObjectExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            // 第一次追加写，请确保传输的数据长度大于等于 128KB
            byte[] data1 = new byte[128 * 1024];
            Arrays.fill(data1, (byte) 'A');
            ByteArrayInputStream stream = new ByteArrayInputStream(data1);
            // 如果需要设置 appendable 对象的元数据，可在第一次追加写的时候设置，后续无需再添加
            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
            // 设置对象访问权限，此处为私有权限
            options.setAclType(ACLType.ACL_PRIVATE);
            // 设置对象存储类型
            options.setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD);
            // SDK 默认会根据 objectKey 后缀识别 Content-Type，也可以自定义设置
            options.setContentType("application/json");
            // 自定义对象的元数据，对于自定义的元数据，SDK 会自动对 key 添加
            // "X-Tos-Meta-" 的前缀，因此用户无需自行添加。
            Map<String, String> custom = new HashMap<>();
            custom.put("name", "volc_user");
            // 在 TOS 服务端存储的元数据为："X-Tos-Meta-name: volc_user"
            options.setCustomMetadata(custom);

            // 附：ObjectMetaRequestOptions 支持 builder 风格构造对象，示例如下
//            ObjectMetaRequestOptions options = ObjectMetaRequestOptions.builder()
//                    .aclType(ACLType.ACL_PRIVATE)
//                    .storageClass(StorageClassType.STORAGE_CLASS_STANDARD)
//                    .contentType("application/json")
//                    .customMetadata(custom)
//                    .build();

            // 注意：当前 TOS 使用 appendObject 接口时需要传入数据长度和偏移量
            long contentLength = data1.length;
            // 第一次追加写，偏移量为0
            long offset = 0;
            AppendObjectInput input = new AppendObjectInput().setBucket(bucketName).setKey(objectKey)
                    .setContent(stream).setContentLength(contentLength).setOffset(offset).setOptions(options);
            AppendObjectOutput output = tos.appendObject(input);
            System.out.println("appendObject first time succeed, object's nextAppendOffset is " + output.getNextAppendOffset());
            System.out.println("appendObject first time succeed, object's crc64 is " + output.getHashCrc64ecma());

            // 第二次追加写
            byte[] data2 = new byte[128 * 1024 + 1024];
            Arrays.fill(data2, (byte) 'B');
            // 偏移量为当前对象的长度，即从对象末尾追加写数据。
            contentLength = data2.length;
            offset = data1.length;
            // 由于 SDK 默认开启 crc64 校验，从第二次追加写开始，之后每次调用都需要传入上一次追加写请求返回的 crc64 值。
            String preHashCrc64 = output.getHashCrc64ecma();
            input = new AppendObjectInput().setBucket(bucketName).setKey(objectKey).setContent(new ByteArrayInputStream(data2))
                    .setOffset(offset).setContentLength(contentLength).setPreHashCrc64ecma(preHashCrc64);
            output = tos.appendObject(input);
            System.out.println("appendObject second time succeed, object's nextAppendOffset is " + output.getNextAppendOffset());
            System.out.println("appendObject second time succeed, object's crc64 is " + output.getHashCrc64ecma());

            // 第三次追加写
            byte[] data3 = new byte[256 * 1024 + 1024];
            Arrays.fill(data3, (byte) 'C');
            // 偏移量为当前对象的长度，即从对象末尾追加写数据。
            contentLength = data3.length;
            offset = data1.length + data2.length;
            // 由于 SDK 默认开启 crc64 校验，从第二次追加写开始，之后每次调用都需要传入上一次追加写请求返回的 crc64 值。
            preHashCrc64 = output.getHashCrc64ecma();
            input = new AppendObjectInput().setBucket(bucketName).setKey(objectKey).setContent(new ByteArrayInputStream(data3))
                    .setOffset(offset).setContentLength(contentLength).setPreHashCrc64ecma(preHashCrc64);
            output = tos.appendObject(input);
            System.out.println("appendObject third time succeed, object's nextAppendOffset is " + output.getNextAppendOffset());
            System.out.println("appendObject third time succeed, object's crc64 is " + output.getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("appendObject failed");
            e.printStackTrace();
        }
    }
}

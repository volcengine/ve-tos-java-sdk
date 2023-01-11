package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
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

        String bucketName = "bucket-example";
        // 对象名，模拟 example_dir 下的 example_object.txt 文件
        String objectKey = "example_dir/example_object.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            String data = "1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'1234567890abcdefghijklmnopqrstuvwxyz~!@#$%^&*()_+<>?,./   :'";
            ByteArrayInputStream stream = new ByteArrayInputStream(data.getBytes());
            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
            // 以下所有设置参数均为可选，参数值仅供参考，请根据业务实际需要进行设置。
            // 设置对象访问权限，此处为私有权限
            options.setAclType(ACLType.ACL_PRIVATE);
            // 设置对象存储类型
            options.setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD);
            // SDK 默认会根据 objectKey 后缀识别 Content-Type，也可以自定义设置
            options.setContentType("text/plain");
            // 设置对象MD5，用于服务端校验数据是否与客户端传输的一致。此 MD5 值需要您自行计算传入
            options.setContentMD5("yjtlyPoGKxvDj+QOPocqjg==");
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
}

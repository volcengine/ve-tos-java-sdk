package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.AppendObjectInput;
import com.volcengine.tos.model.object.AppendObjectOutput;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AppendObjectWithFileInputStreamExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 对象名，模拟 example_dir 下的 example_object.txt 文件
        String objectKey = "example_dir/example_object.txt";
        // 本地文件路径，请保证文件存在，暂不支持文件夹功能
        String filePath1 = "example_dir/example_file_1.txt";
        // 本地文件路径，请保证文件存在，暂不支持文件夹功能
        String filePath2 = "example_dir/example_file_2.txt";
        // 本地文件路径，请保证文件存在，暂不支持文件夹功能
        String filePath3 = "example_dir/example_file_3.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            AppendObjectOutput output = null;
            // 第一次追加写
            try(FileInputStream stream = new FileInputStream(filePath1)) {
                // 注意：当前 TOS 使用 appendObject 接口时需要传入数据长度和偏移量
                // 此处为文件长度
                long contentLength = new File(filePath1).length();
                // 第一次追加写，偏移量为0
                long offset = 0;
                AppendObjectInput input = new AppendObjectInput().setBucket(bucketName).setKey(objectKey)
                        .setContent(stream).setContentLength(contentLength).setOffset(offset);

//                // 如果需要设置 appendable 对象的元数据，可在第一次追加写的时候设置，后续无需再添加
//                ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
//                // 以下所有设置参数均为可选，参数值仅供参考，请根据业务实际需要进行设置。
//                // 设置对象访问权限，此处为私有权限
//                options.setAclType(ACLType.ACL_PRIVATE);
//                // 设置对象存储类型
//                options.setStorageClass(StorageClassType.STORAGE_CLASS_STANDARD);
//                // SDK 默认会根据 objectKey 后缀识别 Content-Type，也可以自定义设置
//                options.setContentType("application/json");
//                // 自定义对象的元数据，对于自定义的元数据，SDK 会自动对 key 添加
//                // "X-Tos-Meta-" 的前缀，因此用户无需自行添加。
//                Map<String, String> custom = new HashMap<>();
//                custom.put("name", "volc_user");
//                // 在 TOS 服务端存储的元数据为："X-Tos-Meta-name: volc_user"
//                options.setCustomMetadata(custom);
//                input.setOptions(options);

                output = tos.appendObject(input);
                System.out.println("appendObject first time succeed, object's nextAppendOffset is " + output.getNextAppendOffset());
                System.out.println("appendObject first time succeed, object's crc64 is " + output.getHashCrc64ecma());
            } catch (IOException e) {
                System.out.println("appendObject read file failed");
                e.printStackTrace();
            }


            // 第二次追加写
            try(FileInputStream stream = new FileInputStream(filePath2)) {
                // 注意：当前 TOS 使用 appendObject 接口时需要传入数据长度和偏移量
                // 此处为文件长度
                long contentLength = new File(filePath2).length();
                // 偏移量可以从上次 appendObject 的结果中获取，也可以通过 headObject 获取 content-length。
                long offset = output.getNextAppendOffset();
                // 由于 SDK 默认开启 crc64 校验，从第二次追加写开始，之后每次调用都需要传入上一次追加写请求返回的 crc64 值。
                String preHashCrc64 = output.getHashCrc64ecma();
                AppendObjectInput input = new AppendObjectInput().setBucket(bucketName).setKey(objectKey).setContent(stream)
                        .setOffset(offset).setContentLength(contentLength).setPreHashCrc64ecma(preHashCrc64);
                output = tos.appendObject(input);
                System.out.println("appendObject second time succeed, object's nextAppendOffset is " + output.getNextAppendOffset());
                System.out.println("appendObject second time succeed, object's crc64 is " + output.getHashCrc64ecma());
            } catch (IOException e) {
                System.out.println("appendObject read file failed");
                e.printStackTrace();
            }

            // 第三次追加写
            try(FileInputStream stream = new FileInputStream(filePath3)) {
                // 注意：当前 TOS 使用 appendObject 接口时需要传入数据长度和偏移量
                // 此处为文件长度
                long contentLength = new File(filePath3).length();
                // 偏移量可以从上次 appendObject 的结果中获取。
                long offset = output.getNextAppendOffset();
                // 由于 SDK 默认开启 crc64 校验，从第二次追加写开始，之后每次调用都需要传入上一次追加写请求返回的 crc64 值。
                String preHashCrc64 = output.getHashCrc64ecma();
                AppendObjectInput input = new AppendObjectInput().setBucket(bucketName).setKey(objectKey).setContent(stream)
                        .setOffset(offset).setContentLength(contentLength).setPreHashCrc64ecma(preHashCrc64);
                output = tos.appendObject(input);
                System.out.println("appendObject second time succeed, object's nextAppendOffset is " + output.getNextAppendOffset());
                System.out.println("appendObject second time succeed, object's crc64 is " + output.getHashCrc64ecma());
            } catch (IOException e) {
                System.out.println("appendObject read file failed");
                e.printStackTrace();
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("appendObject failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("appendObject failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("appendObject failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

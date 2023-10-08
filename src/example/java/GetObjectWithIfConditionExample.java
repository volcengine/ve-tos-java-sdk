package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetObjectWithIfConditionExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        // 对象名
        String objectKey = "example_dir/example_object.txt";
        // 对象数据保存的本地文件路径，需保证不存在，否则会覆盖原有文件
        String filePath = "example_dir/example_file.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        // 以下代码展示几种限定条件的使用方式，您可以选择其中一种，也可以选择多种进行组合。
        ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();

        // 条件1：如果指定的时间早于对象最后修改时间，则将其下载到本地文件
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse("2022-09-13 14:36:01");
            options.setIfModifiedSince(date);
        } catch (ParseException e) {
            System.out.println("date parse failed");
            e.printStackTrace();
        }

        // 条件2：如果指定的时间不早于对象最后修改时间，则将其下载到本地文件
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse("2222-12-31 14:36:01");
            options.setIfUnmodifiedSince(date);
        } catch (ParseException e) {
            System.out.println("date parse failed");
            e.printStackTrace();
        }

        // 条件3：如果指定的 Etag 与对象的 Etag 匹配，则将其下载到本地文件
        // 对象的 Etag 可以通过 headObject 接口获取

        HeadObjectV2Input head = new HeadObjectV2Input().setBucket(bucketName).setKey(objectKey);
        HeadObjectV2Output headOutput = tos.headObject(head);
        String etag = headOutput.getHeadObjectBasicOutput().getEtag();
        // 设置条件
        options.setIfMatch(etag);

        // 条件4：如果指定的 Etag 与对象的 Etag 不匹配，则将其下载到本地文件
        // 此处设置一个错误的 etag，与对象 Etag 不匹配，仅作为示例
        String wrongEtag = "XXXYYYZZZ";
        options.setIfNoneMatch(wrongEtag);

        GetObjectV2Input input = new GetObjectV2Input().setBucket(bucketName).setKey(objectKey).setOptions(options);
        File file = new File(filePath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            // 此处判断文件路径的父文件夹是否存在，不存在则创建父文件夹
            // 如果父文件夹不存在且不创建，直接写入会报 FileNotFoundException
            file.getParentFile().mkdirs();
        }
        try(FileOutputStream fos = new FileOutputStream(file);
            GetObjectV2Output output = tos.getObject(input)) {
            if (output.getContent() != null) {
                byte[] buffer = new byte[4096];
                int length;
                while ((length = output.getContent().read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }
            }
            fos.flush();
            System.out.println("getObject succeed, object's metadata is " + output.getGetObjectBasicOutput());
        } catch (IOException e) {
            System.out.println("write data to file failed");
            e.printStackTrace();
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("getObject failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            // 使用限定条件下载，如果服务端返回 304/412 状态码，SDK 将抛出 TosServerException，您需要自行处理。
            System.out.println("getObject failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("getObject failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

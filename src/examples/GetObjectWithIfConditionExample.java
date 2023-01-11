package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.GetObjectV2Input;
import com.volcengine.tos.model.object.GetObjectV2Output;
import com.volcengine.tos.model.object.ObjectMetaRequestOptions;

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

        String bucketName = "your bucket name";
        // 需要确保下载的数据已存在
        String objectKey = "your object key";
        String filePath = "your file path";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            // 以下代码展示，如果指定的时间早于对象最后修改时间，则将其下载到本地文件
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse("2022-09-13 14:36:01");
            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions().setIfModifiedSince(date);
            GetObjectV2Input input = new GetObjectV2Input().setBucket(bucketName).setKey(objectKey).setOptions(options);
            try(FileOutputStream fileOutputStream = new FileOutputStream(filePath);
                GetObjectV2Output output = tos.getObject(input)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = output.getContent().read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, length);
                }
                System.out.println("getObject succeed, object's metadata is " + output.getGetObjectBasicOutput());
            } catch (IOException e) {
                System.out.println("write data to file failed");
                e.printStackTrace();
            }
        } catch (TosException e) {
            // SDK 针对 304 状态码会返回 TosException，用户需要自行判断
            if (e.getStatusCode() == 304) {
                System.out.println("server return 304 not modified");
            } else if (e.getStatusCode() == 404) {
                // 下载不存在的对象会返回404
                System.out.println("the object you want to download is not found");
            } else {
                System.out.println("getObject failed");
            }
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.GetObjectV2Input;
import com.volcengine.tos.model.object.GetObjectV2Output;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetObjectInStringExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        // 需要确保下载的数据已存在
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            GetObjectV2Input input = new GetObjectV2Input().setBucket(bucketName).setKey(objectKey);
            // 以下代码展示如何将数据下载到本地文件
            try(GetObjectV2Output output = tos.getObject(input);
                BufferedReader reader = new BufferedReader(new InputStreamReader(output.getContent()))) {
                System.out.println("begin to read content in object.");
                while (true) {
                    String line = reader.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println(line);
                }
                System.out.println("getObject succeed, object's metadata is " + output.getGetObjectBasicOutput());
            } catch (IOException e) {
                System.out.println("read data in object failed");
                e.printStackTrace();
            }
        } catch (TosException e) {
            if (e.getStatusCode() == 404) {
                // 下载不存在的对象会返回404
                System.out.println("the object you want to download is not found");
            } else {
                System.out.println("getObject failed");
            }
            e.printStackTrace();
        }
    }
}

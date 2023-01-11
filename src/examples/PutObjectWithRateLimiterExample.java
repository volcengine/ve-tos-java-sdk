package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.util.ratelimit.DefaultRateLimiter;
import com.volcengine.tos.model.object.PutObjectBasicInput;
import com.volcengine.tos.model.object.PutObjectInput;
import com.volcengine.tos.model.object.PutObjectOutput;

import java.io.FileInputStream;
import java.io.IOException;

public class PutObjectWithRateLimiterExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";
        String filePath = "your file's path to putObject";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try(FileInputStream inputStream = new FileInputStream(filePath)){
            // 配置上传对象平均为 5MB/s 最大为 20MB/s，其中上传对象和下载对象平均速度必须大于 8KB 每秒
            RateLimiter limiter = new DefaultRateLimiter(5 * 1024 * 1024, 20 * 1024 * 1024);
            PutObjectBasicInput basicInput = new PutObjectBasicInput().setBucket(bucketName).setKey(objectKey).setRateLimiter(limiter);
            PutObjectInput putObjectInput = new PutObjectInput().setPutObjectBasicInput(basicInput).setContent(inputStream);
            PutObjectOutput output = tos.putObject(putObjectInput);
            System.out.println("putObject succeed, object's etag is " + output.getEtag());
            System.out.println("putObject succeed, object's crc64 is " + output.getHashCrc64ecma());
        } catch (TosException e) {
            System.out.println("putObject failed");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("putObject read file failed");
            e.printStackTrace();
        }
    }
}

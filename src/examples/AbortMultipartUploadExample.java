package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.AbortMultipartUploadInput;

public class AbortMultipartUploadExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        // 指定的需要取消的分片上传任务的uploadID，
        // 需保证该 uploadID 已通过初始化分片上传接口 createMultipartUpload 调用返回，
        // 否则，对于不存在的 uploadID 会抛出 404 not found exception。
        String uploadID = "the specific uploadID";
        // 与 uploadID 对应的对象 key
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            AbortMultipartUploadInput input = new AbortMultipartUploadInput().setBucket(bucketName)
                    .setKey(objectKey).setUploadID(uploadID);
            tos.abortMultipartUpload(input);
        } catch (TosException e) {
            System.out.println("abortMultipartUpload failed");
            e.printStackTrace();
        }
    }
}

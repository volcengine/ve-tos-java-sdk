package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.ListPartsInput;
import com.volcengine.tos.model.object.ListPartsOutput;
import com.volcengine.tos.model.object.UploadedPartV2;

public class ListPartsExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        // 指定的需要列举的分片上传任务的uploadID，
        // 需保证该 uploadID 已通过初始化分片上传接口 createMultipartUpload 调用返回，
        // 否则，对于不存在的 uploadID 会抛出 404 not found exception。
        String uploadID = "the specific uploadID";
        // 与 uploadID 对应的对象 key
        String objectKey = "your object key";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
        try{
            ListPartsInput input = new ListPartsInput()
                    // 必须设置 bucket, key, uploadID
                    .setBucket(bucketName).setKey(objectKey).setUploadID(uploadID);
            ListPartsOutput output = tos.listParts(input);
            System.out.printf("listParts succeed, is truncated? %b, next partNumber marker is %d.\n," +
                     output.isTruncated(), output.getNextPartNumberMarker());
            if (output.getUploadedParts() != null) {
                for (int i = 0; i < output.getUploadedParts().size(); i++) {
                    UploadedPartV2 upload = output.getUploadedParts().get(i);
                    System.out.printf("No.%d uploaded part is %s\n", i+1, upload);
                }
            }
        } catch (TosException e) {
            System.out.println("listParts failed");
            e.printStackTrace();
        }
    }
}

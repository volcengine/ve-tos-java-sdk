package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.ListMultipartUploadsV2Input;
import com.volcengine.tos.model.object.ListMultipartUploadsV2Output;
import com.volcengine.tos.model.object.ListedUpload;

public class ListMultipartUploadsExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
        try{
            ListMultipartUploadsV2Input input = new ListMultipartUploadsV2Input()
                    // 必须设置 bucket name
                    .setBucket(bucketName).setMaxUploads(10);
            ListMultipartUploadsV2Output output = tos.listMultipartUploads(input);
            System.out.printf("listMultipartUploads succeed, is truncated? %b, next uploadIDMarker is %s, " +
                    "next keyMarker is %s.\n", output.isTruncated(), output.getNextUploadIdMarker(), output.getNextKeyMarker());
            if (output.getUploads() != null) {
                for (int i = 0; i < output.getUploads().size(); i++) {
                    ListedUpload upload = output.getUploads().get(i);
                    System.out.printf("No.%d listed upload is %s\n", i+1, upload);
                }
            }
        } catch (TosException e) {
            System.out.println("listMultipartUploads failed");
            e.printStackTrace();
        }
    }
}

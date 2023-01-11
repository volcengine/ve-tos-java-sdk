package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.ListObjectVersionsV2Input;
import com.volcengine.tos.model.object.ListObjectVersionsV2Output;
import com.volcengine.tos.model.object.ListedDeleteMarkerEntry;

public class ListObjectVersionsExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        int maxKeys = 10;

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
        try{
            ListObjectVersionsV2Input input = new ListObjectVersionsV2Input()
                    // 必须设置 bucket name
                    .setBucket(bucketName).setMaxKeys(maxKeys);
            ListObjectVersionsV2Output output = tos.listObjectVersions(input);
            System.out.printf("listObjectVersions succeed, is truncated? %b, next key marker is %s, " +
                            "next version id marker is %s.\n", output.isTruncated(),
                    output.getNextKeyMarker(), output.getNextVersionIDMarker());
            if (output.getDeleteMarkers() != null) {
                for (int i = 0; i < output.getDeleteMarkers().size(); i++) {
                    ListedDeleteMarkerEntry entry = output.getDeleteMarkers().get(i);
                    System.out.printf("No.%d listed entry is %s\n", i+1, entry);
                }
            }
        } catch (TosException e) {
            System.out.println("listObjectVersions failed");
            e.printStackTrace();
        }
    }
}

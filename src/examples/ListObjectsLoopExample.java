package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.ListObjectsV2Input;
import com.volcengine.tos.model.object.ListObjectsV2Output;
import com.volcengine.tos.model.object.ListedObjectV2;

public class ListObjectsLoopExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        int maxKeys = 1000;

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
        try{
            boolean isTruncated = true;
            String marker = "";
            int total = 0;
            while (isTruncated) {
                ListObjectsV2Input input = new ListObjectsV2Input()
                        // 必须设置 bucket name
                        .setBucket(bucketName).setMaxKeys(maxKeys).setMarker(marker);
                ListObjectsV2Output output = tos.listObjects(input);
                System.out.printf("listObjects succeed, is truncated? %b, next marker is %s.\n",
                        output.isTruncated(), output.getNextMarker());
                if (output.getContents() != null) {
                    for (int i = 0; i < output.getContents().size(); i++) {
                        ListedObjectV2 object = output.getContents().get(i);
                        System.out.printf("No.%d listed object is %s\n", i+1+total, object);
                    }
                    total += output.getContents().size();
                }
                isTruncated = output.isTruncated();
                marker = output.getNextMarker();
            }
        } catch (TosException e) {
            System.out.println("listObjects failed");
            e.printStackTrace();
        }
    }
}

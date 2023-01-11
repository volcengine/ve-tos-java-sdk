package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.model.object.ObjectMetaRequestOptions;
import com.volcengine.tos.model.object.SetObjectMetaInput;
import com.volcengine.tos.model.object.SetObjectMetaOutput;

import java.util.HashMap;
import java.util.Map;

public class SetObjectMetaExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "your bucket name";
        String objectKey = "your object key";
        String contentType = "the specific object content-type";
        Map<String, String> customMeta = new HashMap<>();

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            ObjectMetaRequestOptions options = new ObjectMetaRequestOptions();
            options.setContentType(contentType);
            customMeta.put("userKey", "userValue");
            options.setCustomMetadata(customMeta);
            SetObjectMetaInput input = new SetObjectMetaInput().setBucket(bucketName).setKey(objectKey)
                    .setOptions(options);
            SetObjectMetaOutput output = tos.setObjectMeta(input);
            System.out.println("setObjectMeta succeed, request info is " + output);
        } catch (TosException e) {
            System.out.println("setObjectMeta failed");
            e.printStackTrace();
        }
    }
}

package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

import java.util.ArrayList;
import java.util.List;

public class DeleteMultiObjectsExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        String objectKey = "example_dir/example_object.txt";
        String objectKey1 = "example_dir/example_object_1.txt";
        String objectKey2 = "example_dir/example_object_2.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            List<ObjectTobeDeleted> objs = new ArrayList<>();
            objs.add(new ObjectTobeDeleted().setKey(objectKey));
            objs.add(new ObjectTobeDeleted().setKey(objectKey1));
            objs.add(new ObjectTobeDeleted().setKey(objectKey2));
            DeleteMultiObjectsV2Input input = new DeleteMultiObjectsV2Input().setBucket(bucketName).setObjects(objs);
            DeleteMultiObjectsV2Output output = tos.deleteMultiObjects(input);
            System.out.println("deleteMultiObjects succeed.");
            if (output.getDeleteds() != null) {
                for (int i = 0; i < output.getDeleteds().size(); i++){
                    Deleted deleted = output.getDeleteds().get(i);
                    System.out.println("deleted object is " + deleted);
                }
            }
            if (output.getErrors() != null) {
                for (int i = 0; i < output.getErrors().size(); i++){
                    DeleteError error = output.getErrors().get(i);
                    System.out.println("delete error is " + error);
                }
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("deleteMultiObjects failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("deleteMultiObjects failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("deleteMultiObjects failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

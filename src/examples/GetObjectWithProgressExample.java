package example;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.model.object.GetObjectV2Input;
import com.volcengine.tos.model.object.GetObjectV2Output;

import java.io.FileOutputStream;
import java.io.IOException;

public class GetObjectWithProgressExample {
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
            DataTransferListener listener = new DataTransferListener() {
                @Override
                public void dataTransferStatusChange(DataTransferStatus dataTransferStatus) {
                    if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_STARTED) {
                        System.out.println("get object started.");
                    } else if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_RW) {
                        System.out.printf("get object, read %d bytes once, has read %d bytes, total %d bytes.\n",
                                dataTransferStatus.getRwOnceBytes(), dataTransferStatus.getConsumedBytes(),
                                dataTransferStatus.getTotalBytes());
                    } else if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_FAILED) {
                        System.out.printf("get object failed, has read %d bytes, total %d bytes.\n",
                                dataTransferStatus.getConsumedBytes(), dataTransferStatus.getTotalBytes());
                    } else if (dataTransferStatus.getType() == DataTransferType.DATA_TRANSFER_SUCCEED) {
                        System.out.printf("get object succeed, has read %d bytes, total %d bytes.\n",
                                dataTransferStatus.getConsumedBytes(), dataTransferStatus.getTotalBytes());
                    }
                }
            };
            GetObjectV2Input input = new GetObjectV2Input().setBucket(bucketName).setKey(objectKey).setDataTransferListener(listener);
            // 以下代码展示如何将数据下载到本地文件
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

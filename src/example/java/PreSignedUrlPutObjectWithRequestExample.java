package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpMethod;
import com.volcengine.tos.model.object.PreSignedURLInput;
import com.volcengine.tos.model.object.PreSignedURLOutput;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PreSignedUrlPutObjectWithRequestExample {
    // 建议使用时将 OkHttpClient 设置为静态单例对象
    private static OkHttpClient client;

    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        String objectKey = "example_dir/example_object.txt";
        // 单位为秒，设置3600秒即1小时后过期
        long expires = 3600;

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            PreSignedURLInput input = new PreSignedURLInput().setBucket(bucketName).setKey(objectKey)
                    .setHttpMethod(HttpMethod.PUT).setExpires(expires);
            PreSignedURLOutput output = tos.preSignedURL(input);
            System.out.println("preSignedURL succeed, the signed url is " + output.getSignedUrl());
            System.out.println("preSignedURL succeed, the signed header is " + output.getSignedHeader());
            doPutObject(output.getSignedUrl(), "Hello Volcengine TOS.");
        } catch (IOException e) {
            System.out.println("putObject failed");
            e.printStackTrace();
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("preSignedURL failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("preSignedURL failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }

    private static void doPutObject(String url, String content) throws IOException {
        if (client == null) {
            ConnectionPool connectionPool = new ConnectionPool(1024, 60000, TimeUnit.MILLISECONDS);
            Dispatcher dispatcher = new Dispatcher();
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            client = builder.dispatcher(dispatcher)
                    .connectionPool(connectionPool).retryOnConnectionFailure(true).connectTimeout(10000, TimeUnit.MILLISECONDS)
                    .readTimeout(30000, TimeUnit.MILLISECONDS).writeTimeout(30000, TimeUnit.MILLISECONDS)
                    .followRedirects(false).followSslRedirects(false).build();
        }
        Request.Builder builder = new Request.Builder().url(url);
        builder.put(RequestBody.create(MediaType.parse("binary/octet-stream"), content));
        Response response = null;
        try{
            response = client.newCall(builder.build()).execute();
        } finally{
            if (response != null) {
                response.close();
            }
        }
    }
}

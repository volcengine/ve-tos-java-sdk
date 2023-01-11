package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.model.object.*;

public class ListObjectVersionsByDelimiterExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        int maxKeys = 10;
        String delimiter = "/";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);
        try{
            boolean isTruncated = true;
            String keyMarker = null;
            String versionIdMarker = null;
            while (isTruncated) {
                ListObjectVersionsV2Input input = new ListObjectVersionsV2Input().setBucket(bucketName)
                        .setMaxKeys(maxKeys).setDelimiter(delimiter).setKeyMarker(keyMarker).setVersionIDMarker(versionIdMarker);
                ListObjectVersionsV2Output output = tos.listObjectVersions(input);
                System.out.printf("listObjectVersions succeed, is truncated? %b, next key marker is %s, " +
                                "next version id marker is %s.\n", output.isTruncated(),
                        output.getNextKeyMarker(), output.getNextVersionIDMarker());
                if (output.getVersions() != null) {
                    for (int i = 0; i < output.getVersions().size(); i++) {
                        ListedObjectVersion object = output.getVersions().get(i);
                        System.out.printf("Listed object version, key is %s, size is %d, versionId is %s, " +
                                        "storageClass is %s, ETag is %s, last modified is %s, isLatest? %b.\n",
                                object.getKey(), object.getSize(), object.getVersionID(), object.getStorageClass(),
                                object.getEtag(), object.getLastModified(), object.isLatest());
                    }
                }
                if (output.getCommonPrefixes() != null) {
                    for (int i = 0; i < output.getCommonPrefixes().size(); i++) {
                        ListedCommonPrefix commonPrefix = output.getCommonPrefixes().get(i);
                        System.out.println("Listed commonPrefix is " + commonPrefix.getPrefix());
                        // commonPrefix 为子目录，可用其作为 prefix 参数继续往下列举
                        listCommonPrefix(bucketName, maxKeys, delimiter, commonPrefix.getPrefix(), tos);
                    }
                }
                if (output.getDeleteMarkers() != null) {
                    for (int i = 0; i < output.getDeleteMarkers().size(); i++) {
                        ListedDeleteMarkerEntry deleteMarker = output.getDeleteMarkers().get(i);
                        System.out.printf("Listed deleteMarker, key is %s, versionId is %s, lastModified is %s, " +
                                "isLatest? %b, owner is %s.\n", deleteMarker.getKey(), deleteMarker.getVersionID(),
                                deleteMarker.getLastModified(), deleteMarker.isLatest(), deleteMarker.getOwner());
                    }
                }
                isTruncated = output.isTruncated();
                keyMarker = output.getNextKeyMarker();
                versionIdMarker = output.getNextVersionIDMarker();
            }
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("listObjectVersions failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("listObjectVersions failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("listObjectVersions failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }

    private static void listCommonPrefix(String bucketName, int maxKeys, String delimiter, String prefix, TOSV2 tos) {
        boolean isTruncated = true;
        String keyMarker = null;
        String versionIdMarker = null;
        while (isTruncated) {
            ListObjectVersionsV2Input input = new ListObjectVersionsV2Input().setBucket(bucketName).setMaxKeys(maxKeys)
                    .setDelimiter(delimiter).setKeyMarker(keyMarker).setVersionIDMarker(versionIdMarker).setPrefix(prefix);
            ListObjectVersionsV2Output output = tos.listObjectVersions(input);
            System.out.printf("listObjectVersions succeed, is truncated? %b, next key marker is %s, " +
                            "next version id marker is %s.\n", output.isTruncated(),
                    output.getNextKeyMarker(), output.getNextVersionIDMarker());
            if (output.getVersions() != null) {
                for (int i = 0; i < output.getVersions().size(); i++) {
                    ListedObjectVersion object = output.getVersions().get(i);
                    System.out.printf("Listed object version, key is %s, size is %d, versionId is %s, " +
                                    "storageClass is %s, ETag is %s, last modified is %s, isLatest? %b.\n",
                            object.getKey(), object.getSize(), object.getVersionID(), object.getStorageClass(),
                            object.getEtag(), object.getLastModified(), object.isLatest());
                }
            }
            if (output.getCommonPrefixes() != null) {
                for (int i = 0; i < output.getCommonPrefixes().size(); i++) {
                    ListedCommonPrefix commonPrefix = output.getCommonPrefixes().get(i);
                    System.out.println("Listed commonPrefix is " + commonPrefix.getPrefix());
                }
            }
            if (output.getDeleteMarkers() != null) {
                for (int i = 0; i < output.getDeleteMarkers().size(); i++) {
                    ListedDeleteMarkerEntry deleteMarker = output.getDeleteMarkers().get(i);
                    System.out.printf("Listed deleteMarker, key is %s, versionId is %s, lastModified is %s, " +
                                "isLatest? %b, owner is %s.\n", deleteMarker.getKey(), deleteMarker.getVersionID(),
                                deleteMarker.getLastModified(), deleteMarker.isLatest(), deleteMarker.getOwner());
                }
            }
            isTruncated = output.isTruncated();
            keyMarker = output.getNextKeyMarker();
            versionIdMarker = output.getNextVersionIDMarker();
        }
    }
}

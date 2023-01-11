package example.java;

import com.volcengine.tos.TOSV2;
import com.volcengine.tos.TOSV2ClientBuilder;
import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosServerException;
import com.volcengine.tos.comm.common.CannedType;
import com.volcengine.tos.comm.common.GranteeType;
import com.volcengine.tos.comm.common.PermissionType;
import com.volcengine.tos.model.acl.GrantV2;
import com.volcengine.tos.model.acl.GranteeV2;
import com.volcengine.tos.model.acl.Owner;
import com.volcengine.tos.model.object.ObjectAclRulesV2;
import com.volcengine.tos.model.object.PutObjectACLInput;
import com.volcengine.tos.model.object.PutObjectACLOutput;

import java.util.ArrayList;
import java.util.List;

public class PutObjectAclWithGrantsExample {
    public static void main(String[] args) {
        String endpoint = "your endpoint";
        String region = "your region";
        String accessKey = "your access key";
        String secretKey = "your secret key";

        String bucketName = "bucket-example";
        String objectKey = "example_dir/example_object.txt";

        TOSV2 tos = new TOSV2ClientBuilder().build(region, endpoint, accessKey, secretKey);

        try{
            // 待授权的用户账号id，如果 GranteeType 为 GRANTEE_USER，此项必选
            String accountId = "the granted account id";
            // 待授权的用户账号名，非必选参数
            String accountDisplayName = "the granted account display name";
            GranteeV2 grantee1 = new GranteeV2().setType(GranteeType.GRANTEE_USER)
                    .setId(accountId).setDisplayName(accountDisplayName);
            // 对特定账号为 accountId 的用户授予 PERMISSION_FULL_CONTROL 的权限
            GrantV2 grant1 = new GrantV2().setGrantee(grantee1).setPermission(PermissionType.PERMISSION_FULL_CONTROL);

            GranteeV2 grantee2 = new GranteeV2().setType(GranteeType.GRANTEE_GROUP)
                    .setCanned(CannedType.CANNED_ALL_USERS);
            // 对所有用户授予读权限
            GrantV2 grant2 = new GrantV2().setGrantee(grantee2).setPermission(PermissionType.PERMISSION_READ);

            // 授权列表
            List<GrantV2> grantList = new ArrayList<>();
            grantList.add(grant1);
            grantList.add(grant2);

            // Owner 信息，ownerId 必选，ownerDisplayName 可选
            String ownerId = "your owner id";
            String ownerDisplayName = "your owner display name";
            Owner owner = new Owner().setId(ownerId).setDisplayName(ownerDisplayName);
            PutObjectACLInput input = new PutObjectACLInput().setBucket(bucketName).setKey(objectKey)
                    .setObjectAclRules(new ObjectAclRulesV2().setGrants(grantList).setOwner(owner));
            PutObjectACLOutput output = tos.putObjectAcl(input);
            System.out.println("putObjectAcl succeed.");
        } catch (TosClientException e) {
            // 操作失败，捕获客户端异常，一般情况是请求参数错误，此时请求并未发送
            System.out.println("putObjectAcl failed");
            System.out.println("Message: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
        } catch (TosServerException e) {
            // 操作失败，捕获服务端异常，可以获取到从服务端返回的详细错误信息
            System.out.println("putObjectAcl failed");
            System.out.println("StatusCode: " + e.getStatusCode());
            System.out.println("Code: " + e.getCode());
            System.out.println("Message: " + e.getMessage());
            System.out.println("RequestID: " + e.getRequestID());
        } catch (Throwable t) {
            // 作为兜底捕获其他异常，一般不会执行到这里
            System.out.println("putObjectAcl failed");
            System.out.println("unexpected exception, message: " + t.getMessage());
        }
    }
}

package com.volcengine.tos.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.volcengine.tos.Consts;
import com.volcengine.tos.comm.common.*;
import com.volcengine.tos.internal.util.DateConverter;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.object.ListPartsOutput;
import org.testng.annotations.Test;

import org.testng.Assert;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class TosUtilsTest {
    @Test
    public void uriEncodeTest(){
        String out = TosUtils.uriEncode("23i23+___", true);
        Assert.assertEquals(out, "23i23%2B___");

        out = TosUtils.uriEncode("23i23 ___", true);
        Assert.assertEquals(out, "23i23%20___");

        out = TosUtils.uriEncode("23i23 /___", true);
        Assert.assertEquals(out, "23i23%20%2F___");

        out = TosUtils.uriEncode("23i23 /___", false);
        Assert.assertEquals(out, "23i23%20/___");

        out = TosUtils.uriEncode("/中文测试/", true);
        Assert.assertEquals(out, "%2F%E4%B8%AD%E6%96%87%E6%B5%8B%E8%AF%95%2F");
    }

    @Test
    void dateConvertTest(){
        String rfc1123String = "Tue, 23 Aug 2022 16:50:43 GMT";
//        String rfc1123String = "Fri, 02 Sep 2022 11:04:56 GMT";
        Date date = DateConverter.rfc1123StringToDate(rfc1123String);
        Assert.assertEquals(date.toInstant().getEpochSecond(), 1661273443);

        String formattedRFC1123 = DateConverter.dateToRFC1123String(date);
        Assert.assertEquals(formattedRFC1123, rfc1123String);

        String iso8601String = "2022-08-23T16:50:43.000Z";
        date = DateConverter.iso8601StringToDate(iso8601String);
        Assert.assertEquals(date.toInstant().getEpochSecond(), 1661273443);

        String formattedISO8601 = DateConverter.dateToISO8601String(date);
        Assert.assertEquals(formattedISO8601, iso8601String);

        Date now = new Date();
        System.out.println(DateConverter.dateToRFC3339String(now));
    }

    @Test
    void supportedRegionTest() {
        Map<String, List<String>> supportedRegion = TosUtils.getSupportedRegion();
        Assert.assertEquals(supportedRegion.size(), 3);
        Assert.assertEquals(supportedRegion.get("cn-beijing").get(0), "tos-cn-beijing.volces.com");
        Assert.assertEquals(supportedRegion.get("cn-shanghai").get(0), "tos-cn-shanghai.volces.com");
        Assert.assertEquals(supportedRegion.get("cn-guangzhou").get(0), "tos-cn-guangzhou.volces.com");
    }

    @Test
    void ipTest() {
        String addr = null;
        Assert.assertFalse(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "localhost";
        Assert.assertTrue(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "127.0.0.1";
        Assert.assertTrue(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "127.0.0.256";
        Assert.assertFalse(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "0::0::0";
        Assert.assertFalse(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "fe80::1551:1234:fbb:fcc3";
        Assert.assertFalse(ParamsChecker.isLocalhostOrIpAddress(addr));
        addr = "[fe80::1551:1234:fbb:fcc3]";
        Assert.assertTrue(ParamsChecker.isLocalhostOrIpAddress(addr));
    }

    @Test
    void jsonMapperTest() throws JsonProcessingException {
        String data = "{\"Bucket\": \"bucket-example\",\"Key\": \"object-aa\",\"UploadId\": \"6e72287f9de97be8012970c691db8e31\"," +
                "\"PartNumberMarker\": 0,\"NextPartNumberMarker\": 0,\"MaxParts\": 1000,\"IsTruncated\": true,\"StorageClass\": " +
                "\"STANDARD\",\"Parts\": [{\"PartNumber\": 1,\"LastModified\": \"2022-02-02T22:22:22.000Z\",\"ETag\": " +
                "\"\\\"1e0688ec5fc9689bb1e93d201925579d\\\"\",\"Size\": 5242880}]}";
        ListPartsOutput output = Consts.JSON.readValue(data, new TypeReference<ListPartsOutput>(){});
        Assert.assertEquals(output.getStorageClass(), StorageClassType.STORAGE_CLASS_STANDARD);
        data = data.replace("STANDARD", "IA");
        output = Consts.JSON.readValue(data, new TypeReference<ListPartsOutput>(){});
        Assert.assertEquals(output.getStorageClass(), StorageClassType.STORAGE_CLASS_IA);
        // unknown type
        data = data.replace("IA", "XXX-YYY");
        output = Consts.JSON.readValue(data, new TypeReference<ListPartsOutput>(){});
        Assert.assertEquals(output.getStorageClass(), StorageClassType.STORAGE_CLASS_UNKNOWN);
    }

    private static class AllEnumType {
        @JsonProperty("StorageClass")
        private StorageClassType storageClassType;
        @JsonProperty("Canned")
        private CannedType cannedType;
        @JsonProperty("CertStatus")
        private CertStatusType certStatusType;
        @JsonProperty("Grantee")
        private GranteeType granteeType;
        @JsonProperty("Permission")
        private PermissionType permissionType;
        @JsonProperty("Protocol")
        private ProtocolType protocolType;
        @JsonProperty("Redirect")
        private RedirectType redirectType;
        @JsonProperty("Status")
        private StatusType statusType;
        @JsonProperty("StorageClassID")
        private StorageClassInheritDirectiveType storageClassInheritDirectiveType;
        @JsonProperty("VersioningStatus")
        private VersioningStatusType versioningStatusType;

        public StorageClassType getStorageClassType() {
            return storageClassType;
        }

        public AllEnumType setStorageClassType(StorageClassType storageClassType) {
            this.storageClassType = storageClassType;
            return this;
        }

        public CannedType getCannedType() {
            return cannedType;
        }

        public AllEnumType setCannedType(CannedType cannedType) {
            this.cannedType = cannedType;
            return this;
        }

        public CertStatusType getCertStatusType() {
            return certStatusType;
        }

        public AllEnumType setCertStatusType(CertStatusType certStatusType) {
            this.certStatusType = certStatusType;
            return this;
        }

        public GranteeType getGranteeType() {
            return granteeType;
        }

        public AllEnumType setGranteeType(GranteeType granteeType) {
            this.granteeType = granteeType;
            return this;
        }

        public PermissionType getPermissionType() {
            return permissionType;
        }

        public AllEnumType setPermissionType(PermissionType permissionType) {
            this.permissionType = permissionType;
            return this;
        }

        public ProtocolType getProtocolType() {
            return protocolType;
        }

        public AllEnumType setProtocolType(ProtocolType protocolType) {
            this.protocolType = protocolType;
            return this;
        }

        public RedirectType getRedirectType() {
            return redirectType;
        }

        public AllEnumType setRedirectType(RedirectType redirectType) {
            this.redirectType = redirectType;
            return this;
        }

        public StatusType getStatusType() {
            return statusType;
        }

        public AllEnumType setStatusType(StatusType statusType) {
            this.statusType = statusType;
            return this;
        }

        public StorageClassInheritDirectiveType getStorageClassInheritDirectiveType() {
            return storageClassInheritDirectiveType;
        }

        public AllEnumType setStorageClassInheritDirectiveType(StorageClassInheritDirectiveType storageClassInheritDirectiveType) {
            this.storageClassInheritDirectiveType = storageClassInheritDirectiveType;
            return this;
        }

        public VersioningStatusType getVersioningStatusType() {
            return versioningStatusType;
        }

        public AllEnumType setVersioningStatusType(VersioningStatusType versioningStatusType) {
            this.versioningStatusType = versioningStatusType;
            return this;
        }
    }

    @Test
    void enumTypeJsonMapperTest() throws JsonProcessingException {
        AllEnumType allEnumType = new AllEnumType().setStorageClassType(StorageClassType.STORAGE_CLASS_STANDARD)
                .setCannedType(CannedType.CANNED_ALL_USERS).setVersioningStatusType(VersioningStatusType.VERSIONING_STATUS_SUSPENDED)
                .setCertStatusType(CertStatusType.CERT_STATUS_BOUND).setGranteeType(GranteeType.GRANTEE_USER)
                .setPermissionType(PermissionType.PERMISSION_READ).setProtocolType(ProtocolType.PROTOCOL_HTTP)
                .setRedirectType(RedirectType.REDIRECT_MIRROR).setStatusType(StatusType.STATUS_ENABLED)
                .setStorageClassInheritDirectiveType(StorageClassInheritDirectiveType.STORAGE_CLASS_ID_SOURCE_OBJECT);
        String data = Consts.JSON.writeValueAsString(allEnumType);
        Assert.assertEquals(data, "{\"StorageClass\":\"STANDARD\",\"Canned\":\"AllUsers\",\"CertStatus\":\"CertBound\"," +
                "\"Grantee\":\"CanonicalUser\",\"Permission\":\"READ\",\"Protocol\":\"http\",\"Redirect\":\"Mirror\"," +
                "\"Status\":\"Enabled\",\"StorageClassID\":\"SOURCE_OBJECT\",\"VersioningStatus\":\"Suspended\"}");
        data = data.replace("STANDARD", "XXX")
                .replace("AllUsers", "XXX")
                .replace("CertBound", "XXX")
                .replace("CanonicalUser", "XXX")
                .replace("READ", "XXX")
                .replace("http", "XXX")
                .replace("Mirror", "XXX")
                .replace("Enabled", "XXX")
                .replace("SOURCE_OBJECT", "XXX")
                .replace("Suspended", "XXX");
        AllEnumType unknownType = Consts.JSON.readValue(data, new TypeReference<AllEnumType>(){});
        Assert.assertEquals(unknownType.getStorageClassType(), StorageClassType.STORAGE_CLASS_UNKNOWN);
        Assert.assertEquals(unknownType.getCannedType(), CannedType.CANNED_UNKNOWN);
        Assert.assertEquals(unknownType.getCertStatusType(), CertStatusType.CERT_STATUS_UNKNOWN);
        Assert.assertEquals(unknownType.getGranteeType(), GranteeType.GRANTEE_UNKNOWN);
        Assert.assertEquals(unknownType.getPermissionType(), PermissionType.PERMISSION_UNKNOWN);
        Assert.assertEquals(unknownType.getProtocolType(), ProtocolType.PROTOCOL_UNKNOWN);
        Assert.assertEquals(unknownType.getRedirectType(), RedirectType.REDIRECT_UNKNOWN);
        Assert.assertEquals(unknownType.getStatusType(), StatusType.STATUS_UNKNOWN);
        Assert.assertEquals(unknownType.getStorageClassInheritDirectiveType(), StorageClassInheritDirectiveType.STORAGE_CLASS_ID_UNKNOWN);
        Assert.assertEquals(unknownType.getVersioningStatusType(), VersioningStatusType.VERSIONING_STATUS_UNKNOWN);
    }

    @Test
    void headerEncodeTest() {
        // 编码含中文
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "中+"), "%E4%B8%AD%2B");
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "中*"), "%E4%B8%AD%2A");
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "中~"), "%E4%B8%AD%7E");
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "中/"), "%E4%B8%AD%2F");
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "中\\"), "%E4%B8%AD%5C");
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "中 "), "%E4%B8%AD%20");
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "中%20"), "%E4%B8%AD%2520");
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "中文"), "%E4%B8%AD%E6%96%87");
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "%中文%"), "%25%E4%B8%AD%E6%96%87%25");
        // 编码不含中文
        Assert.assertEquals(TosUtils.tryEncodeValue("key", "%%+ ~//\\%20"), "%%+ ~//\\%20");

        // 解码含中文
        Assert.assertEquals(TosUtils.tryDecodeValue("key", "%E4%B8%AD+"), "中+");
        Assert.assertEquals(TosUtils.tryDecodeValue("key", "%E4%B8%AD*"), "中*");
        Assert.assertEquals(TosUtils.tryDecodeValue("key", "%E4%B8%AD~"), "中~");
        Assert.assertEquals(TosUtils.tryDecodeValue("key", "%E4%B8%AD/"), "中/");
        Assert.assertEquals(TosUtils.tryDecodeValue("key", "%E4%B8%AD\\"), "中\\");
        Assert.assertEquals(TosUtils.tryDecodeValue("key", "%E4%B8%AD "), "中 ");
        Assert.assertEquals(TosUtils.tryDecodeValue("key", "%E4%B8%AD%20"), "中 ");
        Assert.assertEquals(TosUtils.tryDecodeValue("key", "%E4%B8%AD%E6%96%87"), "中文");

        // 解码失败返回原值
        Assert.assertEquals(TosUtils.tryDecodeValue("key", "%%E4%B8%AD%E6%96%87%"), "%%E4%B8%AD%E6%96%87%");
    }
}

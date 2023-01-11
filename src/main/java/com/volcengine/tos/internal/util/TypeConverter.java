package com.volcengine.tos.internal.util;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.common.*;

import static com.volcengine.tos.comm.common.Consts.*;

public class TypeConverter {
    public static ACLType convertACLType(String type) {
        if (type == null) {
            return null;
        }
        if (StringUtils.equals(type, ACL_PRIVATE)) {
            return ACLType.ACL_PRIVATE;
        } else if (StringUtils.equals(type, ACL_PUBLIC_READ)) {
            return ACLType.ACL_PUBLIC_READ;
        } else if (StringUtils.equals(type, ACL_PUBLIC_READ_WRITE)) {
            return ACLType.ACL_PUBLIC_READ_WRITE;
        } else if (StringUtils.equals(type, ACL_AUTH_READ)) {
            return ACLType.ACL_AUTHENTICATED_READ;
        } else if (StringUtils.equals(type, ACL_BUCKET_OWNER_READ)) {
            return ACLType.ACL_BUCKET_OWNER_READ;
        } else if (StringUtils.equals(type, ACL_BUCKET_OWNER_FULL_CONTROL)) {
            return ACLType.ACL_BUCKET_OWNER_FULL_CONTROL;
        } else if (StringUtils.equals(type, ACL_LOG_DELIVERY_WRITE)) {
            return ACLType.ACL_LOG_DELIVERY_WRITE;
        } else {
            throw new TosClientException("invalid acl type: " + type, null);
        }
    }

    public static StorageClassType convertStorageClassType(String storageClass) {
        if (storageClass == null) {
            return null;
        }
        if (StringUtils.equals(storageClass, STORAGE_CLASS_STANDARD)) {
            return StorageClassType.STORAGE_CLASS_STANDARD;
        } else if (StringUtils.equals(storageClass, STORAGE_CLASS_IA)) {
            return StorageClassType.STORAGE_CLASS_IA;
        } else if (StringUtils.equals(storageClass, STORAGE_CLASS_ARCHIVE_FR)) {
            return StorageClassType.STORAGE_CLASS_ARCHIVE_FR;
        } else {
            throw new TosClientException("invalid storage class: " + storageClass, null);
        }
    }

    public static MetadataDirectiveType convertMetadataDirectiveType(String metadataDirectiveType) {
        if (metadataDirectiveType == null) {
            return null;
        }
        if (StringUtils.equals(metadataDirectiveType, METADATA_DIRECTIVE_COPY)) {
            return MetadataDirectiveType.METADATA_DIRECTIVE_COPY;
        } else if (StringUtils.equals(metadataDirectiveType, METADATA_DIRECTIVE_REPLACE)) {
            return MetadataDirectiveType.METADATA_DIRECTIVE_REPLACE;
        } else {
            throw new TosClientException("invalid metadataDirectiveType: " + metadataDirectiveType, null);
        }
    }

    public static PermissionType convertPermissionType(String permissionType) {
        if (permissionType == null) {
            return null;
        }
        if (StringUtils.equals(permissionType, PERMISSION_TYPE_FULL_CONTROL)) {
            return PermissionType.PERMISSION_FULL_CONTROL;
        } else if (StringUtils.equals(permissionType, PERMISSION_TYPE_READ)) {
            return PermissionType.PERMISSION_READ;
        } else if (StringUtils.equals(permissionType, PERMISSION_TYPE_READ_ACP)) {
            return PermissionType.PERMISSION_READ_ACP;
        } else if (StringUtils.equals(permissionType, PERMISSION_TYPE_WRITE)) {
            return PermissionType.PERMISSION_WRITE;
        } else if (StringUtils.equals(permissionType, PERMISSION_TYPE_WRITE_ACP)) {
            return PermissionType.PERMISSION_WRITE_ACP;
        } else {
            throw new TosClientException("invalid permissionType: " + permissionType, null);
        }
    }

    public static CannedType convertCannedType(String cannedType) {
        if (cannedType == null) {
            return null;
        }
        if (StringUtils.equals(cannedType, CANNED_ALL_USERS)) {
            return CannedType.CANNED_ALL_USERS;
        } else if (StringUtils.equals(cannedType, CANNED_AUTHENTICATED_USERS)) {
            return CannedType.CANNED_AUTHENTICATED_USERS;
        } else if (StringUtils.equals(cannedType, CANNED_LOG_DELIVERY)) {
            return CannedType.CANNED_LOG_DELIVERY;
        } else {
            throw new TosClientException("invalid cannedType: " + cannedType, null);
        }
    }

    public static GranteeType convertGranteeType(String granteeType) {
        if (granteeType == null) {
            return null;
        }
        if (StringUtils.equals(granteeType, GRANTEE_GROUP)) {
            return GranteeType.GRANTEE_GROUP;
        } else if (StringUtils.equals(granteeType, GRANTEE_USER)) {
            return GranteeType.GRANTEE_USER;
        } else {
            throw new TosClientException("invalid granteeType: " + granteeType, null);
        }
    }
}
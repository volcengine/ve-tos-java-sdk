package com.volcengine.tos.internal.util;

import com.volcengine.tos.comm.common.ACLType;
import com.volcengine.tos.comm.common.MetadataDirectiveType;
import com.volcengine.tos.comm.common.StorageClassType;
import org.apache.commons.lang3.StringUtils;

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
            throw new IllegalArgumentException("invalid acl type: " + type);
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
            throw new IllegalArgumentException("invalid storage class: " + storageClass);
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
            throw new IllegalArgumentException("invalid metadataDirectiveType: " + metadataDirectiveType);
        }
    }
}
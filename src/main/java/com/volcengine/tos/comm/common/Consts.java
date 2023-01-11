package com.volcengine.tos.comm.common;

public interface Consts {
    /**
     * replace source object metadata when calling copyObject
     */
    String METADATA_DIRECTIVE_REPLACE = "REPLACE";

    /**
     * copy source object metadata when calling copyObject
     */
    String METADATA_DIRECTIVE_COPY = "COPY";

    String ACL_PRIVATE = "private";
    String ACL_PUBLIC_READ = "public-read";
    String ACL_PUBLIC_READ_WRITE = "public-read-write";
    String ACL_AUTH_READ = "authenticated-read";
    String ACL_BUCKET_OWNER_READ = "bucket-owner-read";
    String ACL_BUCKET_OWNER_FULL_CONTROL = "bucket-owner-full-control";
    String ACL_LOG_DELIVERY_WRITE = "log-delivery-write";

    String PERMISSION_TYPE_READ = "READ";
    String PERMISSION_TYPE_WRITE = "WRITE";
    String PERMISSION_TYPE_READ_ACP = "READ_ACP";
    String PERMISSION_TYPE_WRITE_ACP = "WRITE_ACP";
    String PERMISSION_TYPE_FULL_CONTROL = "FULL_CONTROL";

    String STORAGE_CLASS_STANDARD = "STANDARD";
    String STORAGE_CLASS_IA = "IA";
    String STORAGE_CLASS_ARCHIVE_FR = "ARCHIVE_FR";

    String CANNED_ALL_USERS = "AllUsers";
    String CANNED_AUTHENTICATED_USERS = "AuthenticatedUsers";
    String CANNED_LOG_DELIVERY = "LogDelivery";

    String GRANTEE_GROUP = "Group";
    String GRANTEE_USER = "CanonicalUser";
}

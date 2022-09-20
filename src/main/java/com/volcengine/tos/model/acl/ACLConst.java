package com.volcengine.tos.model.acl;

/**
 * deprecated, use ACLType instead
 */
@Deprecated
public interface ACLConst {
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
}

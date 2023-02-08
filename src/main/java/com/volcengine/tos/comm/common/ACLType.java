package com.volcengine.tos.comm.common;

public enum ACLType {
    /**
     * set bucket or object acl to private
     */
    ACL_PRIVATE("private"),
    /**
     * set bucket or object acl to public-read
     */
    ACL_PUBLIC_READ("public-read"),
    /**
     * set bucket or object acl to public-read-write
     */
    ACL_PUBLIC_READ_WRITE("public-read-write"),
    /**
     * set bucket or object acl to authenticated-read
     */
    ACL_AUTHENTICATED_READ("authenticated-read"),
    /**
     * set bucket or object acl to bucket-owner-read
     */
    ACL_BUCKET_OWNER_READ("bucket-owner-read"),
    /**
     * set bucket or object acl to bucket-owner-full-control
     */
    ACL_BUCKET_OWNER_FULL_CONTROL("bucket-owner-full-control"),
    /**
     * set bucket or object acl to log-delivery-write
     */
    ACL_LOG_DELIVERY_WRITE("log-delivery-write"),
    /**
     * set object acl to bucket-owner-entrusted
     */
    ACL_BUCKET_OWNER_ENTRUSTED("bucket-owner-entrusted"),
    /**
     * acl default type
     */
    ACL_UNKNOWN("unknown");

    private String type;
    private ACLType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}

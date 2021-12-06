package com.volcengine.tos.comm;

public interface TosHeader {
    /**
     * HTTP header
     */
    String HEADER_USER_AGENT = "User-Agent";
    String HEADER_CONTENT_LENGTH = "Content-Length";
    String HEADER_CONTENT_TYPE = "Content-Type";
    String HEADER_CONTENT_MD5 = "Content-MD5";
    String HEADER_CONTENT_LANGUAGE = "Content-Language";
    String HEADER_CONTENT_ENCODING = "Content-Encoding";
    String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    String HEADER_LAST_MODIFIED = "Last-Modified";
    String HEADER_CACHE_CONTROL = "Cache-Control";
    String HEADER_EXPIRES = "Expires";
    String HEADER_ETAG = "ETag";
    String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";
    String HEADER_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";
    String HEADER_IF_MATCH = "If-Match";
    String HEADER_IF_NONE_MATCH = "If-None-Match";
    String HEADER_RANGE = "Range";
    String HEADER_CONTENT_RANGE = "Content-Range";
    String HEADER_LOCATION = "Location";

    /**
     * TOS Header
     */
    String HEADER_CONTENT_SHA256 = "X-Tos-Content-Sha256";
    String HEADER_VERSIONID = "X-Tos-Version-Id";
    String HEADER_DELETE_MARKER = "X-Tos-Delete-Marker";
    String HEADER_STORAGE_CLASS = "X-Tos-Storage-Class";
    String HEADER_RESTORE = "X-Tos-Restore";
    String HEADER_MIRROR_TAG = "X-Tos-Tag";
    String HEADER_SSE_CUSTOMER_ALGORITHM = "X-Tos-Server-Side-Encryption-Customer-Algorithm";
    String HEADER_SSE_CUSTOMER_KEY_MD5 = "X-Tos-Server-Side-Encryption-Customer-Key-MD5";
    String HEADER_SSE_CUSTOMER_KEY = "X-Tos-Server-Side-Encryption-Customer-Key";
    String HEADER_REQUEST_ID = "X-Tos-Request-Id";
    String HEADER_BUCKET_REGION = "X-Tos-Bucket-Region";
    String HEADER_ACL = "X-Tos-Acl";
    String HEADER_GRANT_FULL_CONTROL = "X-Tos-Grant-Full-Control";
    String HEADER_GRANT_READ = "X-Tos-Grant-Read";
    String HEADER_GRANT_READ_ACP = "X-Tos-Grant-Read-Acp";
    String HEADER_GRANT_WRITE = "X-Tos-Grant-Write";
    String HEADER_GRANT_WRITE_ACP = "X-Tos-Grant-Write-Acp";
    String HEADER_NEXT_APPEND_OFFSET = "X-Tos-Next-Append-Offset";
    String HEADER_OBJECT_TYPE = "X-Tos-Object-Type";
    String HEADER_METADATA_DIRECTIVE = "X-Tos-Metadata-Directive";
    String HEADER_COPY_SOURCE = "X-Tos-Copy-Source";
    String HEADER_COPY_SOURCE_IF_MATCH = "X-Tos-Copy-Source-If-Match";
    String HEADER_COPY_SOURCE_IF_NONE_MATCH = "X-Tos-Copy-Source-If-None-Match";
    String HEADER_COPY_SOURCE_IF_MODIFIED_SINCE = "X-Tos-Copy-Source-If-Modified-Since";
    String HEADER_COPY_SOURCE_IF_UNMODIFIED_SINCE = "X-Tos-Copy-Source-If-Unmodified-Since";
    String HEADER_COPY_SOURCE_RANGE = "X-Tos-Copy-Source-Range";
    String HEADER_COPY_SOURCE_VERSION_ID = "X-Tos-Copy-Source-Version-Id";
    String HEADER_WEBSITE_REDIRECT_LOCATION = "X-Tos-Website-Redirect-Location";
    String HEADER_CS_TYPE = "X-Tos-Cs-Type";
    String HEADER_META_PREFIX = "X-Tos-Meta-";

    /**
     * replace source object metadata when calling copyObject
     */
    String METADATA_DIRECTIVE_REPLACE = "REPLACE";

    /**
     * copy source object metadata when calling copyObject
     */
    String METADATA_DIRECTIVE_COPY = "COPY";
}

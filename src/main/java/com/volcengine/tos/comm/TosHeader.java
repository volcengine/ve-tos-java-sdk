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
    String HEADER_RETRY_AFTER = "Retry-After";
    String HEADER_EXPECT = "Expect";
    String HEADER_HOST = "Host";

    /**
     * TOS Header
     */
    String HEADER_CONTENT_SHA256 = "X-Tos-Content-Sha256";
    String HEADER_VERSIONID = "X-Tos-Version-Id";
    String HEADER_DELETE_MARKER = "X-Tos-Delete-Marker";
    String HEADER_STORAGE_CLASS = "X-Tos-Storage-Class";
    String HEADER_AZ_REDUNDANCY = "X-Tos-Az-Redundancy";
    String HEADER_RESTORE = "X-Tos-Restore";
    String HEADER_MIRROR_TAG = "X-Tos-Tag";
    String HEADER_SSE_CUSTOMER_ALGORITHM = "X-Tos-Server-Side-Encryption-Customer-Algorithm";
    String HEADER_SSE_CUSTOMER_KEY_MD5 = "X-Tos-Server-Side-Encryption-Customer-Key-MD5";
    String HEADER_SSE_CUSTOMER_KEY = "X-Tos-Server-Side-Encryption-Customer-Key";
    String HEADER_SSE = "x-tos-server-side-encryption";
    String HEADER_CRC64 = "x-tos-hash-crc64ecma";
    String HEADER_CRC32 = "x-tos-hash-crc32c";
    String HEADER_REQUEST_ID = "X-Tos-Request-Id";
    String HEADER_EC = "X-Tos-Ec";
    String HEADER_ID_2 = "X-Tos-Id-2";
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
    String HEADER_COPY_SOURCE_SSE_CUSTOMER_ALGORITHM = "x-tos-copy-source-server-side-encryption-customer-algorithm";
    String HEADER_COPY_SOURCE_SSE_CUSTOMER_KEY_MD5 = "x-tos-copy-source-server-side-encryption-customer-key-MD5";
    String HEADER_COPY_SOURCE_SSE_CUSTOMER_KEY = "x-tos-copy-source-server-side-encryption-customer-key";
    String HEADER_TRAFFIC_LIMIT = "x-tos-traffic-limit";
    String HEADER_COMPLETE_ALL = "x-tos-complete-all";
    String HEADER_CALLBACK = "x-tos-callback";
    String HEADER_CALLBACK_VAR = "x-tos-callback-var";
    String HEADER_RESTORE_REQUEST_DATERequestDate = "x-tos-restore-request-date";
    String HEADER_RESTORE_EXPIRY_DATE = "x-tos-restore-expiry-date";
    String HEADER_RESTORE_EXPIRY_DAYS = "x-tos-restore-expiry-days";
    String HEADER_RESTORE_TIER = "x-tos-restore-tier";
    String HEADER_TAGGING = "x-tos-tagging";
    String HEADER_TAGGING_DIRECTIVE = "x-tos-tagging-directive";
    String HEADER_WEBSITE_REDIRECT_LOCATION = "X-Tos-Website-Redirect-Location";
    String HEADER_CS_TYPE = "X-Tos-Cs-Type";
    String HEADER_META_PREFIX = "X-Tos-Meta-";
    String HEADER_SDK_RETRY_COUNT = "x-sdk-retry-count";
    String HEADER_PROJECT_NAME = "x-tos-project-name";
    String HEADER_REPLICATION_STATUS = "x-tos-replication-status";
    String HEADER_FORBID_OVERWRITE = "x-tos-forbid-overwrite";
    String HEADER_X_IF_MATCH = "x-tos-if-match";
    String HEADER_ALLOW_SAME_ACTION_OVERLAP = "x-tos-allow-same-action-overlap";
    String HEADER_SYMLINK_TARGET = "x-tos-symlink-target";
    String HEADER_SYMLINK_BUCKET = "x-tos-symlink-bucket";
    String HEADER_SYMLINK_TARGET_SIZE = "x-tos-symlink-target-size";
    String HEADER_BUCKET_TYPE = "x-tos-bucket-type";
    String HEADER_NEXT_MODIFY_OFFSET = "x-tos-next-modify-offset";
    String HEADER_DIRECTORY = "x-tos-directory";
    String HEADER_TAGGING_COUNT = "x-tos-tagging-count";

    /**
     *  only for getObject() method queries
     */
    String QUERY_RESPONSE_CONTENT_TYPE = "response-content-type";
    String QUERY_RESPONSE_CONTENT_LANGUAGE = "response-content-language";
    String QUERY_RESPONSE_CONTENT_ENCODING = "response-content-encoding";
    String QUERY_RESPONSE_CONTENT_DISPOSITION = "response-content-disposition";
    String QUERY_RESPONSE_CACHE_CONTROL = "response-cache-control";
    String QUERY_RESPONSE_EXPIRES = "response-expires";
    String QUERY_DATA_PROCESS = "x-tos-process";
    String QUERY_DOC_PAGE = "x-tos-doc-page";
    String QUERY_DOC_SRC_TYPE = "x-tos-doc-src-type";
    String QUERY_DOC_DST_TYPE = "x-tos-doc-dst-type";
    String QUERY_SAVE_BUCKET = "x-tos-save-bucket";
    String QUERY_SAVE_OBJECT = "x-tos-save-object";
}

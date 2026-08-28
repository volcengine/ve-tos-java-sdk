package com.volcengine.tos.model.object;

import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.TosHeader;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.GenericInput;

import java.util.HashMap;
import java.util.Map;

public class DeleteObjectInput extends GenericInput {
    private String bucket;
    private String key;
    private String versionID;
    private boolean recursive;
    private boolean recursiveByServer;
    private boolean skipTrash;
    private DeleteObjectRecursiveOption recursiveOption;
    private String notificationCustomParameters;
    private String ifMatch;

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public String getVersionID() {
        return versionID;
    }

    public DeleteObjectInput setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public DeleteObjectInput setKey(String key) {
        this.key = key;
        return this;
    }

    public DeleteObjectInput setVersionID(String versionID) {
        this.versionID = versionID;
        return this;
    }

    public boolean isRecursive() {
        return recursive;
    }

    public DeleteObjectInput setRecursive(boolean recursive) {
        this.recursive = recursive;
        return this;
    }

    public boolean isSkipTrash() {
        return skipTrash;
    }

    public DeleteObjectInput setSkipTrash(boolean skipTrash) {
        this.skipTrash = skipTrash;
        return this;
    }

    public DeleteObjectRecursiveOption getRecursiveOption() {
        return recursiveOption;
    }

    public DeleteObjectInput setRecursiveOption(DeleteObjectRecursiveOption recursiveOption) {
        this.recursiveOption = recursiveOption;
        return this;
    }

    public String getNotificationCustomParameters() {
        return notificationCustomParameters;
    }

    public DeleteObjectInput setNotificationCustomParameters(String notificationCustomParameters) {
        this.notificationCustomParameters = notificationCustomParameters;
        return this;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public DeleteObjectInput setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
        return this;
    }

    public Map<String, String> getAllSettedHeaders() {
        Map<String, String> headers = new HashMap<>();
        TosUtils.withHeader(headers, TosHeader.HEADER_NOTIFICATION_CUSTOM_PARAMETERS, notificationCustomParameters);
        TosUtils.withHeader(headers, TosHeader.HEADER_IF_MATCH, ifMatch);
        return headers;
    }

    @Override
    public String toString() {
        return "DeleteObjectInput{" +
                "bucket='" + bucket + '\'' +
                ", key='" + key + '\'' +
                ", versionID='" + versionID + '\'' +
                ", recursive=" + recursive +
                ", recursiveByServer=" + recursiveByServer +
                ", skipTrash=" + skipTrash +
                ", recursiveOption=" + recursiveOption +
                ", ifMatch=" + ifMatch +
                ", notificationCustomParameters='" + notificationCustomParameters + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String bucket;
        private String key;
        private String versionID;
        private boolean recursive;
        private boolean skipTrash;
        private DeleteObjectRecursiveOption recursiveOption;
        private String notificationCustomParameters;
        private String ifMatch;

        private Builder() {
        }

        public Builder bucket(String bucket) {
            this.bucket = bucket;
            return this;
        }

        public Builder key(String key) {
            this.key = key;
            return this;
        }

        public Builder versionID(String versionID) {
            this.versionID = versionID;
            return this;
        }

        public Builder recursive(boolean recursive) {
            this.recursive = recursive;
            return this;
        }

        public Builder skipTrash(boolean skipTrash) {
            this.skipTrash = skipTrash;
            return this;
        }

        public Builder RecursiveOption(DeleteObjectRecursiveOption recursiveOption) {
            this.recursiveOption = recursiveOption;
            return this;
        }

        public Builder notificationCustomParameters(String notificationCustomParameters) {
            this.notificationCustomParameters = notificationCustomParameters;
            return this;
        }

        public Builder ifMatch(String ifMatch) {
            this.ifMatch = ifMatch;
            return this;
        }

        public DeleteObjectInput build() {
            DeleteObjectInput deleteObjectInput = new DeleteObjectInput();
            deleteObjectInput.bucket = this.bucket;
            deleteObjectInput.versionID = this.versionID;
            deleteObjectInput.key = this.key;
            deleteObjectInput.recursive = this.recursive;
            deleteObjectInput.skipTrash = this.skipTrash;
            deleteObjectInput.recursiveOption = this.recursiveOption;
            deleteObjectInput.setNotificationCustomParameters(notificationCustomParameters);
            deleteObjectInput.setIfMatch(ifMatch);
            return deleteObjectInput;
        }
    }

    public static class DeleteObjectRecursiveOption {
        private int batchDeleteSize = 100;

        private int batchDeleteTaskNum = 1;

        private DeleteMultiObjectsEventListener eventListener = event -> {
        };

        private int deleteFailedRetryCount;

        // for testing only
        private boolean forceUseHns;

        public int getBatchDeleteSize() {
            return batchDeleteSize;
        }

        public DeleteObjectRecursiveOption setBatchDeleteSize(int batchDeleteSize) {
            this.batchDeleteSize = batchDeleteSize;
            return this;
        }

        public int getBatchDeleteTaskNum() {
            return batchDeleteTaskNum;
        }

        public DeleteObjectRecursiveOption setBatchDeleteTaskNum(int batchDeleteTaskNum) {
            this.batchDeleteTaskNum = batchDeleteTaskNum;
            return this;
        }

        public DeleteMultiObjectsEventListener getEventListener() {
            return eventListener;
        }

        public DeleteObjectRecursiveOption setEventListener(DeleteMultiObjectsEventListener eventListener) {
            this.eventListener = eventListener;
            return this;
        }

        public int getDeleteFailedRetryCount() {
            return deleteFailedRetryCount;
        }

        public DeleteObjectRecursiveOption setDeleteFailedRetryCount(int deleteFailedRetryCount) {
            this.deleteFailedRetryCount = deleteFailedRetryCount;
            return this;
        }

        @Override
        public String toString() {
            return "DeleteObjectRecursiveOption{" +
                    "batchDeleteSize=" + batchDeleteSize +
                    ", batchDeleteTaskNum=" + batchDeleteTaskNum +
                    ", eventListener=" + eventListener +
                    ", deleteFailedRetryCount=" + deleteFailedRetryCount +
                    '}';
        }
    }

    public static class DeleteMultiObjectsEvent {
        private final String bucket;

        private final DeleteMultiObjectsV2Output output;

        private final TosException err;

        public DeleteMultiObjectsEvent(String bucket, DeleteMultiObjectsV2Output output, TosException err) {
            this.bucket = bucket;
            this.output = output;
            this.err = err;
        }

        public String getBucket() {
            return bucket;
        }

        public DeleteMultiObjectsV2Output getOutput() {
            return output;
        }

        public TosException getErr() {
            return err;
        }

        @Override
        public String toString() {
            return "DeleteMultiObjectsEvent{" +
                    "bucket='" + bucket + '\'' +
                    ", output=" + output +
                    ", err=" + err +
                    '}';
        }
    }

    public interface DeleteMultiObjectsEventListener {
        void eventChange(DeleteMultiObjectsEvent event);
    }
}

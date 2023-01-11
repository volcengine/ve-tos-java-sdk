package com.volcengine.tos.internal.taskman;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.internal.Consts;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.*;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class Util {
    static boolean needAbortTask(int statusCode) {
        return statusCode == HttpStatus.FORBIDDEN
                || statusCode == HttpStatus.NOT_FOUND
                || statusCode == HttpStatus.METHOD_NOT_ALLOWED;
    }

    static void postUploadEvent(UploadEventListener listener, UploadEvent event) {
        if (listener != null && event != null) {
            listener.eventChange(event);
        }
    }

    static void postDownloadEvent(DownloadEventListener listener, DownloadEvent event) {
        if (listener != null && event != null) {
            listener.eventChange(event);
        }
    }

    static void postCopyEvent(CopyEventListener listener, CopyEvent event) {
        if (listener != null && event != null) {
            listener.eventChange(event);
        }
    }

    static void postDataTransferStatus(DataTransferListener listener, DataTransferStatus status) {
        if (listener != null && status != null) {
            listener.dataTransferStatusChange(status);
        }
    }

    static void validatePartSize(long partSize) {
        if (partSize < Consts.MIN_PART_SIZE || partSize > Consts.MAX_PART_SIZE) {
            throw new TosClientException("invalid part size, the size must be [5242880, 5368709120]", null);
        }
    }

    static int determineTaskNum(int taskNum) {
        if (taskNum > Consts.MAX_TASK_NUM) {
            return Consts.MAX_TASK_NUM;
        }
        if (taskNum < Consts.MIN_TASK_NUM) {
            return Consts.MIN_TASK_NUM;
        }
        return taskNum;
    }

    static synchronized void deleteCheckpointFile(String checkpointFilePath) {
        if (StringUtils.isEmpty(checkpointFilePath)) {
            return;
        }
        File file = new File(checkpointFilePath);
        if (file.isFile() && file.exists()) {
            file.delete();
        }
    }

    static String checkpointPathMd5(String bucket, String key, String versionId) {
        StringBuilder sb = new StringBuilder(bucket).append(".").append(key);
        if (StringUtils.isNotEmpty(versionId)) {
            sb.append(".").append(versionId);
        }
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(sb.toString().getBytes());
            return new String(Base64.encodeBase64(bytes)).replace('+', '-').replace('/', '_');
        } catch (NoSuchAlgorithmException e) {
            throw new TosClientException("tos: unable to compute md5", e);
        }
    }
}

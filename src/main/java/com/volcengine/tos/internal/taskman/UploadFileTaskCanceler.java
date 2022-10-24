package com.volcengine.tos.internal.taskman;

import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.HttpStatus;
import com.volcengine.tos.internal.TosObjectRequestHandler;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.AbortMultipartUploadInput;
import com.volcengine.tos.model.object.CancelHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class UploadFileTaskCanceler implements InternalCancelHook, CancelHook, AbortTaskHook {
    private static final Logger log = LoggerFactory.getLogger(UploadFileTaskCanceler.class);
    private TosObjectRequestHandler handler;
    private String bucket;
    private String key;
    private String uploadID;
    private String checkpointFilePath;
    private boolean enableCheckpoint;
    private TaskManager taskMan;
    private boolean aborted;
    private Lock lock;

    public UploadFileTaskCanceler() {
        this.lock = new ReentrantLock();
    }

    public UploadFileTaskCanceler(TosObjectRequestHandler handler, TaskManager taskMan,
                                  String bucket, String key, String uploadID,
                                  String checkpointFilePath, boolean enableCheckpoint) {
        this.handler = handler;
        this.taskMan = taskMan;
        this.bucket = bucket;
        this.key = key;
        this.uploadID = uploadID;
        this.checkpointFilePath = checkpointFilePath;
        this.enableCheckpoint = enableCheckpoint;
        this.lock = new ReentrantLock();
    }

    public TosObjectRequestHandler getHandler() {
        return handler;
    }

    public UploadFileTaskCanceler setHandler(TosObjectRequestHandler handler) {
        this.handler = handler;
        return this;
    }

    @Override
    public void internal() {
        // do nothing
    }

    @Override
    public synchronized void abort() {
        try{
            if (taskMan != null) {
                taskMan.suspend();
            }
            this.handler.abortMultipartUpload(new AbortMultipartUploadInput()
                    .setBucket(this.bucket).setKey(this.key).setUploadID(this.uploadID));
        } catch (TosException e) {
            if (e.getStatusCode() != HttpStatus.NOT_FOUND) {
                throw e;
            }
            log.debug("tos: abortMultipartUpload return 404 and will ignore it.");
        } finally {
            if (taskMan != null) {
                // only used for user abort, program abort do not need to shut down.
                taskMan.shutdown();
            }
            if (this.enableCheckpoint) {
                Util.deleteCheckpointFile(this.checkpointFilePath);
            }
        }
    }

    @Override
    public void cancel(boolean isAbort) {
        if (this.handler == null || this.taskMan == null || StringUtils.isEmpty(this.bucket)
                || StringUtils.isEmpty(this.key) || StringUtils.isEmpty(this.uploadID)) {
            return;
        }
        if (lock.tryLock()) {
            try{
                if (aborted) {
                    return;
                }
                if (isAbort) {
                    this.abort();
                } else {
                    if (taskMan != null) {
                        taskMan.shutdown();
                    }
                }
                aborted = true;
            } finally {
                lock.unlock();
            }
        }
    }

    public String getBucket() {
        return bucket;
    }

    public UploadFileTaskCanceler setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public UploadFileTaskCanceler setKey(String key) {
        this.key = key;
        return this;
    }

    public String getUploadID() {
        return uploadID;
    }

    public UploadFileTaskCanceler setUploadID(String uploadID) {
        this.uploadID = uploadID;
        return this;
    }

    public String getCheckpointFilePath() {
        return checkpointFilePath;
    }

    public UploadFileTaskCanceler setCheckpointFilePath(String checkpointFilePath) {
        this.checkpointFilePath = checkpointFilePath;
        return this;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public UploadFileTaskCanceler setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
        return this;
    }

    public TaskManager getTaskMan() {
        return taskMan;
    }

    public UploadFileTaskCanceler setTaskMan(TaskManager taskMan) {
        this.taskMan = taskMan;
        return this;
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }
}

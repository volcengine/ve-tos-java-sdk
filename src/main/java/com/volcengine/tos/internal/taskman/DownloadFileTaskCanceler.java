package com.volcengine.tos.internal.taskman;

import com.volcengine.tos.internal.TosObjectRequestHandler;
import com.volcengine.tos.model.object.CancelHook;

import java.io.File;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DownloadFileTaskCanceler implements AbortTaskHook, InternalCancelHook, CancelHook {
    private TosObjectRequestHandler handler;
    private String bucket;
    private String key;
    private String checkpointFilePath;
    private String tempFilePath;
    private boolean enableCheckpoint;
    private TaskManager taskMan;
    private boolean aborted;
    private Lock lock;

    public DownloadFileTaskCanceler() {
        this.lock = new ReentrantLock();
    }

    public DownloadFileTaskCanceler(TosObjectRequestHandler handler, TaskManager taskMan,
                                    String bucket, String key, String checkpointFilePath,
                                    boolean enableCheckpoint, String tempFilePath) {
        this.handler = handler;
        this.taskMan = taskMan;
        this.bucket = bucket;
        this.key = key;
        this.checkpointFilePath = checkpointFilePath;
        this.enableCheckpoint = enableCheckpoint;
        this.tempFilePath = tempFilePath;
        this.lock = new ReentrantLock();
    }


    @Override
    public void abort() {
        if (this.taskMan != null) {
            taskMan.shutdown();
        }
        Util.deleteCheckpointFile(this.checkpointFilePath);
        new File(tempFilePath).delete();
    }

    @Override
    public void internal() {

    }

    @Override
    public void cancel(boolean isAbort) {
        if (isAbort) {
            this.abort();
        } else {
            if (this.taskMan != null) {
                taskMan.shutdown();
            }
        }
    }

    public TosObjectRequestHandler getHandler() {
        return handler;
    }

    public DownloadFileTaskCanceler setHandler(TosObjectRequestHandler handler) {
        this.handler = handler;
        return this;
    }

    public String getBucket() {
        return bucket;
    }

    public DownloadFileTaskCanceler setBucket(String bucket) {
        this.bucket = bucket;
        return this;
    }

    public String getKey() {
        return key;
    }

    public DownloadFileTaskCanceler setKey(String key) {
        this.key = key;
        return this;
    }

    public String getCheckpointFilePath() {
        return checkpointFilePath;
    }

    public DownloadFileTaskCanceler setCheckpointFilePath(String checkpointFilePath) {
        this.checkpointFilePath = checkpointFilePath;
        return this;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public DownloadFileTaskCanceler setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
        return this;
    }

    public TaskManager getTaskMan() {
        return taskMan;
    }

    public DownloadFileTaskCanceler setTaskMan(TaskManager taskMan) {
        this.taskMan = taskMan;
        return this;
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    public String getTempFilePath() {
        return tempFilePath;
    }

    public DownloadFileTaskCanceler setTempFilePath(String tempFilePath) {
        this.tempFilePath = tempFilePath;
        return this;
    }
}

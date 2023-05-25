package com.volcengine.tos.internal.taskman;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.TosException;
import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.comm.event.DownloadEventType;
import com.volcengine.tos.comm.ratelimit.RateLimiter;
import com.volcengine.tos.internal.TosObjectRequestHandler;
import com.volcengine.tos.internal.model.CRC64Checksum;
import com.volcengine.tos.internal.model.ConcurrentDataTransferListenInputStream;
import com.volcengine.tos.model.object.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.zip.CheckedInputStream;

import static com.volcengine.tos.internal.Consts.DEFAULT_READ_BUFFER_SIZE;

class DownloadFileTask extends TosTask implements TaskOutput<DownloadPartInfo> {
    private DownloadFileCheckpoint checkpoint;
    private HeadObjectV2Input headObjectV2Input;
    private DownloadPartInfo partInfo;
    private boolean enableCheckpoint;
    private String checkpointFile;
    private boolean enableCrcCheck;
    private DownloadEventListener downloadEventListener;
    private TosObjectRequestHandler handler;
    private RateLimiter rateLimiter;
    private DataTransferListener dataTransferListener;
    private long trafficLimit;
    private final AtomicLong consumedBytes;

    public DownloadFileTask(DownloadFileCheckpoint checkpoint, int taskIdx, AtomicLong consumedBytes) {
        this.checkpoint = checkpoint;
        this.partInfo = checkpoint.getDownloadPartInfos().get(taskIdx);
        this.consumedBytes = consumedBytes;
    }

    @Override
    public Callable<TaskOutput<?>> getCallableTask() {
        return () -> {
            DownloadEvent event = new DownloadEvent().setBucket(checkpoint.getBucket())
                    .setKey(checkpoint.getKey()).setVersionID(checkpoint.getVersionID())
                    .setFilePath(checkpoint.getDownloadFileInfo().getFilePath())
                    .setTempFilePath(checkpoint.getDownloadFileInfo().getTempFilePath())
                    .setCheckpointFile(checkpointFile);
            DataTransferStatus status = new DataTransferStatus()
                    .setTotalBytes(checkpoint.getDownloadObjectInfo().getObjectSize());
            try(RandomAccessFile output = new RandomAccessFile(
                    checkpoint.getDownloadFileInfo().getTempFilePath(), "rw")) {
                output.seek(partInfo.getRangeStart());

                ObjectMetaRequestOptions options = headObjectV2Input.getOptions();
                if (options == null) {
                    options = new ObjectMetaRequestOptions();
                }
                options.setRange(partInfo.getRangeStart(), partInfo.getRangeEnd());
                if (trafficLimit != 0) {
                    options.setTrafficLimit(trafficLimit);
                }

                GetObjectV2Input get = new GetObjectV2Input()
                        .setBucket(headObjectV2Input.getBucket())
                        .setKey(headObjectV2Input.getKey())
                        .setOptions(options)
                        .setVersionID(headObjectV2Input.getVersionID())
                        .setRateLimiter(rateLimiter);
                GetObjectV2Output got = handler.getObject(get);
                long totalSize = checkpoint.getDownloadObjectInfo().getObjectSize();
                InputStream wrapped = got.getContent();
                try {
                    wrapped = wrapInputStream(got.getContent(), totalSize);
                    byte[] buffer = new byte[DEFAULT_READ_BUFFER_SIZE];
                    int once = 0;
                    while ((once = wrapped.read(buffer)) > 0) {
                        output.write(buffer, 0, once);
                    }
                    partInfo.setCompleted(true);
                    if (enableCrcCheck) {
                        partInfo.setHashCrc64ecma(((CheckedInputStream) wrapped).getChecksum().getValue());
                    }
                    if (enableCheckpoint) {
                        this.checkpoint.writeToFile(this.checkpointFile);
                    }
                    event.setDownloadEventType(DownloadEventType.DownloadEventDownloadPartSucceed).setDownloadPartInfo(partInfo);
                    Util.postDownloadEvent(downloadEventListener, event);
                } catch (IOException e) {
                    TosClientException te = new TosClientException("tos: write data to local file failed", e);
                    Util.postDownloadEvent(this.downloadEventListener, event.setTosException(te).setDownloadPartInfo(partInfo)
                            .setDownloadEventType(DownloadEventType.DownloadEventDownloadPartFailed));
                    Util.postDataTransferStatus(dataTransferListener, status.setConsumedBytes(consumedBytes.get())
                            .setType(DataTransferType.DATA_TRANSFER_FAILED));
                    throw te;
                } finally {
                    if (wrapped != null) {
                        wrapped.close();
                    }
                }
            } catch (IOException e) {
                TosClientException te = new TosClientException("tos: write data to local file failed", e);
                Util.postDownloadEvent(this.downloadEventListener, event.setTosException(te)
                        .setDownloadPartInfo(partInfo)
                        .setDownloadEventType(DownloadEventType.DownloadEventDownloadPartFailed));
                Util.postDataTransferStatus(dataTransferListener, status.setConsumedBytes(consumedBytes.get())
                        .setType(DataTransferType.DATA_TRANSFER_FAILED));
                throw te;
            } catch (TosException e) {
                if (Util.needAbortTask(e.getStatusCode())) {
                    Util.postDownloadEvent(this.downloadEventListener, event.setTosException(e)
                            .setDownloadPartInfo(partInfo)
                            .setDownloadEventType(DownloadEventType.DownloadEventDownloadPartAborted));
                    Util.postDataTransferStatus(dataTransferListener, status.setConsumedBytes(consumedBytes.get())
                            .setType(DataTransferType.DATA_TRANSFER_FAILED));
                    throw e;
                } else {
                    Util.postDownloadEvent(this.downloadEventListener, event.setTosException(e)
                            .setDownloadPartInfo(partInfo)
                            .setDownloadEventType(DownloadEventType.DownloadEventDownloadPartFailed));
                }
            }

            return this;
        };
    }

    @Override
    public DownloadPartInfo getOutput() {
        return partInfo;
    }

    private InputStream wrapInputStream(InputStream stream, long objectSize) {
        if (this.dataTransferListener != null) {
            stream = new ConcurrentDataTransferListenInputStream(stream, dataTransferListener, objectSize, consumedBytes);
        }
        if (this.enableCrcCheck) {
            stream = new CheckedInputStream(stream, new CRC64Checksum());
        }
        return stream;
    }

    public DownloadFileCheckpoint getCheckpoint() {
        return checkpoint;
    }

    public DownloadFileTask setCheckpoint(DownloadFileCheckpoint checkpoint) {
        this.checkpoint = checkpoint;
        return this;
    }

    public HeadObjectV2Input getHeadObjectV2Input() {
        return headObjectV2Input;
    }

    public DownloadFileTask setHeadObjectV2Input(HeadObjectV2Input headObjectV2Input) {
        this.headObjectV2Input = headObjectV2Input;
        return this;
    }

    public DownloadPartInfo getPartInfo() {
        return partInfo;
    }

    public DownloadFileTask setPartInfo(DownloadPartInfo partInfo) {
        this.partInfo = partInfo;
        return this;
    }

    public boolean isEnableCheckpoint() {
        return enableCheckpoint;
    }

    public DownloadFileTask setEnableCheckpoint(boolean enableCheckpoint) {
        this.enableCheckpoint = enableCheckpoint;
        return this;
    }

    public String getCheckpointFile() {
        return checkpointFile;
    }

    public DownloadFileTask setCheckpointFile(String checkpointFile) {
        this.checkpointFile = checkpointFile;
        return this;
    }

    public boolean isEnableCrcCheck() {
        return enableCrcCheck;
    }

    public DownloadFileTask setEnableCrcCheck(boolean enableCrcCheck) {
        this.enableCrcCheck = enableCrcCheck;
        return this;
    }

    public DownloadEventListener getDownloadEventListener() {
        return downloadEventListener;
    }

    public DownloadFileTask setDownloadEventListener(DownloadEventListener downloadEventListener) {
        this.downloadEventListener = downloadEventListener;
        return this;
    }

    public TosObjectRequestHandler getHandler() {
        return handler;
    }

    public DownloadFileTask setHandler(TosObjectRequestHandler handler) {
        this.handler = handler;
        return this;
    }

    public RateLimiter getRateLimiter() {
        return rateLimiter;
    }

    public DownloadFileTask setRateLimiter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        return this;
    }

    public DataTransferListener getDataTransferListener() {
        return dataTransferListener;
    }

    public DownloadFileTask setDataTransferListener(DataTransferListener dataTransferListener) {
        this.dataTransferListener = dataTransferListener;
        return this;
    }

    public long getTrafficLimit() {
        return trafficLimit;
    }

    public DownloadFileTask setTrafficLimit(long trafficLimit) {
        this.trafficLimit = trafficLimit;
        return this;
    }
}

package com.volcengine.tos.internal.model;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.io.Retryable;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.internal.util.base64.Base64;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Checksum;

public class TosRawTrailerInputStream extends FilterInputStream implements RetryCountNotifier, Retryable {

    private long remaining;
    private final String trailerHeaderKey;
    private final Checksum checkSum;
    private String trailerHeaderContent;

    public TosRawTrailerInputStream(InputStream in, long remaining, String trailerHeaderKey) {
        super(in);
        this.remaining = remaining;
        this.trailerHeaderKey = trailerHeaderKey + ":";
        this.checkSum = new CRC64Checksum();
    }

    @Override
    public int read() throws IOException {
        int n = this.in.read();
        if (this.remaining > 0) {
            this.remaining--;
            this.checkSum.update(n);
            return n;
        }
        this.extractTrailerHeader(new byte[]{(byte) n}, 0, 1);
        this.checkCrc64();
        return -1;
    }

    @Override
    public int read(@NotNull byte[] b, int off, int len) throws IOException {
        int readOnce = this.in.read(b, off, len);
        if (readOnce == -1) {
            // check crc64
            this.checkCrc64();
            return -1;
        }

        if (this.remaining > readOnce) {
            this.remaining -= readOnce;
            this.checkSum.update(b, off, readOnce);
            return readOnce;
        }

        int ret = (int) this.remaining;
        this.remaining = 0;
        this.checkSum.update(b, off, ret);
        this.extractTrailerHeader(b, off + ret, readOnce - ret);
        return ret;
    }

    private void extractTrailerHeader(byte[] b, int off, int len) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        if (len > 0) {
            bos.write(b, off, len);
        }
        byte[] p = new byte[1024];
        int readOnce;
        while ((readOnce = this.in.read(p)) != -1) {
            bos.write(p, 0, readOnce);
        }
        trailerHeaderContent = bos.toString().trim();
    }

    private void checkCrc64() {
        if (StringUtils.isEmpty(this.trailerHeaderContent) || !this.trailerHeaderContent.startsWith(this.trailerHeaderKey)) {
            throw new TosClientException("try to check crc64 for getting object by trailer header failed", null);
        }

        String actual = this.trailerHeaderContent.substring(this.trailerHeaderKey.length()).trim();
        String expect = new String(Base64.encodeBase64(TosUtils.longToByteArray(this.checkSum.getValue())));
        if (!actual.equals(expect)) {
            throw new TosClientException("check crc64 for getting object by trailer header failed, expect base64 crc64 " + expect +
                    ", actual base64 crc64 " + actual, null);
        }
    }

    @Override
    public void setRetryCount(int retryCount) {
        if (this.in instanceof RetryCountNotifier) {
            ((RetryCountNotifier) this.in).setRetryCount(retryCount);
        }
    }
}

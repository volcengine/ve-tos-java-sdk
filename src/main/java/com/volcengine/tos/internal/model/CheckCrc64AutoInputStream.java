package com.volcengine.tos.internal.model;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.Utils;
import com.volcengine.tos.internal.util.CRC64Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;

public class CheckCrc64AutoInputStream extends CheckedInputStream implements RetryCountNotifier {
    private final String serverCrc64;
    public CheckCrc64AutoInputStream(InputStream in, Checksum cksum, String serverCrc64) {
        super(in, cksum);
        this.serverCrc64 = serverCrc64;
    }

    @Override
    public int read() throws IOException {
        int n = super.read();
        if (n == -1) {
            checkCrc64();
        }
        return n;
    }

    @Override
    public int read(byte[] buf, int off, int len) throws IOException {
        int n = super.read(buf, off, len);
        if (n == -1) {
            checkCrc64();
        }
        return n;
    }

    @Override
    public void close() throws IOException {
        super.close();
    }

    private void checkCrc64() {
        long clientCrc64 = super.getChecksum().getValue();
        if (!Utils.isSameHashCrc64Ecma(clientCrc64, serverCrc64)) {
            String clientCrc64String = CRC64Utils.longToUnsignedLongString(clientCrc64);
            throw new TosClientException("tos: expect crc64 " + serverCrc64 +
                    ", actual crc64 " + clientCrc64String, null);
        }
    }

    @Override
    public void setRetryCount(int retryCount) {
        if (this.in instanceof RetryCountNotifier) {
            ((RetryCountNotifier) this.in).setRetryCount(retryCount);
        }
    }
}

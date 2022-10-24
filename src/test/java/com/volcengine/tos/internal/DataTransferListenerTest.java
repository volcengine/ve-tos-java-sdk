package com.volcengine.tos.internal;

import com.volcengine.tos.comm.event.DataTransferListener;
import com.volcengine.tos.comm.event.DataTransferStatus;
import com.volcengine.tos.comm.event.DataTransferType;
import com.volcengine.tos.internal.model.DataTransferListenInputStream;
import com.volcengine.tos.internal.model.SimpleDataTransferListenInputStream;
import com.volcengine.tos.internal.util.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataTransferListenerTest {
    @Test
    void listenerTest() throws IOException {
        ByteArrayInputStream in = new ByteArrayInputStream(StringUtils.randomString(514 << 10).getBytes());
        List<DataTransferStatus> statusList = new ArrayList<>();
        DataTransferListener listener = statusList::add;
        DataTransferListenInputStream inputStream = new SimpleDataTransferListenInputStream(in, listener, 514 << 10);
        StringUtils.toString(inputStream);
        Assert.assertEquals(statusList.size(), 4);
        Assert.assertEquals(statusList.get(0).getType(), DataTransferType.DATA_TRANSFER_STARTED);
        Assert.assertEquals(statusList.get(0).getTotalBytes(), 526336);
        Assert.assertEquals(statusList.get(0).getConsumedBytes(), 0);

        Assert.assertEquals(statusList.get(1).getType(), DataTransferType.DATA_TRANSFER_RW);
        Assert.assertEquals(statusList.get(1).getConsumedBytes(), 524288);
        Assert.assertEquals(statusList.get(1).getRwOnceBytes(), 524288);

        Assert.assertEquals(statusList.get(2).getType(), DataTransferType.DATA_TRANSFER_RW);
        Assert.assertEquals(statusList.get(2).getConsumedBytes(), 526336);
        Assert.assertEquals(statusList.get(2).getRwOnceBytes(), 2048);

        Assert.assertEquals(statusList.get(3).getType(), DataTransferType.DATA_TRANSFER_SUCCEED);
        Assert.assertEquals(statusList.get(3).getConsumedBytes(), 526336);
    }
}

package com.volcengine.tos.internal.taskman;


import com.volcengine.tos.internal.TosObjectRequestHandler;
import com.volcengine.tos.model.object.UploadFileV2Input;
import com.volcengine.tos.model.object.UploadPartInfo;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;

public class UploadFileTaskHandlerTest {
    private UploadFileTaskHandler uploadFileTaskHandler;
    private Method getPartsFromFileMethod;

    @BeforeTest
    void init() throws Exception {
        uploadFileTaskHandler = new UploadFileTaskHandler(new UploadFileV2Input().setFilePath(""), new TosObjectRequestHandler(null, null), true);

        // Use reflection to access private method
        getPartsFromFileMethod = UploadFileTaskHandler.class.getDeclaredMethod("getPartsFromFile", long.class, long.class);
        getPartsFromFileMethod.setAccessible(true);
    }

    @Test
    @SuppressWarnings("unchecked")
    void getPartsFromFileTest() throws Exception {
        // 0字节文件
        List<UploadPartInfo> parts = (List<UploadPartInfo>) getPartsFromFileMethod.invoke(
                uploadFileTaskHandler, 0L, 20 * 1024 * 1024);
        Assert.assertEquals(parts.size(), 1);
        Assert.assertEquals(parts.get(0).getPartNumber(), 1);
        Assert.assertEquals(parts.get(0).getPartSize(), 0);

        // 20MB文件
        parts = (List<UploadPartInfo>) getPartsFromFileMethod.invoke(
                uploadFileTaskHandler, 20 * 1024 * 1024L, 20 * 1024 * 1024);
        Assert.assertEquals(parts.size(), 1);
        Assert.assertEquals(parts.get(0).getPartNumber(), 1);
        Assert.assertEquals(parts.get(0).getPartSize(), 20 * 1024 * 1024);

        // 200MB文件
        parts = (List<UploadPartInfo>) getPartsFromFileMethod.invoke(
                uploadFileTaskHandler, 200 * 1024 * 1024L, 20 * 1024 * 1024);
        Assert.assertEquals(parts.size(), 10);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(parts.get(i).getPartNumber(), i + 1);
            Assert.assertEquals(parts.get(i).getPartSize(), 20 * 1024 * 1024);
        }

        // 200MB+ 1文件
        parts = (List<UploadPartInfo>) getPartsFromFileMethod.invoke(
                uploadFileTaskHandler, 200 * 1024 * 1024L + 1, 20 * 1024 * 1024);
        Assert.assertEquals(parts.size(), 11);
        for (int i = 0; i < 11; i++) {
            Assert.assertEquals(parts.get(i).getPartNumber(), i + 1);
            Assert.assertEquals(parts.get(i).getPartSize(), i == 10 ? 1 : 20 * 1024 * 1024);
        }

        // 200MB- 1文件
        parts = (List<UploadPartInfo>) getPartsFromFileMethod.invoke(
                uploadFileTaskHandler, 200 * 1024 * 1024L - 1, 20 * 1024 * 1024);
        Assert.assertEquals(parts.size(), 10);
        for (int i = 0; i < 10; i++) {
            Assert.assertEquals(parts.get(i).getPartNumber(), i + 1);
            Assert.assertEquals(parts.get(i).getPartSize(), i == 9 ? 20 * 1024 * 1024 - 1 : 20 * 1024 * 1024);
        }
    }
}
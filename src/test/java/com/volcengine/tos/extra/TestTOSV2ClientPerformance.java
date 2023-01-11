package com.volcengine.tos.extra;

import com.volcengine.tos.*;
import com.volcengine.tos.auth.StaticCredentials;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.model.object.GetObjectV2Input;
import com.volcengine.tos.model.object.GetObjectV2Output;
import com.volcengine.tos.model.object.PutObjectInput;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class TestTOSV2ClientPerformance {
    private static final TOSV2 client = new TOSV2ClientBuilder().build(TOSClientConfiguration.builder().region(Consts.region)
            .endpoint(Consts.endpoint).credentials(new StaticCredentials(Consts.accessKey, Consts.secretKey)).build());
    private static final int file512KBNum = 100;
    private static final int file20MBNum = 10;
    private static final List<String> fileNameList = new ArrayList<>();
    private static final List<String> keyList = new ArrayList<>();
    private static String file1GBName;
    private static final Random random = new Random();

    @BeforeTest
    void initTest() {
        prepareLocalData();
        keyList.addAll(prepareKeyList(1000));
    }

    void prepareLocalData() {
        prepareFile(file512KBNum, 512 * 1024);
        prepareFile(file20MBNum, 20 * 1024 * 1024);
//        file1GBName = prepareLargeFile(1024 * 1024 * 1024);
    }

    private void prepareFile(int fileNum, long fileSize) {
        for (int i = 0; i < fileNum; i++) {
            String fileName = getUniqueObjectKey();
            long length = 0;
            byte[] tmp = new byte[1024];
            Arrays.fill(tmp, (byte) ('A' + i));

            try(FileOutputStream fos = new FileOutputStream(fileName)) {
                while (length < fileSize) {
                    fos.write(tmp);
                    length += tmp.length;
                }
            } catch (IOException e) {
                Assert.fail();
            }
            Assert.assertEquals(new File(fileName).length(), fileSize);
            fileNameList.add(fileName);
        }
    }

    private String prepareLargeFile(long fileSize) {
        String fileName = getUniqueObjectKey();
        long length = 0;
        byte[] tmp = new byte[1024];
        Arrays.fill(tmp, (byte) ('A'));

        try(FileOutputStream fos = new FileOutputStream(fileName)) {
            while (length < fileSize) {
                fos.write(tmp);
                length += tmp.length;
            }
        } catch (IOException e) {
            Assert.fail();
        }
        Assert.assertEquals(new File(fileName).length(), fileSize);
        return fileName;
    }

    private List<String> prepareKeyList(int keyNum) {
        List<String> res = new ArrayList<>(keyNum);
        for (int i = 0; i < keyNum; i++) {
            res.add(getUniqueObjectKey());
        }
        return res;
    }

    private List<String> chooseARandomKeyAndFile() {
        return Arrays.asList(keyList.get(random.nextInt(keyList.size())), fileNameList.get(random.nextInt(fileNameList.size())));
    }

    @Test
    void concurrentThread10Test() {
        int threadNum = 10;
        int reqTimes = 1000;
        int failedNum = runAndGetResult(threadNum, reqTimes);
        Assert.assertTrue(failedNum < 10);
    }

    @Test
    void concurrentThread100Test() {
        int threadNum = 100;
        int reqTimes = 10000;
        int failedNum = runAndGetResult(threadNum, reqTimes);
        Assert.assertTrue(failedNum < 100);
    }

    @Test
    void concurrentThread500Test() {
        int threadNum = 500;
        int reqTimes = 50000;
        int failedNum = runAndGetResult(threadNum, reqTimes);
        Assert.assertTrue(failedNum < 500);
    }

    private int runAndGetResult(int threadNum, int reqTimes) {
        ExecutorService executorService = new ThreadPoolExecutor(threadNum, threadNum, 1000, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        List<Future<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < reqTimes; i++) {
            futures.add(executorService.submit(() ->{
                int ret = 0;
                List<String> keyAndFile = chooseARandomKeyAndFile();
                try{
                    client.putObject(new PutObjectInput()
                            .setBucket(Consts.bucket).setKey(keyAndFile.get(0))
                            .setContent(new FileInputStream(keyAndFile.get(1))));
                    try(GetObjectV2Output output = client.getObject(new GetObjectV2Input()
                            .setBucket(Consts.bucket).setKey(keyAndFile.get(0)))) {
                        if (output.getContent() != null) {
                            byte[] a = new byte[1024];
                            while(output.getContent().read(a) != -1) {
                                // ignore
                            }
                        }
                    } catch (IOException e) {
                        ret = 1;
                    }
                }catch (TosException | FileNotFoundException e) {
                    ret = 1;
                }
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    ret = 1;
                }
                return ret;
            }));
        }
        int failedNum = 0;
        try{
            for (Future<Integer> future : futures) {
                failedNum += future.get();
            }
        } catch (CancellationException | ExecutionException | InterruptedException e) {
            // 执行用户被取消 // 执行中断 // 执行失败
            Assert.fail();
        } finally {
            executorService.shutdown();
        }
        try{
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Assert.fail();
        }
        return failedNum;
    }

    private String getUniqueObjectKey() {
        return StringUtils.randomString(10);
    }
}

package com.volcengine.tos.internal;

import com.volcengine.tos.internal.util.FileUtils;
import okhttp3.HttpUrl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileTest {
    @Test
    void parseFilePathTest() throws IOException {
        String path = "aaa/bbb/ccc";
        String key = "ddd.txt";
        deleteFileRecursive(new File("aaa"));
        try{
            // dir exists
            Assert.assertTrue(new File(path).mkdirs());
            Assert.assertEquals(FileUtils.parseFilePath(path, key), path + "/" + key);

            Assert.assertEquals(FileUtils.parseFilePath(path, key+"/"), "");
            Assert.assertTrue(new File(path+"/"+key+"/").exists());
            Assert.assertTrue(new File(path+"/"+key+"/").isDirectory());

            key = "eee.txt";
            path = path + "/" + key; // aaa/bbb/ccc/eee.txt
            Assert.assertEquals(FileUtils.parseFilePath(path, key), path);
            new File(path).createNewFile();
            Assert.assertEquals(FileUtils.parseFilePath(path, key), path);

            // dir not exists
            path = "aaa/bbb/fff";
            key = "ggg.txt";
            Assert.assertEquals(FileUtils.parseFilePath(path, key), path);
            Assert.assertTrue(new File(path.substring(0, 7)).exists());
            Assert.assertTrue(new File(path.substring(0, 7)).isDirectory());

            path = "aaa/bbb/hhh/";
            key = "iii.txt";
            Assert.assertEquals(FileUtils.parseFilePath(path, key), path + key);
            Assert.assertTrue(new File(path).exists());
            Assert.assertTrue(new File(path).isDirectory());
        } finally {
            deleteFileRecursive(new File("aaa"));
        }
    }

    protected static boolean deleteFileRecursive(File dirFile) {
        if (dirFile == null) {
            return false;
        }
        if (!dirFile.exists()) {
            return false;
        }
        if (dirFile.isFile()) {
            return dirFile.delete();
        } else {
            for (File file : dirFile.listFiles()) {
                deleteFileRecursive(file);
            }
        }
        return dirFile.delete();
    }

    @Test
    void urlParseTest() {
        String url = "https://localhost";
        HttpUrl parsedUrl = HttpUrl.parse(url);
        System.out.println(parsedUrl.port());
    }

//    @Test
//    void fileRenameTest() throws IOException {
//        File src = new File("aaa.txt");
//        File dst = new File("bbb.txt");
//        try(FileOutputStream srcs = new FileOutputStream(src);
//            FileOutputStream dsts = new FileOutputStream(dst)) {
//            srcs.write("aaa.txt".getBytes());
//            dsts.write("bbb.txt".getBytes());
//        }
//        Assert.assertEquals(StringUtils.toString(new FileInputStream(src)), "aaa.txt");
//        dst.renameTo(src);
//        Assert.assertEquals(StringUtils.toString(new FileInputStream(src)), "bbb.txt");
//    }

//    @Test
//    void stringTest(){
//        OffsetDateTime now = Instant.now().atOffset(ZoneOffset.UTC);
//        String date8601 = now.format(SigningUtils.iso8601Layout);
//        String dateDay = now.format(SigningUtils.yyyyMMdd);
//        String region = "cn-beijing";
//        String hexValue = "1238IG789hIYTSDSDJOA9572348479";
//        final char split = '\n';
//        long start = System.nanoTime();
//        for (int i = 0; i < 100000000; i++) {
//            String buf = SigningUtils.signPrefix + split +
//                    date8601 + split +
//                    dateDay + '/' + region + "/tos/request" + split +
//                    hexValue;
//        }
//        long end = System.nanoTime();
//        System.out.println((end-start) / 1e6 + " ms");
//
//        start = System.nanoTime();
//        for (int i = 0; i < 100000000; i++) {
//            StringBuilder builder = new StringBuilder();
//            builder.append(split);
//            builder.append(date8601);
//            builder.append(split);
//            builder.append(dateDay);
//            builder.append('/');
//            builder.append(region);
//            builder.append("/tos/request");
//            builder.append(split);
//            builder.append(hexValue);
//            String buf = builder.toString();
//        }
//        end = System.nanoTime();
//        System.out.println((end-start) / 1e6 + " ms");
//
//    }
    @Test
    void filePathTest() {
        List<String> res = getFileListFromPath("useVolcSDK/src/main/java/self/basic/CreateTOSV2ClientWithIdlegExample.java");
        Assert.assertNotNull(res);
        for (String re : res) {
            System.out.println(re);
        }
    }
    private static List<String> getFileListFromPath(String filePath) {
        List<String> fileList = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) {
            return fileList;
        }
        File[] files = file.listFiles();
        if (files == null || files.length == 0) {
            return fileList;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                fileList.addAll(getFileListFromPath(f.getPath()));
            } else {
                fileList.add(f.getPath());
            }
        }
        return fileList;
    }
}

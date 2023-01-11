package com.volcengine.tos.internal;

import com.volcengine.tos.internal.util.FileUtils;
import okhttp3.HttpUrl;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;

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
}

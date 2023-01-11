package com.volcengine.tos.internal.util;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream;
import com.volcengine.tos.comm.io.TosRepeatableFileInputStream;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

public class FileUtils {
    public static InputStream getFileContent(FileInputStream fileInputStream, File file, String filePath) {
        if (fileInputStream != null) {
            return new TosRepeatableFileInputStream(fileInputStream);
        }
        if (file != null) {
            try {
                return new TosRepeatableFileInputStream(file);
            } catch (IOException e) {
                throw new TosClientException("getFileContent failed.", e);
            }
        }
        if (filePath != null) {
            try {
                FileInputStream fis = new FileInputStream(filePath);
                return new TosRepeatableFileInputStream(fis);
            } catch (IOException e) {
                throw new TosClientException("getFileContent failed.", e);
            }
        }
        throw new TosClientException("file info is not set in the input, please set filepath at least", null);
    }

    public static InputStream getBoundedFileContent(FileInputStream fileInputStream, File file, String filePath, long offset, long partSize) {
        if (offset < 0) {
            throw new TosClientException("file offset is small than 0", null);
        }
        if (fileInputStream != null) {
            try{
                fileInputStream.skip(offset);
                return new TosRepeatableBoundedFileInputStream(fileInputStream, partSize);
            } catch (IOException e) {
                throw new TosClientException("getBoundedFileContent failed.", e);
            }
        }
        if (file != null) {
            try{
                FileInputStream fis = new FileInputStream(file);
                fis.skip(offset);
                return new TosRepeatableBoundedFileInputStream(fis, partSize);
            } catch (IOException e) {
                throw new TosClientException("getBoundedFileContent failed.", e);
            }
        }
        if (filePath != null) {
            try{
                FileInputStream fis = new FileInputStream(filePath);
                fis.skip(offset);
                return new TosRepeatableBoundedFileInputStream(fis, partSize);
            } catch (IOException e) {
                throw new TosClientException("getBoundedFileContent failed.", e);
            }
        }
        throw new TosClientException("file info is not set in the input, please set filepath at least", null);
    }

    public static String parseFilePath(String filePath, String key) {
        ParamsChecker.ensureNotNull(filePath, "filePath");
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isDirectory()) {
                return buildNewPathWithKeyAndCreateDir(filePath, key);
            }
        } else {
            if (filePath.endsWith(File.separator)){
                // if not exists aa/bb/, create dir
                return buildNewPathWithKeyAndCreateDir(filePath, key);
            }
            // if not exists aa/bb, create parent dir which is aa
            if (!file.getParentFile().exists() && !file.getParentFile().mkdirs()) {
                throw new TosClientException("tos: can not create directory in: " + file.getParentFile().getPath(), null);
            }
        }
        return filePath;
    }

    private static String buildNewPathWithKeyAndCreateDir(String filePath, String key) {
        String split = File.separator;
        if (filePath.endsWith(File.separator)) {
            // create parent dir
            new File(filePath).mkdirs();
            split = "";
        }
        String newPath = filePath + split + key;
        if (StringUtils.isNotEmpty(newPath) && newPath.endsWith(File.separator)) {
            if (!(new File(newPath).mkdirs())) {
                throw new TosClientException("tos: can not create directory in: " + newPath, null);
            }
            return "";
        }
        return newPath;
    }
}

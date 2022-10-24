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
    public static InputStream getFileContent(FileInputStream fileInputStream, File file, String filePath) throws FileNotFoundException {
        if (fileInputStream != null) {
            return new TosRepeatableFileInputStream(fileInputStream);
        }
        if (file != null) {
            return new TosRepeatableFileInputStream(file);
        }
        if (filePath != null) {
            FileInputStream fis = new FileInputStream(filePath);
            return new TosRepeatableFileInputStream(fis);
        }
        throw new IllegalArgumentException("file info is not set in the input, pls set filepath at least");
    }

    public static InputStream getBoundedFileContent(FileInputStream fileInputStream, File file, String filePath, long offset, long partSize) throws IOException {
        if (offset < 0) {
            throw new IllegalArgumentException("file offset is small than 0");
        }
        if (fileInputStream != null) {
            fileInputStream.skip(offset);
            return new TosRepeatableBoundedFileInputStream(fileInputStream, partSize);
        }
        if (file != null) {
            FileInputStream fis = new FileInputStream(file);
            fis.skip(offset);
            return new TosRepeatableBoundedFileInputStream(fis, partSize);
        }
        if (filePath != null) {
            FileInputStream fis = new FileInputStream(filePath);
            fis.skip(offset);
            return new TosRepeatableBoundedFileInputStream(fis, partSize);
        }
        throw new IllegalArgumentException("file info is not set in the input, pls set filepath at least");
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

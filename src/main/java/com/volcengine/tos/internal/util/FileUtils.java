package com.volcengine.tos.internal.util;

import com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream;
import com.volcengine.tos.comm.io.TosRepeatableFileInputStream;

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
}

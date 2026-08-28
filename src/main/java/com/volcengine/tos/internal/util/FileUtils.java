package com.volcengine.tos.internal.util;

import com.volcengine.tos.TosClientException;
import com.volcengine.tos.comm.io.TosRepeatableBoundedFileInputStream;
import com.volcengine.tos.comm.io.TosRepeatableFileInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


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

    public static long getFileLength(File file, String filePath) {
        if (file != null) {
            return file.length();
        }
        if (StringUtils.isNotEmpty(filePath)) {
            return new File(filePath).length();
        }
        return -1;
    }

    public static InputStream getBoundedFileContent(FileInputStream fileInputStream, File file, String filePath, long offset, long partSize) {
        if (offset < 0) {
            throw new TosClientException("file offset is small than 0", null);
        }
        if (fileInputStream != null) {
            try {
                fileInputStream.skip(offset);
                return new TosRepeatableBoundedFileInputStream(fileInputStream, partSize);
            } catch (IOException e) {
                throw new TosClientException("getBoundedFileContent failed.", e);
            }
        }
        if (file != null) {
            try {
                FileInputStream fis = new FileInputStream(file);
                fis.skip(offset);
                return new TosRepeatableBoundedFileInputStream(fis, partSize);
            } catch (IOException e) {
                throw new TosClientException("getBoundedFileContent failed.", e);
            }
        }
        if (filePath != null) {
            try {
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
            if (filePath.endsWith(File.separator)) {
                // if not exists aa/bb/, create dir
                return buildNewPathWithKeyAndCreateDir(filePath, key);
            }

            if (file.getParentFile() == null) {
                throw new TosClientException("tos: the directory is not exist: " + file.getPath(), null);
            }

            // if not exists aa/bb, create parent dir which is aa
            FileUtils.ensureDirectoryExists(file.getParentFile());
        }
        return filePath;
    }

    private static String buildNewPathWithKeyAndCreateDir(String filePath, String key) {
        String split = File.separator;
        if (filePath.endsWith(File.separator)) {
            // create parent dir
            FileUtils.ensureDirectoryExists(new File(filePath));
            split = "";
        }
        String newPath = filePath + split + key;
        if (StringUtils.isNotEmpty(newPath) && newPath.endsWith(File.separator)) {
            FileUtils.ensureDirectoryExists(new File(newPath));
            return "";
        }
        return newPath;
    }

    private static void ensureDirectoryExists(File dir) {
        if (!dir.exists()) {
            dir.mkdirs();
            // 二次检查：可能是另一个线程已经创建了
            if (!dir.exists()) {
                throw new TosClientException("tos: can not create directory in: " + dir.getPath(), null);
            }
        }
    }

    public static boolean renameTo(File src, File dest, boolean overwrite) {
        if (src == null || dest == null) {
            return false;
        }
        if (!src.exists()) {
            return false;
        }

        try {
            if (overwrite) {
                try {
                    Files.move(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
                } catch (IOException e) {
                    Files.move(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                if (dest.exists()) {
                    return false;
                }
                Files.move(src.toPath(), dest.toPath());
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

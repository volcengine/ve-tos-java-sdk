package com.volcengine.tos.model.object;

import java.io.File;
import java.io.FileInputStream;

public class PutObjectFromFileInput {
    private PutObjectBasicInput putObjectBasicInput;
    private String filePath;
    private File file;
    private FileInputStream fileInputStream;

    public PutObjectFromFileInput(PutObjectBasicInput putObjectBasicInput, String filePath) {
        this.putObjectBasicInput = putObjectBasicInput;
        this.filePath = filePath;
    }

    public PutObjectFromFileInput(PutObjectBasicInput putObjectBasicInput, File file) {
        this.putObjectBasicInput = putObjectBasicInput;
        this.file = file;
    }

    public PutObjectFromFileInput(PutObjectBasicInput putObjectBasicInput, FileInputStream fileInputStream) {
        this.putObjectBasicInput = putObjectBasicInput;
        this.fileInputStream = fileInputStream;
    }

    public PutObjectFromFileInput setPutObjectBasicInput(PutObjectBasicInput putObjectBasicInput) {
        this.putObjectBasicInput = putObjectBasicInput;
        return this;
    }

    public PutObjectFromFileInput setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public PutObjectFromFileInput setFile(File file) {
        this.file = file;
        return this;
    }

    public PutObjectFromFileInput setFileInputStream(FileInputStream fileInputStream) {
        this.fileInputStream = fileInputStream;
        return this;
    }

    public PutObjectBasicInput getPutObjectBasicInput() {
        return putObjectBasicInput;
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return file;
    }

    public FileInputStream getFileInputStream() {
        return fileInputStream;
    }

    @Override
    public String toString() {
        return "PutObjectFromFileInput{" +
                "putObjectBasicInput=" + putObjectBasicInput +
                ", filePath='" + filePath + '\'' +
                ", file=" + file +
                ", fileInputStream=" + fileInputStream +
                '}';
    }
}

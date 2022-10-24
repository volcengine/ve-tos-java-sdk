package com.volcengine.tos.model.object;

import java.io.File;

public class GetObjectToFileInput {
    private GetObjectV2Input getObjectInputV2;
    private String filePath;
    private File file;

    public GetObjectToFileInput(GetObjectV2Input getObjectInputV2, String filePath) {
        this.getObjectInputV2 = getObjectInputV2;
        this.filePath = filePath;
    }

    public GetObjectToFileInput(GetObjectV2Input getObjectInputV2, File file) {
        this.getObjectInputV2 = getObjectInputV2;
        this.file = file;
    }

    public GetObjectV2Input getGetObjectInputV2() {
        return getObjectInputV2;
    }

    public String getFilePath() {
        return filePath;
    }

    public File getFile() {
        return file;
    }

    public GetObjectToFileInput setGetObjectInputV2(GetObjectV2Input getObjectInputV2) {
        this.getObjectInputV2 = getObjectInputV2;
        return this;
    }

    public GetObjectToFileInput setFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }

    public GetObjectToFileInput setFile(File file) {
        this.file = file;
        return this;
    }

    public static GetObjectToFileInputBuilder builder() {
        return new GetObjectToFileInputBuilder();
    }

    @Override
    public String toString() {
        return "GetObjectToFileInput{" +
                "getObjectInputV2=" + getObjectInputV2 +
                ", filePath='" + filePath + '\'' +
                '}';
    }


    public static final class GetObjectToFileInputBuilder {
        private GetObjectV2Input getObjectInputV2;
        private String filePath;
        private File file;

        private GetObjectToFileInputBuilder() {
        }

        public GetObjectToFileInputBuilder getObjectInputV2(GetObjectV2Input getObjectInputV2) {
            this.getObjectInputV2 = getObjectInputV2;
            return this;
        }

        public GetObjectToFileInputBuilder filePath(String filePath) {
            this.filePath = filePath;
            return this;
        }

        public GetObjectToFileInputBuilder file(File file) {
            this.file = file;
            return this;
        }

        public GetObjectToFileInput build() {
            GetObjectToFileInput getObjectToFileInput = new GetObjectToFileInput(getObjectInputV2, filePath);
            getObjectToFileInput.setFile(file);
            return getObjectToFileInput;
        }
    }
}

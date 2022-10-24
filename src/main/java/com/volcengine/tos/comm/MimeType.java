package com.volcengine.tos.comm;

import com.volcengine.tos.TosClientException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MimeType {

    public static final String DEFAULT_MIMETYPE = "binary/octet-stream";

    private static MimeType mimetype = null;

    private HashMap<String, String> mimetypeMap = new HashMap<>();

    private MimeType() {
    }

    public synchronized static MimeType getInstance() throws TosClientException{
        if (mimetype != null){
            return mimetype;
        }
        mimetype = new MimeType();
        // 从配置文件读取扩展名映射
        try(InputStream mimeTypesStream = mimetype.getClass().getResourceAsStream("/tos.mime.types")){
            if (mimeTypesStream != null) {
                mimetype.loadMimetypesFromFile(mimeTypesStream);
            }
        } catch (IOException e) {
            throw new TosClientException("tos: failed to load mime types from file", e);
        }
        return mimetype;
    }

    private void loadMimetypesFromFile(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            line = line.trim();
            if (line.length() != 0 && !line.startsWith("#")) {
                StringTokenizer st = new StringTokenizer(line, " \t");
                if (st.countTokens() > 1) {
                    String extension = st.nextToken();
                    String mimeTypeName = st.nextToken();
                    // 逐行读取到map
                    mimetypeMap.put(extension, mimeTypeName);
                }
            }
        }
    }

    public String getMimetype(String objectKey) {
        String mimeType = getMimetypeByObjectKey(objectKey);
        if (mimeType != null) {
            return mimeType;
        }
        return DEFAULT_MIMETYPE;
    }

    private String getMimetypeByObjectKey(String objectKey) {
        int lastPeriodIndex = objectKey.lastIndexOf(".");
        if (lastPeriodIndex > 0 && lastPeriodIndex + 1 < objectKey.length()) {
            String ext = objectKey.substring(lastPeriodIndex + 1).toLowerCase();
            if (mimetypeMap.containsKey(ext)) {
                return mimetypeMap.get(ext);
            }
        }
        return null;
    }
}

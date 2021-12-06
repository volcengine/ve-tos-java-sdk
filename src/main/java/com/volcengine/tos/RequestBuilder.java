package com.volcengine.tos;

import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.comm.TosHeader;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Map;
import java.util.Objects;
import java.net.URLEncoder;

class RequestBuilder {
    private Signer signer;
    private String scheme;
    private String host;
    private String bucket;
    private String object;
    private int URLMode;
    private long contentLength;
    private HttpRange range;
    private Map<String, String> headers;
    private Map<String, String> query;
    private boolean autoRecognizeContentType = true;

    RequestBuilder(){}

    RequestBuilder(Signer signer, String scheme, String host, String bucket, String object,
                   int urlMode, Map<String, String> headers, Map<String, String> query){
        Objects.requireNonNull(signer, "signer is null");
        Objects.requireNonNull(scheme, "scheme is null");
        Objects.requireNonNull(host, "host is null");
        Objects.requireNonNull(bucket, "bucket is null");
        Objects.requireNonNull(object, "object is null");
        this.signer = signer;
        this.scheme = scheme;
        this.host = host;
        this.bucket = bucket;
        this.object = object;
        this.URLMode = urlMode;
        this.headers = headers;
        this.query = query;
    }

    public RequestBuilder setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Map<String, String> getQuery() {
        return query;
    }

    public RequestBuilder setQuery(Map<String, String> query) {
        this.query = query;
        return this;
    }

    HttpRange getRange() {
        return range;
    }

    void setRange(HttpRange range) {
        this.range = range;
    }

    RequestBuilder withQuery(String key, String value){
        this.query.put(key, value);
        return this;
    }

    RequestBuilder withHeader(String key, String value){
        if(value != null && value.length() != 0){
            this.headers.put(key, value);
        }
        return this;
    }

    Map<String, String> getHeaders() {
        return headers;
    }

    public boolean isAutoRecognizeContentType() {
        return autoRecognizeContentType;
    }

    public void setAutoRecognizeContentType(boolean autoRecognizeContentType) {
        this.autoRecognizeContentType = autoRecognizeContentType;
    }

    RequestBuilder withContentLength(long length){
        this.contentLength = length;
        return this;
    }

    private TosRequest build(String method, InputStream stream) throws IOException{
        String[] hostAndPath = hostPath();
        TosRequest request = new TosRequest(scheme, method, hostAndPath[0], hostAndPath[1], stream, query, headers);
        if (stream != null){
            if (contentLength != 0) {
                request.setContentLength(contentLength);
            } else{
                request.setContentLength(tryResolveLength(stream));
            }
        }
        return request;
    }

    private String[] hostPath(){
        String[] res = new String[]{this.host, ""};
        if (StringUtils.isEmpty(this.bucket)){
            res[1] = "/";
            return res;
        }
        res[0] = this.bucket+"."+this.host;
        res[1] = "/"+this.object;
        return res;
    }

    TosRequest Build(String method, InputStream stream) throws TosClientException{
        TosRequest request;
        try{
            request = build(method, stream);
        } catch (IOException e) {
            throw new TosClientException("build tos request failed", e);
        }
        if(signer != null){
            Map<String, String> signed = this.signer.signHeader(request);
            for (String key : signed.keySet()){
                request.getHeaders().put(key, signed.get(key));
            }
        }
        return request;
    }

    TosRequest BuildWithCopySource(String method, String srcBucket, String srcObject) throws TosClientException {
        TosRequest request;
        try{
            request = build(method, null);
        } catch (IOException e){
            throw new TosClientException("build tos request failed", e);
        }
        String versionID = request.getQuery().get("versionId");
        request.getQuery().remove("versionId");

        String cpSrcHeader = "";
        try{
            cpSrcHeader = copySource(srcBucket, srcObject, versionID);
        } catch (UnsupportedEncodingException e) {
            throw new TosClientException("object key encode exception", e);
        }
        request.getHeaders().put(TosHeader.HEADER_COPY_SOURCE, cpSrcHeader);
        if(signer != null){
            Map<String, String> signed = this.signer.signHeader(request);
            for (String key : signed.keySet()){
                request.getHeaders().put(key, signed.get(key));
            }
        }
        return request;
    }

    private String copySource(String bucket, String object, String versionID) throws UnsupportedEncodingException {
        if (versionID == null || versionID.equals("")) {
            return "/" + bucket + "/" + URLEncoder.encode(object, "UTF-8");
        }
        return "/" + bucket + "/" + URLEncoder.encode(object, "UTF-8") + "?versionId=" + versionID;
    }

    String preSignedURL(String method, Duration ttl) throws TosClientException {
        TosRequest request;
        try{
            request = build(method, null);
        } catch (IOException e){
            throw new TosClientException("build tos request failed", e);
        }

        if (this.signer == null){
            throw new TosClientException("tos: credentials is not set when the tos client was created", null);
        }
        Map<String, String> query = this.signer.signQuery(request, ttl);
        for (String key : query.keySet()){
            request.getQuery().put(key, query.get(key));
        }
        String url = "";
        url = request.toURL().toString();
        return url;
    }

    public static int tryResolveLength(InputStream stream) throws IOException {
        return stream.available() == 0 ? -1 : stream.available();
    }

}

class HttpRange {
    private long start;
    private long end;

    long getStart() {
        return start;
    }

    HttpRange setStart(long start) {
        this.start = start;
        return this;
    }

    long getEnd() {
        return end;
    }

    public HttpRange setEnd(long end) {
        this.end = end;
        return this;
    }

    @Override
    public String toString(){
        return "bytes=" + start + "-" + end;
    }
}
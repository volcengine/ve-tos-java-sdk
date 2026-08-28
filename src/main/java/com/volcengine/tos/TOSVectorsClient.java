package com.volcengine.tos;

import com.volcengine.tos.auth.SignV4;
import com.volcengine.tos.auth.Signer;
import com.volcengine.tos.internal.*;
import com.volcengine.tos.internal.util.ParamsChecker;
import com.volcengine.tos.internal.util.StringUtils;
import com.volcengine.tos.internal.util.TosUtils;
import com.volcengine.tos.model.vectors.CreateIndexInput;
import com.volcengine.tos.model.vectors.CreateIndexOutput;
import com.volcengine.tos.model.vectors.CreateVectorBucketInput;
import com.volcengine.tos.model.vectors.CreateVectorBucketOutput;
import com.volcengine.tos.model.vectors.DeleteIndexInput;
import com.volcengine.tos.model.vectors.DeleteIndexOutput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketInput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketOutput;
import com.volcengine.tos.model.vectors.DeleteVectorsInput;
import com.volcengine.tos.model.vectors.DeleteVectorsOutput;
import com.volcengine.tos.model.vectors.GetIndexInput;
import com.volcengine.tos.model.vectors.GetIndexOutput;
import com.volcengine.tos.model.vectors.GetVectorBucketInput;
import com.volcengine.tos.model.vectors.GetVectorBucketOutput;
import com.volcengine.tos.model.vectors.GetVectorsInput;
import com.volcengine.tos.model.vectors.GetVectorsOutput;
import com.volcengine.tos.model.vectors.ListIndexesInput;
import com.volcengine.tos.model.vectors.ListIndexesOutput;
import com.volcengine.tos.model.vectors.ListVectorsInput;
import com.volcengine.tos.model.vectors.ListVectorsOutput;
import com.volcengine.tos.model.vectors.ListVectorBucketsInput;
import com.volcengine.tos.model.vectors.ListVectorBucketsOutput;
import com.volcengine.tos.model.vectors.PutVectorsInput;
import com.volcengine.tos.model.vectors.PutVectorsOutput;
import com.volcengine.tos.model.vectors.PutVectorBucketPolicyInput;
import com.volcengine.tos.model.vectors.PutVectorBucketPolicyOutput;
import com.volcengine.tos.model.vectors.GetVectorBucketPolicyInput;
import com.volcengine.tos.model.vectors.GetVectorBucketPolicyOutput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketPolicyInput;
import com.volcengine.tos.model.vectors.DeleteVectorBucketPolicyOutput;
import com.volcengine.tos.model.vectors.QueryVectorsInput;
import com.volcengine.tos.model.vectors.QueryVectorsOutput;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import static com.volcengine.tos.internal.Consts.DEFAULT_USER_AGENT_ITEM;

public class TOSVectorsClient implements TOSVectors{
    private TOSVectorsClientConfiguration config;
    private TosVectorsRequestHandler vectorsRequestHandler;
    private Transport transport;
    private Signer signer;
    private TosRequestFactory factory;
    protected TOSVectorsClient(TOSVectorsClientConfiguration config) {
        validateAndInitConfig(config);
        initRequestHandler();
    }
    private void validateAndInitConfig(TOSVectorsClientConfiguration conf) {
        ParamsChecker.ensureNotNull(conf, "TOSClientConfiguration");
        ParamsChecker.ensureNotNull(conf.getRegion(), "region");
        this.config = conf;
    }
    private void initRequestHandler() {
        if (this.factory == null) {
            if (this.transport == null) {
                setIsHttpByEndpoint(this.config.getEndpoint());
                this.transport = new RequestTransport(this.config.getTransportConfig());
            }
            // 允许 signer 为空，匿名访问
            if (this.signer == null) {
                if (this.config.getCredentialsProvider() != null) {
                    this.signer = new SignV4(this.config.getCredentialsProvider(), this.config.getRegion(), Consts.TOS_VECTORS_SERVICE_NAME);
                }
            }
            this.factory = new TosRequestFactory(this.signer, this.config.getEndpoint()).setUserAgent(this.buildUserAgent());
        }
        this.vectorsRequestHandler = new TosVectorsRequestHandler(this.transport, this.factory);
    }
    private void setIsHttpByEndpoint(String endpoint) {
        if (this.config == null || this.config.getTransportConfig() == null || StringUtils.isEmpty(endpoint)) {
            return;
        }
        boolean isHttp = endpoint.startsWith(Consts.SCHEME_HTTP);
        config.getTransportConfig().setHttp(isHttp);
    }
    private String buildUserAgent() {
        if (StringUtils.isEmpty(this.config.getUserAgentProductName()) && StringUtils.isEmpty(this.config.getUserAgentSoftName())
                && StringUtils.isEmpty(this.config.getUserAgentSoftVersion()) && (this.config.getUserAgentCustomizedKeyValues() == null || this.config.getUserAgentCustomizedKeyValues().isEmpty())) {
            return TosUtils.getUserAgent();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(TosUtils.getUserAgent());
        sb.append(" -- ");
        if (StringUtils.isEmpty(this.config.getUserAgentProductName())) {
            sb.append(DEFAULT_USER_AGENT_ITEM);
        } else {
            sb.append(this.config.getUserAgentProductName());
        }
        sb.append("/");
        if (StringUtils.isEmpty(this.config.getUserAgentSoftName())) {
            sb.append(DEFAULT_USER_AGENT_ITEM);
        } else {
            sb.append(this.config.getUserAgentSoftName());
        }
        sb.append("/");
        if (StringUtils.isEmpty(this.config.getUserAgentSoftVersion())) {
            sb.append(DEFAULT_USER_AGENT_ITEM);
        } else {
            sb.append(this.config.getUserAgentSoftVersion());
        }
        if (this.config.getUserAgentCustomizedKeyValues() != null && !this.config.getUserAgentCustomizedKeyValues().isEmpty()) {
            sb.append(" (");
            int index = 0;
            for (Map.Entry<String, String> e : this.config.getUserAgentCustomizedKeyValues().entrySet()) {
                if (e.getKey() == null || e.getValue() == null) {
                    continue;
                }
                sb.append(e.getKey());
                sb.append("/");
                sb.append(e.getValue());
                if (index != this.config.getUserAgentCustomizedKeyValues().size() - 1) {
                    sb.append(";");
                }
                index++;
            }
            sb.append(")");
        }
        return sb.toString();
    }

    @Override
    public CreateVectorBucketOutput createVectorBucket(CreateVectorBucketInput input) throws TosException {
        return vectorsRequestHandler.createVectorBucket(input);
    }

    @Override
    public GetVectorBucketOutput getVectorBucket(GetVectorBucketInput input) throws TosException {
        return vectorsRequestHandler.getVectorBucket(input);
    }

    @Override
    public DeleteVectorBucketOutput deleteVectorBucket(DeleteVectorBucketInput input) throws TosException {
        return vectorsRequestHandler.deleteVectorBucket(input);
    }

    @Override
    public ListVectorBucketsOutput listVectorBuckets(ListVectorBucketsInput input) throws TosException {
        return vectorsRequestHandler.listVectorBuckets(input);
    }

    @Override
    public CreateIndexOutput createIndex(CreateIndexInput input) throws TosException {
        return vectorsRequestHandler.createIndex(input);
    }

    @Override
    public GetIndexOutput getIndex(GetIndexInput input) throws TosException {
        return vectorsRequestHandler.getIndex(input);
    }

    @Override
    public DeleteIndexOutput deleteIndex(DeleteIndexInput input) throws TosException {
        return vectorsRequestHandler.deleteIndex(input);
    }

    @Override
    public ListIndexesOutput listIndexes(ListIndexesInput input) throws TosException {
        return vectorsRequestHandler.listIndexes(input);
    }

    @Override
    public PutVectorsOutput putVectors(PutVectorsInput input) throws TosException {
        return vectorsRequestHandler.putVectors(input);
    }

    @Override
    public DeleteVectorsOutput deleteVectors(DeleteVectorsInput input) throws TosException {
        return vectorsRequestHandler.deleteVectors(input);
    }

    @Override
    public GetVectorsOutput getVectors(GetVectorsInput input) throws TosException {
        return vectorsRequestHandler.getVectors(input);
    }

    @Override
    public ListVectorsOutput listVectors(ListVectorsInput input) throws TosException {
        return vectorsRequestHandler.listVectors(input);
    }

    @Override
    public PutVectorBucketPolicyOutput putVectorBucketPolicy(PutVectorBucketPolicyInput input) throws TosException {
        return vectorsRequestHandler.putVectorBucketPolicy(input);
    }

    @Override
    public GetVectorBucketPolicyOutput getVectorBucketPolicy(GetVectorBucketPolicyInput input) throws TosException {
        return vectorsRequestHandler.getVectorBucketPolicy(input);
    }

    @Override
    public DeleteVectorBucketPolicyOutput deleteVectorBucketPolicy(DeleteVectorBucketPolicyInput input) throws TosException {
        return vectorsRequestHandler.deleteVectorBucketPolicy(input);
    }

    @Override
    public QueryVectorsOutput queryVectors(QueryVectorsInput input) throws TosException {
        return vectorsRequestHandler.queryVectors(input);
    }

    @Override
    public void close() throws IOException {
        if (this.transport != null && this.transport instanceof Closeable) {
            try {
                ((Closeable) this.transport).close();
            } catch (IOException ex) {

            }
        }
        if (this.config != null && this.config.getCredentialsProvider() != null && (this.config.getCredentialsProvider() instanceof Closeable)) {
            try {
                ((Closeable) this.config.getCredentialsProvider()).close();
            } catch (IOException ex) {

            }
        }
    }
}

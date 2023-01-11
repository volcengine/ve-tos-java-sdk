package com.volcengine.tos.auth;
import com.volcengine.tos.internal.TosRequest;

import java.time.Duration;
import java.util.Map;

public interface Signer {
    Map<String, String> signHeader(TosRequest req);
    Map<String, String> signQuery(TosRequest req, Duration ttl);
    Credentials getCredential();
    String getRegion();
}

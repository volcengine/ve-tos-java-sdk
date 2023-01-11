package com.volcengine.tos.model.object;

import java.util.Map;

public interface PreSignedPolicyURLGenerator {
    String getSignedURLForList(Map<String, String> additionalQuery);
    String getSignedURLForGetOrHead(String key, Map<String, String> additionalQuery);
}

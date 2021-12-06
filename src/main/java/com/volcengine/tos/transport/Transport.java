package com.volcengine.tos.transport;

import com.volcengine.tos.TosRequest;
import com.volcengine.tos.TosResponse;

import java.io.IOException;

public interface Transport {
    /**
     * a roundTrip includes doing a Tos request and wrapping the result with Tos response
     *
     * @param request Tos Request
     * @return {@link TosResponse}
     * @throws IOException
     */
    TosResponse roundTrip(TosRequest request) throws IOException;
}

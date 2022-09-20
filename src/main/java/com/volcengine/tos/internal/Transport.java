package com.volcengine.tos.internal;

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

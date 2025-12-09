package com.cinema.common.base;

public class BaseRequest {
    // common request metadata (traceId, userId, etc.)
    private String traceId;
    private String userId;

    public String getTraceId() { return traceId; }
    public void setTraceId(String traceId) { this.traceId = traceId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
}

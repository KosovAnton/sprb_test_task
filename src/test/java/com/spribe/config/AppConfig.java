package com.spribe.config;

import java.util.Map;

public class AppConfig {
    private String baseUrl;
    private Integer connectionTimeoutMs;
    private Integer readTimeoutMs;
    private Boolean logHttp;
    private Boolean userDelete;
    private String editor;
    private Map<String, String> defaultHeaders;

    public String getBaseUrl() {
        return baseUrl;
    }

    public Integer getConnectionTimeoutMs() {
        return connectionTimeoutMs;
    }

    public void setConnectionTimeoutMs(Integer connectionTimeoutMs) {
        this.connectionTimeoutMs = connectionTimeoutMs;
    }

    public Integer getReadTimeoutMs() {
        return readTimeoutMs;
    }

    public void setReadTimeoutMs(Integer readTimeoutMs) {
        this.readTimeoutMs = readTimeoutMs;
    }

    public Boolean isLogHttp() {
        return logHttp != null && logHttp;
    }

    public void setLogHttp(Boolean logHttp) {
        this.logHttp = logHttp;
    }

    public Boolean isUserDelete() {
        return userDelete != null && userDelete;
    }

    public void setUserDelete(Boolean userDelete) {
        this.userDelete = userDelete;
    }

    public String getEditor() {
        return editor;
    }

    public Map<String, String> getDefaultHeaders() {
        return defaultHeaders;
    }

    public void setDefaultHeaders(Map<String, String> defaultHeaders) {
        this.defaultHeaders = defaultHeaders;
    }
}

package com.spribe.http.spec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.spribe.config.AppConfig;
import com.spribe.http.filters.AllureRestAssuredFilter;
import com.spribe.http.filters.Slf4jRestAssuredFilter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.*;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static io.restassured.RestAssured.config;

public final class RequestSpecFactory {
    private RequestSpecFactory() {
    }

    private static ObjectMapper mapper() {
        var m = new ObjectMapper();
        m.registerModule(new JavaTimeModule());
        return m;
    }

    private static RestAssuredConfig raConfig(AppConfig cfg) {
        return config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", cfg.getConnectionTimeoutMs())
                        .setParam("http.socket.timeout", cfg.getReadTimeoutMs()))
                .encoderConfig(EncoderConfig.encoderConfig()
                        .defaultContentCharset(StandardCharsets.UTF_8)
                        .appendDefaultContentCharsetToContentTypeIfUndefined(true))
                .decoderConfig(DecoderConfig.decoderConfig()
                        .defaultContentCharset(StandardCharsets.UTF_8))
                .objectMapperConfig(new ObjectMapperConfig()
                        .jackson2ObjectMapperFactory((type, s) -> mapper()));
    }

    public static RequestSpecification create(AppConfig cfg, PrintStream log) {
        var b = new RequestSpecBuilder()
                .setBaseUri(cfg.getBaseUrl())
                .setConfig(raConfig(cfg))
                .setRelaxedHTTPSValidation()
                .setAccept(ContentType.JSON)
                .setContentType(ContentType.JSON);

        Map<String, String> headers = cfg.getDefaultHeaders();
        if (headers != null && !headers.isEmpty()) b.addHeaders(headers);

        b.addFilter(AllureRestAssuredFilter.get());
        b.addFilter(new Slf4jRestAssuredFilter());

        if (cfg.isLogHttp()) {
            b.addFilter(new RequestLoggingFilter(LogDetail.ALL, log));
            b.addFilter(new ResponseLoggingFilter(LogDetail.ALL, log));
        }

        return b.build();
    }

    public static RequestSpecProvider provider(AppConfig cfg, PrintStream log) {
        final RequestSpecification base = create(cfg, log);
        return () -> base;
    }
}

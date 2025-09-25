package com.spribe.http.filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Set;

public class Slf4jRestAssuredFilter implements Filter {
    private final Logger httpLog = LoggerFactory.getLogger("HTTP");

    public static final Set<String> SENSITIVE_HEADERS = Set.of(
            "Authorization", "Cookies", "Set-Cookie"
    );

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        //Request
        String method = requestSpec.getMethod();
        String uri = requestSpec.getURI();

        httpLog.info(">>> {} {}", method, uri);
        httpLog.debug("Request headers:\n{}", maskHeaders(requestSpec.getHeaders().toString()));

        Object body = requestSpec.getBody();
        if (body != null) {
            httpLog.debug("Request body:\n{}", body);
        }

        //Execute
        long t = System.nanoTime();
        Response response = ctx.next(requestSpec, responseSpec);
        long elapsedMs = (System.nanoTime() - t) / 1_000_000;

        //Response
        int status = response.statusCode();
        httpLog.info("<<< {} {} ({} ms)", status, response.statusLine(), elapsedMs);

        String respBody;

        try {
            respBody = new String(response.getBody().asByteArray(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            respBody = "<failed to read body: " + e.getMessage() + ">";
        }

        if (!respBody.isBlank()) {
            httpLog.debug("Response body:\n{}", respBody);
        }

        return response;
    }

    public static String maskHeaders(String headersDump) {
        String masked = headersDump;
        for (String h : SENSITIVE_HEADERS) {
            masked = masked.replaceAll("(?i)(" + h + ":\\s*)([^\\r\\n]+)", "$1***");
        }

        return masked;
    }
}

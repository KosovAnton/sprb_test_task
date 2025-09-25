package com.spribe.http.filters;

import io.qameta.allure.restassured.AllureRestAssured;

public final class AllureRestAssuredFilter {
    private static final AllureRestAssured INSTANCE = new AllureRestAssured();

    public static AllureRestAssured get() {
        return INSTANCE;
    }
}

package com.spribe.http.spec;

import io.restassured.specification.RequestSpecification;

public interface RequestSpecProvider {
    RequestSpecification get();
}

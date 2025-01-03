package com.bbc.rms.baseurl;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;


public class RMS_BaseUrl {

    public static RequestSpecification spec;

    public static void setUp() {
        String baseUrl = "https://testapi.io/api/RMSTest/ibltest/";

        spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(baseUrl)
                .build();
    }

}

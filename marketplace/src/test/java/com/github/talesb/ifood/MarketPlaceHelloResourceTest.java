package com.github.talesb.ifood;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class MarketPlaceHelloResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hellomarketplace")
          .then()
             .statusCode(200)
             .body(is("Hello MarketPlace"));
    }

}
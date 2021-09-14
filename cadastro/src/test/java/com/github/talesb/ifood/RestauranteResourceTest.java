package com.github.talesb.ifood;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.approvaltests.Approvals;

@QuarkusTest
public class RestauranteResourceTest {

    @Test
    public void testBuscarRestaurantes() {
      String resultado =  given()
          .when().get("/restaurantes")
          .then()
             .statusCode(200)
             .extract()
             .asString();
      
      Approvals.verifyJson(resultado);
    }

}
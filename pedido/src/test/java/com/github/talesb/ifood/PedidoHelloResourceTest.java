package com.github.talesb.ifood;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class PedidoHelloResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/hellopedido")
          .then()
             .statusCode(200)
             .body(is("Hello Pedido"));
    }

}
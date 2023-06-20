package com.amsidh.vertx.resources;

import com.amsidh.vertx.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class QuataResourceTest {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void getAssetForQuataTest(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    webClient.get("/quata/1")
      .send().onComplete(response -> {
        if (response.succeeded()) {
          JsonObject responseBody = response.result().bodyAsJsonObject();
          Assertions.assertEquals("{\"id\":1,\"name\":\"SBI\"}", responseBody.getJsonObject("asset").encode());
          Assertions.assertEquals(200, response.result().statusCode());
          testContext.completeNow();
        }
      });
  }
  @Test
  void assetNotFoundForQuataIdTest(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    webClient.get("/quata/99")
      .send().onComplete(response -> {
        if (response.succeeded()) {
          JsonObject responseBody = response.result().bodyAsJsonObject();
          Assertions.assertEquals("{\"message\":\"Quata Asset not found with quata id 99\",\"path\":\"/quata/99\"}", responseBody.encode());
          Assertions.assertEquals(404, response.result().statusCode());
          testContext.completeNow();
        }
      });
  }
}

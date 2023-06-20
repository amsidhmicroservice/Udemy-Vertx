package com.amsidh.vertx.resources;

import com.amsidh.vertx.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class AssetResourceTest {

  @BeforeEach
  void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
    vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
  }

  @Test
  void getAllAssetTest(Vertx vertx, VertxTestContext testContext) throws Throwable {
    WebClient webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(MainVerticle.PORT));
    webClient.get("/assets")
      .send().onComplete(response -> {
        if (response.succeeded()) {
          String responseBody = response.result().bodyAsString();
          Assertions.assertEquals("[[{\"id\":1,\"name\":\"SBI\"},{\"id\":2,\"name\":\"SBM\"},{\"id\":3,\"name\":\"Canara\"},{\"id\":4,\"name\":\"BOI\"},{\"id\":5,\"name\":\"BOB\"},{\"id\":6,\"name\":\"ICICI\"}]]", responseBody);
          Assertions.assertEquals(200, response.result().statusCode());
          testContext.completeNow();
        }
      });

  }
}

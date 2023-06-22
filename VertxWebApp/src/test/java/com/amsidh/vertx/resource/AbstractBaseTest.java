package com.amsidh.vertx.resource;

import com.amsidh.vertx.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static com.amsidh.vertx.config.ConfigLoader.SERVER_PORT;

@ExtendWith(VertxExtension.class)
public abstract class AbstractBaseTest {
    public static final Integer TEST_SERVER_PORT = 8888;
    WebClient webClient;

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        System.setProperty(SERVER_PORT, String.valueOf(TEST_SERVER_PORT));
        webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }
}

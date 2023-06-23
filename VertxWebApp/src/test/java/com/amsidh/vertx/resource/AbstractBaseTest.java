package com.amsidh.vertx.resource;

import com.amsidh.vertx.config.ConfigLoader;
import com.amsidh.vertx.verticle.MainVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.client.WebClientOptions;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public abstract class AbstractBaseTest {
    public static final Integer TEST_SERVER_PORT = 8888;
    WebClient webClient;

    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        System.setProperty(ConfigLoader.SERVER_PORT, String.valueOf(TEST_SERVER_PORT));
        System.setProperty(ConfigLoader.DB_HOST, "localhost");
        System.setProperty(ConfigLoader.DB_PORT, "5432");
        System.setProperty(ConfigLoader.DB_DATABASE, "customerdb");
        System.setProperty(ConfigLoader.DB_USER, "postgres");
        System.setProperty(ConfigLoader.DB_PASSWORD, "secret");

        webClient = WebClient.create(vertx, new WebClientOptions().setDefaultPort(TEST_SERVER_PORT));
        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }
}

package com.amsidh.udemy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.Random;

public class MainVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.createHttpServer().requestHandler(req -> {
      req.response()
        .putHeader("content-type", "text/plain")
        .end("Hello World");
    }).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        LOGGER.info("The HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
    vertx.setPeriodic(500, id -> {
      LOGGER.info("Redeploy " + new Random().nextDouble());
    });
  }
}

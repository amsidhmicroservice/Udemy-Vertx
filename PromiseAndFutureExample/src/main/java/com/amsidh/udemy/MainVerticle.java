package com.amsidh.udemy;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class MainVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName(), handler -> {
      if (handler.failed()) {
        LOGGER.error("Failed to load MainVerticle", handler.cause());
      }
    });
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();

    // Promise with Void
    Promise<Void> voidPromise = Promise.promise();
    vertx.setTimer(500, handler -> {
      voidPromise.complete();
    });
    Future<Void> future = voidPromise.future();
    future.onSuccess(hander -> {
      LOGGER.info("Operation completed");
    }).onFailure(handler -> {
      LOGGER.error("Operation failed", handler.getCause());
    });

    // Promise with String

    Promise<String> stringPromise = Promise.promise();
    vertx.setTimer(1000, handler -> {
      stringPromise.complete("StringPromise is completed");
    });
    Future<String> stringFuture = stringPromise.future();
    stringFuture.onSuccess(handler -> {
      LOGGER.info("StringFuture :" + handler);
    }).onFailure(throwable -> LOGGER.error("StringFuture failed", throwable));

    // Promise with JsonObject
    Promise<JsonObject> jsonObjectPromise = Promise.promise();
    vertx.setTimer(1000, handlr -> {
      jsonObjectPromise.complete(new JsonObject().put("id", 1).put("name", "Amsidh"));
      throw new RuntimeException("Manually throwing");

    });
    Future<JsonObject> jsonObjectFuture = jsonObjectPromise.future();
    jsonObjectFuture.onSuccess(result -> LOGGER.info("JsonObjectFuture is completed and result is " + result))
      .onFailure(throwable -> LOGGER.error("Failed JsonObjectFuture", throwable));


  }

}

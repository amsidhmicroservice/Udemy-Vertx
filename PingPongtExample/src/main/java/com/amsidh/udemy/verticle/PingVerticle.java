package com.amsidh.udemy.verticle;

import com.amsidh.udemy.model.Ping;
import com.amsidh.udemy.model.Pong;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonObject;

public class PingVerticle extends AbstractVerticle {

  private static final Logger LOGGER = LoggerFactory.getLogger(PingVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();

    Ping ping = Ping.builder().message("Hello World").enable(true).build();
    vertx.eventBus().<Pong>request(PingVerticle.class.getName(), ping, messageAsyncResult -> {
      if (messageAsyncResult.succeeded()) {
        LOGGER.info("Delivery status " + messageAsyncResult.result().body());
      } else {
        LOGGER.error("Message sending failed", messageAsyncResult.cause());
      }
    });
  }
}

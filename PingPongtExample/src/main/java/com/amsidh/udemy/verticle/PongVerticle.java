package com.amsidh.udemy.verticle;

import com.amsidh.udemy.model.Ping;
import com.amsidh.udemy.model.Pong;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class PongVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(PongVerticle.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.eventBus().<Ping>consumer(PingVerticle.class.getName(), handler -> {
      LOGGER.info("Message received " + handler.body());
      handler.reply(Pong.builder().id(1).build());
    }).exceptionHandler(throwable -> LOGGER.error("Failed ", throwable));
  }
}

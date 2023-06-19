package com.amsidh.udemy.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class Consumer1 extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.eventBus().<String>consumer("my-message-address", result -> {
      System.out.println("Consumer1 Message received " + result.body());
    });
  }
}

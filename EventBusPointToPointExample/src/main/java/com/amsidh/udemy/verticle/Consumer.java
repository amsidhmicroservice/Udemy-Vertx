package com.amsidh.udemy.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class Consumer extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.eventBus().consumer("my.address.bus", result -> {
      String receivedMessage = (String) result.body();
      System.out.println("Consumer Message received "+ receivedMessage);
    });
  }
}

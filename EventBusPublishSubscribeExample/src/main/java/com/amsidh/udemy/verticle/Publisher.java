package com.amsidh.udemy.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.time.Duration;

public class Publisher extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.setPeriodic(Duration.ofSeconds(2).toMillis(), repeat -> {
      System.out.println("Message published");
      vertx.eventBus().publish("my-message-address", "Publishing message to every one");
    });
  }
}

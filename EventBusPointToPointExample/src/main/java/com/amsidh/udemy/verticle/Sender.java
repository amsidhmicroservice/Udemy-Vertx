package com.amsidh.udemy.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.time.Duration;

public class Sender extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.setPeriodic(Duration.ofSeconds(2).toMillis(), h -> {
      System.out.println("Sending message");
      vertx.eventBus().send("my.address.bus", "Sender Hello World");
    });

  }
}

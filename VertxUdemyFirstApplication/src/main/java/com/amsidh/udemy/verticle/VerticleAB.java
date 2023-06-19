package com.amsidh.udemy.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class VerticleAB extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println("Verticle " + VerticleAB.class.getName() + "started");
    startPromise.complete();
  }
}

package com.amsidh.udemy.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class VerticleAA extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    System.out.println("Verticle " + VerticleAA.class.getName() + "started and config is " + this.config());
    startPromise.complete();
  }
}

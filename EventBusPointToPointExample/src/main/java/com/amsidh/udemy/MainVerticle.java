package com.amsidh.udemy;

import com.amsidh.udemy.verticle.Consumer;
import com.amsidh.udemy.verticle.Sender;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;

public class MainVerticle extends AbstractVerticle {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.deployVerticle(Sender.class.getName());
    vertx.deployVerticle(Consumer.class.getName());
  }
}

package com.amsidh.udemy;

import com.amsidh.udemy.verticle.Consumer1;
import com.amsidh.udemy.verticle.Consumer2;
import com.amsidh.udemy.verticle.Consumer3;
import com.amsidh.udemy.verticle.Publisher;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
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
    vertx.deployVerticle(Publisher.class.getName());
    vertx.deployVerticle(Consumer1.class.getName());
    vertx.deployVerticle(Consumer2.class.getName());
    vertx.deployVerticle(Consumer3.class.getName(), new DeploymentOptions().setInstances(3));
  }
}

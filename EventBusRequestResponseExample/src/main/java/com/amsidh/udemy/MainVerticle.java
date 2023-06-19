package com.amsidh.udemy;

import com.amsidh.udemy.verticle.RequestVerticle;
import com.amsidh.udemy.verticle.ResponseVerticle;
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
    vertx.deployVerticle(RequestVerticle.class.getName());
    vertx.deployVerticle(ResponseVerticle.class.getName(), new DeploymentOptions().setInstances(2));
  }
}

package com.amsidh.udemy.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import java.util.UUID;

public class VerticleA extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    String id = UUID.randomUUID().toString();
    vertx.deployVerticle(VerticleAA.class.getName(), new DeploymentOptions().setInstances(2).setConfig(new JsonObject().put("id", id)));
    vertx.deployVerticle(VerticleAB.class.getName());

    System.out.println("Verticle " + VerticleA.class.getName() + "started");
    startPromise.complete();

  }
}

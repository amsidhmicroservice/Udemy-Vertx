package com.amsidh.udemy;

import io.vertx.core.*;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class EventLoopExample extends AbstractVerticle {

  Logger LOG = LoggerFactory.getLogger(EventLoopExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions()
      .setMaxEventLoopExecuteTime(500)
      .setMaxWorkerExecuteTimeUnit(TimeUnit.MILLISECONDS)
      .setBlockedThreadCheckInterval(1)
      .setBlockedThreadCheckIntervalUnit(TimeUnit.MILLISECONDS)
      .setEventLoopPoolSize(4));

    vertx.deployVerticle(EventLoopExample.class.getName(), new DeploymentOptions().setInstances(4));

  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Start "+ EventLoopExample.class.getName());
    startPromise.complete();
    //Do not do this in verticle
     Thread.sleep(5000);

  }
}

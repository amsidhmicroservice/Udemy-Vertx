package com.amsidh.udemy;

import com.amsidh.udemy.verticle.PingVerticle;
import com.amsidh.udemy.verticle.PongVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class MainVerticle extends AbstractVerticle {
  private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(MainVerticle.class.getName());
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.deployVerticle(PingVerticle.class.getName()).onFailure(logOnError("Failed to deploy PingVerticle"));
    vertx.deployVerticle(PongVerticle.class.getName()).onFailure(logOnError("Failed to deploy PongVerticle"));
  }

  private static Handler<Throwable> logOnError(String Failed_to_deploy_PingVerticle) {
    return throwable -> LOGGER.error(Failed_to_deploy_PingVerticle, throwable);
  }
}

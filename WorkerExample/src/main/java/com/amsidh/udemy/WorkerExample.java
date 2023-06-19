package com.amsidh.udemy;

import com.amsidh.udemy.verticle.BlockingCodeVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class WorkerExample extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(WorkerExample.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(WorkerExample.class.getName(), new DeploymentOptions()
      .setWorkerPoolName("my-worker-thread"));
  }

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(new BlockingCodeVerticle(),
      new DeploymentOptions().setWorker(true)
      .setWorkerPoolSize(1).setWorkerPoolName("my-worker-verticle"));
    startPromise.complete();
    executeBlockingCode();
  }

  private void executeBlockingCode() {
    vertx.executeBlocking(event -> {
      LOG.info("Executing blocking code" + " Thread name " + Thread.currentThread().getName());
      try {
        Thread.sleep(5000);
        event.complete();
      } catch (InterruptedException ex) {
        LOG.error("Failed ", ex);
        event.fail("Blocking code failed" + " Thread name " + Thread.currentThread().getName());
      }
    }, result -> {
      if (result.succeeded()) {
        LOG.info("All Blocking operation completed" + " Thread name " + Thread.currentThread().getName());
      } else {
        LOG.error("Blocking operation failed ", result.cause());
      }
    });
  }
}

package com.amsidh.udemy.verticle;

import com.amsidh.udemy.WorkerExample;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;

public class BlockingCodeVerticle extends AbstractVerticle {
  private static final Logger LOG = LoggerFactory.getLogger(WorkerExample.class);

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    LOG.info("Deployed as Worker BlockingCode Verticle");
    startPromise.complete();
    Thread.sleep(5000);
  }
}

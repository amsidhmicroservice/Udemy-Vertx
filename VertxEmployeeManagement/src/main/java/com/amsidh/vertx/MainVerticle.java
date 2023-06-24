package com.amsidh.vertx;

import com.amsidh.vertx.verticle.RestApiVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    vertx.deployVerticle(RestApiVerticle.class.getName(), new DeploymentOptions()
        .setInstances(getHalfNumberOfInstances()))
      .onFailure(error -> log.error("Error in RestApiVerticle deploying {}", error))
      .onSuccess(handlerId -> {
        log.info("Deployment of {} with id {}", RestApiVerticle.class.getName(), handlerId);
        startPromise.complete();
      });
  }


  private Integer getHalfNumberOfInstances() {
    //return Runtime.getRuntime().availableProcessors()/2;
    return 1;
  }
}

package com.amsidh.vertx;

import com.amsidh.vertx.resource.CustomerRestApiVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MainVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.deployVerticle(CustomerRestApiVerticle.class.getName(), new DeploymentOptions()
                        .setInstances(getHalfProcessors()))
                .onFailure(startPromise::fail)
                .onSuccess(id -> {
                    log.info("{} started with id-> {}", CustomerRestApiVerticle.class.getName(), id);
                    startPromise.complete();
                });
    }

    private static int getHalfProcessors() {
        return Math.max(1, Runtime.getRuntime().availableProcessors()) / 2;
    }


}


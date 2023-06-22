package com.amsidh.vertx;

import com.amsidh.vertx.resource.CustomerRestApiVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MainVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        /*vertx.deployVerticle(ConfigVerticle.class.getName())
                .onFailure(startPromise::fail)
                .onSuccess(handler -> {
                    log.info("Deployed {} with id {}", ConfigVerticle.class.getName(), handler);
                }).compose(next -> loadCustomerRestApiVerticle(startPromise));*/
        loadCustomerRestApiVerticle(startPromise);

    }

    private Future<String> loadCustomerRestApiVerticle(Promise<Void> startPromise) {
        return vertx.deployVerticle(CustomerRestApiVerticle.class.getName(), new DeploymentOptions()
                        .setInstances(getHalfProcessors()))
                .onFailure(startPromise::fail)
                .onSuccess(id -> {
                    log.info("Deployed {} with id {}", CustomerRestApiVerticle.class.getName(), id);
                    startPromise.complete();
                });
    }

    private static int getHalfProcessors() {
        return Math.max(1, Runtime.getRuntime().availableProcessors()) / 2;
    }


}


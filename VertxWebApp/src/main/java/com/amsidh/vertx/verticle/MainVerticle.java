package com.amsidh.vertx.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MainVerticle extends AbstractVerticle {
    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        deployVersionInfoVerticle(startPromise)
                .compose(next -> deployDatabaseMigrationVerticle(startPromise))
                .compose(next -> loadCustomerRestApiVerticle(startPromise));
    }

    private Future<String> deployVersionInfoVerticle(Promise<Void> startPromise) {
        return vertx.deployVerticle(VersionInfoVerticle.class.getName()).onFailure(startPromise::fail)
                .onSuccess(handler -> log.info("Verticle  VersionInfoVerticle id {} deployed successfully!!!", handler));
    }

    private Future<String> deployDatabaseMigrationVerticle(Promise<Void> startPromise) {
        return vertx.deployVerticle(FlywayDatabaseMigrationVerticle.class.getName()).onFailure(startPromise::fail)
                .onSuccess(handler -> log.info("Verticle  FlywayDatabaseMigrationVerticle id {} deployed successfully!!!", handler));
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


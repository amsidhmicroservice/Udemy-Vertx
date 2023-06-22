package com.amsidh.vertx.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigVerticle extends AbstractVerticle {


    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        //Config Storage Option
        ConfigStoreOptions configStoreOptionsYaml = new ConfigStoreOptions()
                .setType("file")
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path", "application.yaml"));
        //Config Retriever Option
        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions()
                .addStore(configStoreOptionsYaml);

        ConfigRetriever configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
        configRetriever.getConfig()
                .onFailure(startPromise::fail)
                .onSuccess(handler -> {
                    log.info("Configuration loaded successfully!!!");
                    startPromise.complete();
                });
    }
}

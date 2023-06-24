package com.amsidh.vertx.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConfigLoader {
  public static Future<ConfigModel> load(Vertx vertx) {
    log.info("Loading all properties from yaml configuration file");
    ConfigStoreOptions yamlConfigStoreOptions = new ConfigStoreOptions()
      .setType("file")
      .setFormat("yaml")
      .setConfig(new JsonObject().put("path", "application.yaml"));
    ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions()
      .addStore(yamlConfigStoreOptions);
    ConfigRetriever configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
    return configRetriever.getConfig().map(ConfigModel::from);
  }
}

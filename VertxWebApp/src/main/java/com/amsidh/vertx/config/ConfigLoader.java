package com.amsidh.vertx.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;


@Slf4j
public class ConfigLoader {
    public static final String CONFIG_FILE = "application.yaml";
    // Exposed Environment Variables
    public static final String SERVER_PORT = "SERVER_PORT";
    public static final String DB_HOST = "DB_HOST";
    public static final String DB_PORT = "DB_PORT";
    public static final String DB_DATABASE = "DB_DATABASE";
    public static final String DB_USER = "DB_USER";
    public static final String DB_PASSWORD = "DB_PASSWORD";
    static final List<String> EXPOSED_ENVIRONMENT_VARIABLES = Arrays.asList(SERVER_PORT, DB_HOST, DB_PORT, DB_DATABASE, DB_USER, DB_PASSWORD);

    public static Future<CustomerConfig> load(Vertx vertx) {
        final JsonArray exposedKeys = new JsonArray();
        EXPOSED_ENVIRONMENT_VARIABLES.forEach(exposedKeys::add);
        log.info("Fetch configuration for {}", exposedKeys.encode());
        //Config Storage Option
        ConfigStoreOptions envConfigStoreOptions = new ConfigStoreOptions()
                .setType("env")
                .setConfig(new JsonObject().put("keys", exposedKeys));

        ConfigStoreOptions sysConfigStoreOptions = new ConfigStoreOptions()
                .setType("sys")
                .setConfig(new JsonObject().put("cache", false));

        ConfigStoreOptions yamlConfigStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path", CONFIG_FILE));

        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions()
                .addStore(sysConfigStoreOptions)
                .addStore(envConfigStoreOptions)
                .addStore(yamlConfigStoreOptions);

        ConfigRetriever configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
        return configRetriever.getConfig().map(CustomerConfig::from);

    }
}

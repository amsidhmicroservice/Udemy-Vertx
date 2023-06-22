package com.amsidh.vertx.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.List;


public class ConfigLoader {
    public static final String SERVER_PORT = "SERVER_PORT";
    static final List<String> EXPOSED_ENVIRONMENT_VARIABLES = Arrays.asList(SERVER_PORT);

    public static Future<JsonObject> load(Vertx vertx) {
        //Config Storage Option
        JsonArray exposedKeys = new JsonArray();
        EXPOSED_ENVIRONMENT_VARIABLES.forEach(exposedKeys::add);

        ConfigStoreOptions envConfigStoreOptionsEnv = new ConfigStoreOptions()
                .setType("env")
                .setConfig(new JsonObject().put("keys", exposedKeys));

        ConfigStoreOptions sysConfigStoreOption = new ConfigStoreOptions()
                .setType("sys").setConfig(new JsonObject().put("cache", false));

        //Config Storage Option
        ConfigStoreOptions yamlConfigStoreOptions = new ConfigStoreOptions()
                .setType("file")
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path", "application.yaml"));

        ConfigRetrieverOptions configRetrieverOptions = new ConfigRetrieverOptions()
                .addStore(sysConfigStoreOption)
                .addStore(envConfigStoreOptionsEnv)
                .addStore(yamlConfigStoreOptions);

        ConfigRetriever configRetriever = ConfigRetriever.create(vertx, configRetrieverOptions);
        return configRetriever.getConfig();
    }
}

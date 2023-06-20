package com.amsidh.vertx.resources;

import com.amsidh.vertx.model.Asset;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.Router;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class AssetResource {
  public static final Map<Integer, Asset> ASSETS = new HashMap<>();

  static {
    ASSETS.put(1, Asset.builder().id(1).name("SBI").build());
    ASSETS.put(2, Asset.builder().id(2).name("SBM").build());
    ASSETS.put(3, Asset.builder().id(3).name("Canara").build());
    ASSETS.put(4, Asset.builder().id(4).name("BOI").build());
    ASSETS.put(5, Asset.builder().id(5).name("BOB").build());
    ASSETS.put(6, Asset.builder().id(6).name("ICICI").build());
  }

  public static void attach(Router router) {
    log.info("Inside getAllAssets method of AssetResource");
    router.get("/assets").handler(routingContext -> {
      final JsonArray jsonArray = JsonArray.of(ASSETS.values());
      log.info("Request URL {} respond with {}", routingContext.normalizedPath(), jsonArray.encodePrettily());
      routingContext.response().end(jsonArray.toBuffer());
    });
  }
}

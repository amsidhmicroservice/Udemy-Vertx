package com.amsidh.vertx.resources;

import com.amsidh.vertx.model.Asset;
import com.amsidh.vertx.model.Quata;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;


@Slf4j
@RequiredArgsConstructor
public class QuataResource {
  public static void attach(Router router) {
    log.info("Inside getAllAssets method of AssetResource");
    router.get("/quata/:quataId").handler(routingContext -> {
      Integer quataId = Integer.valueOf(routingContext.pathParam("quataId"));
      Optional<Asset> optionalAsset = Optional.ofNullable(AssetResource.ASSETS.get(quataId));
      if (optionalAsset.isPresent()) {
        Quata quata = Quata.builder().asset(optionalAsset.get()).bidPrice(randomBigDecimal()).askPrice(randomBigDecimal()).finalPrice(randomBigDecimal()).quantity(randomBigDecimal()).build();
        JsonObject jsonObject = quata.toJsonObject();
        log.info("Request URL {} respond with {}", routingContext.normalizedPath(), jsonObject.encodePrettily());
        routingContext.response().end(jsonObject.toBuffer());
      } else {
        log.info("Quata Asset not found with quata id {}", quataId);
        routingContext.response().setStatusCode(HttpResponseStatus.NOT_FOUND.code())
          .end(new JsonObject().put("message", String.format("Quata Asset not found with quata id %d", quataId)).put("path", routingContext.normalizedPath()).toBuffer());
      }
    });
  }

  private static BigDecimal randomBigDecimal() {
    return BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(0, 100));
  }
}

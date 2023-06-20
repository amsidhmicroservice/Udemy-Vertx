package com.amsidh.vertx;

import com.amsidh.vertx.resources.AssetResource;
import com.amsidh.vertx.resources.QuataResource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainVerticle extends AbstractVerticle {

  public static final int PORT = 8888;

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    log.info("Inside start method of MainVerticle");
    HttpServer httpServer = vertx.createHttpServer();
    Router router = Router.router(vertx);
    AssetResource.attach(router);
    QuataResource.attach(router);
    router.route().failureHandler(handleFailure());

    httpServer.requestHandler(router).listen(PORT, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        log.info("The HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  private static Handler<RoutingContext> handleFailure() {
    return routingContext -> {
      if (routingContext.response().ended()) {
        // Do nothing
        return;
      }
      log.error("Request failed.", routingContext.failure());
      routingContext.response().setStatusCode(500).end(new JsonObject().put("message", "Failed to handle the request").toBuffer());
    };
  }
}

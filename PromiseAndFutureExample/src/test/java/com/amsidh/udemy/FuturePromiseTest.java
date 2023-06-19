package com.amsidh.udemy;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.impl.logging.Logger;
import io.vertx.core.impl.logging.LoggerFactory;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(VertxExtension.class)
public class FuturePromiseTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(FuturePromiseTest.class);

  @Test
  public void promiseSucessTest(Vertx vertx, VertxTestContext vertxTestContext) {
    LOGGER.info("Start");
    Promise<String> promise = Promise.<String>promise();
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOGGER.info("Success");
      vertxTestContext.completeNow();
    });
    LOGGER.info("End");
  }

  @Test
  public void promiseFailTest(Vertx vertx, VertxTestContext vertxTestContext) {
    LOGGER.info("Start");
    Promise<String> promise = Promise.<String>promise();
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed"));
      LOGGER.info("Failed");
      vertxTestContext.completeNow();
    });
    LOGGER.info("End");
  }

  @Test
  public void futureSuccessTest(Vertx vertx, VertxTestContext vertxTestContext) {
    LOGGER.info("Start");
    Promise<String> promise = Promise.<String>promise();
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOGGER.info("Timer Done");
    });
    promise.future().onSuccess(result -> {
      LOGGER.info("Result " + result);
      vertxTestContext.completeNow();
    }).onFailure(throwable -> {
      LOGGER.error("Failed", throwable);
      vertxTestContext.failed();
    });
  }

  @Test
  public void futureFailedTest(Vertx vertx, VertxTestContext vertxTestContext) {
    LOGGER.info("Start");
    Promise<String> promise = Promise.<String>promise();
    vertx.setTimer(500, id -> {
      promise.fail(new RuntimeException("Failed"));
      LOGGER.info("Timer Done");
    });
    promise.future().onSuccess(vertxTestContext::failNow)
      .onFailure(throwable -> {
        LOGGER.error("Failed", throwable);
        vertxTestContext.completeNow();
      });
  }

  @Test
  public void futureMapTest(Vertx vertx, VertxTestContext vertxTestContext) {
    LOGGER.info("Start");
    Promise<String> promise = Promise.<String>promise();
    vertx.setTimer(500, id -> {
      promise.complete("Success");
      LOGGER.info("Timer Done");
    });
    promise.future()
      .map(message -> new JsonObject().put("status", message))
      .map(jsonObject -> new JsonArray().add(jsonObject))
      .onSuccess(result -> {
        LOGGER.info("Result " + result);
        vertxTestContext.completeNow();
      }).onFailure(vertxTestContext::failNow);
  }

  @Test
  public void futureCoordinationTest(Vertx vertx, VertxTestContext vertxTestContext) {
    vertx.createHttpServer()

      .requestHandler(request -> {
        LOGGER.info("Request" + request);
      })

      .listen(8585)
      .onFailure(error -> {
        LOGGER.error("Failed to start the server", error);
        vertxTestContext.failNow(error);
      })
      .compose(httpServer -> {
        LOGGER.info("Another task");
        return Future.succeededFuture(httpServer);
      })
      .compose(httpServer -> {
        LOGGER.info("Still more task...");
        return Future.succeededFuture(httpServer);
      })
      .onSuccess(message -> {
        LOGGER.info("Server started successfully on port " + message.actualPort());
        vertxTestContext.completeNow();
      });
  }

  @Test
  public void futureComposition(Vertx vertx, VertxTestContext vertxTestContext) {
    Promise p1 = Promise.promise();
    Promise<String> p2 = Promise.<String>promise();
    Promise<JsonObject> p3 = Promise.<JsonObject>promise();

    vertx.setTimer(500, id -> {
      p1.complete();
      p2.complete();
      //p3.fail("Failed");
      p3.complete();
    });

    Future f1 = p1.future();
    Future f2 = p2.future();
    Future f3 = p3.future();

    //CompositeFuture.any(f1, f2, f3).onFailure(vertxTestContext::failNow)
    CompositeFuture.all(f1, f2, f3).onFailure(vertxTestContext::failNow)
      .onSuccess(result -> {
        LOGGER.info("Success");
        vertxTestContext.completeNow();
      });

  }

}

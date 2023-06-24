package com.amsidh.vertx.verticle;

import com.amsidh.vertx.config.ConfigLoader;
import com.amsidh.vertx.config.ConfigModel;
import com.amsidh.vertx.config.DatabaseConfigModel;
import com.amsidh.vertx.handlers.GetAllEmployeeHandler;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.SqlConnectOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestApiVerticle extends AbstractVerticle {
  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader.load(vertx)
      .onFailure(error -> log.error("Error in loading configuration", error))
      .onSuccess(configModel -> {
        //Get Pool for database
        Pool pool = getPool(configModel);
        //add all routes
        Router router = Router.router(vertx);
        //For any failed request
        router.route()
          .handler(BodyHandler.create())
          .failureHandler(errorContext -> handleErrorRequest(startPromise, errorContext, "Something went wrong " + errorContext.failure().getMessage()));

        //Fetch All employee
        router.get("/employees").handler(new GetAllEmployeeHandler(pool));


        startHttpServer(startPromise, router, configModel);
      })
      .compose(next -> {
        return vertx.deployVerticle(DatabaseMigrationVerticle.class.getName())
          .onFailure(error -> log.error("Error in deploying DatabaseMigrationVerticle {}", error))
          .onSuccess(handlerId -> {
            log.info("Deployment of {} with id {}", DatabaseMigrationVerticle.class.getName(), handlerId);
            startPromise.complete();
          });
      }).onFailure(error -> log.error("Error in loading Configuration and deploying DatabaseMigrationVerticle", error))
      .onSuccess(handlerId -> {
        log.info("Data Migration is successfully");
        startPromise.complete();
      });
  }

  private Pool getPool(ConfigModel configModel) {
    //Create Pool for database
    DatabaseConfigModel databaseConfigModel = configModel.getDatabaseConfigModel();
    SqlConnectOptions sqlConnectOptions = new MySQLConnectOptions()
      .setHost(databaseConfigModel.getDbhost())
      .setPort(databaseConfigModel.getDbport())
      .setDatabase(databaseConfigModel.getDatabase())
      .setUser(databaseConfigModel.getUsername())
      .setPassword(databaseConfigModel.getPassword());
    PoolOptions poolOptions = new PoolOptions().setMaxSize(4);
    return Pool.pool(vertx, sqlConnectOptions, poolOptions);
  }


  private static void handleErrorRequest(Promise<Void> startPromise, RoutingContext routingContext, String errorMessage) {
    handleErrorResponseTemplate(routingContext, errorMessage);
    startPromise.complete();
  }

  public static void handleErrorResponseTemplate(RoutingContext routingContext, String errorMessage) {
    JsonObject errorTemplate = new JsonObject()
      .put("message", String.format("Request failed due to some error %s", errorMessage));
    routingContext.response()
      .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
      .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .end(errorTemplate.toBuffer());
  }


  private void startHttpServer(Promise<Void> startPromise, Router router, ConfigModel configModel) {
    HttpServer httpServer = vertx.createHttpServer();
    httpServer.requestHandler(router)
      .listen(configModel.getServerport(), httpServerAsyncResult -> {
        if (httpServerAsyncResult.succeeded()) {
          log.info("HTTP server started on port {}", configModel.getServerport());
          startPromise.complete();
        } else {
          startPromise.fail(httpServerAsyncResult.cause());
        }
      });
  }
}

package com.amsidh.vertx.verticle;

import com.amsidh.vertx.config.ConfigLoader;
import com.amsidh.vertx.config.CustomerConfig;
import com.amsidh.vertx.config.DbConfig;
import com.amsidh.vertx.handlers.*;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerRestApiVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        ConfigLoader.load(vertx)
                .onFailure(startPromise::fail)
                .onSuccess(configuration -> {
                    log.info("Retrieved Configuration {}", configuration);
                    Router router = configureHttpRoutes(configuration);
                    startHttpServer(startPromise, router, configuration);
                });


    }

    private Router configureHttpRoutes(CustomerConfig configuration) {
        Router router = Router.router(vertx);
        router.route()
                .handler(BodyHandler.create())
                .failureHandler(handleFailure());
        //One pool for each RestApi Verticle
        Pool pool = getDatabasePool(configuration);
        //Get All Customers
        router.get("/customers").handler(new GetAllCustomersHandler(pool));
        //Get Customer By id
        router.get("/customers/:id").handler(new GetCustomerHandler(pool));
        //Create customer
        router.post("/customers").consumes("*/json").handler(new PostCustomerHandler(pool));
        //Update customer
        router.put("/customers/:id").consumes("*/json").handler(new PutCustomerHandler(pool));
        //Delete customer
        router.delete("/customers/:id").handler(new DeleteCustomerHandler(pool));
        return router;
    }

    private Pool getDatabasePool(CustomerConfig configuration) {
        DbConfig dbConfig = configuration.getDbConfig();
        PgConnectOptions pgConnectOptions = new PgConnectOptions()
                .setHost(dbConfig.getDbHost())
                .setPort(dbConfig.getDbPort())
                .setDatabase(dbConfig.getDbDatabase())
                .setUser(dbConfig.getDbUserName())
                .setPassword(dbConfig.getDbPassword());

        PoolOptions poolOptions = new PoolOptions().setMaxSize(4);
        PgPool pgPool = PgPool.pool(vertx, pgConnectOptions, poolOptions);
        return pgPool;
    }

    private void startHttpServer(Promise<Void> startPromise, Router router, CustomerConfig configuration) {

        HttpServer httpServer = vertx.createHttpServer();
        httpServer.requestHandler(router)
                .exceptionHandler(error -> log.error("HTTP Server error: ", error))
                .listen(configuration.getServerPort(), httpServerAsyncResult -> {
                    if (httpServerAsyncResult.succeeded()) {
                        startPromise.complete();
                        log.info("HTTP server started on port {}", httpServer.actualPort());
                    } else {
                        startPromise.fail(httpServerAsyncResult.cause());
                    }
                });
    }

    private Handler<RoutingContext> handleFailure() {
        return errorContext -> {
            if (errorContext.response().ended()) {
                // Ignore completed response
                return;
            }
            log.error("Route Error:", errorContext.failure());
            errorContext.response()
                    .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end(new JsonObject().put("message", "Something went wrong").toBuffer());
        };
    }
}

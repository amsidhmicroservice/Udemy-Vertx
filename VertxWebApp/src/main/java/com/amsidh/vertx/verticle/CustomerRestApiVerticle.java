package com.amsidh.vertx.verticle;

import com.amsidh.vertx.config.ConfigLoader;
import com.amsidh.vertx.config.CustomerConfig;
import com.amsidh.vertx.handlers.*;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomerRestApiVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        Router router = Router.router(vertx);
        router.route()
                .handler(BodyHandler.create())
                .failureHandler(handleFailure());

        //Get All Customers
        router.get("/customers").handler(new GetAllCustomersHandler());
        //Get Customer By id
        router.get("/customers/:id").handler(new GetCustomerHandler());
        //Create customer
        router.post("/customers").consumes("*/json").handler(new PostCustomerHandler());
        //Update customer
        router.put("/customers/:id").consumes("*/json").handler(new PutCustomerHandler());
        //Delete customer
        router.delete("/customers/:id").handler(new DeleteCustomerHandler());

        ConfigLoader.load(vertx)
                .onFailure(startPromise::fail)
                .onSuccess(configuration -> {
                    log.info("Retrieved Configuration {}", configuration);
                    startHttpServer(startPromise, router, configuration);
                });


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
                    .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                    .end(new JsonObject().put("message", "Something went wrong :(").toBuffer());
        };
    }
}

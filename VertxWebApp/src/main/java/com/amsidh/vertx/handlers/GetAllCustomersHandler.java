package com.amsidh.vertx.handlers;

import com.amsidh.vertx.model.Customer;
import com.amsidh.vertx.service.CustomerService;
import com.amsidh.vertx.service.impl.CustomerServiceImpl;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class GetAllCustomersHandler implements Handler<RoutingContext> {

    CustomerService customerService = new CustomerServiceImpl();

    @Override
    public void handle(RoutingContext routingContext) {
        List<Customer> customers = customerService.getAllCustomer();
        JsonArray response = new JsonArray(customers);
        //manuallyDelaying(routingContext);
        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                .end(response.encodePrettily());
    }

    private void manuallyDelaying(RoutingContext routingContext) {
        try {
            long l = ThreadLocalRandom.current().nextLong(100, 300);
            Thread.sleep(l);
            if (l % 2 == 0) {
                log.error("Manual Error");
                routingContext.response()
                        .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                        .end(new JsonObject().put("message", "Manually error").toBuffer());
            } else {
                List<Customer> customers = customerService.getAllCustomer();
                JsonArray response = new JsonArray(customers);
                log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
                routingContext.response()
                        .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                        .end(response.encodePrettily());
            }
        } catch (InterruptedException e) {
            log.error("Thread Interrupted Exception", e);
        }
    }
}

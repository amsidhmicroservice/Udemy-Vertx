package com.amsidh.vertx.handlers;

import com.amsidh.vertx.model.Customer;
import com.amsidh.vertx.service.CustomerService;
import com.amsidh.vertx.service.impl.CustomerServiceImpl;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class PostCustomerHandler implements Handler<RoutingContext> {
    CustomerService customerService = new CustomerServiceImpl();
    Pool pool;

    public PostCustomerHandler(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        JsonObject requestBody = routingContext.getBodyAsJson();
        Customer customer = requestBody.mapTo(Customer.class);
        Optional<Customer> optionalCustomer = customerService.saveCustomer(customer);
        JsonObject response;
        if (optionalCustomer.isPresent()) {
            response = new JsonObject(Json.encode(optionalCustomer.get()));
        } else {
            response = new JsonObject().put("message", "Customer not saved ");
        }
        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                .end(response.encodePrettily());
    }
}

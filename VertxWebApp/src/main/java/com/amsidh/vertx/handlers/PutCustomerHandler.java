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
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PutCustomerHandler implements Handler<RoutingContext> {
    CustomerService customerService = new CustomerServiceImpl();

    @Override
    public void handle(RoutingContext routingContext) {
        String id = routingContext.pathParam("id");
        JsonObject jsonObject = routingContext.getBodyAsJson();
        Customer customer = jsonObject.mapTo(Customer.class);
        Customer updatedCustomer = customerService.updateCustomer(Integer.parseInt(id), customer);
        JsonObject response = new JsonObject(Json.encode(updatedCustomer));
        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                .end(response.encodePrettily());
    }
}

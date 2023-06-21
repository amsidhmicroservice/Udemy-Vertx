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

import java.util.Optional;

@Slf4j
public class GetCustomerHandler implements Handler<RoutingContext> {

    CustomerService customerService = new CustomerServiceImpl();

    @Override
    public void handle(RoutingContext routingContext) {
        String id = routingContext.pathParam("id");
        Optional<Customer> customerOptional = customerService.getCustomerById(Long.parseLong(id));
        JsonObject response;
        if (customerOptional.isPresent()) {
            response = new JsonObject(Json.encode(customerOptional.get()));
        } else {
            response = new JsonObject();
            response.put("message", String.format("Customer with customerId %s does not exist", id));
        }
        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                .end(response.encodePrettily());
    }
}

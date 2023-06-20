package com.amsidh.vertx.handlers;

import com.amsidh.vertx.service.CustomerService;
import com.amsidh.vertx.service.impl.CustomerServiceImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeleteCustomerHandler implements Handler<RoutingContext> {
    CustomerService customerService = new CustomerServiceImpl();

    @Override
    public void handle(RoutingContext routingContext) {
        String id = routingContext.pathParam("id");
        customerService.deleteCustomerById(Integer.parseInt(id));
        JsonObject response = new JsonObject();
        response.put("message", "Customer with id " + id + " deleted successfully!!!");
        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(response.encodePrettily());
    }
}

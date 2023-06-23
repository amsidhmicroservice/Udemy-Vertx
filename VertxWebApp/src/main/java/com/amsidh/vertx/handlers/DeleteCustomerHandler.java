package com.amsidh.vertx.handlers;

import com.amsidh.vertx.model.Customer;
import com.amsidh.vertx.service.CustomerService;
import com.amsidh.vertx.service.impl.CustomerServiceImpl;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class DeleteCustomerHandler implements Handler<RoutingContext> {
    CustomerService customerService = new CustomerServiceImpl();
    Pool pool;

    public DeleteCustomerHandler(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String id = routingContext.pathParam("id");
        Optional<Customer> optionalCustomer = customerService.deleteCustomerById(Long.parseLong(id));
        JsonObject response = new JsonObject();
        if (optionalCustomer.isPresent()) {
            response.put("message", "Customer with id " + id + " deleted successfully!!!");
        } else {
            response.put("message", "Customer with id " + id + " does not exists!!!");
        }
        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
        routingContext.response()
                .putHeader("content-type", "application/json")
                .end(response.encodePrettily());
    }
}

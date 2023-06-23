package com.amsidh.vertx.handlers;

import com.amsidh.vertx.model.Customer;
import com.amsidh.vertx.service.CustomerService;
import com.amsidh.vertx.service.impl.CustomerServiceImpl;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Optional;

@Slf4j
public class GetCustomerHandler implements Handler<RoutingContext> {

    CustomerService customerService = new CustomerServiceImpl();
    Pool pool;

    public GetCustomerHandler(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String customerId = routingContext.pathParam("id");
        SqlTemplate.forQuery(pool, "select * from customer.customer where id=#{customerId}")
                .mapTo(Customer.class)
                .execute(Collections.singletonMap("customerId", Long.parseLong(customerId)))
                .onFailure(error -> {
                    log.error("Database call failed with an error {}", error);
                    routingContext.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end(new JsonObject().put("message", error.getMessage()).toBuffer());
                })
                .onSuccess(customer -> {
                    JsonObject response;
                    if (customer.iterator().hasNext()) {
                        response = new JsonObject(Json.encode(customer.iterator().next()));
                    } else {
                        response = new JsonObject();
                        response.put("message", String.format("Customer with customerId %s does not exist", customerId));
                    }
                    log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
                    routingContext.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                            .end(response.encodePrettily());
                });

        //getEmployeeByIdFromInMemory(routingContext, customerId);
    }

    private void getEmployeeByIdFromInMemory(RoutingContext routingContext, String customerId) {
        Optional<Customer> customerOptional = customerService.getCustomerById(Long.parseLong(customerId));
        JsonObject response;
        if (customerOptional.isPresent()) {
            response = new JsonObject(Json.encode(customerOptional.get()));
        } else {
            response = new JsonObject();
            response.put("message", String.format("Customer with customerId %s does not exist", customerId));
        }
        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                .end(response.encodePrettily());
    }
}

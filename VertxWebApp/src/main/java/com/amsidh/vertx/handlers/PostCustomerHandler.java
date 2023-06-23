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
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Optional;

@Slf4j
public class PostCustomerHandler implements Handler<RoutingContext> {
    public static final String SAVE_CUSTOMER_SQL_QUERY = "INSERT INTO CUSTOMER.CUSTOMER(name) VALUES (#{customerName})";
    CustomerService customerService = new CustomerServiceImpl();
    Pool pool;

    public PostCustomerHandler(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        JsonObject requestBody = routingContext.getBodyAsJson();
        Customer customer = requestBody.mapTo(Customer.class);
        SqlTemplate.forUpdate(pool, SAVE_CUSTOMER_SQL_QUERY)
                .mapTo(Customer.class)
                .execute(Collections.singletonMap("customerName", customer.getName()))
                .onFailure(error -> {
                    log.error("Save customer database query failed with reason {}", error);
                    routingContext.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end(new JsonObject().put("message", error.getMessage()).toBuffer());
                })
                .onSuccess(customers -> {
                    if(!routingContext.response().ended()) {
                        JsonObject response = new JsonObject().put("message", "Customer saved successfully");
                        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
                        routingContext.response()
                                .setStatusCode(HttpResponseStatus.CREATED.code())
                                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                                .end(response.encodePrettily());
                    }
                });


        //saveCustomerInMemoryCollection(routingContext, customer);
    }

    private void saveCustomerInMemoryCollection(RoutingContext routingContext, Customer customer) {
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

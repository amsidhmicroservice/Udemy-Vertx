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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class PutCustomerHandler implements Handler<RoutingContext> {
    CustomerService customerService = new CustomerServiceImpl();
    Pool pool;

    public PutCustomerHandler(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String id = routingContext.pathParam("id");
        JsonObject jsonObject = routingContext.getBodyAsJson();
        Customer customer = jsonObject.mapTo(Customer.class);
        // Transaction
        pool.withTransaction(client -> {
            // 1 - Get customer for customer id
            return SqlTemplate.forQuery(client, "select * from customer.customer where id=#{customerId}")
                    .mapTo(Customer.class)
                    .execute(Collections.singletonMap("customerId", Long.parseLong(id)))
                    .onFailure(error -> {
                        log.error("Get customer database call failed with an error {}", error);
                        routingContext.response()
                                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                .end(new JsonObject().put("message", error.getMessage()).toBuffer());
                    })
                    .compose(next -> {
                        Map<String, Object> parameterMap= new HashMap<>();
                        parameterMap.put("customerName", customer.getName());
                        parameterMap.put("customerId", Long.parseLong(id));
                        //Update customer
                        return SqlTemplate.forUpdate(client, "update customer.customer set name=#{customerName} where id=#{customerId}")
                                .execute(parameterMap)
                                .onFailure(error -> {
                                    log.error("Fail to update customer {}", error);
                                    routingContext.response()
                                            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                            .end(new JsonObject().put("message", error.getMessage()).toBuffer());
                                });
                    })
                    .onFailure(error -> {
                        log.error("Fail database query for get and update", error);
                        routingContext.response()
                                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                                .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                                .end(new JsonObject().put("message", error.getMessage()).toBuffer());
                    })
                    .onSuccess(result -> {
                        // 3 - Both succeeded
                        routingContext.response()
                                .setStatusCode(HttpResponseStatus.NO_CONTENT.code())
                                .end();
                    });
        });

        //updateCustomerInMemoryCollection(routingContext, id, customer);
    }

    private void updateCustomerInMemoryCollection(RoutingContext routingContext, String id, Customer customer) {
        Optional<Customer> optionalCustomer = customerService.updateCustomer(Long.parseLong(id), customer);
        JsonObject response;
        if (optionalCustomer.isPresent()) {
            response = new JsonObject(Json.encode(optionalCustomer.get()));
        } else {
            response = new JsonObject();
            response.put("message", "Customer with id " + id + " does not exists!!!");
        }
        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
        routingContext.response()
                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                .end(response.encodePrettily());
    }
}

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
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class GetAllCustomersHandler implements Handler<RoutingContext> {

    public static final String SELECT_ALL_CUSTOMER_QUERY = "select * from customer.customer";
    CustomerService customerService = new CustomerServiceImpl();
    Pool pool;

    public GetAllCustomersHandler(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void handle(RoutingContext routingContext) {

        //Fetch all employee using simple Pool
        //getAllEmployeeUsingPool(routingContext);
        //Fetch all employee using SQL Template
        SqlTemplate.forQuery(this.pool, SELECT_ALL_CUSTOMER_QUERY)
                .mapTo(Customer.class)
                .execute(Collections.emptyMap())
                .onFailure(error -> {
                    log.error("Database call failed with an error {}", error);
                    routingContext.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end(new JsonObject().put("message", error.getMessage()).toBuffer());
                })
                .onSuccess(customers -> {
                    JsonArray response = new JsonArray();
                    customers.forEach(response::add);
                    log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
                    routingContext.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                            .end(response.encodePrettily());
                });
    }

    private void getAllEmployeeUsingPool(RoutingContext routingContext) {
        pool.query(SELECT_ALL_CUSTOMER_QUERY)
                .execute()
                .onFailure(error -> {
                    log.error("Database call failed with an error {}", error);
                    routingContext.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end(new JsonObject().put("message", error.getMessage()).toBuffer());
                })
                .onSuccess(rows -> {
                    List<Customer> customers = new ArrayList<>();
                    rows.forEach(row -> customers.add(Customer.builder().id(row.getLong("id")).name(row.getString("name")).build()));
                    JsonArray response = new JsonArray(customers);
                    log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
                    routingContext.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                            .end(response.encodePrettily());
                });
    }

}

package com.amsidh.vertx.handlers;

import com.amsidh.vertx.model.Customer;
import com.amsidh.vertx.service.CustomerService;
import com.amsidh.vertx.service.impl.CustomerServiceImpl;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Optional;

@Slf4j
public class DeleteCustomerHandler implements Handler<RoutingContext> {
    public static final String DELETE_CUSTOMER_SQL_QUERY = "DELETE FROM CUSTOMER.CUSTOMER WHERE id=#{customerId}";
    CustomerService customerService = new CustomerServiceImpl();
    Pool pool;

    public DeleteCustomerHandler(Pool pool) {
        this.pool = pool;
    }

    @Override
    public void handle(RoutingContext routingContext) {
        String customerId = routingContext.pathParam("id");
        //deleteFromInmemoryCollection(routingContext, customerId);
        SqlTemplate.forUpdate( pool, DELETE_CUSTOMER_SQL_QUERY)
                .execute(Collections.singletonMap("customerId", Long.parseLong(customerId)))
                .onFailure(error -> {
                    log.error("Delete customer from database query failed with reason {}", error);
                    routingContext.response()
                            .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                            .setStatusCode(HttpResponseStatus.INTERNAL_SERVER_ERROR.code())
                            .end(new JsonObject().put("message", error.getMessage()).toBuffer());
                })
                .onSuccess(customers -> {
                        JsonObject response = new JsonObject().put("message", "Customer deleted successfully");
                        log.info("Request {} and Response {} ", routingContext.normalizedPath(), response.encode());
                        routingContext.response()
                                .setStatusCode(HttpResponseStatus.OK.code())
                                .putHeader(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
                                .end(response.encodePrettily());
                });
    }

    private void deleteFromInmemoryCollection(RoutingContext routingContext, String id) {
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

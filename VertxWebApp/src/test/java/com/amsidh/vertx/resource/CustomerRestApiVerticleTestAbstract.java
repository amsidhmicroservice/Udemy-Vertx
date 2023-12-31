package com.amsidh.vertx.resource;

import com.amsidh.vertx.model.Customer;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomerRestApiVerticleTestAbstract extends AbstractBaseTest {
    @Test
    public void getAllCustomerTest(Vertx vertx, VertxTestContext vertxTestContext) {
        webClient.get("/customers").send().onSuccess(successHandler -> {
            Assertions.assertEquals(HttpResponseStatus.OK.code(), successHandler.statusCode());
            Assertions.assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), successHandler.getHeader(HttpHeaderNames.CONTENT_TYPE.toString()));
        }).compose(next -> {
            webClient.get("/customer").send().onSuccess(bufferHttpResponse -> {
                Assertions.assertEquals(HttpResponseStatus.NOT_FOUND.code(), bufferHttpResponse.statusCode());
                Assertions.assertEquals("<html><body><h1>Resource not found</h1></body></html>", bufferHttpResponse.bodyAsString());
                vertxTestContext.completeNow();
            });
            return Future.succeededFuture();
        });
    }

    @Test
    public void getCustomerByIdTest(Vertx vertx, VertxTestContext vertxTestContext) {
        webClient.get("/customers/4").send().onSuccess(successHandler -> {
            Assertions.assertEquals(HttpResponseStatus.OK.code(), successHandler.statusCode());
            Assertions.assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), successHandler.getHeader(HttpHeaderNames.CONTENT_TYPE.toString()));
            Assertions.assertEquals("{\"id\":4,\"name\":\"Adithi\"}", successHandler.bodyAsJsonObject().encode());
        }).compose(next -> {
            webClient.get("/customers/100").send().onSuccess(bufferHttpResponse -> {
                Assertions.assertEquals(HttpResponseStatus.OK.code(), bufferHttpResponse.statusCode());
                Assertions.assertEquals("{\"message\":\"Customer with customerId 100 does not exist\"}", bufferHttpResponse.bodyAsJsonObject().encode());
                vertxTestContext.completeNow();
            });
            return Future.succeededFuture();
        });
    }

    @Test
    public void saveCustomerTest(Vertx vertx, VertxTestContext vertxTestContext) {
        final Customer customer = Customer.builder().id(100L).name("HundredCustomer").build();
        webClient.post("/customers")
                .putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .sendBuffer(JsonObject.mapFrom(customer).toBuffer())
                .onSuccess(successHandler -> {
                    Assertions.assertEquals(HttpResponseStatus.OK.code(), successHandler.statusCode());
                    Assertions.assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), successHandler.getHeader(HttpHeaderNames.CONTENT_TYPE.toString()));
                    Assertions.assertEquals("{\"id\":100,\"name\":\"HundredCustomer\"}", successHandler.bodyAsJsonObject().encode());
                }).compose(next -> {
                    webClient.delete("/customers/100").send().onSuccess(bufferHttpResponse -> {
                        Assertions.assertEquals(HttpResponseStatus.OK.code(), bufferHttpResponse.statusCode());
                        Assertions.assertEquals("{\"message\":\"Customer with id 100 deleted successfully!!!\"}", bufferHttpResponse.bodyAsJsonObject().encode());
                        vertxTestContext.completeNow();
                    });
                    return Future.succeededFuture();
                });
    }

    @Test
    public void putCustomerTest(Vertx vertx, VertxTestContext vertxTestContext) {
        final Customer customer = Customer.builder().id(1L).name("Amsidh Babasha Lokhande").build();
        webClient.put("/customers/1")
                .putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                .sendBuffer(JsonObject.mapFrom(customer).toBuffer())
                .onSuccess(successHandler -> {
                    Assertions.assertEquals(HttpResponseStatus.OK.code(), successHandler.statusCode());
                    Assertions.assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), successHandler.getHeader(HttpHeaderNames.CONTENT_TYPE.toString()));
                    Assertions.assertEquals("{\"id\":1,\"name\":\"Amsidh Babasha Lokhande\"}", successHandler.bodyAsJsonObject().encode());
                }).compose(next -> {
                    webClient.put("/customers/100")
                            .putHeader(HttpHeaderNames.CONTENT_TYPE.toString(), HttpHeaderValues.APPLICATION_JSON.toString())
                            .sendBuffer(JsonObject.mapFrom(customer).toBuffer())
                            .onSuccess(bufferHttpResponse -> {
                                Assertions.assertEquals(HttpResponseStatus.OK.code(), bufferHttpResponse.statusCode());
                                Assertions.assertEquals("{\"message\":\"Customer with id 100 does not exists!!!\"}", bufferHttpResponse.bodyAsJsonObject().encode());
                                vertxTestContext.completeNow();
                            });
                    return Future.succeededFuture();
                });
    }

    @Test
    public void deleteCustomerByIdTest(Vertx vertx, VertxTestContext vertxTestContext) {
        webClient.delete("/customers/1").send().onSuccess(successHandler -> {
            Assertions.assertEquals(HttpResponseStatus.OK.code(), successHandler.statusCode());
            Assertions.assertEquals(HttpHeaderValues.APPLICATION_JSON.toString(), successHandler.getHeader(HttpHeaderNames.CONTENT_TYPE.toString()));
            Assertions.assertEquals("{\"message\":\"Customer with id 1 deleted successfully!!!\"}", successHandler.bodyAsJsonObject().encode());
        }).compose(next -> {
            webClient.delete("/customers/100").send().onSuccess(bufferHttpResponse -> {
                Assertions.assertEquals(HttpResponseStatus.OK.code(), bufferHttpResponse.statusCode());
                Assertions.assertEquals("{\"message\":\"Customer with id 100 does not exists!!!\"}", bufferHttpResponse.bodyAsJsonObject().encode());
                vertxTestContext.completeNow();
            });
            return Future.succeededFuture();
        });
    }
}

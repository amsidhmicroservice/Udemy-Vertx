package com.amsidh.vertx.handlers;

import com.amsidh.vertx.constant.SqlQueryConstant;
import com.amsidh.vertx.verticle.RestApiVerticle;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class SaveEmployeeHandler implements Handler<RoutingContext> {

  private final Pool pool;

  public SaveEmployeeHandler(Pool pool) {
    this.pool = pool;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    log.info("Inside handle method of SaveEmployeeHandler");
    saveEmployee(routingContext, routingContext.getBodyAsJson());
    log.info("OnSuccess block is completed");
  }

  private void saveEmployee(RoutingContext routingContext, JsonObject jsonObject) {
    Map<String, Object> map = jsonObject.getMap();
    SqlTemplate.forUpdate(pool, SqlQueryConstant.SAVE_EMPLOYEE_QUERY)
      .execute(map)
      .onFailure(error -> RestApiVerticle.handleErrorResponseTemplate(routingContext, String.format("Failed to save employee with %s reason %s", map.toString(), error.getMessage())))
      .onSuccess(handle -> {
        JsonObject response = new JsonObject().put("message", "Employee saved successfully");
        routingContext.response()
          .setStatusCode(HttpResponseStatus.CREATED.code())
          .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .end(response.toBuffer());
      });
  }
}

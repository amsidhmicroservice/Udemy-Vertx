package com.amsidh.vertx.handlers;

import com.amsidh.vertx.constant.SqlQueryConstant;
import com.amsidh.vertx.verticle.RestApiVerticle;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public class GetAllEmployeeHandler implements Handler<RoutingContext> {


  private final Pool pool;

  public GetAllEmployeeHandler(Pool pool) {

    this.pool = pool;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    log.info("Inside handle method of GetAllEmployeeHandler class");
    SqlTemplate.forQuery(pool, SqlQueryConstant.SELECT_FROM_EMPLOYEE)
      .mapTo(Row::toJson)
      .execute(Collections.emptyMap())
      .onFailure(throwable -> RestApiVerticle.handleErrorResponseTemplate(routingContext, throwable.getMessage()))
      .onSuccess(jsonObjects -> {
        handleSuccessHttpResponse(routingContext, jsonObjects);
      });
  }

  static void handleSuccessHttpResponse(RoutingContext routingContext, RowSet<JsonObject> jsonObjects) {
    JsonArray jsonArray = new JsonArray();
    jsonObjects.forEach(jsonArray::add);
    routingContext.response()
      .setStatusCode(HttpResponseStatus.OK.code())
      .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
      .end(jsonArray.toBuffer());
  }
}

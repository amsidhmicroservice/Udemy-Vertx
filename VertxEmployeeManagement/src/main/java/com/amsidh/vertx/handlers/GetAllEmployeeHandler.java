package com.amsidh.vertx.handlers;

import com.amsidh.vertx.verticle.RestApiVerticle;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public class GetAllEmployeeHandler implements Handler<RoutingContext> {

  public static final String SELECT_FROM_EMPLOYEE = "select * from employee";
  private final Pool pool;

  public GetAllEmployeeHandler(Pool pool) {

    this.pool = pool;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    SqlTemplate.forQuery(pool, SELECT_FROM_EMPLOYEE)
      .mapTo(Row::toJson)
      .execute(Collections.emptyMap())
      .onFailure(throwable -> RestApiVerticle.handleErrorResponseTemplate(routingContext, throwable.getMessage()))
      .onSuccess(jsonObjects -> {
        log.info("All employee count {}", jsonObjects.rowCount());
        JsonArray jsonArray = new JsonArray();
        jsonObjects.forEach(jsonArray::add);
        routingContext.response()
          .setStatusCode(HttpResponseStatus.OK.code())
          .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .end(jsonArray.toBuffer());
      });
  }
}

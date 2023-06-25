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
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public class DeleteEmployeeHandler implements Handler<RoutingContext> {

  private final Pool pool;

  public DeleteEmployeeHandler(Pool pool) {
    this.pool = pool;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    log.info("Inside handle method of DeleteEmployeeHandler");
    Integer employeeId = Integer.parseInt(routingContext.pathParam("employeeId"));
    SqlTemplate.forUpdate(pool, SqlQueryConstant.DELETE_EMPLOYEE_QUERY)
      .mapTo(Row::toJson)
      .execute(Collections.singletonMap("employeeId", employeeId))
      .onFailure(error -> RestApiVerticle.handleErrorResponseTemplate(routingContext, String.format("Delete employee by id %d sql query failed with reason %s", employeeId, error.getMessage())))
      .onSuccess(result -> {
        log.info(String.format("Employee with id %d is deleted successfully", employeeId));
        routingContext.response()
          .putHeader(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON)
          .setStatusCode(HttpResponseStatus.OK.code())
          .end(new JsonObject().put("message", String
              .format("Employee with id %d is deleted successfully", employeeId))
            .toBuffer());
      });
  }
}

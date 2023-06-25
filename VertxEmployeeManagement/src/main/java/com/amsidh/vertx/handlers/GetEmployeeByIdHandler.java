package com.amsidh.vertx.handlers;

import com.amsidh.vertx.constant.SqlQueryConstant;
import com.amsidh.vertx.verticle.RestApiVerticle;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;

@Slf4j
public class GetEmployeeByIdHandler implements Handler<RoutingContext> {

  private final Pool pool;

  public GetEmployeeByIdHandler(Pool pool) {
    this.pool = pool;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    int employeeId = Integer.parseInt(routingContext.pathParam("employeeId"));
    SqlTemplate.forQuery(pool, SqlQueryConstant.SQL_SELECT_EMPLOYEE_BY_ID)
      .mapTo(Row::toJson)
      .execute(Collections.singletonMap("employeeId", employeeId))
      .onFailure(error -> RestApiVerticle.handleErrorResponseTemplate(routingContext, String.format("Fetch employee by id %d sql query failed with reason %s", employeeId, error.getMessage())))
      .onSuccess(jsonObjects -> {
        GetAllEmployeeHandler.handleSuccessHttpResponse(routingContext, jsonObjects);
      });


  }
}

package com.amsidh.vertx.handlers;

import com.amsidh.vertx.constant.SqlQueryConstant;
import com.amsidh.vertx.verticle.RestApiVerticle;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.templates.SqlTemplate;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Map;

@Slf4j
public class UpdateEmployeeHandler implements Handler<RoutingContext> {
  private final Pool pool;

  public UpdateEmployeeHandler(Pool pool) {
    this.pool = pool;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    Integer employeeId = Integer.parseInt(routingContext.pathParam("employeeId"));
    pool.withTransaction(client -> {
      //Fetch employee by id
      return SqlTemplate.forQuery(pool, SqlQueryConstant.SELECT_EMPLOYEE_BY_ID).mapTo(Row::toJson).execute(Collections.singletonMap("employeeId", employeeId)).onFailure(error -> RestApiVerticle.handleErrorResponseTemplate(routingContext, String.format("Fetch employee by id %d sql query failed with reason %s", employeeId, error.getMessage())))
        // Update employee
        .compose(next -> {
          Map<String, Object> objectMap = routingContext.getBodyAsJson().getMap();
          objectMap.put("employeeId", employeeId);
          log.info("Updating employee");
          return SqlTemplate.forUpdate(pool, SqlQueryConstant.UPDATE_EMPLOYEE_QUERY).execute(objectMap).onFailure(error -> RestApiVerticle.handleErrorResponseTemplate(routingContext, String.format("Failed to update employee with id %d sql query failed with reason %s", employeeId, error.getMessage())));
        }).onFailure(error -> RestApiVerticle.handleErrorResponseTemplate(routingContext, String.format("Failed to update employee with id %d and reason %s", employeeId, error.getMessage())))
        // 3 - Both succeeded
        .onSuccess(result -> routingContext.response().setStatusCode(HttpResponseStatus.NO_CONTENT.code()).end());
    });
  }
}

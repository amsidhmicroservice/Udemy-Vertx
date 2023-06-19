package com.amsidh.udemy.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class ResponseVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.eventBus().<List<Employee>>consumer(Constant.MY_ADDRESS_BUS, message -> {
      List<Employee> employees = message.body();
      System.out.println("ResponseVerticle Message Received");
      employees.forEach(System.out::println);
      message.reply(new JsonObject().put("message", "ResponseVerticle We have received your message successfully"));

    });
  }
}

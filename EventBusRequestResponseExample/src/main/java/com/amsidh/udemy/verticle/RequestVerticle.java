package com.amsidh.udemy.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class RequestVerticle extends AbstractVerticle {


  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    startPromise.complete();
    vertx.setPeriodic(Duration.ofSeconds(5).toMillis(), repeat -> {
      System.out.println("Message sending");
      vertx.eventBus().request(Constant.MY_ADDRESS_BUS, getEmployees(), reply -> {
        if (reply.succeeded()) {
          System.out.println("ACK received with message" + reply.result().body());
        } else {
          System.out.println("Failed to deliver message" + reply.cause());
        }
      });
    });

  }

  private List<Employee> getEmployees() {
    return Arrays.asList(new Employee(1, "Amsidh"), new Employee(2, "Anjali"), new Employee(3, "Adithi"), new Employee(4, "Aditya"));
  }
}


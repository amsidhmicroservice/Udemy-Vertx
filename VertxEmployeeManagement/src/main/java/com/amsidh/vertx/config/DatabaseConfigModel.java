package com.amsidh.vertx.config;

import io.vertx.core.json.JsonObject;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class DatabaseConfigModel {
  private String dbhost;
  private Integer dbport;
  private String database;
  private String databaseurl;
  private String username;
  private String password;
  private String flywaylogger;

  public static DatabaseConfigModel from(JsonObject jsonObject) {
    return DatabaseConfigModel.builder()
      .dbhost(jsonObject.getString("db_host"))
      .dbport(jsonObject.getInteger("db_port"))
      .database(jsonObject.getString("db_database"))
      .databaseurl(jsonObject.getString("db_url"))
      .username(jsonObject.getString("db_username"))
      .password(jsonObject.getString("db_password"))
      .flywaylogger(jsonObject.getString("flyway_logger"))
      .build();
  }
}

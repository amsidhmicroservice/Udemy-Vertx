package com.amsidh.vertx.config;

import io.vertx.core.json.JsonObject;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class ConfigModel implements Serializable {
  private Integer serverport;
  private DatabaseConfigModel databaseConfigModel;

  public static ConfigModel from(JsonObject jsonObject) {
    return ConfigModel.builder()
      .serverport(jsonObject.getInteger("http_server_port"))
      .databaseConfigModel(DatabaseConfigModel.from(jsonObject))
      .build();
  }
}

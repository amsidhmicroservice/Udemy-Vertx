package com.amsidh.vertx.config;

import io.vertx.core.json.JsonObject;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class CustomerConfig {
    int serverPort;
    String version;
    DbConfig dbConfig;

    public static CustomerConfig from(final JsonObject config) {
        return CustomerConfig.builder()
                .serverPort(config.getInteger(ConfigLoader.SERVER_PORT))
                .version(config.getJsonObject("application").getString("version"))
                .dbConfig(parseDbConfig(config))
                .build();
    }

    private static DbConfig parseDbConfig(JsonObject config) {
        return DbConfig.builder()
                .dbHost(config.getString(ConfigLoader.DB_HOST))
                .dbPort(config.getInteger(ConfigLoader.DB_PORT))
                .dbUserName(config.getString(ConfigLoader.DB_USER))
                .dbPassword(config.getString(ConfigLoader.DB_PASSWORD))
                .dbDatabase(config.getString(ConfigLoader.DB_DATABASE))
                .build();
    }
}

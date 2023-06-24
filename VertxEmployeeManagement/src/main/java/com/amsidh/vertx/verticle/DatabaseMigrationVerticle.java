package com.amsidh.vertx.verticle;

import com.amsidh.vertx.config.ConfigLoader;
import com.amsidh.vertx.config.DatabaseConfigModel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;

import java.util.Objects;

@Slf4j
public class DatabaseMigrationVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    ConfigLoader.load(vertx)
      .onFailure(startPromise::fail)
      .onSuccess(configModel -> {
        vertx.executeBlocking(promise -> {
            migrateDatabaseWithFlyway(configModel.getDatabaseConfigModel());
          }).onFailure(error -> log.error("Error in database migration {}", error))
          .onSuccess(handle -> {
            log.info("Database schema migration is success!!!");
            startPromise.complete();
          });
      });

  }

  private static void migrateDatabaseWithFlyway(DatabaseConfigModel databaseConfigModel) {
    Flyway flyway = Flyway.configure()
      .loggers(databaseConfigModel.getFlywaylogger())
      .dataSource(databaseConfigModel.getDatabaseurl(), databaseConfigModel.getUsername(), databaseConfigModel.getPassword())
      .load();

    MigrationInfoService info = flyway.info();
    MigrationInfo migrationInfo = info.current();
    if (Objects.nonNull(migrationInfo)) {
      log.info("db schema is at version {}", migrationInfo.getVersion());
    }

    MigrationInfo[] pending = info.pending();
    log.info("Database schema pending migration are {}", pending);

    flyway.migrate();
  }
}

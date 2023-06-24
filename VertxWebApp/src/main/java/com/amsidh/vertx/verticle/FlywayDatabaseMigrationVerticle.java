package com.amsidh.vertx.verticle;

import com.amsidh.vertx.config.ConfigLoader;
import com.amsidh.vertx.config.CustomerConfig;
import com.amsidh.vertx.config.DbConfig;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class FlywayDatabaseMigrationVerticle extends AbstractVerticle {

    public static final String POSTGRES_DATABASE_URL_FORMAT = "jdbc:postgresql://%s:%d/%s";
    public static final String DATABASE_SCHEMA = "customer";

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        ConfigLoader.load(vertx)
                .onFailure(startPromise::fail)
                .onSuccess(configuration -> {
                    vertx.executeBlocking(promise -> {
                                databaseMigration(configuration);
                            }).onFailure(error -> log.error("Failed to migrate DB schema with error {}", error))
                            .onSuccess(handle -> {
                                log.info("Database schema migration is success!!!");
                                startPromise.complete();
                            });
                    startPromise.complete();
                });
    }

    private static void databaseMigration(CustomerConfig configuration) {
        DbConfig dbConfig = configuration.getDbConfig();
        String databaseUrl = String.format(POSTGRES_DATABASE_URL_FORMAT, dbConfig.getDbHost(),
                dbConfig.getDbPort(), dbConfig.getDbDatabase());
        log.info("Migrating DB schema using using jdbc url {}", databaseUrl);
        Flyway flyway = Flyway.configure()
                .loggers("slf4j")
                .dataSource(databaseUrl, dbConfig.getDbUserName(), dbConfig.getDbPassword())
                .schemas(DATABASE_SCHEMA)
                .defaultSchema(DATABASE_SCHEMA)
                .load();
        displayDatabaseSchemaVersion(flyway);
        displayPendingMigration(flyway);
        flyway.migrate();
    }

    private static void displayPendingMigration(Flyway flyway) {
        MigrationInfo[] pending = flyway.info().pending();
        String pendingMigration;
        if (Objects.isNull(pending)) {
            pendingMigration = "[]";
        } else {
            pendingMigration = Arrays.stream(pending).map(migrationInfo -> migrationInfo.getVersion() + "-" + migrationInfo.getDescription()).collect(Collectors.joining(",", "[", "]"));
        }
        log.info("Database schema pending migration are {}", pendingMigration);
    }

    private static void displayDatabaseSchemaVersion(Flyway flyway) {
        Optional<MigrationInfo> optionalMigrationInfo = Optional.ofNullable(flyway.info().current());
        if (optionalMigrationInfo.isPresent()) {
            log.info("db schema is at version {}", optionalMigrationInfo.get().getVersion());
        }
    }
}

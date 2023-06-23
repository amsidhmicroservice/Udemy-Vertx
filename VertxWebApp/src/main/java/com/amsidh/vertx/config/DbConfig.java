package com.amsidh.vertx.config;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class DbConfig {
    private String dbHost;
    private Integer dbPort;
    private String dbUserName;
    private String dbPassword;
    private String dbDatabase;
}

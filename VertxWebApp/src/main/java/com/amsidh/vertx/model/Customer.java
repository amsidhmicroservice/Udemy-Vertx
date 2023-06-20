package com.amsidh.vertx.model;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Customer implements Serializable {
    private Integer id;
    private String name;
}


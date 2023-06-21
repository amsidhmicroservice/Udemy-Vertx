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
    private Long id;
    private String name;
}


package com.amsidh.vertx.model;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Asset implements Serializable {
  private Integer id;
  private String name;
}

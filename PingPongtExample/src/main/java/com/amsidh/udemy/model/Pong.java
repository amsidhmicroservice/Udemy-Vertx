package com.amsidh.udemy.model;


import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Pong implements Serializable {
  private Integer id;
}

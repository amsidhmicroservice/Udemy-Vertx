package com.amsidh.udemy.model;


import lombok.*;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class Ping implements Serializable {
  private String message;
  private Boolean enable;
}

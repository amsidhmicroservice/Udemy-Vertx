package com.amsidh.vertx.model;

import io.vertx.core.json.JsonObject;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class Quata implements Serializable {
  private Asset asset;
  private BigDecimal bidPrice;
  private BigDecimal askPrice;
  private BigDecimal finalPrice;
  private BigDecimal quantity;

  public JsonObject toJsonObject() {
    return JsonObject.mapFrom(this);
  }
}

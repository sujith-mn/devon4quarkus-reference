package com.devonfw.quarkus.productmanagement.service.v1.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {

  private Long id;

  private String title;

  private String basicInfo;

  // private int numberOfLegs;

  private String description;

  private BigDecimal price;

}

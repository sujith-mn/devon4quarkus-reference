package com.devonfw.quarkus.productmanagement.domain.model;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchCriteria {

  private String title;

  private Integer pageNumber;

  private Integer pageSize;

  private BigDecimal price, priceMin, priceMax;
}

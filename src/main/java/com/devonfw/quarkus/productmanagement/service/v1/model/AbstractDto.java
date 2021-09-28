package com.devonfw.quarkus.productmanagement.service.v1.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public abstract class AbstractDto {
  private int modificationCounter;

  private Long id;
}

package com.devonfw.demoquarkus.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalDto {

  private Long id;

  private String name;

  private String basicInfo;

  private int numberOfLegs;

}

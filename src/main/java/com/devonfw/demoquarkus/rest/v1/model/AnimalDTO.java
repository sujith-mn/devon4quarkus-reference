package com.devonfw.demoquarkus.rest.v1.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalDTO {

    private Long id;

    private String name;

    private String basicInfo;

    private int numberOfLegs;

}

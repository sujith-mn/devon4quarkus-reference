package com.devonfw.demoquarkus.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="Animal")
// A JPA entity requires at least 2 things @Entity annotation and an ID
// by default, the DB table will have the same name as our class
public class AnimalEntity {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  // every primitive attribute on this class will be represented as column in animal table
  private String basicInfo;

  private int numberOfLegs;

}

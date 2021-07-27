package com.devonfw.demoquarkus.domain.repo;

import org.springframework.data.domain.Page;

import com.devonfw.demoquarkus.domain.model.Animal;
import com.devonfw.demoquarkus.rest.v1.model.AnimalSearchCriteriaDto;

public interface AnimalFragment {
  public Page<Animal> findAllCriteriaApi(AnimalSearchCriteriaDto dto);

  public Page<Animal> findAllQueryDsl(AnimalSearchCriteriaDto dto);

  public Page<Animal> findByNameNativeQuery(AnimalSearchCriteriaDto dto);

  public Page<Animal> findByNameQuery(AnimalSearchCriteriaDto dto);
}

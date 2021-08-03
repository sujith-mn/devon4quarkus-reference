package com.devonfw.demoquarkus.domain.repo;

import org.springframework.data.domain.Page;

import com.devonfw.demoquarkus.domain.model.AnimalEntity;
import com.devonfw.demoquarkus.service.model.AnimalSearchCriteriaDto;

public interface AnimalFragment {
  public Page<AnimalEntity> findAllCriteriaApi(AnimalSearchCriteriaDto dto);

  public Page<AnimalEntity> findAllQueryDsl(AnimalSearchCriteriaDto dto);

  public Page<AnimalEntity> findByNameNativeQuery(AnimalSearchCriteriaDto dto);

  public Page<AnimalEntity> findByNameQuery(AnimalSearchCriteriaDto dto);
}

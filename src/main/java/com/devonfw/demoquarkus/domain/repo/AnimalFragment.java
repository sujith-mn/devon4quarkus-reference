package com.devonfw.demoquarkus.domain.repo;

import org.springframework.data.domain.Page;
import com.devonfw.demoquarkus.domain.model.Animal;
import com.devonfw.demoquarkus.rest.v1.model.AnimalSearchCriteriaDTO;

public interface AnimalFragment {
	public Page<Animal> findAllCriteriaApi(AnimalSearchCriteriaDTO dto);

	public Page<Animal> findAllQueryDsl(AnimalSearchCriteriaDTO dto);

	public Page<Animal> findByNameNativeQuery(AnimalSearchCriteriaDTO dto);

	public Page<Animal> findByNameQuery(AnimalSearchCriteriaDTO dto);
}

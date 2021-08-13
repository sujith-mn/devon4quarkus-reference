package com.devonfw.demoquarkus.logic;

import com.devonfw.demoquarkus.service.v1.model.AnimalDto;
import com.devonfw.demoquarkus.service.v1.model.AnimalSearchCriteriaDto;
import org.springframework.data.domain.Page;

public interface UcFindAnimal {
    Page<AnimalDto> findAnimals(AnimalSearchCriteriaDto dto);

    Page<AnimalDto> findAnimalsByCriteriaApi(AnimalSearchCriteriaDto dto);

    Page<AnimalDto> findAnimalsByQueryDsl(AnimalSearchCriteriaDto dto);

    Page<AnimalDto> findAnimalsByNameQuery(AnimalSearchCriteriaDto dto);

    Page<AnimalDto> findAnimalsByNameNativeQuery(AnimalSearchCriteriaDto dto);

    Page<AnimalDto> findAnimalsOrderedByName();

    AnimalDto findAnimal(String id);

    AnimalDto findAnimalByName(String name);
}

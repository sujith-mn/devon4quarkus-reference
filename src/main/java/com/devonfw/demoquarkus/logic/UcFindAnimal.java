package com.devonfw.demoquarkus.logic;

import com.devonfw.demoquarkus.service.model.AnimalDto;
import com.devonfw.demoquarkus.service.model.AnimalSearchCriteriaDto;
import org.springframework.data.domain.PageImpl;

public interface UcFindAnimal {
    PageImpl<AnimalDto> findAnimals(AnimalSearchCriteriaDto dto);

    PageImpl<AnimalDto> findAnimalsByCriteriaApi(AnimalSearchCriteriaDto dto);

    PageImpl<AnimalDto> findAnimalsByQueryDsl(AnimalSearchCriteriaDto dto);

    PageImpl<AnimalDto> findAnimalsByNameQuery(AnimalSearchCriteriaDto dto);

    PageImpl<AnimalDto> findAnimalsByNameNativeQuery(AnimalSearchCriteriaDto dto);

    PageImpl<AnimalDto> findAnimalsOrderedByName();

    AnimalDto findAnimal(String id);

    AnimalDto findAnimalByName(String name);
}

package com.devonfw.demoquarkus.logic;

import com.devonfw.demoquarkus.service.v1.model.AnimalDto;
import com.devonfw.demoquarkus.service.v1.model.NewAnimalDto;

public interface UcManageAnimal {
    AnimalDto saveAnimal(NewAnimalDto dto);

    AnimalDto deleteAnimal(String id);
}

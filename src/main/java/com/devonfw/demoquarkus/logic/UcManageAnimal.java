package com.devonfw.demoquarkus.logic;

import com.devonfw.demoquarkus.service.model.AnimalDto;
import com.devonfw.demoquarkus.service.model.NewAnimalDto;

public interface UcManageAnimal {
    AnimalDto saveAnimal(NewAnimalDto dto);

    AnimalDto deleteAnimal(String id);
}

package com.devonfw.demoquarkus.logic;

import com.devonfw.demoquarkus.domain.model.AnimalEntity;
import com.devonfw.demoquarkus.domain.repo.AnimalRepository;
import com.devonfw.demoquarkus.service.mapper.AnimalMapper;
import com.devonfw.demoquarkus.service.model.AnimalDto;
import com.devonfw.demoquarkus.service.model.NewAnimalDto;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

@Named
@Transactional
public class UcManageAnimalImpl implements UcManageAnimal {
    @Inject
    AnimalRepository animalRepository;

    @Inject
    AnimalMapper mapper;

    @Override
    public AnimalDto saveAnimal(NewAnimalDto dto) {
        AnimalEntity created = this.animalRepository.save(this.mapper.create(dto));
        return this.mapper.map(created);
    }

    @Override
    public AnimalDto deleteAnimal(String id) {
        AnimalEntity animal = this.animalRepository.findById(Long.valueOf(id)).get();
        if (animal != null) {
            this.animalRepository.delete(animal);
            return this.mapper.map(animal);
        } else {
            return null;
        }
    }
}

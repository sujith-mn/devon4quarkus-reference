package com.devonfw.demoquarkus.logic;

import com.devonfw.demoquarkus.domain.model.AnimalEntity;
import com.devonfw.demoquarkus.domain.repo.AnimalRepository;
import com.devonfw.demoquarkus.service.mapper.AnimalMapper;
import com.devonfw.demoquarkus.service.model.AnimalDto;
import com.devonfw.demoquarkus.service.model.AnimalSearchCriteriaDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Named
@Transactional
@Slf4j
public class UcFindAnimalImpl implements UcFindAnimal {
    @Inject
    AnimalRepository animalRepository;

    @Inject
    AnimalMapper mapper;

    @Override
    public PageImpl<AnimalDto> findAnimals(AnimalSearchCriteriaDto dto) {
        Iterable<AnimalEntity> animalsIterator = this.animalRepository.findAll();
        List<AnimalEntity> animals = new ArrayList<AnimalEntity>();
        animalsIterator.forEach(animals::add);
        List<AnimalDto> animalsDto = this.mapper.map(animals);
        return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
    }

    @Override
    public PageImpl<AnimalDto> findAnimalsByCriteriaApi(AnimalSearchCriteriaDto dto) {
        List<AnimalEntity> animals = this.animalRepository.findAllCriteriaApi(dto).getContent();
        List<AnimalDto> animalsDto = this.mapper.map(animals);
        return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
    }

    @Override
    public PageImpl<AnimalDto> findAnimalsByQueryDsl(AnimalSearchCriteriaDto dto) {
        List<AnimalEntity> animals = this.animalRepository.findAllQueryDsl(dto).getContent();
        List<AnimalDto> animalsDto = this.mapper.map(animals);
        return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
    }

    @Override
    public PageImpl<AnimalDto> findAnimalsByNameQuery(AnimalSearchCriteriaDto dto) {
        List<AnimalEntity> animals = this.animalRepository.findByNameQuery(dto).getContent();
        List<AnimalDto> animalsDto = this.mapper.map(animals);
        return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
    }

    @Override
    public PageImpl<AnimalDto> findAnimalsByNameNativeQuery(AnimalSearchCriteriaDto dto) {
        List<AnimalEntity> animals = this.animalRepository.findByNameNativeQuery(dto).getContent();
        List<AnimalDto> animalsDto = this.mapper.map(animals);
        return new PageImpl<>(animalsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animalsDto.size());
    }

    @Override
    public PageImpl<AnimalDto> findAnimalsOrderedByName() {
        List<AnimalEntity> animals = this.animalRepository.findAllByOrderByName().getContent();
        List<AnimalDto> animalsDto = this.mapper.map(animals);
        return new PageImpl<>(animalsDto);
    }

    @Override
    public AnimalDto findAnimal(String id) {
        AnimalEntity animal = this.animalRepository.findById(Long.valueOf(id)).get();
        if (animal != null) {
            return this.mapper.map(animal);
        } else {
            return null;
        }
    }

    @Override
    public AnimalDto findAnimalByName(String name) {
        AnimalEntity animal = this.animalRepository.findByName(name);
        if (animal != null) {
            return this.mapper.map(animal);
        } else {
            return null;
        }
    }

    @Override
    public List<String> findAnimalFacts(String id) {
        return getFactsFromUnreliableSource(id);
    }

    private List<String> getFactsFromUnreliableSource(String id) {
        // This is for testing calling a source that can fail
        // Our source will randomly fail
        boolean willFail = new Random().nextBoolean();
        if (willFail) {
            log.info("Ooops, fact fetching failed");
            throw new IllegalStateException("Unreliable source failed");
        } else {
            log.info("Cool, fact fetching succeed");
            return List.of("imagine real data here", "and also this shocking fact");
        }
    }
}

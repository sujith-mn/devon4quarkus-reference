package com.devonfw.demoquarkus.service.v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import com.devonfw.demoquarkus.domain.model.AnimalEntity;
import com.devonfw.demoquarkus.service.v1.model.AnimalDto;
import com.devonfw.demoquarkus.service.v1.model.NewAnimalDto;

//mapstruct will generate an impl class(CDI bean, see pom.xml) from this interface at compile time
@Mapper(uses = OffsetDateTimeMapper.class)
public interface AnimalMapper {

  AnimalDto map(AnimalEntity model);

  AnimalEntity create(NewAnimalDto dto);

  List<AnimalDto> map(List<AnimalEntity> animals);
}

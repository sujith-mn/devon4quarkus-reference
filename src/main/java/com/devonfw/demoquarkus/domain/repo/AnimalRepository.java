package com.devonfw.demoquarkus.domain.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.devonfw.demoquarkus.domain.model.AnimalEntity;

public interface AnimalRepository extends CrudRepository<AnimalEntity, Long>, AnimalFragment {

  @Query("select a from AnimalEntity a where name = :name")
  AnimalEntity findByName(@Param("name") String name);

  Page<AnimalEntity> findAllByOrderByName();
}

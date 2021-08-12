package com.devonfw.demoquarkus.domain.repo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.devonfw.demoquarkus.domain.model.AnimalEntity;
import com.devonfw.demoquarkus.domain.model.AnimalEntity_;
import com.devonfw.demoquarkus.domain.model.QAnimalEntity;
import com.devonfw.demoquarkus.service.v1.model.AnimalSearchCriteriaDto;
import com.querydsl.jpa.impl.JPAQuery;

public class AnimalFragmentImpl implements AnimalFragment {

  @Inject
  EntityManager em;

  @Override
  public Page<AnimalEntity> findAllCriteriaApi(AnimalSearchCriteriaDto dto) {

    CriteriaQuery<AnimalEntity> cq = this.em.getCriteriaBuilder().createQuery(AnimalEntity.class);
    Root<AnimalEntity> root = cq.from(AnimalEntity.class);
    List<Predicate> predicates = new ArrayList<>();
    CriteriaBuilder cb = this.em.getCriteriaBuilder();
    if (dto.getName() != null && !dto.getName().isEmpty()) {
      predicates.add(cb.like(root.get(AnimalEntity_.NAME), dto.getName()));
    }
    if (dto.getNumberOfLegs() != null) {
      predicates.add(cb.equal(root.get(AnimalEntity_.NUMBER_OF_LEGS), dto.getNumberOfLegs()));
    }
    if (!predicates.isEmpty()) {
      cq.where(predicates.toArray(new Predicate[0]));
    }

    // Order by name
    cq.orderBy(cb.desc(root.get(AnimalEntity_.NAME)));

    TypedQuery<AnimalEntity> animals = this.em.createQuery(cq).setFirstResult(dto.getPageNumber() * dto.getPageSize())
        .setMaxResults(dto.getPageSize());
    return new PageImpl<AnimalEntity>(animals.getResultList(), PageRequest.of(dto.getPageNumber(), dto.getPageSize()),
        animals.getResultList().size());
  }

  @Override
  public Page<AnimalEntity> findAllQueryDsl(AnimalSearchCriteriaDto dto) {

    QAnimalEntity animal = QAnimalEntity.animalEntity;
    JPAQuery<AnimalEntity> query = new JPAQuery<AnimalEntity>(this.em);
    query.from(animal);
    if (dto.getName() != null && !dto.getName().isEmpty()) {
      query.where(animal.name.eq(dto.getName()));
    }
    if (dto.getNumberOfLegs() != null) {
      query.where(animal.numberOfLegs.eq(dto.getNumberOfLegs()));
    }

    // Order by name
    query.orderBy(animal.name.desc());

    List<AnimalEntity> animals = query.limit(dto.getPageSize()).offset(dto.getPageNumber() * dto.getPageSize()).fetch();
    return new PageImpl<>(animals, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animals.size());
  }

  @Override
  public Page<AnimalEntity> findByNameQuery(AnimalSearchCriteriaDto dto) {

    Query query = this.em.createQuery("select a from AnimalEntity a where a.name = :name");
    query.setParameter("name", dto.getName());
    List<AnimalEntity> animals = query.getResultList();
    return new PageImpl<>(animals, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animals.size());
  }

  @Override
  public Page<AnimalEntity> findByNameNativeQuery(AnimalSearchCriteriaDto dto) {

    Query query = this.em.createNativeQuery("select * from AnimalEntity where name = :name", AnimalEntity.class);
    query.setParameter("name", dto.getName());
    List<AnimalEntity> animals = query.getResultList();
    return new PageImpl<>(animals, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), animals.size());
  }
}

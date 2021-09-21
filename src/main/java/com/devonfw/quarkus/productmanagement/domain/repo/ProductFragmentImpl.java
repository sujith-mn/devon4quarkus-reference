package com.devonfw.quarkus.productmanagement.domain.repo;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity_;
import com.devonfw.quarkus.productmanagement.domain.model.QProductEntity;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaEto;
import com.querydsl.jpa.impl.JPAQuery;

public class ProductFragmentImpl implements ProductFragment {

  @Inject
  EntityManager em;

  @Override
  public Page<ProductEntity> findAllCriteriaApi(ProductSearchCriteriaEto searchCriteria) {

    CriteriaQuery<ProductEntity> cq = this.em.getCriteriaBuilder().createQuery(ProductEntity.class);
    Root<ProductEntity> root = cq.from(ProductEntity.class);
    List<Predicate> predicates = new ArrayList<>();
    CriteriaBuilder cb = this.em.getCriteriaBuilder();
    if (!StringUtils.isEmpty(searchCriteria.getTitle())) {
      predicates.add(cb.like(root.get(ProductEntity_.TITLE), searchCriteria.getTitle()));
    }

    if (!StringUtils.isEmpty(searchCriteria.getPrice())) {
      predicates.add(cb.gt(root.get(ProductEntity_.PRICE), searchCriteria.getPrice()));
    } else {
      if (!StringUtils.isEmpty(searchCriteria.getPriceMin())) {
        predicates.add(cb.gt(root.get(ProductEntity_.PRICE), searchCriteria.getPriceMin()));
      }
      if (!StringUtils.isEmpty(searchCriteria.getPriceMax())) {
        predicates.add(cb.lt(root.get(ProductEntity_.PRICE), searchCriteria.getPriceMax()));
      }
    }

    if (!predicates.isEmpty()) {
      cq.where(predicates.toArray(new Predicate[0]));
    }

    // Order by title
    cq.orderBy(cb.desc(root.get(ProductEntity_.TITLE)));

    TypedQuery<ProductEntity> products = this.em.createQuery(cq)
        .setFirstResult(searchCriteria.getPageNumber() * searchCriteria.getPageSize())
        .setMaxResults(searchCriteria.getPageSize());
    return new PageImpl<ProductEntity>(products.getResultList(),
        PageRequest.of(searchCriteria.getPageNumber(), searchCriteria.getPageSize()), products.getResultList().size());
  }

  @Override
  public Page<ProductEntity> findAllQueryDsl(ProductSearchCriteriaEto searchCriteria) {

    QProductEntity product = QProductEntity.productEntity;
    JPAQuery<ProductEntity> query = new JPAQuery<ProductEntity>(this.em).from(product);

    if (!StringUtils.isEmpty(searchCriteria.getTitle())) {
      query.where(product.title.eq(searchCriteria.getTitle()));
    }

    if (!StringUtils.isEmpty(searchCriteria.getPrice())) {
      query.where(product.price.eq(searchCriteria.getPrice()));
    } else {
      if (!StringUtils.isEmpty(searchCriteria.getPriceMin())) {
        query.where(product.price.gt(searchCriteria.getPriceMin()));
      }
      if (!StringUtils.isEmpty(searchCriteria.getPriceMax())) {
        query.where(product.price.lt(searchCriteria.getPriceMax()));
      }
    }

    // Order by title
    query.orderBy(product.title.desc());

    List<ProductEntity> products = query.limit(searchCriteria.getPageSize())
        .offset(searchCriteria.getPageNumber() * searchCriteria.getPageSize()).fetch();
    return new PageImpl<>(products, PageRequest.of(searchCriteria.getPageNumber(), searchCriteria.getPageSize()),
        products.size());
  }

}

package com.devonfw.quarkus.productmanagement.domain.repo;

import static com.devonfw.quarkus.productmanagement.utils.StringUtils.isEmpty;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.model.QProductEntity;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQuery;

public class ProductFragmentImpl implements ProductFragment {

  @Inject
  EntityManager em;

  @Override
  public Page<ProductEntity> findByCriteria(ProductSearchCriteriaDto searchCriteria) {

    QProductEntity product = QProductEntity.productEntity;

    Predicate[] predicates = new Predicate[3];
    int index = 0;

    if (!isEmpty(searchCriteria.getTitle())) {
      predicates[index++] = product.title.eq(searchCriteria.getTitle());
    }

    if (searchCriteria.getPrice() != null) {
      predicates[index++] = product.price.eq(searchCriteria.getPrice());
    } else {
      if (searchCriteria.getPriceMin() != null) {
        predicates[index++] = product.price.gt(searchCriteria.getPriceMin());
      }
      if (searchCriteria.getPriceMax() != null) {
        predicates[index++] = product.price.lt(searchCriteria.getPriceMax());
      }
    }

    JPAQuery<ProductEntity> query = new JPAQuery<ProductEntity>(this.em).from(product);
    query.where(predicates);
    query.orderBy(product.title.desc());

    List<ProductEntity> products = query.limit(searchCriteria.getPageSize())
        .offset(searchCriteria.getPageNumber() * searchCriteria.getPageSize()).fetch();
    return new PageImpl<>(products, PageRequest.of(searchCriteria.getPageNumber(), searchCriteria.getPageSize()),
        products.size());
  }

}

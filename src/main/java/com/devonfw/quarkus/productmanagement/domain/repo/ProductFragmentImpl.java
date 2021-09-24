package com.devonfw.quarkus.productmanagement.domain.repo;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.util.StringUtils;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.model.QProductEntity;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaEto;
import com.querydsl.jpa.impl.JPAQuery;

public class ProductFragmentImpl implements ProductFragment {

  @Inject
  EntityManager em;

  @Override
  public Page<ProductEntity> findProducts(ProductSearchCriteriaEto searchCriteria) {

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

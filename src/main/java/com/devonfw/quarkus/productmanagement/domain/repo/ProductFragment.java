package com.devonfw.quarkus.productmanagement.domain.repo;

import org.springframework.data.domain.Page;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaEto;

public interface ProductFragment {

  public Page<ProductEntity> findProducts(ProductSearchCriteriaEto searchCriteria);
}

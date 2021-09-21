package com.devonfw.quarkus.productmanagement.logic;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.service.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductEto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaEto;

import lombok.extern.slf4j.Slf4j;

@Named
@Transactional
@Slf4j
public class UcFindProduct {
  @Inject
  ProductRepository ProductRepository;

  @Inject
  ProductMapper mapper;

  public Page<ProductEto> findProductsByCriteriaApi(ProductSearchCriteriaEto searchCriteria) {

    Page<ProductEntity> products = this.ProductRepository.findAllCriteriaApi(searchCriteria);
    if (products.isEmpty()) {
      return null;
    }
    return this.mapper.map(products);
  }

  public Page<ProductEto> findProductsByQueryDsl(ProductSearchCriteriaEto searchCriteria) {

    Page<ProductEntity> products = this.ProductRepository.findAllQueryDsl(searchCriteria);
    if (products.isEmpty()) {
      return null;
    }
    return this.mapper.map(products);
  }

  public Page<ProductEto> findProductsOrderedByTitle() {

    Page<ProductEntity> products = this.ProductRepository.findAllByOrderByTitle();
    if (products.isEmpty()) {
      return null;
    }
    return this.mapper.map(products);
  }

  public ProductEto findProduct(String id) {

    Optional<ProductEntity> product = this.ProductRepository.findById(Long.valueOf(id));
    if (product.isPresent()) {
      return this.mapper.map(product.get());
    }
    return null;
  }

  public ProductEto findProductByTitle(String title) {

    ProductEntity product = this.ProductRepository.findByTitle(title);
    if (product != null) {
      return this.mapper.map(product);
    }
    return null;
  }
}
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

@Named
@Transactional
public class UcFindProduct {
  @Inject
  ProductRepository ProductRepository;

  @Inject
  ProductMapper mapper;

  public Page<ProductEto> findProducts(ProductSearchCriteriaEto searchCriteria) {

    Page<ProductEntity> products = this.ProductRepository.findProducts(searchCriteria);
    if (products.isEmpty()) {
      return null;
    }
    return this.mapper.map(products);
  }

  public Page<ProductEto> findProductsOrderByTitle() {

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
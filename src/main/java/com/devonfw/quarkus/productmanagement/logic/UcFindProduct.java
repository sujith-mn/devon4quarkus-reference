package com.devonfw.quarkus.productmanagement.logic;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.service.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;

@Named
@Transactional
public class UcFindProduct {
  @Inject
  ProductRepository ProductRepository;

  @Inject
  ProductMapper mapper;

  public Page<ProductDto> findProducts(ProductSearchCriteriaDto searchCriteria) {

    Page<ProductEntity> products = this.ProductRepository.findProducts(searchCriteria);
    if (products.isEmpty()) {
      return null;
    }
    return this.mapper.map(products);
  }

  public Page<ProductDto> findProductsOrderByTitle() {

    Page<ProductEntity> products = this.ProductRepository.findAllByOrderByTitle();
    if (products.isEmpty()) {
      return null;
    }
    return this.mapper.map(products);
  }

  public ProductDto findProduct(String id) {

    Optional<ProductEntity> product = this.ProductRepository.findById(Long.valueOf(id));
    if (product.isPresent()) {
      return this.mapper.map(product.get());
    }
    return null;
  }

  public ProductDto findProductByTitle(String title) {

    ProductEntity product = this.ProductRepository.findByTitle(title);
    if (product != null) {
      return this.mapper.map(product);
    }
    return null;
  }
}
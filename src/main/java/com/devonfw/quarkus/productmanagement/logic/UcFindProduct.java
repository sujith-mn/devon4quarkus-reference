package com.devonfw.quarkus.productmanagement.logic;

import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

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

    List<ProductEntity> products = this.ProductRepository.findAllCriteriaApi(searchCriteria).getContent();
    if (products.isEmpty()) {
      return null;
    }
    List<ProductEto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, searchCriteria.getPageRequest(), productsDto.size());
  }

  public Page<ProductEto> findProductsByQueryDsl(ProductSearchCriteriaEto searchCriteria) {

    List<ProductEntity> products = this.ProductRepository.findAllQueryDsl(searchCriteria).getContent();
    if (products.isEmpty()) {
      return null;
    }
    List<ProductEto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto, searchCriteria.getPageRequest(), productsDto.size());
  }

  public Page<ProductEto> findProductsOrderedByTitle() {

    List<ProductEntity> products = this.ProductRepository.findAllByOrderByTitle().getContent();
    if (products.isEmpty()) {
      return null;
    }
    List<ProductEto> productsDto = this.mapper.map(products);
    return new PageImpl<>(productsDto);
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
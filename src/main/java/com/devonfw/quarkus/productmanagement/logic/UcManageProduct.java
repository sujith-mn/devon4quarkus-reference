package com.devonfw.quarkus.productmanagement.logic;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.service.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductEto;

@Named
@Transactional
public class UcManageProduct {
  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMapper mapper;

  public void saveProduct(ProductEto product) {

    this.productRepository.save(this.mapper.map(product));
  }

  public void deleteProduct(String id) {

    this.productRepository.deleteById(Long.valueOf(id));
  }
}

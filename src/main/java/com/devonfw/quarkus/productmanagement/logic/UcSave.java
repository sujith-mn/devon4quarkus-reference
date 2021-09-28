package com.devonfw.quarkus.productmanagement.logic;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.service.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;

@Named
@Transactional
public class UcSave {

  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMapper mapper;

  public void saveProduct(ProductDto product) {

    this.productRepository.save(this.mapper.map(product));
  }
}

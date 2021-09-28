package com.devonfw.quarkus.productmanagement.logic;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;

@Named
@Transactional
public class UcDelete {

  @Inject
  ProductRepository productRepository;

  public void deleteProduct(String id) {

    this.productRepository.deleteById(Long.valueOf(id));
  }
}

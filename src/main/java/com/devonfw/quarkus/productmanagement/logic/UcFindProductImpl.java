package com.devonfw.quarkus.productmanagement.logic;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.service.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;

import lombok.extern.slf4j.Slf4j;

@Named
@Transactional
@Slf4j
public class UcFindProductImpl implements UcFindProduct {
  @Inject
  ProductRepository ProductRepository;

  @Inject
  ProductMapper mapper;

  @Override
  public Page<ProductDto> findProducts(ProductSearchCriteriaDto dto) {

    Iterable<ProductEntity> ProductsIterator = this.ProductRepository.findAll();
    List<ProductEntity> Products = new ArrayList<ProductEntity>();
    ProductsIterator.forEach(Products::add);
    List<ProductDto> ProductsDto = this.mapper.map(Products);
    return new PageImpl<>(ProductsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), ProductsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByCriteriaApi(ProductSearchCriteriaDto dto) {

    List<ProductEntity> Products = this.ProductRepository.findAllCriteriaApi(dto).getContent();
    List<ProductDto> ProductsDto = this.mapper.map(Products);
    return new PageImpl<>(ProductsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), ProductsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByQueryDsl(ProductSearchCriteriaDto dto) {

    List<ProductEntity> Products = this.ProductRepository.findAllQueryDsl(dto).getContent();
    List<ProductDto> ProductsDto = this.mapper.map(Products);
    return new PageImpl<>(ProductsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), ProductsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByTitleQuery(ProductSearchCriteriaDto dto) {

    List<ProductEntity> Products = this.ProductRepository.findByTitleQuery(dto).getContent();
    List<ProductDto> ProductsDto = this.mapper.map(Products);
    return new PageImpl<>(ProductsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), ProductsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsByTitleNativeQuery(ProductSearchCriteriaDto dto) {

    List<ProductEntity> Products = this.ProductRepository.findByTitleNativeQuery(dto).getContent();
    List<ProductDto> ProductsDto = this.mapper.map(Products);
    return new PageImpl<>(ProductsDto, PageRequest.of(dto.getPageNumber(), dto.getPageSize()), ProductsDto.size());
  }

  @Override
  public Page<ProductDto> findProductsOrderedByTitle() {

    List<ProductEntity> Products = this.ProductRepository.findAllByOrderByTitle().getContent();
    List<ProductDto> ProductsDto = this.mapper.map(Products);
    return new PageImpl<>(ProductsDto);
  }

  @Override
  public ProductDto findProduct(String id) {

    ProductEntity Product = this.ProductRepository.findById(Long.valueOf(id)).get();
    if (Product != null) {
      return this.mapper.map(Product);
    } else {
      return null;
    }
  }

  @Override
  public ProductDto findProductByTitle(String title) {

    ProductEntity Product = this.ProductRepository.findByTitle(title);
    if (Product != null) {
      return this.mapper.map(Product);
    } else {
      return null;
    }
  }
}
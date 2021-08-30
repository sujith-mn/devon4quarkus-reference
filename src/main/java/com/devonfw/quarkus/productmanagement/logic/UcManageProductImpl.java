package com.devonfw.quarkus.productmanagement.logic;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.service.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.NewProductDto;

@Named
@Transactional
public class UcManageProductImpl implements UcManageProduct {
    @Inject
    ProductRepository ProductRepository;

    @Inject
    ProductMapper mapper;

    @Override
    public ProductDto saveProduct(NewProductDto dto) {
        ProductEntity created = this.ProductRepository.save(this.mapper.create(dto));
        return this.mapper.map(created);
    }

    @Override
    public ProductDto deleteProduct(String id) {
        ProductEntity Product = this.ProductRepository.findById(Long.valueOf(id)).get();
        if (Product != null) {
            this.ProductRepository.delete(Product);
            return this.mapper.map(Product);
        } else {
            return null;
        }
    }
}

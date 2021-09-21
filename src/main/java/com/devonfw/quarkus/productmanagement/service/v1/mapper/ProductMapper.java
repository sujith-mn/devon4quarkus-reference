package com.devonfw.quarkus.productmanagement.service.v1.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.tkit.quarkus.rs.mappers.OffsetDateTimeMapper;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductEto;

//mapstruct will generate an impl class(CDI bean, see pom.xml) from this interface at compile time
@Mapper(uses = OffsetDateTimeMapper.class)
public interface ProductMapper {

  ProductEto map(ProductEntity model);

  ProductEntity map(ProductEto dto);

  List<ProductEto> map(List<ProductEntity> Products);
}

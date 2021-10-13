package com.devonfw.quarkus.productmanagement.service.v1;

import static javax.ws.rs.core.Response.created;
import static javax.ws.rs.core.Response.status;

import java.util.Optional;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;

import com.devonfw.quarkus.productmanagement.domain.model.ProductEntity;
import com.devonfw.quarkus.productmanagement.domain.repo.ProductRepository;
import com.devonfw.quarkus.productmanagement.service.v1.mapper.ProductMapper;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;

@Path("/product/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRestService {

  @Inject
  ProductRepository productRepository;

  @Inject
  ProductMapper productMapper;

  @Context
  UriInfo uriInfo;

  @POST
  @Path("search")
  public Page<ProductDto> getAllQueryDsl(@BeanParam ProductSearchCriteriaDto searchCriteria) {

    Page<ProductEntity> products = this.productRepository.findByCriteria(searchCriteria);
    if (products.isEmpty()) {
      return Page.empty();
    }
    return this.productMapper.map(products);
  }

  @GET
  public Page<ProductDto> getAllOrderedByTitle() {

    Page<ProductEntity> products = this.productRepository.findAllByOrderByTitle();
    if (products.isEmpty()) {
      return Page.empty();
    }
    return this.productMapper.map(products);
  }

  @APIResponses({
  @APIResponse(responseCode = "201", description = "OK, New Product created", content = @Content(schema = @Schema(implementation = ProductDto.class))),
  @APIResponse(responseCode = "400", description = "Client side error, invalid request"),
  @APIResponse(responseCode = "500") })
  @Operation(operationId = "createNewProduct", description = "Stores new Product in DB")
  @POST
  public Response createNewProduct(ProductDto product) {

    if (StringUtils.isEmpty(product.getTitle())) {
      throw new WebApplicationException("Title was not set on request.", 400);
    }

    ProductEntity productEntity = this.productRepository.save(this.productMapper.map(product));

    UriBuilder uriBuilder = this.uriInfo.getAbsolutePathBuilder().path(Long.toString(productEntity.getId()));
    return created(uriBuilder.build()).build();
  }

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
  @APIResponse(responseCode = "404", description = "Product not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "getProductById", description = "Returns Product with given id")
  @GET
  @Path("{id}")
  public ProductDto getProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    Optional<ProductEntity> product = this.productRepository.findById(Long.valueOf(id));
    if (product.isPresent()) {
      return this.productMapper.map(product.get());
    }
    return null;
  }

  @GET
  @Path("title/{title}")
  public ProductDto getProductByTitle(@PathParam("title") String title) {

    ProductEntity product = this.productRepository.findByTitle(title);
    if (product != null) {
      return this.productMapper.map(product);
    }
    return null;
  }

  @APIResponses({
  @APIResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
  @APIResponse(responseCode = "404", description = "Product not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "deleteProductById", description = "Deletes the Product with given id")
  @DELETE
  @Path("{id}")
  public Response deleteProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    this.productRepository.deleteById(Long.valueOf(id));
    return status(Status.NO_CONTENT.getStatusCode()).build();
  }

}

package com.devonfw.quarkus.productmanagement.service.v1;

import javax.inject.Inject;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.springframework.data.domain.PageImpl;

import com.devonfw.quarkus.productmanagement.logic.UcFindProduct;
import com.devonfw.quarkus.productmanagement.logic.UcManageProduct;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductEto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaEto;

@Path("/product/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRestService {

  @Inject
  UcFindProduct ucFindProduct;

  @Inject
  UcManageProduct ucManageProduct;

  @POST
  @Path("search")
  public PageImpl<ProductEto> getAllQueryDsl(@BeanParam ProductSearchCriteriaEto productSearch) {

    return (PageImpl) this.ucFindProduct.findProducts(productSearch);
  }

  @GET
  public PageImpl<ProductEto> getAllOrderedByTitle() {

    return (PageImpl) this.ucFindProduct.findProductsOrderByTitle();
  }

  @APIResponses({
  @APIResponse(responseCode = "201", description = "OK, New Product created", content = @Content(schema = @Schema(implementation = ProductEto.class))),
  @APIResponse(responseCode = "400", description = "Client side error, invalid request"),
  @APIResponse(responseCode = "500") })
  @Operation(operationId = "createNewProduct", description = "Stores new Product in DB")
  @POST
  public void createNewProduct(ProductEto product) {

    this.ucManageProduct.saveProduct(product);
  }

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductEto.class))),
  @APIResponse(responseCode = "404", description = "Product not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "getProductById", description = "Returns Product with given id")
  @GET
  @Path("{id}")
  public ProductEto getProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    return this.ucFindProduct.findProduct(id);
  }

  @GET
  @Path("title/{title}")
  public ProductEto getProductByTitle(@PathParam("title") String title) {

    return this.ucFindProduct.findProductByTitle(title);
  }

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductEto.class))),
  @APIResponse(responseCode = "404", description = "Product not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "deleteProductById", description = "Deletes the Product with given id")
  @DELETE
  @Path("{id}")
  public void deleteProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    this.ucManageProduct.deleteProduct(id);
  }

}

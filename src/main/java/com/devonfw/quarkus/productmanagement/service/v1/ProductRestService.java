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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.springframework.data.domain.PageImpl;
import org.springframework.util.StringUtils;

import com.devonfw.quarkus.productmanagement.logic.UcDelete;
import com.devonfw.quarkus.productmanagement.logic.UcFindProduct;
import com.devonfw.quarkus.productmanagement.logic.UcSave;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;

@Path("/product/v1")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRestService {

  @Inject
  UcFindProduct ucFindProduct;

  @Inject
  UcDelete ucDelete;

  @Inject
  UcSave ucSave;

  @POST
  @Path("search")
  public PageImpl<ProductDto> getAllQueryDsl(@BeanParam ProductSearchCriteriaDto productSearch) {

    return (PageImpl) this.ucFindProduct.findProducts(productSearch);
  }

  @GET
  public PageImpl<ProductDto> getAllOrderedByTitle() {

    return (PageImpl) this.ucFindProduct.findProductsOrderByTitle();
  }

  @APIResponses({
  @APIResponse(responseCode = "201", description = "OK, New Product created", content = @Content(schema = @Schema(implementation = ProductDto.class))),
  @APIResponse(responseCode = "400", description = "Client side error, invalid request"),
  @APIResponse(responseCode = "500") })
  @Operation(operationId = "save", description = "Stores new Product in DB")
  @POST
  public Response save(ProductDto product) {

    if (StringUtils.isEmpty(product.getTitle())) {
      throw new WebApplicationException("Title was not set on request.", 400);
    }
    this.ucSave.saveProduct(product);
    return Response.ok().status(201).build();
  }

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
  @APIResponse(responseCode = "404", description = "Product not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "getProductById", description = "Returns Product with given id")
  @GET
  @Path("{id}")
  public ProductDto getProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    return this.ucFindProduct.findProduct(id);
  }

  @GET
  @Path("title/{title}")
  public ProductDto getProductByTitle(@PathParam("title") String title) {

    return this.ucFindProduct.findProductByTitle(title);
  }

  @APIResponses({
  @APIResponse(responseCode = "204", description = "OK", content = @Content(schema = @Schema(implementation = ProductDto.class))),
  @APIResponse(responseCode = "404", description = "Product not found"), @APIResponse(responseCode = "500") })
  @Operation(operationId = "deleteProductById", description = "Deletes the Product with given id")
  @DELETE
  @Path("{id}")
  public Response deleteProductById(@Parameter(description = "Product unique id") @PathParam("id") String id) {

    this.ucDelete.deleteProduct(id);
    return Response.status(204).build();
  }

}

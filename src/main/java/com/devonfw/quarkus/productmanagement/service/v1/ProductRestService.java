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
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

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

//In Quarkus all JAX-RS resources are treated as CDI beans
//default is Singleton scope
@Path("/products/v1")
// how we serialize response
@Produces(MediaType.APPLICATION_JSON)
// how we deserialize params
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRestService {

  // using @Context we can inject contextual info from JAXRS(e.g. http request, current uri info, endpoint info...)
  @Context
  UriInfo uriInfo;

  @Inject
  UcFindProduct ucFindProduct;

  @Inject
  UcManageProduct ucManageProduct;

  @APIResponses({
  @APIResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = ProductEto.class))),
  @APIResponse(responseCode = "500") })
  @Operation(operationId = "Get Products", description = "Returns list of Products matching given criteria, uses pagination")
  @POST
  // REST service methods should not declare exceptions, any thrown error will be transformed by exceptionMapper in
  // tkit-rest
  // We did not define custom @Path - so it will use class level path
  @Path("searchbycriteria")
  public PageImpl<ProductEto> getAllCriteriaApi(@BeanParam ProductSearchCriteriaEto productSearch) {

    return (PageImpl) this.ucFindProduct.findProductsByCriteriaApi(productSearch);
  }

  @POST
  @Path("searchbydsl")
  public PageImpl<ProductEto> getAllQueryDsl(@BeanParam ProductSearchCriteriaEto productSearch) {

    return (PageImpl) this.ucFindProduct.findProductsByQueryDsl(productSearch);
  }

  @GET
  public PageImpl<ProductEto> getAllOrderedByTitle() {

    return (PageImpl) this.ucFindProduct.findProductsOrderedByTitle();
  }

  @APIResponses({
  @APIResponse(responseCode = "201", description = "OK, New Product created", content = @Content(schema = @Schema(implementation = ProductEto.class))),
  @APIResponse(responseCode = "400", description = "Client side error, invalid request"),
  @APIResponse(responseCode = "500") })
  @Operation(operationId = "createNewProduct", description = "Stores new Product in DB")
  @POST
  // We did not define custom @Path - so it will use class level path.
  // Although we now have 2 methods with same path, it is ok, because it is a different method (get vs post)
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

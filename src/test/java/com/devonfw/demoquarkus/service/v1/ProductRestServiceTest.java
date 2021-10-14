package com.devonfw.demoquarkus.service.v1;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.Response.Status.CREATED;
import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.tkit.quarkus.test.docker.DockerComposeTestResource;

import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@Tag("integration")
@QuarkusTestResource(DockerComposeTestResource.class)
class ProductRestServiceTest {

  @Test
  void testAll() {

    ProductDto product1 = new ProductDto();
    product1.setTitle("Notebook");
    product1.setDescription("ZBook");
    product1.setPrice(BigDecimal.valueOf(1));

    ProductDto product2 = new ProductDto();
    product2.setTitle("McBook");
    product2.setDescription("Apple Notebook");
    product2.setPrice(BigDecimal.valueOf(2));

    ProductSearchCriteriaDto productSearch = new ProductSearchCriteriaDto();
    productSearch.setTitle("Notebook");

    given().when().body(product1).contentType(MediaType.APPLICATION_JSON).post("/product/v1").then().log().all()
        .statusCode(CREATED.getStatusCode());

    given().when().body(product2).contentType(MediaType.APPLICATION_JSON).post("/product/v1").then().log().all()
        .statusCode(CREATED.getStatusCode());

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1/1").then().log().all()
        .statusCode(OK.getStatusCode()).body("title", equalTo("Notebook"));

    given().when().body(productSearch).contentType(MediaType.APPLICATION_JSON).post("/product/v1/search").then().log()
        .all().statusCode(OK.getStatusCode()).extract().jsonPath().getString("content[0].title").equals("Notebook");

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1").then().log().all()
        .statusCode(OK.getStatusCode()).extract().jsonPath().getString("content[0].title").equals("Notebook");

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1/title/McBook").then().log().all()
        .statusCode(OK.getStatusCode()).body("description", equalTo("Apple Notebook"));

    given().when().contentType(MediaType.APPLICATION_JSON).delete("/product/v1/1").then().log().all()
        .statusCode(NO_CONTENT.getStatusCode());

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1/1").then().log().all()
        .statusCode(NO_CONTENT.getStatusCode());

  }

}
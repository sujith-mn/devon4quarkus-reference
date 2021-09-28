package com.devonfw.demoquarkus.service.v1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.tkit.quarkus.rs.models.PageResultDTO;
import org.tkit.quarkus.test.WithDBData;
import org.tkit.quarkus.test.docker.DockerComposeTestResource;

import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;

//Before you run this test, tkit-test extension starts docker containers from resources/docker-compose.yaml
//we get a real postgresdb for our tests which will be stopped after tests. No manual test setup is needed.
@QuarkusTest
@QuarkusTestResource(DockerComposeTestResource.class)
class ProductRestServiceTest {// extends AbstractTest {

  @Test
  // we also started a micro container, that can populated DB with data from excel
  // annotating class or method with @WithDBData allows us to scope data for each test even if we use the same DB
  @WithDBData(value = "data/product.xls", deleteBeforeInsert = true)
  void getAll() {

    Response response = given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1").then().statusCode(200)
        .extract().response();

    PageResultDTO<ProductDto> productsReturned = response.as(new TypeRef<PageResultDTO<ProductDto>>() {
    });

    // we import data from /import.sql - ergo expect 1 result
    assertEquals(2, productsReturned.getTotalElements());
  }

  @Test
  void getNonExistingTest() {

    Response response = given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1/doesnoexist").then()
        .log().all().statusCode(404).extract().response();
  }

  @Test
  @WithDBData(value = "data/empty.xls", deleteBeforeInsert = true)
  void save() {

    ProductDto product = new ProductDto();
    product.setTitle("HP Notebook");
    product.setDescription("ZBook");
    product.setPrice(BigDecimal.valueOf(1));

    Response response = given().when().body(product).contentType(MediaType.APPLICATION_JSON).post("/product/v1").then()
        .log().all().statusCode(201).header("Location", notNullValue()).extract().response();

    assertEquals(201, response.statusCode());

    response = given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1").then().log().all()
        .statusCode(200).extract().response();

    PageResultDTO<ProductDto> productsReturned = response.as(new TypeRef<>() {
    });
    assertEquals(1, productsReturned.getTotalElements());
    ProductDto created = productsReturned.getStream().get(0);
    assertNotNull(created);
    assertEquals(product.getTitle(), created.getTitle());
  }

  @Test
  @WithDBData(value = "data/product.xls", deleteBeforeInsert = true)
  public void testGetById() {

    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/product/v1/1").then().statusCode(200)
        .body("description", equalTo("Apple Notebook"));
  }

  @Test
  @WithDBData(value = "data/product.xls", deleteBeforeInsert = true)
  public void deleteById() {

    // delete
    given().when().log().all().contentType(MediaType.APPLICATION_JSON).delete("/product/v1/1").then().statusCode(200)
        .body("title", equalTo("MacBook Pro"));

    // after deletion it should be deleted
    given().when().log().all().contentType(MediaType.APPLICATION_JSON).get("/product/v1/1").then().statusCode(404);

  }

}
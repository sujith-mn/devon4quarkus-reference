package com.devonfw.demoquarkus.service.v1;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.math.BigDecimal;

import javax.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;
import org.tkit.quarkus.test.docker.DockerComposeTestResource;

import com.devonfw.quarkus.productmanagement.service.v1.model.ProductDto;
import com.devonfw.quarkus.productmanagement.service.v1.model.ProductSearchCriteriaDto;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

//Before you run this test, tkit-test extension starts docker containers from resources/docker-compose.yaml
//we get a real postgresdb for our tests which will be stopped after tests. No manual test setup is needed.
@QuarkusTest
@QuarkusTestResource(DockerComposeTestResource.class)
class ProductRestServiceTest {// extends AbstractTest {

  @Test
  void testAll() {

    ProductDto product = new ProductDto();
    product.setTitle("Notebook");
    product.setDescription("ZBook");
    product.setPrice(BigDecimal.valueOf(1));
    product.setId(null);

    ProductDto product1 = new ProductDto();
    product1.setTitle("McBook");
    product1.setDescription("Apple Notebook");
    product1.setPrice(BigDecimal.valueOf(1));

    ProductSearchCriteriaDto productSearch = new ProductSearchCriteriaDto();
    productSearch.setTitle("Notebook");

    given().when().body(product).contentType(MediaType.APPLICATION_JSON).post("/product/v1").then().log().all()
        .statusCode(201);

    given().when().body(product1).contentType(MediaType.APPLICATION_JSON).post("/product/v1").then().log().all()
        .statusCode(201);

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1/1").then().log().all().statusCode(200)
        .body("title", equalTo("Notebook"));

    given().when().body(productSearch).contentType(MediaType.APPLICATION_JSON).post("/product/v1/search").then().log()
        .all().statusCode(200).extract().jsonPath().getString("content[0].title").equals("Notebook");

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1").then().log().all().statusCode(200)
        .extract().jsonPath().getString("content[0].title").equals("Notebook");

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1/title/McBook").then().log().all()
        .statusCode(200).body("description", equalTo("Apple Notebook"));

    given().when().contentType(MediaType.APPLICATION_JSON).delete("/product/v1/1").then().log().all().statusCode(204);

    given().when().contentType(MediaType.APPLICATION_JSON).get("/product/v1/1").then().log().all().statusCode(204);

  }

}
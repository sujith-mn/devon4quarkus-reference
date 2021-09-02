package com.devonfw.quarkus.productmanagement.service.v1.model;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewProductDto {

  @Schema(nullable = false, description = "Product title", minLength = 3, maxLength = 50)
  private String title;

  @Schema(description = "Product tag line", minLength = 3, maxLength = 50)
  private String basicInfo;

  @Schema(nullable = false, description = "Product description", minLength = 3, maxLength = 50)
  private String description;
}

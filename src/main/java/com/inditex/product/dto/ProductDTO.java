package com.inditex.product.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProductDTO {
  @NotNull @NotBlank private String name;
  @NotNull @NotBlank private String description;
}

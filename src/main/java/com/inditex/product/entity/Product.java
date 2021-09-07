package com.inditex.product.entity;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Setter
public class Product {
  @NotNull
  @Min(0)
  private int id;

  @NotNull @NotBlank private String name;
  @NotNull @NotBlank private String description;
}

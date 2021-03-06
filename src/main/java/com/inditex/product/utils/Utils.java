package com.inditex.product.utils;

import com.inditex.product.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

  private List<Product> products;

  public List<Product> createObjects() {
    products = new ArrayList<>();
    Product product;
    for (int i = 0; i < 5; i++) {
      product = new Product();
      product.setId(i);
      product.setName("Camiseta número " + i);
      product.setDescription("Camiseta con estampado del número " + i);
      products.add(product);
    }
    return products;
  }

  public List<Product> concatListProducts(List<Product> listOne, List<Product> listTwo) {
    return Stream.concat(listOne.stream(), listTwo.stream()).collect(Collectors.toList());
  }
}

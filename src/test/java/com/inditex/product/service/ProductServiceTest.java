package com.inditex.product.service;

import com.inditex.product.entity.Product;
import com.inditex.product.utils.Constants;
import com.inditex.product.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductServiceTest {

  private ProductService productService;
  private List<Product> productList;
  private List<Product> productsExpected;
  private Product product;
  private Utils utils;

  @TestConfiguration
  static class ProductControllerTestContextConfiguration {

    @Bean
    public RunnerService runnerService() {
      return new RunnerService();
    }
  }

  @BeforeEach
  void setUp() {
    productService = new ProductService();
    utils = new Utils();
    product = new Product();
    productList = utils.createObjects();
    productsExpected = new ArrayList<>();

    product.setId(Constants.ID_CAMISETA);
    product.setName(Constants.NAME);
    product.setDescription(Constants.DESCRIPTION);
    productsExpected.add(product);
  }

  @Test
  void getProductsByParameterId() {
    Map<String, String> map = new HashMap<>();
    map.put("id", String.valueOf(Constants.ID_CAMISETA));
    productList = productService.getProductsByParameter(productList, map);
    assertTrue(productsExpected.get(0).getId() == productList.get(0).getId());
    assertTrue(productsExpected.get(0).getName().equals(productList.get(0).getName()));
    assertTrue(
        productsExpected.get(0).getDescription().equals(productList.get(0).getDescription()));
  }

  @Test
  void getProductsByParameterName() {
    Map<String, String> map = new HashMap<>();
    map.put("name", Constants.NAME);
    productList = productService.getProductsByParameter(productList, map);
    assertTrue(productsExpected.get(0).getId() == productList.get(0).getId());
    assertTrue(productsExpected.get(0).getName().equals(productList.get(0).getName()));
    assertTrue(
        productsExpected.get(0).getDescription().equals(productList.get(0).getDescription()));
  }

  @Test
  void getProductsByParameterDescription() {
    Map<String, String> map = new HashMap<>();
    map.put("description", Constants.DESCRIPTION);
    productList = productService.getProductsByParameter(productList, map);
    assertTrue(productsExpected.get(0).getId() == productList.get(0).getId());
    assertTrue(productsExpected.get(0).getName().equals(productList.get(0).getName()));
    assertTrue(
        productsExpected.get(0).getDescription().equals(productList.get(0).getDescription()));
  }

  @Test
  void addProduct() {
    product.setId(Constants.ID_CAMISETA_ADD);
    productList = productService.addProduct(productList, product);
    assertTrue(productsExpected.get(0).getId() == productList.get(productList.size() - 1).getId());
  }
}

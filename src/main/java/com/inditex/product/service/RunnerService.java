package com.inditex.product.service;

import com.inditex.product.entity.Product;
import com.inditex.product.repository.RunnerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RunnerService implements RunnerRepository {

  public List<Product> products;

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
}

package com.inditex.product.service;

import com.inditex.product.dto.ProductDTO;
import com.inditex.product.entity.Product;
import com.inditex.product.event.AuditEventPublisher;
import com.inditex.product.repository.ProductRepository;
import com.inditex.product.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService implements ProductRepository {

  @Autowired private AuditEventPublisher auditEventPublisher;
  private List<Product> productList;
  private List<Product> productListAux;
  private Utils utils;

  @Override
  public List<Product> getProductsByParameter(List<Product> products, Map<String, String> params) {

    productList = new ArrayList<>();
    productListAux = new ArrayList<>();
    utils = new Utils();

    if (params.containsKey("id")
        || params.containsKey("name")
        || params.containsKey("description")) {

      if (params.containsKey("id")) {
        productList =
            products.stream()
                .filter(p -> String.valueOf(p.getId()).equals(params.get("id")))
                .collect(Collectors.toList());
      }
      if (params.containsKey("name")) {
        productListAux =
            products.stream()
                .filter(p -> p.getName().contains(params.get("name")))
                .collect(Collectors.toList());

        productList = utils.concatListProducts(productList, productListAux);
      }
      if (params.containsKey("description")) {
        productListAux =
            products.stream()
                .filter(p -> p.getDescription().contains(params.get("description")))
                .collect(Collectors.toList());

        productList = utils.concatListProducts(productList, productListAux);
      }
      return productList;
    }
    return null;
  }

  @Override
  public List<Product> addProduct(List<Product> products, Product product) {
    products.add(product);
    return products;
  }

  @Override
  public List<Product> addProductAsync(List<Product> products, Product product) {
    products.add(product);
    // Event listener publish event to send security information
    auditEventPublisher.publishEvent("Product ".concat(product.getName()).concat(" saved"));
    return products;
  }

  @Override
  public List<Product> updateProduct(List<Product> products, int idProduct, ProductDTO product) {
    products.stream()
        .filter(p -> p.getId() == idProduct)
        .forEach(
            p -> {
              p.setName(product.getName());
              p.setDescription(product.getDescription());
            });
    return products;
  }

  @Override
  public List<Product> deleteProduct(List<Product> products, int idProduct) {
    products.removeIf(p -> p.getId() == idProduct);
    return products;
  }

  @Override
  public boolean findProduct(List<Product> products, Product product) {
    return products.stream()
        .anyMatch(p -> p.getId() == product.getId() || p.getName().equals(product.getName()));
  }

  @Override
  public boolean findProductById(List<Product> products, int idProduct) {
    return products.stream().anyMatch(p -> p.getId() == idProduct);
  }

  @Override
  public boolean findByProductName(List<Product> products, ProductDTO product) {
    return products.stream().anyMatch(p -> p.getName().equals(product.getName()));
  }
}

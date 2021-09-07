package com.inditex.product.repository;

import com.inditex.product.dto.ProductDTO;
import com.inditex.product.entity.Product;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public interface ProductRepository {

  List<Product> getProductsByParameter(List<Product> productList, Map<String, String> params);

  List<Product> addProduct(List<Product> productList, Product product);

  List<Product> addProductAsync(List<Product> productList, Product product);

  List<Product> updateProduct(List<Product> productList, int idProduct, ProductDTO product);

  List<Product> deleteProduct(List<Product> productList, int idProduct);

  boolean findProduct(List<Product> productList, Product product);

  boolean findProductById(List<Product> productList, int idProduct);

  boolean findByProductName(List<Product> productList, ProductDTO product);
}

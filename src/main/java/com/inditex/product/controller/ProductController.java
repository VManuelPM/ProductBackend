package com.inditex.product.controller;

import com.inditex.product.dto.MessageDTO;
import com.inditex.product.dto.ProductDTO;
import com.inditex.product.entity.Product;
import com.inditex.product.service.ProductService;
import com.inditex.product.service.RunnerService;
import com.inditex.product.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired private ProductService productService;
  @Autowired private RunnerService runnerService;

  private List<Product> productList;
  private Utils utils;
  private Set<ConstraintViolation<Product>> violations;
  private Set<ConstraintViolation<ProductDTO>> violationsDto;

  @PostConstruct
  public void load() {
    utils = new Utils();
    productList = runnerService.createObjects();
  }

  @GetMapping("")
  public ResponseEntity<List<Product>> getProducts() {
    return new ResponseEntity<>(productList, HttpStatus.OK);
  }

  @GetMapping("/search")
  public ResponseEntity<?> getProductsByParameter(@RequestParam Map<String, String> params) {

    List<Product> products = productService.getProductsByParameter(productList, params);
    if (products != null) {
      return new ResponseEntity(products, HttpStatus.OK);
    }
    return new ResponseEntity(new MessageDTO("Products not found"), HttpStatus.NOT_FOUND);

  }

  @PostMapping("")
  public ResponseEntity<?> saveProduct(@Valid @RequestBody Product product) {

    boolean pExists = productService.findProduct(productList, product);
    if (pExists)
      return new ResponseEntity(new MessageDTO("Product already exists"), HttpStatus.CONFLICT);
    productList = productService.addProduct(productList, product);
    return new ResponseEntity<>(
            new MessageDTO("Product ".concat(product.getName()).concat(" added")), HttpStatus.CREATED);
  }

  @PostMapping("/async")
  public ResponseEntity<?> saveProductAsync(@Valid @RequestBody Product product) {

    boolean pExists = productService.findProduct(productList, product);
    if (pExists)
      return new ResponseEntity(new MessageDTO("Product already exists"), HttpStatus.CONFLICT);
    productList = productService.addProductAsync(productList, product);
    return new ResponseEntity<>(
        new MessageDTO("Product ".concat(product.getName()).concat(" added")), HttpStatus.CREATED);
  }

  @PutMapping("/{idProduct}")
  public ResponseEntity<?> updateProduct(
      @PathVariable("idProduct") int idProduct, @Valid @RequestBody ProductDTO product) {

    boolean pExists = productService.findProductById(productList, idProduct);
    boolean pNameExists = productService.findByProductName(productList, product);
    if (!pExists)
      return new ResponseEntity(new MessageDTO("Product not found"), HttpStatus.NOT_FOUND);
    if (pNameExists)
      return new ResponseEntity(
          new MessageDTO(
              "Product with the name ".concat(product.getName()).concat(" already exists")),
          HttpStatus.CONFLICT);
    productList = productService.updateProduct(productList, idProduct, product);
    return new ResponseEntity(
        new MessageDTO("Product has been successfully updated"), HttpStatus.OK);
  }

  @DeleteMapping("/{idProduct}")
  public ResponseEntity<?> deleteProduct(@PathVariable("idProduct") int idProduct) {

    boolean pExists = productService.findProductById(productList, idProduct);
    if (!pExists)
      return new ResponseEntity(new MessageDTO("Product not found"), HttpStatus.NOT_FOUND);
    productList = productService.deleteProduct(productList, idProduct);
    return new ResponseEntity(
        new MessageDTO("Product has been successfully deleted"), HttpStatus.OK);
  }
}

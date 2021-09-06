package com.inditex.product.controller;

import com.inditex.product.dto.MensajeDTO;
import com.inditex.product.dto.ProductDTO;
import com.inditex.product.entity.Product;
import com.inditex.product.event.AuditEventPublisher;
import com.inditex.product.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductController {

    private List<Product> productList;
    private Utils utils;
    private Set<ConstraintViolation<Product>> violations;
    private Set<ConstraintViolation<ProductDTO>> violationsDto;

    @Autowired
    private AuditEventPublisher auditEventPublisher;

    public ProductController() {
        utils = new Utils();
        productList = utils.createObjects();
    }

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getProducts() {
        return new ResponseEntity<List<Product>>(productList, HttpStatus.OK);
    }

    @GetMapping("/getBy/{param}")
    public ResponseEntity<?> getProductsByParameter(@PathVariable("param") String param) {
        List<Product> products = productList.stream()
                .filter(p -> String.valueOf(p.getId()).equals(param)
                        || p.getName().contains(param)
                        || p.getDescription().equals(param)).collect(Collectors.toList());
        return new ResponseEntity(products, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody Product product) {
        boolean pExists = productList.stream()
                .anyMatch(p -> p.getId() == product.getId()
                        || p.getName().equals(product.getName()));
        if (pExists)
            return new ResponseEntity(new MensajeDTO("Product already exists"), HttpStatus.CONFLICT);
        productList.add(product);
        //Event listener publish event to send security information
        auditEventPublisher.publishEvent("Product ".concat(product.getName()).concat(" saved"));
        return new ResponseEntity<>(new MensajeDTO("Product ".concat(product.getName()).concat(" added")), HttpStatus.CREATED);
    }

    @PutMapping("/update/{idProduct}")
    public ResponseEntity<?> updateProduct(@PathVariable("idProduct") int idProduct, @Valid @RequestBody ProductDTO product) {
        boolean pExists = utils.pExists(productList, idProduct);

        boolean pNameExists = productList.stream()
                .anyMatch(p -> p.getName().equals(product.getName()));

        if (!pExists)
            return new ResponseEntity(new MensajeDTO("Product not found"), HttpStatus.NOT_FOUND);

        if (pNameExists)
            return new ResponseEntity(new MensajeDTO("Product with the name "
                    .concat(product.getName())
                    .concat(" already exists")), HttpStatus.CONFLICT);

        productList.stream().
                filter(p -> p.getId() == idProduct)
                .forEach(p -> {
                    p.setName(product.getName());
                    p.setDescription(product.getDescription());
                });

        return new ResponseEntity(new MensajeDTO("Product has been successfully updated"), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{idProduct}")
    public ResponseEntity<?> deleteProduct(@PathVariable("idProduct") int idProduct) {
        boolean pExists = utils.pExists(productList, idProduct);
        if (!pExists)
            return new ResponseEntity(new MensajeDTO("Product not found"), HttpStatus.NOT_FOUND);

        productList.removeIf(p -> p.getId() == idProduct);
        return new ResponseEntity(new MensajeDTO("Product has been successfully deleted"), HttpStatus.OK);
    }


}

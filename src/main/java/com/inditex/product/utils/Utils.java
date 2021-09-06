package com.inditex.product.utils;

import com.inditex.product.dto.MensajeDTO;
import com.inditex.product.entity.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    private List<Product> products;

    public List<Product> createObjects() {
        products = new ArrayList<>();
        Product product;
        for (int i = 0; i < 5; i++) {
            product = new Product();
            product.setId(i);
            product.setName("Camiseta número" + i);
            product.setDescription("Camiseta con estampado del número " + i);
            products.add(product);
        }
        return products;
    }

    public boolean pExists(List<Product> productList, int idProduct){
        boolean pExists = productList.stream()
                .anyMatch(p -> p.getId() == idProduct);
        if(pExists){
            return true;
        }
        return false;
    }
}

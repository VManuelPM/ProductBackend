package com.inditex.product.repository;

import com.inditex.product.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RunnerRepository {

    List<Product> createObjects();

}

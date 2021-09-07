package com.inditex.product.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductDTOTest {

  private ProductDTO productDTO;

  @BeforeEach
  void setUp() {
    productDTO = new ProductDTO();
  }

  @Test
  public void testGetAndSetName() {
    productDTO.setName("test");
    assertEquals("test", productDTO.getName());
  }

  @Test
  public void testGetAndSetDescription() {
    productDTO.setDescription("test");
    assertEquals("test", productDTO.getDescription());
  }
}

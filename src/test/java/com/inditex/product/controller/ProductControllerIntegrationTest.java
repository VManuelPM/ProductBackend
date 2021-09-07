package com.inditex.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inditex.product.dto.ProductDTO;
import com.inditex.product.entity.Product;
import com.inditex.product.event.AuditEvent;
import com.inditex.product.event.AuditEventPublisher;
import com.inditex.product.service.ProductService;
import com.inditex.product.service.RunnerService;
import com.inditex.product.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerIntegrationTest {

  @Autowired private MockMvc mockMvc;

  @Mock private AuditEventPublisher auditEventPublisher;

  @Mock private ProductService productService;

  @Mock private RunnerService runnerService;

  @InjectMocks private ProductController productController;

  private JacksonTester<Product> productJacksonTester;
  private JacksonTester<ProductDTO> productDTOJacksonTester;
  private Product product;
  private ProductDTO productDTO;

  @TestConfiguration
  static class ProductControllerTestContextConfiguration {

    @Bean
    public AuditEventPublisher auditEventPublisher() {
      return new AuditEventPublisher();
    }

    @Bean
    public ProductService productService() {
      return new ProductService();
    }

    @Bean
    public RunnerService runnerService() {
      return new RunnerService();
    }
  }

  @BeforeEach
  void setUp() {
    JacksonTester.initFields(this, new ObjectMapper());
    product = new Product();
    product.setId(Constants.ID_CAMISETA);
    product.setName(Constants.NAME);
    product.setDescription(Constants.DESCRIPTION);

    productDTO = new ProductDTO();
    productDTO.setName(Constants.NAME);
    productDTO.setDescription(Constants.DESCRIPTION);
  }

  @Test
  void getProducts() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.get("/products");
    MvcResult result = mockMvc.perform(request).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void getProductsByParameter() throws Exception {
    product.setId(Constants.ID_CAMISETA_UPDATE);
    product.setName(Constants.NAME_UPDATE);
    RequestBuilder request =
        MockMvcRequestBuilders.get("/products/search?id=" + Constants.ID_CAMISETA_UPDATE);
    MvcResult result = mockMvc.perform(request).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    assertThat(result.getResponse().getContentAsString(StandardCharsets.UTF_8))
        .isEqualTo("[" + productJacksonTester.write(product).getJson() + "]");
  }

  @Test
  void saveProduct() throws Exception {
    RequestBuilder request =
        MockMvcRequestBuilders.post("/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(productJacksonTester.write(product).getJson());
    MvcResult result = mockMvc.perform(request).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  void saveProductAsync() throws Exception {
    product.setId(Constants.ID_CAMISETA_ADD_ASYNC);
    product.setName(Constants.NAME_ASYNC);
    RequestBuilder request =
        MockMvcRequestBuilders.post("/products/async")
            .contentType(MediaType.APPLICATION_JSON)
            .content(productJacksonTester.write(product).getJson());
    MvcResult result = mockMvc.perform(request).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());
  }

  @Test
  void updateProduct() throws Exception {
    productDTO.setName(Constants.NAME_UPDATE);
    RequestBuilder request =
        MockMvcRequestBuilders.put("/products/" + Constants.ID_CAMISETA_UPDATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(productDTOJacksonTester.write(productDTO).getJson());
    MvcResult result = mockMvc.perform(request).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
  }

  @Test
  void updateProductNotFound() throws Exception {
    RequestBuilder request =
        MockMvcRequestBuilders.put("/products/" + Constants.ID_CAMISETA_ADD)
            .contentType(MediaType.APPLICATION_JSON)
            .content(productDTOJacksonTester.write(productDTO).getJson());
    MvcResult result = mockMvc.perform(request).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
  }

  @Test
  void updateProductNameExists() throws Exception {
    productDTO.setName(Constants.NAME_UPDATE);
    RequestBuilder request =
        MockMvcRequestBuilders.put("/products/" + Constants.ID_CAMISETA_UPDATE)
            .contentType(MediaType.APPLICATION_JSON)
            .content(productDTOJacksonTester.write(productDTO).getJson());
    MvcResult result = mockMvc.perform(request).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
  }

  @Test
  void deleteProduct() throws Exception {
    RequestBuilder request = MockMvcRequestBuilders.delete("/products/0");
    MvcResult result = mockMvc.perform(request).andReturn();
    assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
  }
}

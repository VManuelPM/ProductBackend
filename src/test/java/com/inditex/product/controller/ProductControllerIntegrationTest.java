package com.inditex.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inditex.product.dto.ProductDTO;
import com.inditex.product.entity.Product;
import com.inditex.product.event.AuditEvent;
import com.inditex.product.event.AuditEventPublisher;
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

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuditEventPublisher auditEventPublisher;

    @InjectMocks
    private ProductController productController;

    private JacksonTester<Product> productJacksonTester;
    private JacksonTester<ProductDTO> productDTOJacksonTester;

    @TestConfiguration
    static class ProductControllerTestContextConfiguration {

        @Bean
        public AuditEventPublisher auditEventPublisher() {
            return new AuditEventPublisher();
        }
    }


    @BeforeEach
    void setUp() {
        JacksonTester.initFields(this, new ObjectMapper());

    }

    @Test
    void getProducts() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.get("/products/all");
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void getProductsByParameter() throws Exception {
        Product product = new Product();
        product.setId(0);
        product.setName("Camiseta");
        product.setDescription("Camiseta test");
        RequestBuilder request = MockMvcRequestBuilders.get("/products/getBy/0");
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(
               "[" + productJacksonTester.write(product).getJson() +"]"
        );
    }

    @Test
    void saveProduct() throws Exception {
        Product product = new Product();
        product.setId(150);
        product.setName("camiseta");
        product.setDescription("camiseta test");
        RequestBuilder request = MockMvcRequestBuilders.post("/products/add").contentType(MediaType.APPLICATION_JSON).content(productJacksonTester.write(product).getJson());
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CREATED.value());

    }

    @Test
    void updateProduct() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Camiseta");
        productDTO.setDescription("Camiseta test");
        RequestBuilder request = MockMvcRequestBuilders.put("/products/update/0").contentType(MediaType.APPLICATION_JSON).content(productDTOJacksonTester.write(productDTO).getJson());
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    void updateProductNotFound() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Camiseta");
        productDTO.setDescription("Camiseta test");
        RequestBuilder request = MockMvcRequestBuilders.put("/products/update/100").contentType(MediaType.APPLICATION_JSON).content(productDTOJacksonTester.write(productDTO).getJson());
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateProductNameExists() throws Exception {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Camiseta");
        productDTO.setDescription("Camiseta test");
        RequestBuilder request = MockMvcRequestBuilders.put("/products/update/0").contentType(MediaType.APPLICATION_JSON).content(productDTOJacksonTester.write(productDTO).getJson());
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    @Test
    void deleteProduct() throws Exception {
        RequestBuilder request = MockMvcRequestBuilders.delete("/products/delete/0");
        MvcResult result = mockMvc.perform(request).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(HttpStatus.OK.value());
    }
}
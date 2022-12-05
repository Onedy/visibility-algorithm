package com.inditex.visibilityalgorithm.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inditex.visibilityalgorithm.core.entity.Product;
import com.inditex.visibilityalgorithm.core.entity.Size;
import com.inditex.visibilityalgorithm.core.repository.ProductRepository;
import com.inditex.visibilityalgorithm.core.repository.SizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.inditex.visibilityalgorithm.controller.ResourcesUtils.readResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ShopIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void init() {
        productRepository.deleteAll();
        sizeRepository.deleteAll();
    }

    @Test
    void shouldRetrieveProducts_whenGetToProductsEndpoint() throws Exception {
        initDb();

        mockMvc.perform(get("/shop/product")
                .header("version", 1))
            .andExpect(status().isOk());
    }

    @Test
    void shouldCreateProduct_whenSizeExists() throws Exception {
        Size savedSize = sizeRepository.save(Size.builder()
            .quantity(5)
            .backSoon(true)
            .special(false)
            .build());
        Product productToSave = Product.builder()
            .sequence(5)
            .sizes(List.of(savedSize))
            .build();

        MvcResult result = mockMvc.perform(post("/shop/product")
                .header("version", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(productToSave)))
            .andExpect(status().isCreated())
            .andReturn();

        var returnedProduct = objectMapper.readValue(result.getResponse().getContentAsString(), com.inditex.visibilityalgorithm.dto.out.Product.class);

        assertProductWithAllSizesIsSaved(returnedProduct, 1);
    }

    @Test
    void shouldCreateProduct_whenOnlySomeSizesExist() throws Exception {
        Size savedSize = sizeRepository.save(Size.builder()
            .quantity(5)
            .backSoon(true)
            .special(false)
            .build());
        Product productToSave = Product.builder()
            .sequence(5)
            .sizes(List.of(savedSize, Size.builder()
                .quantity(2)
                .backSoon(false)
                .special(true)
                .build()))
            .build();

        MvcResult result = mockMvc.perform(post("/shop/product")
                .header("version", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(productToSave)))
            .andExpect(status().isCreated())
            .andReturn();

        var returnedProduct = objectMapper.readValue(result.getResponse().getContentAsString(), com.inditex.visibilityalgorithm.dto.out.Product.class);

        assertProductWithAllSizesIsSaved(returnedProduct, 2);
    }

    @Test
    void shouldCreateProductAndSizes_whenNeitherProductNorSizeExist() throws Exception {
        MvcResult result = mockMvc.perform(post("/shop/product")
                .header("version", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(readResource("integration-tests/new-product-with-new-size.json")))
            .andExpect(status().isCreated())
            .andReturn();

        var returnedProduct = objectMapper.readValue(result.getResponse().getContentAsString(), com.inditex.visibilityalgorithm.dto.out.Product.class);

        assertProductWithAllSizesIsSaved(returnedProduct, 1);
    }

    @Test
    void shouldReturnNotFound_whenProductDoesntExist() throws Exception {
        mockMvc.perform(get("/shop/product/848348648912")
                .header("version", 1))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest_whenBodyIsNotValid() throws Exception {
        mockMvc.perform(post("/shop/product")
                .header("version", 1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(readResource("integration-tests/product-with-not-valid-fields.json")))
            .andExpect(status().isBadRequest())
            .andExpect(content().json("[\"sequence must be greater than or equal to 0\"," +
                "\"sizes[0].quantity must be greater than or equal to 0\"]"));
    }

    private void assertProductWithAllSizesIsSaved(com.inditex.visibilityalgorithm.dto.out.Product returnedProduct, int expectedSizesLength) {
        assertThat(returnedProduct).isNotNull();
        assertThat(returnedProduct.id()).isNotNull();
        assertThat(returnedProduct.sizes())
            .hasSize(expectedSizesLength)
            .extracting("id").isNotNull();
    }

    private void initDb() {
        Size size1 = sizeRepository.save(Size.builder()
            .id(1L)
            .quantity(5)
            .backSoon(true)
            .special(false)
            .build());
        Size size2 = sizeRepository.save(Size.builder()
            .id(2L)
            .quantity(3)
            .backSoon(false)
            .special(true)
            .build());
        productRepository.saveAll(List.of(Product.builder()
                .id(1L)
                .sequence(2)
                .sizes(List.of(size1))
                .build(),
            Product.builder()
                .id(2L)
                .sequence(4)
                .sizes(List.of(size2))
                .build()));
    }
}
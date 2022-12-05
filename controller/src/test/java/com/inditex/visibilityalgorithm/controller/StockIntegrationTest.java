package com.inditex.visibilityalgorithm.controller;

import com.inditex.visibilityalgorithm.core.entity.Product;
import com.inditex.visibilityalgorithm.core.entity.Size;
import com.inditex.visibilityalgorithm.core.repository.ProductRepository;
import com.inditex.visibilityalgorithm.core.repository.SizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class StockIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SizeRepository sizeRepository;

    @BeforeEach
    void init() {
        productRepository.deleteAll();
    }

    @Test
    void shouldRetrieveProducts_whenGetToProductsEndpoint() throws Exception {
        initDb();

        mockMvc.perform(get("/stock/products")
                .header("version", 1))
            .andExpect(status().isOk());
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
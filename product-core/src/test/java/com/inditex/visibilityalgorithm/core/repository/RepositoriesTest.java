package com.inditex.visibilityalgorithm.core.repository;

import com.inditex.visibilityalgorithm.core.entity.Product;
import com.inditex.visibilityalgorithm.core.entity.Size;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void init() {
        productRepository.deleteAll();
    }

    @Test
    void shouldAutowireAndRetrieveProduct_whenSavingOne() {
        Product newProduct = Product.builder()
            .sequence(2)
            .sizes(List.of(Size.builder()
                .quantity(5)
                .backSoon(true)
                .special(false)
                .build()))
            .build();

        Product createdProduct = productRepository.save(newProduct);

        Optional<Product> foundProduct = productRepository.findById(createdProduct.getId());

        assertThat(foundProduct)
            .get()
            .extracting("id")
            .isNotNull();
        assertThat(foundProduct.get().getSizes())
            .hasSize(1);
    }
}

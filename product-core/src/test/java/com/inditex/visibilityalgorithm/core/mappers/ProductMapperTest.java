package com.inditex.visibilityalgorithm.core.mappers;

import com.inditex.visibilityalgorithm.core.entity.Product;
import com.inditex.visibilityalgorithm.core.entity.Size;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProductMapperTest {

    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Test
    void shouldMapEntitytoDto() {
        Product productEntity = Product.builder()
            .id(1L)
            .sequence(2)
            .sizes(List.of(Size.builder()
                .id(1L)
                .quantity(5)
                .backSoon(true)
                .special(false)
                .build()))
            .build();

        com.inditex.visibilityalgorithm.dto.out.Product productDto = mapper.toDto(productEntity);

        assertThat(productDto)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("sequence", 2);
        assertThat(productDto.sizes()).hasSize(1);
        assertThat(productDto.sizes().get(0))
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("quantity", 5)
            .hasFieldOrPropertyWithValue("backSoon", true)
            .hasFieldOrPropertyWithValue("special", false);
    }
}
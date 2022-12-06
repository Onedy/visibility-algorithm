package com.inditex.visibilityalgorithm.core.service;

import com.inditex.visibilityalgorithm.core.entity.Product;
import com.inditex.visibilityalgorithm.core.mappers.ProductMapper;
import com.inditex.visibilityalgorithm.core.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductCoreImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductCoreImpl productCoreImpl;

    @Test
    void findAll() {
        Pageable pageable = Pageable.ofSize(10);
        when(productRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(mock(Product.class),
            mock(Product.class))));

        var product1Dto = com.inditex.visibilityalgorithm.dto.out.Product.builder()
            .id(1L)
            .sequence(2)
            .sizes(List.of(com.inditex.visibilityalgorithm.dto.out.Size.builder()
                .id(1L)
                .quantity(5)
                .backSoon(true)
                .special(false)
                .build()))
            .build();
        var product2Dto = com.inditex.visibilityalgorithm.dto.out.Product.builder()
            .id(2L)
            .sequence(4)
            .sizes(List.of(com.inditex.visibilityalgorithm.dto.out.Size.builder()
                .id(4L)
                .quantity(2)
                .backSoon(false)
                .special(true)
                .build()))
            .build();
        when(productMapper.toDto(any(Product.class))).thenReturn(product1Dto, product2Dto);

        assertThat(productCoreImpl.findAll(pageable))
            .hasSize(2);
    }
}
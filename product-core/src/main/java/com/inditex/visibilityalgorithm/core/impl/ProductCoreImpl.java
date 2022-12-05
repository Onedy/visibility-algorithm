package com.inditex.visibilityalgorithm.core.impl;

import com.inditex.visibilityalgorithm.core.AbstractProductCore;
import com.inditex.visibilityalgorithm.core.Implementation;
import com.inditex.visibilityalgorithm.core.mappers.ProductMapper;
import com.inditex.visibilityalgorithm.core.repository.ProductRepository;
import com.inditex.visibilityalgorithm.dto.out.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
@Implementation(version = 1)
public class ProductCoreImpl extends AbstractProductCore {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(productMapper::toDto);
    }
}

package com.inditex.visibilityalgorithm.core.impl;

import com.inditex.visibilityalgorithm.core.AbstractProductCore;
import com.inditex.visibilityalgorithm.core.Implementation;
import com.inditex.visibilityalgorithm.core.entity.Size;
import com.inditex.visibilityalgorithm.core.exception.ProductNotFoundException;
import com.inditex.visibilityalgorithm.core.mappers.ProductMapper;
import com.inditex.visibilityalgorithm.core.repository.ProductRepository;
import com.inditex.visibilityalgorithm.core.repository.SizeRepository;
import com.inditex.visibilityalgorithm.dto.out.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Implementation(version = 1)
public class ProductCoreImpl extends AbstractProductCore {
    private final ProductRepository productRepository;
    private final SizeRepository sizeRepository;
    private final ProductMapper productMapper;

    @Override
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
            .map(productMapper::toDto);
    }

    @Override
    @Transactional
    public Product create(Product product) {
        var productEntity = productMapper.toEntity(product);
        ensureSizesAreInDB(productEntity);
        var savedProduct = productRepository.save(productEntity);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public Product findOne(Long id) {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        return productMapper.toDto(product);
    }

    private void ensureSizesAreInDB(com.inditex.visibilityalgorithm.core.entity.Product productEntity) {
        List<Size> sizesToSave = new ArrayList<>();
        productEntity.getSizes().removeIf(size -> {
            boolean shouldSaveFirst = size.getId() == null;
            if (shouldSaveFirst) {
                sizesToSave.add(size);
            }
            return shouldSaveFirst;
        });
        Iterable<Size> savedSizes = sizeRepository.saveAll(sizesToSave);
        savedSizes.forEach(size -> productEntity.getSizes().add(size));
    }
}

package com.inditex.visibilityalgorithm.core;

import com.inditex.visibilityalgorithm.dto.out.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCore {
    Page<Product> findAll(Pageable pageable);

    Product create(Product product);

    Product findOne(Long id);
}

package com.inditex.visibilityalgorithm.core.repository;

import com.inditex.visibilityalgorithm.core.entity.Product;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {
}

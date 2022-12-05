package com.inditex.visibilityalgorithm.core.mappers;

import com.inditex.visibilityalgorithm.dto.out.Product;
import org.mapstruct.Mapper;


@Mapper
public interface ProductMapper {
    Product toDto(com.inditex.visibilityalgorithm.core.entity.Product product);
    com.inditex.visibilityalgorithm.core.entity.Product toEntity(Product product);
}

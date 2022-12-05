package com.inditex.visibilityalgorithm.dto.out;

import lombok.Builder;

import java.util.List;

@Builder
public record Product(Long id, Integer sequence, List<Size> sizes) {
}

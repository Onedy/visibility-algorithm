package com.inditex.visibilityalgorithm.dto.out;

import lombok.Builder;

@Builder
public record Size(Long id, boolean backSoon, boolean special, Integer quantity) {
}

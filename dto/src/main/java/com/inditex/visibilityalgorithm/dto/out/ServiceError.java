package com.inditex.visibilityalgorithm.dto.out;

import lombok.Builder;

@Builder
public record ServiceError(String description) {
}

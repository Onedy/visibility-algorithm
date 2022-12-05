package com.inditex.visibilityalgorithm.dto.out;

import lombok.Builder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Builder
public record Size(Long id,
                   @NotNull Boolean backSoon,

                   @NotNull Boolean special,

                   @PositiveOrZero Integer quantity) {
}

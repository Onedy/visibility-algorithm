package com.inditex.visibilityalgorithm.dto.out;

import lombok.Builder;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Builder
public record Product(Long id,
                      @PositiveOrZero Integer sequence,
                      List<Size> sizes) {
}

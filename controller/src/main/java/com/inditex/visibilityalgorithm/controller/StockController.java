package com.inditex.visibilityalgorithm.controller;

import com.inditex.visibilityalgorithm.core.ProductCore;
import com.inditex.visibilityalgorithm.dto.out.Product;
import com.inditex.visibilityalgorithm.facade.Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockController {

    private final Facade<ProductCore> productFacade;

    @GetMapping("/products")
    public ResponseEntity<Page<Product>> findAllProducts(@RequestHeader Integer version, Pageable pageable) {
        return ResponseEntity.ok(productFacade.get(version).findAll(pageable));
    }
}

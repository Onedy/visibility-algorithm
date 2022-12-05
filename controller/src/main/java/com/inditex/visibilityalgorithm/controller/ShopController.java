package com.inditex.visibilityalgorithm.controller;

import com.inditex.visibilityalgorithm.core.ProductCore;
import com.inditex.visibilityalgorithm.dto.out.Product;
import com.inditex.visibilityalgorithm.facade.Facade;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
public class ShopController {

    private final Facade<ProductCore> productFacade;

    @GetMapping("/product")
    public ResponseEntity<Page<Product>> findAllProducts(@RequestHeader Integer version, Pageable pageable) {
        return ResponseEntity.ok(productFacade.get(version).findAll(pageable));
    }

    @PostMapping("/product")
    public ResponseEntity<Product> createProduct(@RequestHeader Integer version, @Valid @RequestBody Product product) {
        return new ResponseEntity<>(productFacade.get(version).create(product), HttpStatus.CREATED);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> findProduct(@RequestHeader Integer version, @PathVariable Long id) {
        return ResponseEntity.ok(productFacade.get(version).findOne(id));
    }
}

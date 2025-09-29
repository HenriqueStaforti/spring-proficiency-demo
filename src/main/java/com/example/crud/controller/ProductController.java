package com.example.crud.controller;

import com.example.crud.dto.ProductRequestDTO;
import com.example.crud.dto.ProductResponseDTO;
import com.example.crud.dto.ProductUpdateRequestDTO;
import com.example.crud.mapper.ProductMapper;
import com.example.crud.model.ProductEntity;
import com.example.crud.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public ProductController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> create(@RequestBody @Valid ProductRequestDTO data){
        ProductEntity productEntity = productService.create(productMapper.toEntity(data));
        return ResponseEntity.ok(productMapper.toResponseDto(productEntity));
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> list(Pageable pageable) {
        Page<ProductResponseDTO> page = productService.list(pageable).map(productMapper::toResponseDto);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> get(@PathVariable("id") Long productId){
        ProductEntity productEntity = productService.get(productId);
        return ResponseEntity.ok(productMapper.toResponseDto(productEntity));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> update(@PathVariable("id") Long productId, @RequestBody @Valid ProductUpdateRequestDTO data){
        ProductEntity productEntity = productService.update(productId, data);
        return ResponseEntity.ok(productMapper.toResponseDto(productEntity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long productId){
        productService.delete(productId);
        return ResponseEntity.ok().build();
    }
}

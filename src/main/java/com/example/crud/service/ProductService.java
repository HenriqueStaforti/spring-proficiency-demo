package com.example.crud.service;

import com.example.crud.dto.ProductUpdateRequestDTO;
import com.example.crud.exception.ResourceNotFoundException;
import com.example.crud.mapper.ProductMapper;
import com.example.crud.model.ProductEntity;
import com.example.crud.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional
    public ProductEntity create(ProductEntity product) {
        return productRepository.save(product);
    }

    public ProductEntity get(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    public Page<ProductEntity> list(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Transactional
    public ProductEntity update(Long id, ProductUpdateRequestDTO updateDto) {
        ProductEntity existing = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        productMapper.updateEntityFromUpdateDto(updateDto, existing);
        return productRepository.save(existing);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }
}

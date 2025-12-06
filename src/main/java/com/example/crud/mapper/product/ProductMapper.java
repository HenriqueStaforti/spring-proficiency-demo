package com.example.crud.mapper.product;

import com.example.crud.dto.product.ProductRequestDTO;
import com.example.crud.dto.product.ProductResponseDTO;
import com.example.crud.dto.product.ProductUpdateRequestDTO;
import com.example.crud.entity.product.ProductEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductEntity toEntity(ProductRequestDTO dto);

    ProductResponseDTO toResponseDto(ProductEntity product);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromUpdateDto(ProductUpdateRequestDTO dto, @MappingTarget ProductEntity product);

}

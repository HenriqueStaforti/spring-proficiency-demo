package com.example.crud.mapper;

import com.example.crud.dto.ProductRequestDTO;
import com.example.crud.dto.ProductResponseDTO;
import com.example.crud.dto.ProductUpdateRequestDTO;
import com.example.crud.model.ProductEntity;
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

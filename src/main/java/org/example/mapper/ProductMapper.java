package org.example.mapper;

import org.example.dto.category.CategoryCreateDTO;
import org.example.dto.category.CategoryItemDTO;
import org.example.dto.product.ProductItemDTO;
import org.example.model.CategoryEntity;
import org.example.model.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
//    @Mapping(target = "image", ignore = true)
//    CategoryEntity categoryEntityByCategoryCreateDTO(CategoryCreateDTO category);


    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    ProductItemDTO toDto(ProductEntity product);
            //List<ProductItemDTO> toDto(Iterable<ProductEntity> product);
    List<ProductItemDTO> toDto(List<ProductEntity> product);
}


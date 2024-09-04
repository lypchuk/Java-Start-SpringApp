package org.example.mapper;

import org.example.model.CategoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class CategoryMapper {
    @Mapper(target = "image", ignore = true)
    CategoryEntity
}


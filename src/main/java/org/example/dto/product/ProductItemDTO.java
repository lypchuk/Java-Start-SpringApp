package org.example.dto.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.dto.category.CategoryItemDTO;
import org.example.dto.image.ProductImageDto;
import org.example.model.CategoryEntity;
import org.example.model.ProductImageEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductItemDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime creationTime;
    private double price;
    private double discount;
    private Long categoryId;
    private String categoryName;
    private List<ProductImageDto> productImages;
}

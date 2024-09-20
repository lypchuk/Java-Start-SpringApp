package org.example.repo;


import org.example.model.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IImageRepository extends JpaRepository<ProductImageEntity, Long> {
}
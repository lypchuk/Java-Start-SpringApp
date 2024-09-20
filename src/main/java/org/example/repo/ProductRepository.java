package org.example.repo;

import org.example.model.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
//    Page<ProductEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
//    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) " +
//            // "AND LOWER(p.category.name) LIKE LOWER(CONCAT('%', :category, '%')) " +
//            "AND LOWER(p.category.name) IN :categories " +
//            "AND LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%'))")
//    Page<ProductEntity> searchProducts(
//            @Param("name") String name,
//            @Param("categories") String[] categories,
//            @Param("description") String description,
//            Pageable pageable);
}
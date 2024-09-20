package org.example.controllers.api;

import lombok.AllArgsConstructor;
import org.example.dto.product.ProductItemDTO;
import org.example.mapper.ProductMapper;
import org.example.model.ProductEntity;
import org.example.repo.ProductRepository;
import org.example.service.impl.ProductServiceImpl;
import org.example.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.example.service.ICategoryService;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/rest/product")
public class ProductRestController {
    private final ProductRepository productRepository;
    private final ProductServiceImpl productService;
    private final ProductMapper productMapper;
    private final StorageService storageService;

    @Autowired
    private ICategoryService service;




    @GetMapping(value = "/getAllProducts")
    //public ResponseEntity<List<ProductEntity>> getAllProducts(
    public ResponseEntity<List<ProductItemDTO>> getAllProducts(
    ) {
        //var categories = categoryMapper.toDto(categoryRepository.findAll());
        var products = productMapper.toDto(productRepository.findAll());
        return ResponseEntity.ok(products);
    }

//                            @GetMapping(value = "/getByIdProduct/{id}")
//                            public ResponseEntity<ProductEntity> getByIdProduct(@PathVariable("id") Long id)
//                            {
//                                //var product = productRepository.findById(Math.toIntExact(id)).filter(e->e.).get();
//                                var product = productRepository.findById(Math.toIntExact(id)).get();
//                                return ResponseEntity.ok(product);
//                            }

    @GetMapping("/get/{id}")
    public ResponseEntity<ProductItemDTO> getProductById(@PathVariable Long id) {
        try {
            ProductItemDTO productDto = productService.getProductById(id);
            return productDto != null ? ResponseEntity.ok().body(productDto)
                    : ResponseEntity.notFound().build();
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }




}
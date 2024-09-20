package org.example.service.impl;

import org.example.dto.product.ProductItemDTO;
import org.example.exception.ProductException;
import org.example.mapper.ProductMapper;
import org.example.model.PaginationResponse;
import org.example.model.ProductEntity;
import org.example.model.SearchData;
import org.example.repo.CategoryRepository;
import org.example.repo.ProductRepository;
import org.example.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductServiceImpl implements org.example.interfaces.IProductService {

    @Autowired
    private ProductRepository repo;
    @Autowired
    private StorageService storageService;
    @Autowired
    private CategoryRepository categoryRepo;
//    @Autowired
//    private IImageRepository imageRepo;
    @Autowired
    private ProductMapper mapper;

    @Override
    public PaginationResponse<ProductItemDTO> getProducts(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page, size, Sort.by("id"));
        Page<ProductEntity> productsPage = repo.findAll(pageRequest);
        Iterable<ProductItemDTO> categories = mapper.toDto(productsPage.getContent());
        return  new PaginationResponse<ProductItemDTO>(categories,productsPage.getTotalElements());
    }

//    @Override
//    public PaginationResponse<ProductItemDTO> searchProducts(SearchData searchData) {
//        Sort.Direction direction = Objects.equals(searchData.getSortDir(), "descend") ? Sort.Direction.DESC: Sort.Direction.ASC;
//        PageRequest pageRequest = PageRequest.of(
//                searchData.getPage()-1, searchData.getSize(), Sort.by(direction,searchData.getSort()));
//        Page<ProductEntity> productsPage = repo.searchProducts(searchData.getName(),searchData.getCategories(),searchData.getDescription(),pageRequest);
//        Iterable<ProductItemDTO> products = mapper.toDto(productsPage.getContent());
//        return  new PaginationResponse<ProductItemDTO>(products,productsPage.getTotalElements());
//    }

    @Override
    public ProductItemDTO getProductById(Long id) {
        Optional<ProductEntity> product = repo.findById(Math.toIntExact(id));
        if(product.isPresent()){
            return mapper.toDto(product.get());
        }
        else{
            throw new ProductException("Invalid Product id");
        }
    }

    @Override
    public boolean deleteProductById(Long id) throws IOException {
//        Optional<Product> optCategory =  repo.findById(id);
//        boolean isPresent = optCategory.isPresent();
//        if(isPresent){
//            Product product = optCategory.get();
//            repo.delete(product);
//            storageService.deleteImages(product.getImages()
//                    .stream()
//                    .map(ProductImage::getName)
//                    .toList());
//        }
        return  false;
    }
}

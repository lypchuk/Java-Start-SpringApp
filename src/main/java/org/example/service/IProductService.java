package org.example.interfaces;


import org.example.dto.product.ProductItemDTO;

import org.example.model.PaginationResponse;
import org.example.model.SearchData;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public interface IProductService {

    PaginationResponse<ProductItemDTO> getProducts(int page, int size);
    //PaginationResponse<ProductItemDTO> searchProducts(SearchData searchData);
    ProductItemDTO getProductById(Long id);
    boolean deleteProductById(Long id) throws IOException;
    //boolean updateProduct(ProductCreationModel productModel) throws IOException;
}
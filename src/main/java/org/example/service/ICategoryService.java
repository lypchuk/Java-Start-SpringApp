package org.example.service;

import org.example.dto.category.CategoryCreateDTO;
import org.example.model.CategoryEntity;
import org.example.model.Invoice;
import org.example.model.InvoiceCreateModel;

import java.util.List;

public interface ICategoryService {
    public List<CategoryEntity> getAllCategory();
    public CategoryEntity getCategoryById(int id);
    public void deleteCategoryById(int id);

}

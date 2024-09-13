package org.example.service.impl;

import org.example.dto.category.CategoryCreateDTO;
import org.example.exception.InvoiceNotFoundException;
import org.example.model.CategoryEntity;
import org.example.model.Invoice;
import org.example.repo.CategoryRepository;
import org.example.storage.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.service.ICategoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository repo;

    @Autowired
    private StorageService storageService;

    @Override
    public List<CategoryEntity> getAllCategory()  {
        return repo.findAll();
    }

    @Override
    public CategoryEntity getCategoryById(int id)  {
        //var item = repo.findById(id).stream().findFirst();
        Optional<CategoryEntity> item = repo.findById(id);
        return item.orElse(null);
    }

    @Override
    public void deleteCategoryById(int id) {
        try {
            Optional<CategoryEntity> opt = repo.findById(id);
            if(opt.isPresent()) {
                storageService.delete(opt.get().getImage());
            } else {
                throw new InvoiceNotFoundException("Invoice with Id : "+id+" Not Found");
            }
        }
        catch (Exception ex) {
            //ex.getMessage();
        }
        repo.deleteById(id);
    }


}

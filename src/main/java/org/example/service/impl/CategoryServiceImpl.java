package org.example.service.impl;

import org.example.model.CategoryEntity;
import org.example.repo.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.service.ICategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository repo;

    @Override
    public List<CategoryEntity> getAllCategory()  {
        return repo.findAll();
    }
}

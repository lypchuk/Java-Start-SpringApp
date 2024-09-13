package org.example.controllers.api;

import lombok.AllArgsConstructor;
import org.example.exception.CategoryNotFoundException;
import org.example.exception.InvoiceNotFoundException;
import org.example.mapper.CategoryMapper;
import org.example.model.CategoryEntity;
import org.example.model.CategoryUpdateModel;
import org.example.repo.CategoryRepository;
import org.example.storage.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.dto.category.CategoryCreateDTO;
import org.example.service.FileSaveFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.example.service.ICategoryService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("api/rest/category")
public class CategoryRestController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final StorageService storageService;

    @Autowired
    private ICategoryService service;

    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Integer> create(@ModelAttribute CategoryCreateDTO dto) {
        //public String create(@ModelAttribute CategoryCreateDTO dto) {
        try {
            var entity = categoryMapper.categoryEntityByCategoryCreateDTO(dto);
            entity.setCreationTime(LocalDateTime.now());
            String fileName = storageService.saveImages(dto.getFile(), FileSaveFormat.WEBP);
            //String fileName = storageService.saveImage(dto.getImage(), FileSaveFormat.WEBP);
            entity.setImage(fileName);
            categoryRepository.save(entity);
//            var result = categoryMapper.categoryItemDTO(entity); /////////////////////////
            return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
            //return "redirect:category/getAllCategories";
        }
        catch (Exception ex) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            //return ex.getMessage();
        }
    }


    @GetMapping(value = "/getAllCategories")
    public List<CategoryEntity> getAllCategories(
    ) {
        List<CategoryEntity> categories= service.getAllCategory();
        return categories;
    }

    @GetMapping(value = "/getByIdCategory/{id}")
    public CategoryEntity getByIdCategory(@PathVariable("id") int id)
    {
        CategoryEntity item= service.getCategoryById(id);
        return item;
    }


    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(
            @PathVariable("id") int id
            //@RequestParam("id") int id
    ) {
        try {
            CategoryEntity item = categoryRepository.findById(id).get();
            storageService.deleteImages(item.getImage());
            service.deleteCategoryById(id);
                } catch (CategoryNotFoundException e) {
                    return new ResponseEntity<String>("result bad result",
                            HttpStatus.BAD_REQUEST);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
        return new ResponseEntity<String>("result successful result",
                HttpStatus.OK);
    }


    @PatchMapping("/update/{id}")
    public CategoryEntity updateCategory(
            @ModelAttribute CategoryUpdateModel categoryUp,
            @PathVariable int id
    ) throws IOException {
        CategoryEntity item = categoryRepository.findById(id).get();

        if(item == null)
        {
            return new CategoryEntity();
        }

        if(categoryUp.getName() != null)
        {
            item.setName(categoryUp.getName());
        }

        if(categoryUp.getDescription() != null)
        {
            item.setDescription(categoryUp.getDescription());
        }

        if(categoryUp.getFile() != null)
        {
            String imageName = storageService.updateFiles(item.getImage(),
                    categoryUp.getFile(),FileSaveFormat.WEBP);
            item.setImage(imageName);
        }
        return categoryRepository.save(item);
    }


    @GetMapping("/edit")
    public CategoryEntity getEditPage(
            Model model,
            @RequestParam int id
    ) {
        String page = null;
        try {
            CategoryEntity category = service.getCategoryById(id);
            return category;
        } catch (Exception e) {
            return null;
        }
    }

    @GetMapping("/addCategory")
    public String showRegistration() {
        return "addCategoryPage";
    }


}
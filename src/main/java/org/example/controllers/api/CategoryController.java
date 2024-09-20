package org.example.controllers.api;

import lombok.AllArgsConstructor;
import org.example.mapper.CategoryMapper;
import org.example.model.CategoryEntity;
import org.example.repo.CategoryRepository;
import org.example.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.dto.category.CategoryCreateDTO;
import org.example.service.FileSaveFormat;
import org.springframework.http.MediaType;
import org.example.service.ICategoryService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final StorageService storageService;

    @Autowired
    private ICategoryService service;

    @PostMapping(value="/createInSpring", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String createInSpring(@ModelAttribute CategoryCreateDTO dto) {
        try {
            var entity = categoryMapper.categoryEntityByCategoryCreateDTO(dto);
            entity.setCreationTime(LocalDateTime.now());
            String fileName = storageService.saveImages(dto.getFile(), FileSaveFormat.WEBP);
            //String fileName = storageService.saveImage(dto.getImage(), FileSaveFormat.WEBP);
            entity.setImage(fileName);
            categoryRepository.save(entity);
//            var result = categoryMapper.categoryItemDTO(entity); /////////////////////////

            return "redirect:getAllCategories";
        }
        catch (Exception ex) {
            return ex.getMessage();
        }
    }



    @GetMapping(value = "/getAllCategories")
    public String getAllCategories(
            @RequestParam(value = "message", required = false) String message,
            Model model
    ) {
        List<CategoryEntity> categories= service.getAllCategory();
        model.addAttribute("list", categories);
        model.addAttribute("message", message);
        return "allCategoriesPage";
    }

    @GetMapping("/addCategory")
    public String showRegistration() {
        return "addCategoryPage";
    }


}
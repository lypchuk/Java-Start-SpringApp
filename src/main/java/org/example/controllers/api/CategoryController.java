package org.example.controllers.api;

import lombok.AllArgsConstructor;
import org.example.mapper.CategoryMapper;
import org.example.model.CategoryEntity;
import org.example.model.Invoice;
import org.example.repo.CategoryRepository;
import org.example.service.IInvoiceService;
import org.example.storage.impl.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.example.dto.category.CategoryCreateDTO;
import org.example.mapper.CategoryMapper;
import org.example.repo.CategoryRepository;
import org.example.service.FileSaveFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.example.service.ICategoryService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
//@RestController
@AllArgsConstructor
@RequestMapping("api/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final StorageService storageService;

    @Autowired
    private ICategoryService service;

    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //public ResponseEntity<Integer> create(@ModelAttribute CategoryCreateDTO dto) {
    public String create(@ModelAttribute CategoryCreateDTO dto) {
        try {
            var entity = categoryMapper.categoryEntityByCategoryCreateDTO(dto);
            entity.setCreationTime(LocalDateTime.now());
            String fileName = storageService.saveImage(dto.getFile(), FileSaveFormat.WEBP);
            entity.setImage(fileName);
            categoryRepository.save(entity);
//            var result = categoryMapper.categoryItemDTO(entity); /////////////////////////
            //return new ResponseEntity<>(entity.getId(), HttpStatus.OK);
            return "redirect:category/getAllCategories";
        }
        catch (Exception ex) {
            //return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

            return ex.getMessage();
        }
    }

    @GetMapping("/getAllCategories")
    public String getAllCategories(
            @RequestParam(value = "message", required = false) String message,
            Model model
    ) {
        List<CategoryEntity> categories= service.getAllCategory();
        model.addAttribute("list", categories);
        model.addAttribute("message", message);
        return "allCategoriesPage";
        //return categories.toString();
    }

    @GetMapping("/addCategory")
    public String showRegistration() {
        return "addCategoryPage";
    }


}
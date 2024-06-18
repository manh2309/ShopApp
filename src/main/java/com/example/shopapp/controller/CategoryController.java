package com.example.shopapp.controller;

import com.example.shopapp.dtos.CategoriDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.servies.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
//@Validated
public class CategoryController {
    @Autowired
    private ICategoryService iCategoryService;
    @GetMapping("")
    public ResponseEntity<?> getAllCategories(@RequestParam("page") int page,
                                              @RequestParam("limit") int limit){
       List<Category> categories =  iCategoryService.getAllCategories();
        return  ResponseEntity.ok(categories);
    }
    @PostMapping("")
    // Nếu tham số truyền vào là 1 object thì sao? ==> Data Transfer Object = Request Object
    public ResponseEntity<?> createCategories(@RequestBody @Valid CategoriDTO categoriDTO,
                                              BindingResult result){
        if(result.hasErrors()){
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        iCategoryService.createCategory(categoriDTO);
        return  ResponseEntity.ok("Created Category Successfully!");
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategories(@PathVariable Long id,
                                              @Valid @RequestBody CategoriDTO categoriDTO){
        iCategoryService.updateCategory(id,categoriDTO);
        return  ResponseEntity.ok("Update Category Successfully!");
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategories(@PathVariable Long id){
        iCategoryService.deleteCategory(id);
        return  ResponseEntity.ok("Xoa id = "+ id);
    }
}

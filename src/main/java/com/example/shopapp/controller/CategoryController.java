package com.example.shopapp.controller;

import com.example.shopapp.dtos.CategoriDTO;
import jakarta.validation.Valid;
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
    @GetMapping("")
    public ResponseEntity<?> getAllCategories(@RequestParam("page") int page,
                                              @RequestParam("limit") int limit){
        return  ResponseEntity.ok(String.format("Page = %d, limit = %d", page, limit));
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
        return  ResponseEntity.ok("Them moi "+ categoriDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategories(@PathVariable Long id){
        return  ResponseEntity.ok("Cap nhat id = "+ id);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategories(@PathVariable Long id){
        return  ResponseEntity.ok("Xoa id = "+ id);
    }
}

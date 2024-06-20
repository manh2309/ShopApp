package com.example.shopapp.servies;

import com.example.shopapp.dtos.CategoriDTO;
import com.example.shopapp.models.Category;

import java.util.List;
public interface ICategoryService {
    Category createCategory(CategoriDTO categoriDTO);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoriDTO categoriDTO);
    void deleteCategory(long id);
}

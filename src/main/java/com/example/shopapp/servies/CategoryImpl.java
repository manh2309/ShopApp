package com.example.shopapp.servies;

import com.example.shopapp.dtos.CategoriDTO;
import com.example.shopapp.models.Category;
import com.example.shopapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryImpl implements ICategoryService{
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Category createCategory(CategoriDTO categoriDTO) {
        Category newCategory =  Category.builder().name(categoriDTO.getName()).build();
        return categoryRepository.save(newCategory);
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category no found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category updateCategory(long categoryId, CategoriDTO categoriDTO) {
        Category exitstingCategory = getCategoryById(categoryId);
        exitstingCategory.setName(categoriDTO.getName());
        return categoryRepository.save(exitstingCategory);
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }
}

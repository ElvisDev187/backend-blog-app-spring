package com.elvis.blog.services.impl;

import com.elvis.blog.domain.entities.Category;
import com.elvis.blog.repositories.CategoryRepository;
import com.elvis.blog.services.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> listCategories() {
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        String categoryName = category.getName();
       if( categoryRepository.existsByNameIgnoreCase(categoryName)){
           throw new IllegalArgumentException("Category name already exists with name: " + categoryName);
       }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
      Optional<Category> category = categoryRepository.findById(id);
      if(category.isPresent()){
         if(!category.get().getPosts().isEmpty()) {
             throw new IllegalStateException("There are posts in the category");
         }
        categoryRepository.deleteById(id);
      }
    }
}

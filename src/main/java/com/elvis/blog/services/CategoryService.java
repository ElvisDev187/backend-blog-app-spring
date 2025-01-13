package com.elvis.blog.services;

import com.elvis.blog.domain.entities.Category;

import java.util.List;

public interface CategoryService {
    List<Category> listCategories();
    Category createCategory(Category category);
}

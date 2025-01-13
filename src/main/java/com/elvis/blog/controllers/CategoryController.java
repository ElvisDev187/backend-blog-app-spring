package com.elvis.blog.controllers;

import com.elvis.blog.domain.dtos.CategoryDto;
import com.elvis.blog.domain.dtos.CreateCategoryRequest;
import com.elvis.blog.domain.entities.Category;
import com.elvis.blog.mappers.CategoryMapper;
import com.elvis.blog.services.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> listCategories() {
       List<CategoryDto> categories = categoryService.listCategories().stream().map(categoryMapper::toDto).toList();
       return ResponseEntity.ok(categories);
    }


    @PostMapping
    public  ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryRequest createCategoryRequest) {
         Category categoryToCreate = categoryMapper.toEntity(createCategoryRequest);
        Category savedCategory = categoryService.createCategory(categoryToCreate);
         return new  ResponseEntity<>(categoryMapper.toDto(savedCategory), HttpStatus.CREATED);
    }
}
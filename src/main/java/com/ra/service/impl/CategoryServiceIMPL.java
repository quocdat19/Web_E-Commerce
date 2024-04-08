package com.ra.service.impl;

import com.ra.model.dto.request.CategoryRequest;
import com.ra.model.entity.Category;
import com.ra.repository.CategoryRepository;
import com.ra.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CategoryServiceIMPL implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Page<Category> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category add(CategoryRequest categoryRequest) {
        Category category = Category.builder()
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .status(true)
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category edit(CategoryRequest categoryRequest, Long id) {
        Category category = Category.builder()
                .id(id)
                .name(categoryRequest.getName())
                .description(categoryRequest.getDescription())
                .status(true)
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
    @Override
    public List<Category> getbyStatus() {
        return categoryRepository.findByStatus(true);
    }
}

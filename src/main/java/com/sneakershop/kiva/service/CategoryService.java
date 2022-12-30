package com.sneakershop.kiva.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.sneakershop.kiva.entity.Category;
import com.sneakershop.kiva.model.dto.CategoryDTO;
import com.sneakershop.kiva.model.request.CreateCategoryRequest;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> getListCategories();

    Category getCategoryById(long id);

    Category createCategory(CreateCategoryRequest createCategoryRequest);

    void updateCategory(CreateCategoryRequest createCategoryRequest, long id);

    void deleteCategory(long id);

    Page<Category> adminGetListCategory(String id, String name, String status, int page);

    void updateOrderCategory(int[] ids);

    //Đếm số danh mục
    long getCountCategories();
}

package org.example.adminApi.service.category;

import org.example.dto.request.CategoryRequest;
import org.example.dto.response.CategoryResponse;

public interface CategoryService {

    CategoryResponse create(CategoryRequest request);

    Long removeById(Long catId);

    CategoryResponse update(CategoryRequest request, Long catId);
}

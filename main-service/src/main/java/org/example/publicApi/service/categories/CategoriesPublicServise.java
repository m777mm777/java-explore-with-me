package org.example.publicApi.service.categories;

import org.example.dto.response.CategoryResponse;

import java.util.List;

public interface CategoriesPublicServise {

    List<CategoryResponse> getAllCategories(Integer from, Integer size);

    CategoryResponse getCategories(Long catId);
}

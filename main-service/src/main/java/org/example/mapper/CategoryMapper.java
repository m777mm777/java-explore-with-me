package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CategoryRequest;
import org.example.dto.response.CategoryResponse;
import org.example.model.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CategoryMapper {

    public Category toCategory(CategoryRequest request) {
        if (request == null) {
            return null;
        }

        Category category = new Category();
        category.setName(request.getName());

        return category;
    }

    public CategoryResponse toResponse(Category category) {
        if (category == null) {
            return null;
        }

        CategoryResponse.CategoryResponseBuilder categoryResponse = CategoryResponse.builder();
        categoryResponse.id(category.getId());
        categoryResponse.name(category.getName());
        return categoryResponse.build();
    }

    public List<CategoryResponse> toResponseCollection(List<Category> categories) {
        if (categories == null) {
            return null;
        }
        List<CategoryResponse> list = new ArrayList<CategoryResponse>(categories.size());

        for (Category category : categories) {
            list.add(toResponse(category));
        }

        return list;
    }
}

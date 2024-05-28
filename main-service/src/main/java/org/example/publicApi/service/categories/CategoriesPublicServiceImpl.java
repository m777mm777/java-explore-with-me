package org.example.publicApi.service.categories;

import lombok.RequiredArgsConstructor;
import org.example.dto.response.CategoryResponse;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.CategoryMapper;
import org.example.model.Category;
import org.example.publicApi.repository.CategoriesPublicRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriesPublicServiceImpl implements CategoriesPublicServise {

    private final CategoriesPublicRepository categoriesPublicRepository;
    private static final Sort SORT_ID_ASC = Sort.by(Sort.Direction.ASC, "id");
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryResponse> getAllCategories(Integer from, Integer size) {
        Pageable page = PageRequest.of(from > 0 ? from / size : 0, size, SORT_ID_ASC);

        Page<Category> categories = categoriesPublicRepository.findAll(page);
        return categoryMapper.toResponseCollection(categories.getContent());
    }

    @Override
    public CategoryResponse getCategories(Long catId) {
        Category category = categoriesPublicRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Category не найдена"));

        return categoryMapper.toResponse(category);
    }
}

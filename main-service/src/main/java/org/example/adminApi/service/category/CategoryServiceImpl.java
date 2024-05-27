package org.example.adminApi.service.category;

import lombok.RequiredArgsConstructor;
import org.example.adminApi.repository.CategoryRepository;
import org.example.adminApi.repository.EventRepositoryAdmin;
import org.example.dto.request.CategoryRequest;
import org.example.dto.response.CategoryResponse;
import org.example.exceptions.ConflictServerError;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.CategoryMapper;
import org.example.model.Category;
import org.example.model.Event;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventRepositoryAdmin eventRepository;

    @Override
    public CategoryResponse create(CategoryRequest request) {

        Category category;

        try {
            category = categoryRepository.save(categoryMapper.toCategory(request));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictServerError("Категория уже есть");
        }

        return categoryMapper.toResponse(category);
    }

    @Override
    public Long removeById(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена"));

        Optional<Event> event = eventRepository.findByCategoryId(catId);

        if (event.isPresent()) {
            throw new ConflictServerError("Существуют события, связанные с категорией");
        }

        categoryRepository.deleteById(catId);
        return catId;
    }

    @Override
    public CategoryResponse update(CategoryRequest request, Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> new ResourceNotFoundException("Категория не найдена"));

        Optional<Category> categoryOptional = categoryRepository.findByIdNotAndName(catId, request.getName());

        if (categoryOptional.isPresent()) {
            throw new ConflictServerError("Категория уже есть");
        }

        Category category = categoryMapper.toCategory(request);
        category.setId(catId);

        category = categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }
}

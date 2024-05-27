package org.example.publicApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.CategoryResponse;
import org.example.publicApi.service.categories.CategoriesPublicServise;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/categories")
@RequiredArgsConstructor
public class CategoriesControllerPublic {

    private final CategoriesPublicServise categoriesPublicServise;

    @GetMapping()
    public List<CategoryResponse> getAllCategories(@PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GetAllCategories from {} size {}", from, size);
        return categoriesPublicServise.getAllCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryResponse getCategories(@PathVariable Long catId) {
        log.info("GetCategories catId {}", catId);
        return categoriesPublicServise.getCategories(catId);
    }
}

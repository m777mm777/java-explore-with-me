package org.example.adminApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.adminApi.service.category.CategoryService;
import org.example.createAndUpdate.Create;
import org.example.createAndUpdate.Update;
import org.example.dto.request.CategoryRequest;
import org.example.dto.response.CategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(path = "/admin/categories")
@RequiredArgsConstructor
public class CategoriesControllerAdmin {

    private final CategoryService categoryService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse crateCategory(@Validated(Create.class) @RequestBody CategoryRequest request) {
        log.info("CrateCategory request {}", request);
        return categoryService.create(request);
    }

    @PatchMapping("/{catId}")
    public CategoryResponse updateCategory(@Validated(Update.class) @RequestBody CategoryRequest request,
                                           @PathVariable Long catId) {
        log.info("UpdateCategory request {} catId {}", request, catId);
        return categoryService.update(request, catId);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Long removeCategory(@PathVariable Long catId) {
        log.info("RemoveCategory catId {}", catId);
        return categoryService.removeById(catId);
    }
}

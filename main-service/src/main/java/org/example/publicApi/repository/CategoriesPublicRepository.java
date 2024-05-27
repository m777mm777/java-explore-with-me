package org.example.publicApi.repository;

import org.example.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesPublicRepository extends JpaRepository<Category, Long> {
}

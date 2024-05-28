package org.example.adminApi.repository;

import org.example.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByIdNotAndName(Long id, String name);

    Optional<Category> findByName(String name);
}

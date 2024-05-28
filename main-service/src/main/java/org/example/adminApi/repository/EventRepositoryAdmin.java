package org.example.adminApi.repository;

import org.example.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface EventRepositoryAdmin extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
    Optional<Event> findByCategoryId(Long categoryId);
}

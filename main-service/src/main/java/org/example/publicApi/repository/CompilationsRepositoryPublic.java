package org.example.publicApi.repository;

import org.example.model.Compilations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationsRepositoryPublic extends JpaRepository<Compilations, Long> {
    Page<Compilations> findByPinned(Boolean pinned, Pageable page);
}

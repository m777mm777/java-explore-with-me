package org.example.adminApi.repository;

import org.example.model.Compilations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompilationsRepository extends JpaRepository<Compilations, Long>  {
}

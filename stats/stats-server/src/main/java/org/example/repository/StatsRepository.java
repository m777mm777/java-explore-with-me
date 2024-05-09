package org.example.repository;

import org.example.model.Hit;
import org.example.model.Stats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    @Query("SELECT new org.example.model.Stats(app, uri, COUNT(DISTINCT ip)) " +
            "FROM Hit " +
            "WHERE timestamp BETWEEN ?1 AND ?2 " +
            "AND (uri IN (?3) OR (?3) is NULL) " +
            "GROUP BY app, uri " +
            "ORDER BY COUNT(DISTINCT ip) DESC ")
    List<Stats> findUniqueViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT new org.example.model.Stats(app, uri, COUNT(ip)) " +
            "FROM Hit " +
            "WHERE timestamp BETWEEN ?1 AND ?2 " +
            "AND (uri IN (?3) OR (?3) is NULL) " +
            "GROUP BY app, uri " +
            "ORDER BY COUNT(ip) DESC ")
    List<Stats> findViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}


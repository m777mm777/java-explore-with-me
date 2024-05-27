package org.example.privateApi.repository;

import org.example.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByInitiatorId(Long initiatorId, Pageable page);
}

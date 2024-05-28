package org.example.publicApi.repository;

import org.example.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EventPublicRepository extends JpaRepository<Event, Long>, JpaSpecificationExecutor<Event> {
}

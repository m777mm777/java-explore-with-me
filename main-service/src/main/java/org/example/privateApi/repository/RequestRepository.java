package org.example.privateApi.repository;

import org.example.model.Request;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByRequesterIdAndEventId(Long requesterId, Long eventId);

    List<Request> findByEventId(Long eventId, Sort sort);

    Optional<Request> findByRequesterIdAndId(Long userId, Long requestId);

    List<Request> findByRequesterId(Long requesterId);
}

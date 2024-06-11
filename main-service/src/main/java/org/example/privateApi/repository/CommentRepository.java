package org.example.privateApi.repository;

import org.example.model.Comment;
import org.example.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEventId(Long eventId);

    List<Comment> findAllByEventIn(List<Event> eventId);
}

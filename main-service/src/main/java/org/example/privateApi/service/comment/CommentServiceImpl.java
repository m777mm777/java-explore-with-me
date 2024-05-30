package org.example.privateApi.service.comment;

import lombok.RequiredArgsConstructor;
import org.example.adminApi.repository.UserRepository;
import org.example.dto.request.CommentRequest;
import org.example.dto.response.CommentResponse;
import org.example.enums.State;
import org.example.exceptions.ConflictServerError;
import org.example.exceptions.ResourceNotFoundException;
import org.example.mapper.CommentMapper;
import org.example.model.Comment;
import org.example.model.Event;
import org.example.model.User;
import org.example.privateApi.repository.CommentRepository;
import org.example.privateApi.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentResponse create(Long userId, Long eventId, CommentRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictServerError("Event не опубликован");
        }

        Comment comment = commentMapper.toComment(request);
        comment.setAuthor(user);
        comment.setEvent(event);
        comment.setCreated(LocalDateTime.now().withNano(0));

        comment = commentRepository.save(comment);

        return commentMapper.toResponse(comment);
    }

    @Override
    public CommentResponse update(Long userId, Long eventId, Long commentId, CommentRequest request) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment не найден"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictServerError("Изменять комментарий может только создатель");
        }

        if (!comment.getEvent().getId().equals(eventId)) {
            throw new ConflictServerError("Комментарий не от этого Event");
        }

        comment.setText(request.getText());

        comment = commentRepository.save(comment);

        return commentMapper.toResponse(comment);
    }

    @Override
    public Long removeById(Long userId, Long eventId, Long commentId) {

        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event не найден"));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment не найден"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ConflictServerError("Удалять комментарий может только создатель");
        }

        if (!comment.getEvent().getId().equals(eventId)) {
            throw new ConflictServerError("Комментарий не от этого евента");
        }

        commentRepository.deleteById(commentId);

        return commentId;
    }
}

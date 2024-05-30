package org.example.privateApi.service.comment;

import org.example.dto.request.CommentRequest;
import org.example.dto.response.CommentResponse;

public interface CommentService {

    CommentResponse create(Long userId, Long eventId, CommentRequest request);

    CommentResponse update(Long userId, Long eventId, Long commentId, CommentRequest request);

    Long removeById(Long userId, Long eventId, Long commentId);
}

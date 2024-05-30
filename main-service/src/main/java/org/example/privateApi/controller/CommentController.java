package org.example.privateApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.CommentRequest;
import org.example.dto.response.CommentResponse;
import org.example.privateApi.service.comment.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events/{eventId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse crateComment(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @Valid @RequestBody CommentRequest request) {
        log.info("CrateComment userId {} eventId {} request {}", userId, eventId, request);
        return commentService.create(userId, eventId, request);
    }

    @PatchMapping("/{commentId}")
    public CommentResponse updateComment(@PathVariable Long userId,
                                         @PathVariable Long eventId,
                                         @PathVariable Long commentId,
                                         @Valid @RequestBody CommentRequest request) {
        log.info("UpdateComment userId {} eventId {} eventId {} request {}", userId, eventId, commentId, request);
        return commentService.update(userId, eventId, commentId, request);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Long removeComment(@PathVariable Long userId,
                              @PathVariable Long eventId,
                              @PathVariable Long commentId) {
        log.info("RemoveComment userId {} eventId {} commentId {}",userId, eventId, commentId);
        return commentService.removeById(userId, eventId, commentId);
    }
}

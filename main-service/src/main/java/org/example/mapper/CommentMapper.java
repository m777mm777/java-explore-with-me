package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.dto.request.CommentRequest;
import org.example.dto.response.CommentResponse;
import org.example.model.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    public Comment toComment(CommentRequest request) {
        if (request == null) {
            return null;
        }

        Comment comment = new Comment();
        comment.setText(request.getText());

        return comment;
    }

    public CommentResponse toResponse(Comment comment) {
        if (comment == null) {
            return null;
        }

        CommentResponse.CommentResponseBuilder commentResponse = CommentResponse.builder();
        commentResponse.id(comment.getId());
        commentResponse.text(comment.getText());
        commentResponse.author(userMapper.toResponse(comment.getAuthor()));
        commentResponse.event(eventMapper.toResponse(comment.getEvent()));
        commentResponse.created(comment.getCreated());
        return commentResponse.build();
    }

    public List<CommentResponse> toResponseCollection(List<Comment> comments) {
        if (comments == null) {
            return null;
        }
        List<CommentResponse> list = new ArrayList<CommentResponse>(comments.size());

        for (Comment comment : comments) {
            list.add(toResponse(comment));
        }

        return list;
    }
}

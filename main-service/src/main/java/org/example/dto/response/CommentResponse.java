package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentResponse {

    private Long id;

    private String text;

    private EventResponse event;

    private UserResponse author;

    private LocalDateTime created;
}

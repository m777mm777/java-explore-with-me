package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.example.enums.Status;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ParticipationResponse {

    private Long id;

    private LocalDateTime created;

    private Long event;

    private Long requester;

    private Status status;
}

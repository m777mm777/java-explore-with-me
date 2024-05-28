package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class CompilationsResponse {

    private Long id;

    private List<EventShortResponse> events;

    private Boolean pinned;

    private String title;
}

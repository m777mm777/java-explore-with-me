package org.example.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationResponse> confirmedRequests;
    private List<ParticipationResponse> rejectedRequests;
}

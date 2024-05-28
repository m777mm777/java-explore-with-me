package org.example.privateApi.service.request;

import org.example.dto.response.ParticipationResponse;

import java.util.List;

public interface RequestService {

    ParticipationResponse create(Long userId, Long eventId);

    List<ParticipationResponse> getAllRequestUser(Long userId);

    ParticipationResponse cancelRequestByRequester(Long userId, Long requestId);
}

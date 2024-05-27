package org.example.privateApi.service.event;

import org.example.dto.request.EventRequest;
import org.example.dto.request.EventRequestStatusUpdateRequest;
import org.example.dto.request.UpdateEventUserRequest;
import org.example.dto.response.EventRequestStatusUpdateResult;
import org.example.dto.response.EventResponse;
import org.example.dto.response.EventShortResponse;
import org.example.dto.response.ParticipationResponse;

import java.util.List;

public interface EventService {

    EventResponse create(Long userId, EventRequest request);

    EventResponse updateOwnerEvent(Long userId, Long eventId, UpdateEventUserRequest request);

    List<EventShortResponse> getAllEventsByOwner(Long userId, Integer from, Integer size);

    EventResponse getEventsByOwner(Long userId, Long eventId);

    List<ParticipationResponse> getRequestsByUser(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}

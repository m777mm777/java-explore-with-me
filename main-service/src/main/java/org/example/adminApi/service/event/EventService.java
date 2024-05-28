package org.example.adminApi.service.event;

import org.example.dto.request.UpdateEventAdminRequest;
import org.example.dto.response.EventResponse;
import org.example.enums.State;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventResponse updateEvent(Long eventId, UpdateEventAdminRequest request);

    List<EventResponse> getEventsByFilter(List<Long> users,
                                  List<State> states,
                                  List<Long> categories,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  Integer from,
                                  Integer size);
}

package org.example.publicApi.service.event;

import org.example.dto.response.EventResponse;
import org.example.dto.response.EventShortResponse;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventPublicService {

    List<EventShortResponse> getAllEvents(String text,
                                          List<Long> categories,
                                          Boolean paid,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          Boolean onlyAvailable,
                                          String sort,
                                          Integer from,
                                          Integer size,
                                          HttpServletRequest httpServletRequest);

    EventResponse getEvent(Long id, HttpServletRequest httpServletRequest);

}

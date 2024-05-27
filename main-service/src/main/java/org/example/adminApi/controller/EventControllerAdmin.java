package org.example.adminApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.adminApi.service.event.EventService;
import org.example.dto.request.UpdateEventAdminRequest;
import org.example.dto.response.EventResponse;
import org.example.enums.State;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class EventControllerAdmin {

    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventResponse updateEvent(@PathVariable Long eventId,
                                     @Valid @RequestBody UpdateEventAdminRequest request) {
        log.info("UpdateEventAdmin eventId {} request {}", eventId, request);
        return eventService.updateEvent(eventId, request);
    }

    @GetMapping()
    public List<EventResponse> getEvents(@RequestParam(required = false) List<Long> users,
                                         @RequestParam(required = false) List<State> states,
                                         @RequestParam(required = false) List<Long> categories,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                         @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                                         @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("GetEventsAdmin users {} " +
                "states {} " +
                "categories {} " +
                "rangeStart {} " +
                "rangeEnd {} " +
                "from {} " +
                "size {}",
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size);

        return eventService.getEventsByFilter(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}

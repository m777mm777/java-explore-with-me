package org.example.privateApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.request.EventRequestStatusUpdateRequest;
import org.example.dto.request.UpdateEventUserRequest;
import org.example.dto.response.EventRequestStatusUpdateResult;
import org.example.privateApi.service.event.EventService;
import org.example.dto.request.EventRequest;
import org.example.dto.response.EventResponse;
import org.example.dto.response.EventShortResponse;
import org.example.dto.response.ParticipationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class EventControllerPrivate {

    private final EventService eventService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse crateEvent(@PathVariable Long userId,
                                    @Valid @RequestBody EventRequest request) {
        log.info("CrateEvent userId {} request {}", userId, request);
        return eventService.create(userId, request);
    }

    @PatchMapping("/{eventId}")
    public EventResponse updateOwnerEvent(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @Valid @RequestBody UpdateEventUserRequest request) {
        log.info("UpdateOwnerEvent userId {} eventId {} request {}", userId, eventId, request);
        return eventService.updateOwnerEvent(userId, eventId, request);
    }

    @GetMapping()
    public List<EventShortResponse> getAllEventsByOwner(@PathVariable Long userId,
                                                        @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                        @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GetAllEventsByOwner userId {} from {} size {}", userId, from, size);
        return eventService.getAllEventsByOwner(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventResponse getEventsByUser(@PathVariable Long userId,
                                         @PathVariable Long eventId) {
        log.info("GetEventsByOwner userId {} eventId {}", userId, eventId);
        return eventService.getEventsByOwner(userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationResponse> getRequestsByUser(@PathVariable Long userId,
                                                         @PathVariable Long eventId) {
        log.info("GetRequestsByUser userId {} eventId {}", userId, eventId);
        return eventService.getRequestsByUser(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatus(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("UpdateStatus userId {} eventId {} request {}", userId, eventId, request);
        return eventService.updateStatus(userId, eventId, request);
    }

}

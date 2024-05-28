package org.example.privateApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.ParticipationResponse;
import org.example.privateApi.service.request.RequestService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestControllerPrivate {

    private final RequestService requestService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationResponse createRequest(@PathVariable Long userId,
                                               @RequestParam Long eventId) {
        log.info("CreateRequest userId {} eventId {}", userId, eventId);
        return requestService.create(userId, eventId);
    }

    @GetMapping()
    public List<ParticipationResponse> getAllRequestUser(@PathVariable Long userId) {
        log.info("GetRequest userId {}", userId);
        return requestService.getAllRequestUser(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationResponse cancelRequestByRequester(@PathVariable Long userId,
                                                          @PathVariable Long requestId) {
        log.info("CancelRequestByRequester userId {} requestId {}", userId, requestId);
        return requestService.cancelRequestByRequester(userId, requestId);
    }
}

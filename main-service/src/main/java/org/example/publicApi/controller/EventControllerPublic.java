package org.example.publicApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.EventResponse;
import org.example.dto.response.EventShortResponse;
import org.example.publicApi.service.event.EventPublicService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/events")
@RequiredArgsConstructor
public class EventControllerPublic {

    private final EventPublicService eventPublicService;
    public static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    @GetMapping()
    public List<EventShortResponse> getAllEvents(@RequestParam(required = false) String text,
                                                 @RequestParam(required = false) List<Long> categories,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT)LocalDateTime rangeStart,
                                                 @RequestParam(required = false) @DateTimeFormat(pattern = FORMAT)LocalDateTime rangeEnd,
                                                 @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                 @RequestParam(defaultValue = "EVENT_DATE") String sort,
                                                 @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                 @Positive @RequestParam(defaultValue = "10") Integer size,
                                                 HttpServletRequest httpServletRequest) {
        log.info("GetAllEvents text {}" +
                        " categories {}" +
                        " paid {}" +
                        " rangeStart {}" +
                        " rangeEnd {}" +
                        " onlyAvailable {}" +
                        " sort {}" +
                        " from {}" +
                        " size {}" +
                        " httpServletRequest {}",
                text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, httpServletRequest);
        return eventPublicService.getAllEvents(text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                from,
                size,
                httpServletRequest);
    }

    @GetMapping("/{id}")
    public EventResponse getEvent(@PathVariable Long id,
                                  HttpServletRequest httpServletRequest) {
        log.info("GetEvent id {}", id);
        return eventPublicService.getEvent(id, httpServletRequest);
    }
}

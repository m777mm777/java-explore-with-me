package org.example.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.HitsResponse;
import org.example.dto.HitRequest;
import org.example.dto.StatsResponse;
import org.example.service.StatsService;
import org.example.validate.StatsValidate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;
    private final StatsValidate statsValidate;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitsResponse create(@Valid @RequestBody HitRequest hitRequest) {
        log.info("Create hitDto {}", hitRequest);
        return statsService.create(hitRequest);
    }

    @GetMapping("/stats")
    public List<StatsResponse> getStats(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("GetStats start {} end {} uris {} unique {}", start, end, uris, unique);
        statsValidate.validateTime(start, end);
        return statsService.getStats(start, end, uris, unique);
    }
}

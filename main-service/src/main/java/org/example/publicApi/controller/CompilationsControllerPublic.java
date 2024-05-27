package org.example.publicApi.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.CompilationsResponse;
import org.example.publicApi.service.compilations.CompilationsServicePublic;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
public class CompilationsControllerPublic {

    private final CompilationsServicePublic compilationsServicePublic;

    @GetMapping()
    public List<CompilationsResponse> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                         @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
                                                         @Positive @RequestParam(defaultValue = "10") Integer size) {
        log.info("GetAllCompilations pinned {} from {} size {}", pinned, from, size);
        return compilationsServicePublic.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationsResponse getCompilations(@PathVariable Long compId) {
        log.info("GetCompilations compId {}", compId);
        return compilationsServicePublic.getCompilations(compId);
    }
}

package org.example.publicApi.service.compilations;

import org.example.dto.response.CompilationsResponse;

import java.util.List;

public interface CompilationsServicePublic {

    List<CompilationsResponse> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationsResponse getCompilations(Long compId);
}
